-- =============================================================
-- LAB 7 - CSDL J6Shop: loại hàng, sản phẩm, người sử dụng
-- SQL Server
-- =============================================================

IF DB_ID('J6Shop') IS NULL
    CREATE DATABASE J6Shop;
GO
USE J6Shop;
GO

IF OBJECT_ID('Products', 'U')   IS NOT NULL DROP TABLE Products;
IF OBJECT_ID('Categories', 'U') IS NOT NULL DROP TABLE Categories;
IF OBJECT_ID('Accounts', 'U')   IS NOT NULL DROP TABLE Accounts;
GO

CREATE TABLE Categories (
    Id   VARCHAR(50)  NOT NULL PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL
);

CREATE TABLE Products (
    Id         VARCHAR(50)   NOT NULL PRIMARY KEY,
    Name       NVARCHAR(100) NOT NULL,
    Price      FLOAT         NOT NULL,
    Date       DATE          NULL,
    CategoryId VARCHAR(50)   NULL,
    CONSTRAINT FK_products_categories FOREIGN KEY (CategoryId) REFERENCES Categories(Id)
);

CREATE TABLE Accounts (
    Username VARCHAR(50)   NOT NULL PRIMARY KEY,
    Password VARCHAR(100)  NOT NULL,
    Fullname NVARCHAR(100) NULL,
    Enabled  BIT           NOT NULL,
    Role     VARCHAR(20)   NOT NULL
);
GO

-- Dữ liệu mẫu ------------------------------------------------
INSERT INTO Categories(Id, Name) VALUES
    ('C01', N'Điện thoại'),
    ('C02', N'Laptop'),
    ('C03', N'Phụ kiện');

INSERT INTO Products(Id, Name, Price, Date, CategoryId) VALUES
    ('P01', N'iPhone 15',      25000000, '2026-01-10', 'C01'),
    ('P02', N'Galaxy S24',     20000000, '2026-02-15', 'C01'),
    ('P03', N'MacBook Air M3', 32000000, '2026-03-01', 'C02'),
    ('P04', N'Chuột Logitech',   500000, '2026-03-20', 'C03');

INSERT INTO Accounts(Username, Password, Fullname, Enabled, Role) VALUES
    ('user@gmail.com',  '123', N'Người dùng', 1, 'USER'),
    ('admin@gmail.com', '123', N'Quản trị',   1, 'ADMIN');
GO
