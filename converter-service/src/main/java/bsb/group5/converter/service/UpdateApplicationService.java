package bsb.group5.converter.service;

import bsb.group5.converter.service.model.UpdateNameLegalApplicationDTO;
import bsb.group5.converter.service.model.UpdateStatusApplicationDTO;
import bsb.group5.converter.service.model.ViewUpdateStatusAppDTO;

import java.util.Map;

public interface UpdateApplicationService {
    ViewUpdateStatusAppDTO updateStatus(UpdateStatusApplicationDTO updateStatusApplicationDTO, String username);

    Map<String, Object> updateNameLegal(UpdateNameLegalApplicationDTO updateNameLegalApplicationDTO);
}
