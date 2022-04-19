package bsb.group5.converter.service.validators;

import org.springframework.web.multipart.MultipartFile;

public interface FileValidator {
    boolean isValid(MultipartFile file);
}
