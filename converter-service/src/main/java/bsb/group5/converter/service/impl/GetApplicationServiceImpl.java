package bsb.group5.converter.service.impl;

import bsb.group5.converter.config.PaginationConfig;
import bsb.group5.converter.controllers.model.PaginationEnum;
import bsb.group5.converter.repository.ApplicationRepository;
import bsb.group5.converter.repository.model.Application;
import bsb.group5.converter.service.GetApplicationService;
import bsb.group5.converter.service.converters.ApplicationConverter;
import bsb.group5.converter.service.model.PaginationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetApplicationServiceImpl implements GetApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationConverter applicationConverter;
    private final PaginationConfig paginationConfig;

    @Override
    public List<ViewApplicationDTO> getAll(PaginationDTO paginationDTO) {
        PaginationEnum pagination = paginationDTO.getPagination();
        int page = paginationDTO.getPage() - 1;
        int size;
        Pageable pageable;
        Page<Application> applicationPageList;
        switch (pagination) {
            case Default:
                size = Integer.parseInt(paginationConfig.getPaginationMaxDefault());
                pageable = PageRequest.of(page, size);
                applicationPageList = applicationRepository.findAll(pageable);
                return applicationConverter.convertToViewApplicationDTOList(applicationPageList);
            case Customized:
                size = paginationDTO.getCustomizedPage();
                pageable = PageRequest.of(page, size);
                applicationPageList = applicationRepository.findAll(pageable);
                return applicationConverter.convertToViewApplicationDTOList(applicationPageList);
            default:
                List<Application> applicationList = applicationRepository.findAll();
                return applicationConverter.convertToViewApplicationDTOList(applicationList);
        }
    }

    @Override
    public ViewApplicationDTO getByUUID(UUID id) {
        Application application = applicationRepository.findByUuid(id);
        return applicationConverter.convertToViewApplicationDTO(application);
    }
}
