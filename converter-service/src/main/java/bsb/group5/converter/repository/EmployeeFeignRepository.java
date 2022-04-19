package bsb.group5.converter.repository;

import bsb.group5.converter.service.model.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "EmployeeFeignRepository", url = "localhost:8082/api/employees")
public interface EmployeeFeignRepository {
    @GetMapping(value = "/{id}")
    EmployeeDTO getEmployeeById(
            @PathVariable Long id
    );
}
