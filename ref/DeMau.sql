CREATE DATABASE DeMauSOF3062;
GO

USE DeMauSOF3062;
GO

CREATE TABLE khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ten_khach_hang NVARCHAR(100),
    dia_chi NVARCHAR(200),
    so_dien_thoai VARCHAR(20),
    email VARCHAR(100),
    ngay_sinh DATE,
    gioi_tinh BIT,
    diem_tich_luy FLOAT,
    trang_thai BIT,
    ghi_chu NVARCHAR(255)
)
GO

CREATE TABLE don_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    khach_hang_id INT,
    ma_don_hang VARCHAR(50),
    ngay_dat DATE,
    tong_tien FLOAT,
    dia_chi_giao NVARCHAR(200),
    so_dien_thoai_giao VARCHAR(20),
    ghi_chu NVARCHAR(255),
    trang_thai NVARCHAR(50),
    nguoi_xu_ly NVARCHAR(100),
    FOREIGN KEY (khach_hang_id) REFERENCES khach_hang(id)
)
GO

INSERT INTO khach_hang (ten_khach_hang, dia_chi, so_dien_thoai, email, ngay_sinh, gioi_tinh, diem_tich_luy, trang_thai, ghi_chu) VALUES
(N'Nguyen Van A', N'Ha Noi', '0901234567', 'a@example.com', '1990-05-12', 1, 120.5, 1, N'Khach hang VIP'),
(N'Tran Thi B', N'Hai Phong', '0912345678', 'b@example.com', '1985-08-20', 0, 80.0, 1, N'Khach hang than thiet'),
(N'Le Van C', N'Da Nang', '0923456789', 'c@example.com', '1992-03-15', 1, 45.75, 0, N'Tam dung giao dich'),
(N'Pham Thi D', N'Can Tho', '0934567890', 'd@example.com', '1995-11-02', 0, 150.0, 1, N'Mua hang thuong xuyen'),
(N'Hoang Van E', N'Vung Tau', '0945678901', 'e@example.com', '1988-07-21', 1, 300.25, 1, N'Khach hang doanh nghiep')
GO

INSERT INTO don_hang (khach_hang_id, ma_don_hang, ngay_dat, tong_tien, dia_chi_giao, so_dien_thoai_giao, ghi_chu, trang_thai, nguoi_xu_ly) VALUES
(1, 'DH001', '2025-08-01', 1500000, N'Ha Noi', '0901234567', N'Giao gio hanh chinh', N'Da giao', N'Nguyen Thi X'),
(1, 'DH002', '2025-08-05', 2000000, N'Ha Noi', '0901234567', N'Giao gap', N'Dang giao', N'Tran Van Y'),
(1, 'DH003', '2025-08-10', 500000, N'Ha Noi', '0901234567', N'Khach quen', N'Cho xac nhan', N'Le Thi Z'),
(2, 'DH004', '2025-08-02', 750000, N'Hai Phong', '0912345678', N'Giao sam', N'Da giao', N'Pham Van K'),
(2, 'DH005', '2025-08-06', 1250000, N'Hai Phong', '0912345678', N'Mua hang online', N'Dang giao', N'Nguyen Van L'),
(2, 'DH006', '2025-08-09', 300000, N'Hai Phong', '0912345678', N'Khach yeu cau goi truoc', N'Cho xac nhan', N'Tran Thi M'),
(3, 'DH007', '2025-08-03', 950000, N'Da Nang', '0923456789', N'Giao hang nhanh', N'Da giao', N'Le Van N'),
(3, 'DH008', '2025-08-07', 2200000, N'Da Nang', '0923456789', N'Hang uu tien', N'Dang giao', N'Pham Thi O'),
(3, 'DH009', '2025-08-11', 640000, N'Da Nang', '0923456789', N'Yeu cau bao hanh', N'Cho xac nhan', N'Nguyen Van P'),
(4, 'DH010', '2025-08-04', 890000, N'Can Tho', '0934567890', N'Giao tan nay', N'Da giao', N'Tran Van Q'),
(4, 'DH011', '2025-08-08', 1400000, N'Can Tho', '0934567890', N'Khach hang moi', N'Dang giao', N'Le Thi R'),
(4, 'DH012', '2025-08-12', 350000, N'Can Tho', '0934567890', N'Giao trong ngay', N'Cho xac nhan', N'Pham Van S'),
(5, 'DH013', '2025-08-01', 2700000, N'Vung Tau', '0945678901', N'Don hang lon', N'Da giao', N'Nguyen Thi T'),
(5, 'DH014', '2025-08-06', 1600000, N'Vung Tau', '0945678901', N'Khach hang doanh nghiep', N'Dang giao', N'Tran Van U'),
(5, 'DH015', '2025-08-09', 450000, N'Vung Tau', '0945678901', N'Giao hang ban dem', N'Cho xac nhan', N'Le Van V')
GO
