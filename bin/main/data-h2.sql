-- Sample data for charts visualization
-- This file will be automatically executed by Spring Boot

-- Insert a test user if not exists
MERGE INTO dbo.users (user_id, username, password_hash, created_at) 
KEY(user_id)
VALUES (1, 'testuser', '$2a$10$dummyhashfordemo', CURRENT_TIMESTAMP);

-- Delete existing sample transactions to avoid duplicates
DELETE FROM dbo.transactions WHERE user_id = 1 AND description LIKE 'Salary%' OR description LIKE '%Groceries%';

-- Sample transactions for October
INSERT INTO dbo.transactions (user_id, category_id, amount, txn_type, txn_date, description) VALUES
(1, 1, 3300.00, 'INCOME', '2024-10-01', 'Salary October'),
(1, 4, 500.00, 'EXPENSE', '2024-10-05', 'Groceries'),
(1, 5, 150.00, 'EXPENSE', '2024-10-10', 'Movie tickets'),
(1, 6, 200.00, 'EXPENSE', '2024-10-12', 'Gas'),
(1, 7, 1200.00, 'EXPENSE', '2024-10-15', 'Rent and utilities'),
(1, 4, 300.00, 'EXPENSE', '2024-10-20', 'Groceries'),

-- Sample transactions for November
(1, 1, 3350.00, 'INCOME', '2024-11-01', 'Salary November'),
(1, 4, 550.00, 'EXPENSE', '2024-11-05', 'Groceries'),
(1, 5, 200.00, 'EXPENSE', '2024-11-08', 'Concert'),
(1, 6, 180.00, 'EXPENSE', '2024-11-12', 'Transportation'),
(1, 7, 1250.00, 'EXPENSE', '2024-11-15', 'Bills and utilities'),
(1, 8, 250.00, 'EXPENSE', '2024-11-20', 'New shoes'),

-- Sample transactions for December
(1, 1, 3400.00, 'INCOME', '2024-12-01', 'Salary December'),
(1, 4, 600.00, 'EXPENSE', '2024-12-05', 'Groceries'),
(1, 5, 180.00, 'EXPENSE', '2024-12-10', 'Entertainment'),
(1, 6, 220.00, 'EXPENSE', '2024-12-12', 'Gas and parking'),
(1, 7, 1150.00, 'EXPENSE', '2024-12-15', 'Utilities'),
(1, 12, 150.00, 'EXPENSE', '2024-12-18', 'Christmas gifts'),

-- Sample transactions for January
(1, 1, 3420.00, 'INCOME', '2025-01-01', 'Salary January'),
(1, 4, 580.00, 'EXPENSE', '2025-01-05', 'Groceries'),
(1, 5, 200.00, 'EXPENSE', '2025-01-10', 'New Year party'),
(1, 6, 250.00, 'EXPENSE', '2025-01-12', 'Transport'),
(1, 7, 1280.00, 'EXPENSE', '2025-01-15', 'Bills'),
(1, 10, 150.00, 'EXPENSE', '2025-01-20', 'Doctor visit');
