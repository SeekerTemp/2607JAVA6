package com.vn.test.demau.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "don_hang")
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Khach hang khong duoc de trong")
    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @NotBlank(message = "Ma don hang khong duoc de trong")
    @Column(name = "ma_don_hang")
    private String maDonHang;

    @NotNull(message = "Ngay dat khong duoc de trong")
    @Column(name = "ngay_dat")
    private LocalDate ngayDat;

    @NotNull(message = "Tong tien khong duoc de trong")
    @Positive(message = "Tong tien phai lon hon 0")
    @Column(name = "tong_tien")
    private Double tongTien;

    @Nationalized
    @Column(name = "dia_chi_giao")
    private String diaChiGiao;
    @Column(name = "so_dien_thoai_giao")
    private String soDienThoaiGiao;
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;
    @Nationalized
    @Column(name = "trang_thai")
    private String trangThai;
    @Nationalized
    @Column(name = "nguoi_xu_ly")
    private String nguoiXuLy;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public LocalDate getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDate ngayDat) { this.ngayDat = ngayDat; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public String getDiaChiGiao() { return diaChiGiao; }
    public void setDiaChiGiao(String diaChiGiao) { this.diaChiGiao = diaChiGiao; }

    public String getSoDienThoaiGiao() { return soDienThoaiGiao; }
    public void setSoDienThoaiGiao(String soDienThoaiGiao) { this.soDienThoaiGiao = soDienThoaiGiao; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getNguoiXuLy() { return nguoiXuLy; }
    public void setNguoiXuLy(String nguoiXuLy) { this.nguoiXuLy = nguoiXuLy; }
}
