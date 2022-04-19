package bsb.group5.converter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface AddApplicationService {
    ResponseEntity<Model> addByFile(MultipartFile dtoFile, Model responseBody);
}
