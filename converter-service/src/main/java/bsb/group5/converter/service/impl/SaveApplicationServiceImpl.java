package bsb.group5.converter.service.impl;

import bsb.group5.converter.repository.ApplicationRepository;
import bsb.group5.converter.repository.model.Application;
import bsb.group5.converter.repository.model.ApplicationDetails;
import bsb.group5.converter.service.SaveApplicationService;
import bsb.group5.converter.service.converters.ApplicationConverter;
import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SaveApplicationServiceImpl implements SaveApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationConverter applicationConverter;

    @Override
    @Transactional
    public Set<AddApplicationDTO> filterExisting(Set<AddApplicationDTO> listOfApplications) {
        return listOfApplications.stream()
                .filter(application -> applicationRepository.existsByUuid(application.getApplicationId()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<ViewApplicationDTO> saveAll(Set<AddApplicationDTO> nonExistingApplications) {
        return nonExistingApplications.stream()
                .map(element -> {
                    Application application = applicationConverter.convertToApplication(element);
                    ApplicationDetails details = getApplicationDetails(element);
                    application.setApplicationDetails(details);
                    details.setApplication(application);
                    Application savedApplication = applicationRepository.save(application);
                    return applicationConverter.convertToViewApplicationDTO(savedApplication);
                })
                .collect(Collectors.toSet());
    }

    private ApplicationDetails getApplicationDetails(AddApplicationDTO addApplicationDTO) {
        ApplicationDetails details = new ApplicationDetails();
        details.setCreateDate(LocalDateTime.now());
        details.setLastUpdate(LocalDateTime.now());
        String note = addApplicationDTO.getNote();
        details.setNote(note);
        return details;
    }
}
