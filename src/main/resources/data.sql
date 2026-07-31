INSERT INTO app_user (username, password, fullname, enabled, role)
SELECT 'user@gmail.com', '123', 'Người dùng', TRUE, 'USER'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE username = 'user@gmail.com');

INSERT INTO app_user (username, password, fullname, enabled, role)
SELECT 'admin@gmail.com', '123', 'Quản trị', TRUE, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM app_user WHERE username = 'admin@gmail.com');
