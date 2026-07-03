-- =============================================================
-- LAB 2 - Bài 3: CSDL J6Security2 cho DaoUserDetailsManager
-- SQL Server
-- =============================================================

IF DB_ID('J6Security2') IS NULL
    CREATE DATABASE J6Security2;
GO
USE J6Security2;
GO

IF OBJECT_ID('J6userroles', 'U') IS NOT NULL DROP TABLE J6userroles;
IF OBJECT_ID('J6users', 'U')     IS NOT NULL DROP TABLE J6users;
IF OBJECT_ID('J6roles', 'U')     IS NOT NULL DROP TABLE J6roles;
GO

CREATE TABLE J6users (
    Username VARCHAR(50)  NOT NULL PRIMARY KEY,
    Password VARCHAR(500) NOT NULL,
    Enabled  BIT          NOT NULL
);

CREATE TABLE J6roles (
    Id   VARCHAR(50)  NOT NULL PRIMARY KEY,
    Name NVARCHAR(50) NOT NULL
);

CREATE TABLE J6userroles (
    Id       BIGINT      NOT NULL IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Roleid   VARCHAR(50) NOT NULL,
    CONSTRAINT FK_userroles_users FOREIGN KEY (Username) REFERENCES J6users(Username)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_userroles_roles FOREIGN KEY (Roleid)   REFERENCES J6roles(Id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
GO

-- Dữ liệu mẫu ------------------------------------------------
INSERT INTO J6users(Username, Password, Enabled) VALUES
    ('user@gmail.com',  '{noop}123', 1),
    ('admin@gmail.com', '{noop}123', 1),
    ('both@gmail.com',  '{noop}123', 1);

INSERT INTO J6roles(Id, Name) VALUES
    ('ROLE_USER',  N'Nhân viên'),
    ('ROLE_ADMIN', N'Quản lý');

INSERT INTO J6userroles(Username, Roleid) VALUES
    ('user@gmail.com',  'ROLE_USER'),
    ('admin@gmail.com', 'ROLE_ADMIN'),
    ('both@gmail.com',  'ROLE_USER'),
    ('both@gmail.com',  'ROLE_ADMIN');
GO
