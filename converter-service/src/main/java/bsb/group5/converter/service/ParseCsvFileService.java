package bsb.group5.converter.service;

import bsb.group5.converter.service.exceptions.ParseCSVException;
import org.apache.commons.csv.CSVRecord;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface ParseCsvFileService {

    Iterable<CSVRecord> parse(MultipartFile file, Model model) throws ParseCSVException;
}
