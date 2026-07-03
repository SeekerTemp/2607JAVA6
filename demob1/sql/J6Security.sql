-- =============================================================
-- LAB 2 - Bài 2 (THAM KHẢO): CSDL J6Security cho JdbcUserDetailsManager
-- Lược đồ mặc định của Spring Security. SQL Server.
-- =============================================================

IF DB_ID('J6Security') IS NULL
    CREATE DATABASE J6Security;
GO
USE J6Security;
GO

IF OBJECT_ID('Authorities', 'U') IS NOT NULL DROP TABLE Authorities;
IF OBJECT_ID('Users', 'U')       IS NOT NULL DROP TABLE Users;
GO

CREATE TABLE Users (
    Username VARCHAR(50)  NOT NULL,
    Password VARCHAR(500) NOT NULL,
    Enabled  BIT          NOT NULL,
    PRIMARY KEY (Username)
);

CREATE TABLE Authorities (
    Id        BIGINT      NOT NULL IDENTITY(1,1),
    Username  VARCHAR(50) NOT NULL,
    Authority VARCHAR(50) NOT NULL,
    PRIMARY KEY (Id),
    UNIQUE (Username, Authority),
    FOREIGN KEY (Username) REFERENCES Users(Username)
        ON DELETE CASCADE ON UPDATE CASCADE
);
GO

INSERT INTO Users(Username, Password, Enabled) VALUES
    ('user@gmail.com',  '{noop}123', 1),
    ('admin@gmail.com', '{noop}123', 1),
    ('both@gmail.com',  '{noop}123', 1);

INSERT INTO Authorities(Username, Authority) VALUES
    ('user@gmail.com',  'ROLE_USER'),
    ('admin@gmail.com', 'ROLE_ADMIN'),
    ('both@gmail.com',  'ROLE_USER'),
    ('both@gmail.com',  'ROLE_ADMIN');
GO
