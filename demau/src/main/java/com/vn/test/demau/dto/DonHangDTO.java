package com.vn.test.demau.dto;

import com.vn.test.demau.entity.DonHang;
import java.time.LocalDate;

/** Muc 3: JSON tra ve gom id, maDonHang, ngayDat, tongTien, tenKhachHang, diaChi */
public class DonHangDTO {
    private Integer id;
    private String maDonHang;
    private LocalDate ngayDat;
    private Double tongTien;
    private String tenKhachHang;
    private String diaChi;

    public DonHangDTO(DonHang dh) {
        this.id = dh.getId();
        this.maDonHang = dh.getMaDonHang();
        this.ngayDat = dh.getNgayDat();
        this.tongTien = dh.getTongTien();
        this.tenKhachHang = dh.getKhachHang().getTenKhachHang();
        this.diaChi = dh.getKhachHang().getDiaChi();
    }

    public Integer getId() { return id; }
    public String getMaDonHang() { return maDonHang; }
    public LocalDate getNgayDat() { return ngayDat; }
    public Double getTongTien() { return tongTien; }
    public String getTenKhachHang() { return tenKhachHang; }
    public String getDiaChi() { return diaChi; }
}
