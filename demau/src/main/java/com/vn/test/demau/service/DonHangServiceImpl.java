package com.vn.test.demau.service;

import com.vn.test.demau.dto.DonHangDTO;
import com.vn.test.demau.entity.DonHang;
import com.vn.test.demau.repository.DonHangRepository;
import com.vn.test.demau.repository.KhachHangRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final KhachHangRepository khachHangRepository;

    public DonHangServiceImpl(DonHangRepository donHangRepository, KhachHangRepository khachHangRepository) {
        this.donHangRepository = donHangRepository;
        this.khachHangRepository = khachHangRepository;
    }

    @Override
    public List<DonHangDTO> findAll() {
        return donHangRepository.findAll().stream().map(DonHangDTO::new).toList();
    }

    @Override
    public List<DonHangDTO> findPage(int page) {
        return donHangRepository.findAll(PageRequest.of(page, 5)).map(DonHangDTO::new).getContent();
    }

    @Override
    public DonHangDTO create(DonHang donHang) {
        return save(donHang);
    }

    @Override
    public DonHangDTO update(Integer id, DonHang donHang) {
        donHang.setId(id);
        return save(donHang);
    }

    @Override
    public void delete(Integer id) {
        donHangRepository.deleteById(id);
    }

    private DonHangDTO save(DonHang donHang) {
        donHang.setKhachHang(khachHangRepository.findById(donHang.getKhachHang().getId()).orElseThrow());
        return new DonHangDTO(donHangRepository.save(donHang));
    }
}
