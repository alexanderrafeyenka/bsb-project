package bsb.group5.converter.service;

import bsb.group5.converter.service.model.PaginationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;

import java.util.List;
import java.util.UUID;

public interface GetApplicationService {
    List<ViewApplicationDTO> getAll(PaginationDTO paginationDTO);

    ViewApplicationDTO getByUUID(UUID id);
}
