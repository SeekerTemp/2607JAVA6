package com.example.demo.config;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Nạp dữ liệu companies mẫu khi khởi động
@Component
public class CompanySeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;

    public CompanySeeder(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) {
        if (companyRepository.count() > 0) {
            return;
        }
        companyRepository.save(new Company(null, "FPT Software"));
        companyRepository.save(new Company(null, "Viettel"));
        companyRepository.save(new Company(null, "VNG Corporation"));
        companyRepository.save(new Company(null, "Tiki"));
        companyRepository.save(new Company(null, "Shopee"));
    }
}
