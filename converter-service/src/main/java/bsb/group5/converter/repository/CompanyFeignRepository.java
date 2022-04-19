package bsb.group5.converter.repository;

import bsb.group5.converter.service.model.CompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "CompanyFeignRepository", url = "localhost:8081/api/legals")
public interface CompanyFeignRepository {
    @GetMapping
    List<CompanyDTO> getCompanyByNameLegal(
            @RequestParam(name = "Name_Legal")
                    String nameLegal
    );
}
