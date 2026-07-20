package com.example.demo.api;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Project cổng 8002 - danh sách & chi tiết companies (id, name)
@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {
    @Autowired
    private CompanyRepository companyRepository;

    // Hiển thị danh sách (id, name)
    @GetMapping
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    // Hiển thị chi tiết đối tượng theo mã
    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        return companyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Company create(@RequestBody Company company) {
        company.setId(null);
        return companyRepository.save(company);
    }
}
