package bsb.group5.converter.controllers;

import bsb.group5.converter.config.MessageConfig;
import bsb.group5.converter.controllers.exceptions.PaginationException;
import bsb.group5.converter.controllers.model.PaginationEnum;
import bsb.group5.converter.service.AddApplicationService;
import bsb.group5.converter.service.GetApplicationService;
import bsb.group5.converter.service.UpdateApplicationService;
import bsb.group5.converter.service.model.PaginationDTO;
import bsb.group5.converter.service.model.enums.ResponseBodyFieldsEnum;
import bsb.group5.converter.service.model.UpdateNameLegalApplicationDTO;
import bsb.group5.converter.service.model.UpdateStatusApplicationDTO;
import bsb.group5.converter.service.model.ViewApplicationDTO;
import bsb.group5.converter.service.model.ViewUpdateStatusAppDTO;
import bsb.group5.converter.service.validators.CustomizeValid;
import bsb.group5.converter.service.validators.FileValidator;
import bsb.group5.converter.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/applications")
@AllArgsConstructor
@Validated
@Slf4j
public class ConverterController {

    private final GetApplicationService getApplicationService;
    private final UpdateApplicationService updateApplicationService;
    private final MessageConfig messageConfig;

    private final FileValidator fileValidator;
    private final JwtUtil jwtUtil;
    private final AddApplicationService addApplicationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ViewApplicationDTO>> getApplications(
            @RequestParam(name = "page", required = false, defaultValue = "1")
                    Integer page,
            @RequestParam(name = "pagination", required = false, defaultValue = "NoPagination")
                    PaginationEnum pagination,
            @RequestParam(name = "customized_page", required = false)
            @CustomizeValid
                    Integer customizedPage) {
        PaginationDTO paginationDTO = PaginationDTO.builder()
                .pagination(pagination)
                .page(page)
                .customizedPage(customizedPage)
                .build();
        if (paginationDTO.getPagination() == PaginationEnum.Customized && paginationDTO.getCustomizedPage() == null) {
            throw new PaginationException(messageConfig.getMessagePaginationExceptions());
        }
        List<ViewApplicationDTO> viewApplicationDTOS = getApplicationService.getAll(paginationDTO);
        return new ResponseEntity<>(viewApplicationDTOS, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Model> addByFile(@RequestPart("dtoFile")  MultipartFile dtoFile) {
        Model responseBody = new ExtendedModelMap();
        boolean validFile = fileValidator.isValid(dtoFile);
        if (!validFile) {
            responseBody.addAttribute(ResponseBodyFieldsEnum.InvalidFile.getMessage(), "unsupported file");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
        return addApplicationService.addByFile(dtoFile, responseBody);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewApplicationDTO> getApplication(@PathVariable UUID id) {
        ViewApplicationDTO viewApplicationDTO = getApplicationService.getByUUID(id);
        return new ResponseEntity<>(viewApplicationDTO, HttpStatus.OK);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ViewUpdateStatusAppDTO> updateStatusApplication(
            UpdateStatusApplicationDTO updateStatusApplicationDTO,
            @RequestHeader(name = "AUTHORIZATION") String authorization
    ) {
        String usernameFromHeader = jwtUtil.getUsernameFromHeader(authorization);
        ViewUpdateStatusAppDTO viewUpdateStatusAppDTO = updateApplicationService.updateStatus(updateStatusApplicationDTO, usernameFromHeader);
        return new ResponseEntity<>(viewUpdateStatusAppDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateNameLegalApplication(
            @PathVariable UUID id,
            @RequestParam(name = "Name_Legal") String nameLegal
    ) {
        UpdateNameLegalApplicationDTO updateNameLegalApplicationDTO = UpdateNameLegalApplicationDTO.builder()
                .id(id)
                .nameLegal(nameLegal)
                .build();
        Map<String, Object> responseBody = updateApplicationService.updateNameLegal(updateNameLegalApplicationDTO);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
