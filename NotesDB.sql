CREATE DATABASE NotesDB;
GO
USE NotesDB;
GO
CREATE TABLE Notes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    author NVARCHAR(100) NOT NULL,
    content NVARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NULL
);
GO
INSERT INTO Notes (author, content) VALUES
(N'Trung', N'Ghi chú họp nhóm vào thứ Hai.'),
(N'Lan', N'Nhớ nộp bài trước thứ Sáu.'),
(N'Bảo', N'Liên hệ giảng viên về deadline.');
GO