INSERT INTO notes (author, content, created_at) VALUES
  ('Trung', 'Ghi chú họp nhóm vào thứ Hai.', DATEADD('MINUTE', -30, CURRENT_TIMESTAMP)),
  ('Lan', 'Nhớ nộp bài trước thứ Sáu.', DATEADD('MINUTE', -20, CURRENT_TIMESTAMP)),
  ('Bảo', 'Liên hệ giảng viên về deadline.', DATEADD('MINUTE', -10, CURRENT_TIMESTAMP));
