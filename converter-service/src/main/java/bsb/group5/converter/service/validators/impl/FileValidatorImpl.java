package bsb.group5.converter.service.validators.impl;

import bsb.group5.converter.service.validators.FileValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
@Slf4j
public class FileValidatorImpl implements FileValidator {
    public boolean isValid(MultipartFile file) {
        if (file == null) {
            return false;
        }
        if (file.getSize() == 0) {
            return false;
        }
        if (!Objects.equals(file.getContentType(), "text/csv")) {
            return false;
        }
        return !file.isEmpty();
    }
}
