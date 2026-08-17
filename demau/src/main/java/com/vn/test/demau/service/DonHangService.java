package com.vn.test.demau.service;

import com.vn.test.demau.dto.DonHangDTO;
import com.vn.test.demau.entity.DonHang;
import java.util.List;

public interface DonHangService {
    List<DonHangDTO> findAll();                 // muc 3

    List<DonHangDTO> findPage(int page);        // muc 7 - 5 phan tu/trang

    DonHangDTO create(DonHang donHang);         // muc 4

    DonHangDTO update(Integer id, DonHang donHang); // muc 5

    void delete(Integer id);                    // muc 6
}
