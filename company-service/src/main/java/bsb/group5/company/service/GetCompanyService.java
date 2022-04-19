package bsb.group5.company.service;

import bsb.group5.company.service.model.ViewCompanyDTO;


public interface GetCompanyService {
    ViewCompanyDTO getById(Long id);
}
