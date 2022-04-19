package bsb.group5.converter.service.impl;

import bsb.group5.converter.service.ParseCsvFileService;
import bsb.group5.converter.service.exceptions.ParseCSVException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static bsb.group5.converter.service.model.enums.AddApplicationColumnHeadersEnum.*;


@Component
@Slf4j
public class ParseCsvFileServiceImpl implements ParseCsvFileService {

    @Override
    public Iterable<CSVRecord> parse(MultipartFile file, Model responseBody) throws ParseCSVException {
        try (InputStream inputStream = file.getInputStream()) {
            CSVFormat csvFormat = getCsvFormat();
            try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {
                return csvParser.getRecords();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new ParseCSVException("fail to parse CSV file: " + e.getMessage());
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ParseCSVException("fail to read CSV file: " + e.getMessage());
        }
    }

    private CSVFormat getCsvFormat() {
        return CSVFormat.Builder.create()
                .setSkipHeaderRecord(true)
                .setDelimiter(",")
                .setTrim(true)
                .setQuote('"')
                .setQuoteMode(QuoteMode.ALL)
                .setRecordSeparator("\n")
                .setIgnoreEmptyLines(true)
                .setAllowDuplicateHeaderNames(false)
                .setAllowMissingColumnNames(false)
                .setHeader(ApplicationConvId.name(),
                        Value_Leg.name(),
                        Value_Ind.name(),
                        Percent_Conv.name(),
                        EmployeeId.name(),
                        NameLegal.name(),
                        Note.name())
                .build();
    }
}
