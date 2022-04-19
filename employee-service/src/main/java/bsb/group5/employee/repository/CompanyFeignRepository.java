package bsb.group5.employee.repository;

import bsb.group5.employee.repository.model.CompanyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "CompanyServiceFeignRepository", url = "${api.companies}")
public interface CompanyFeignRepository {
    @GetMapping()
    List<CompanyDTO> search(
            @RequestParam String Name_Legal,
            @RequestParam Integer UNP
    );
}
