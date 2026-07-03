-- Дефолтные юзеры для теста:
-- ADMIN: username=admin, password=Admin123!, email=admin@example.com
-- USER:  username=user,  password=User123!,  email=user@example.com

INSERT INTO users (username, password_hash, email, first_name, last_name, role)
VALUES
    ('admin', '$2b$10$MF9rhKMx/7ZXf6iF639MkujjFcXzRQk0ugLFNAP2yVKVD8kBWon0S', 'admin@example.com', 'Admin', 'User', 'ADMIN'),
    ('user', '$2b$10$SlP11Mic2aVmuYGrnjolZe8mk.HKjvjBZx.Kj/e8QKSwL0jWEgCA2', 'user@example.com', 'Test', 'User', 'USER')
ON CONFLICT (username) DO NOTHING;
