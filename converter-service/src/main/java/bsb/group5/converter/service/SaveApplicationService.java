package bsb.group5.converter.service;

import bsb.group5.converter.service.model.AddApplicationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;

import java.util.Set;

public interface SaveApplicationService {
    Set<AddApplicationDTO> filterExisting(Set<AddApplicationDTO> listOfApplications);

    Set<ViewApplicationDTO> saveAll(Set<AddApplicationDTO> nonExistingApplications);
}
