package bsb.group5.company.service;

import bsb.group5.company.service.model.AddCompanyDTO;
import bsb.group5.company.service.model.ViewCompanyDTO;

public interface AddCompanyService {
    ViewCompanyDTO add(AddCompanyDTO addCompanyDTO);
}
