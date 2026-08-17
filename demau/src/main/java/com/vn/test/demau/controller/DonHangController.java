package com.vn.test.demau.controller;

import com.vn.test.demau.dto.DonHangDTO;
import com.vn.test.demau.entity.DonHang;
import com.vn.test.demau.service.DonHangService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/don-hang")
public class DonHangController {

    private final DonHangService donHangService;

    public DonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    @GetMapping
    public List<DonHangDTO> findAll() {
        return donHangService.findAll();
    }

    @GetMapping("/page")
    public List<DonHangDTO> findPage(@RequestParam(defaultValue = "0") int page) {
        return donHangService.findPage(page);
    }

    @PostMapping
    public DonHangDTO create(@Valid @RequestBody DonHang donHang) {
        return donHangService.create(donHang);
    }

    @PutMapping("/{id}")
    public DonHangDTO update(@PathVariable Integer id, @Valid @RequestBody DonHang donHang) {
        return donHangService.update(id, donHang);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        donHangService.delete(id);
    }
}
