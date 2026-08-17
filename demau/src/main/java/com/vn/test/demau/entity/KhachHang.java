package com.vn.test.demau.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import java.time.LocalDate;

@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Nationalized
    @Column(name = "ten_khach_hang")
    private String tenKhachHang;
    @Nationalized
    @Column(name = "dia_chi")
    private String diaChi;
    @Column(name = "so_dien_thoai")
    private String soDienThoai;
    private String email;
    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;
    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;
    @Column(name = "diem_tich_luy")
    private Double diemTichLuy;
    @Column(name = "trang_thai")
    private Boolean trangThai;
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public Boolean getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(Boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public Double getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(Double diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
