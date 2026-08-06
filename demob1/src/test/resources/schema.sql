-- Ban sao schema Notes trong NotesDB.sql, dung cho test tren H2 (MODE=MSSQLServer).
-- Giu nguyen kieu cot cua de bai de test bat duoc loi anh xa entity.
DROP TABLE IF EXISTS Notes;

CREATE TABLE Notes (
    id INT IDENTITY(1,1) PRIMARY KEY,
    author NVARCHAR(100) NOT NULL,
    content NVARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NULL
);
