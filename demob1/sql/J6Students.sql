-- =============================================================
-- LAB 5 - Bài 4: CSDL J6Students cho Spring Boot REST API
-- SQL Server
-- =============================================================

IF DB_ID('J6Students') IS NULL
    CREATE DATABASE J6Students;
GO
USE J6Students;
GO

IF OBJECT_ID('J6Students', 'U') IS NOT NULL DROP TABLE J6Students;
GO

CREATE TABLE J6Students (
    Id     VARCHAR(50)  NOT NULL PRIMARY KEY,
    Name   NVARCHAR(50) NOT NULL,
    Mark   FLOAT        NOT NULL,
    Gender BIT          NOT NULL
);
GO

INSERT INTO J6Students(Id, Name, Mark, Gender) VALUES
    ('SV001', N'Lý Thái Tổ',            9.5, 1),
    ('SV002', N'Lê Trọng Tấn',          4.5, 1),
    ('SV003', N'Nguyễn Thị Minh Khai',  9.5, 0),
    ('SV004', N'Đoàn Trung Trực',       6.0, 1);
GO
