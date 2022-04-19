package bsb.group5.converter.service.impl;

import bsb.group5.converter.repository.ApplicationRepository;
import bsb.group5.converter.repository.ApplicationUpdateRepository;
import bsb.group5.converter.repository.CompanyFeignRepository;
import bsb.group5.converter.repository.model.Application;
import bsb.group5.converter.repository.model.ApplicationUpdate;
import bsb.group5.converter.repository.model.StatusEnum;
import bsb.group5.converter.service.UpdateApplicationService;
import bsb.group5.converter.service.exceptions.UpdateStatusServiceException;
import bsb.group5.converter.service.model.CompanyDTO;
import bsb.group5.converter.service.model.UpdateNameLegalApplicationDTO;
import bsb.group5.converter.service.model.UpdateStatusApplicationDTO;
import bsb.group5.converter.service.model.ViewUpdateStatusAppDTO;
import bsb.group5.converter.service.model.enums.StatusDTOEnum;
import bsb.group5.converter.util.ResponseBodyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateApplicationServiceImpl implements UpdateApplicationService {
    private static final String STATUS = "status";
    private static final String STATUS_CAN_NOT_BE_CHANGED = "Статус не может быть изменен";
    private static final String APPLICATION_REBIND = "Заявка на конверсию %s перепривязана к %s";
    private static final String APPLICATION_BIND = "Заявка на конверсию %s привязана к %s";
    private static final String COMPANY_NOT_FOUND = "Company %s not found";
    private static final String COMPANY_NOT_EXISTS = "Компания с %s не существует";
    private final ApplicationRepository applicationRepository;
    private final ApplicationUpdateRepository applicationUpdateRepository;
    private final ResponseBodyUtil responseBodyUtil;
    private final CompanyFeignRepository companyFeignRepository;

    @Transactional
    @Override
    public ViewUpdateStatusAppDTO updateStatus(UpdateStatusApplicationDTO updateStatusApplicationDTO, String username) {
        UUID applicationConvId = updateStatusApplicationDTO.getApplicationConvId();
        Application application = applicationRepository.findByUuid(applicationConvId);
        if (application == null) {
            throw new UpdateStatusServiceException(STATUS_CAN_NOT_BE_CHANGED);
        }
        StatusEnum status = application.getStatus();
        StatusDTOEnum statusDTOForUpdate = updateStatusApplicationDTO.getStatus();
        StatusEnum statusForUpdate = StatusEnum.valueOf(statusDTOForUpdate.toString());
        Application updatedApplication;
        switch (status) {
            case New:
                if (statusDTOForUpdate == StatusDTOEnum.InProgress || statusDTOForUpdate == StatusDTOEnum.Rejected) {
                    application.setStatus(statusForUpdate);
                    updatedApplication = applicationRepository.save(application);
                    ApplicationUpdate applicationUpdate = getApplicationUpdate(username, statusForUpdate, updatedApplication);
                    applicationUpdateRepository.save(applicationUpdate);
                    break;
                } else {
                    throw new UpdateStatusServiceException(STATUS_CAN_NOT_BE_CHANGED);
                }
            case InProgress:
                if (statusDTOForUpdate == StatusDTOEnum.Done) {
                    application.setStatus(statusForUpdate);
                    updatedApplication = applicationRepository.save(application);
                    ApplicationUpdate applicationUpdate = getApplicationUpdate(username, statusForUpdate, updatedApplication);
                    applicationUpdateRepository.save(applicationUpdate);
                    break;
                } else {
                    throw new UpdateStatusServiceException(STATUS_CAN_NOT_BE_CHANGED);
                }
            default:
                throw new UpdateStatusServiceException(STATUS_CAN_NOT_BE_CHANGED);
        }
        StatusEnum updatedStatus = updatedApplication.getStatus();
        StatusDTOEnum updatedDTOStatus = StatusDTOEnum.valueOf(updatedStatus.toString());
        return ViewUpdateStatusAppDTO.builder()
                .username(username)
                .status(updatedDTOStatus)
                .build();
    }

    @Transactional
    @Override
    public Map<String, Object> updateNameLegal(UpdateNameLegalApplicationDTO updateNameLegalApplicationDTO) {
        UUID applicationConvId = updateNameLegalApplicationDTO.getId();
        String nameLegalForUpdate = updateNameLegalApplicationDTO.getNameLegal();
        Application application = applicationRepository.findByUuid(applicationConvId);
        if (application == null) {
            throw new UpdateStatusServiceException(String.format(COMPANY_NOT_EXISTS, nameLegalForUpdate));
        }
        String nameLegal = application.getNameLegal();
        List<CompanyDTO> companyByNameLegals = companyFeignRepository.getCompanyByNameLegal(nameLegalForUpdate);
        CompanyDTO companyByNameLegal;
        if (!companyByNameLegals.isEmpty()) {
            companyByNameLegal = companyByNameLegals.get(0);
        } else {
            return responseBodyUtil.getResponseBody(String.format(COMPANY_NOT_FOUND, nameLegalForUpdate), HttpStatus.BAD_REQUEST);
        }
        Long legalId = companyByNameLegal.getId();
        if (!nameLegal.equals(nameLegalForUpdate)) {
            application.setNameLegal(nameLegalForUpdate);
            applicationRepository.save(application);
            String message = String.format(APPLICATION_REBIND, applicationConvId, nameLegalForUpdate);
            return responseBodyUtil.getResponseBodyWithLegalId(legalId.toString(), message, HttpStatus.OK);

        } else {
            String message = String.format(APPLICATION_BIND, applicationConvId, nameLegalForUpdate);
            return responseBodyUtil.getResponseBodyWithLegalId(legalId.toString(), message, HttpStatus.OK);
        }
    }

    private ApplicationUpdate getApplicationUpdate(String username, StatusEnum statusForUpdate, Application application) {
        ApplicationUpdate applicationUpdate = new ApplicationUpdate();
        applicationUpdate.setUsername(username);
        applicationUpdate.setChangedField(STATUS);
        applicationUpdate.setValueChangedTo(statusForUpdate.name());
        applicationUpdate.setApplication(application);
        return applicationUpdate;
    }
}
