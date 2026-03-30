-- database_jastip_internal.sql
CREATE DATABASE IF NOT EXISTS jastip_internal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jastip_internal;

DROP TABLE IF EXISTS order_status_logs;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(30) DEFAULT '',
    password_hash VARCHAR(255) NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address TEXT NOT NULL,
    notes TEXT,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    INDEX idx_customer_phone (phone)
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    description TEXT,
    price INT NOT NULL,
    origin_city VARCHAR(100) NOT NULL,
    eta_days INT NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(40) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price INT NOT NULL,
    total_amount INT NOT NULL,
    status ENUM('pending','diproses','dibelikan','dikirim','selesai','dibatalkan') NOT NULL DEFAULT 'pending',
    notes TEXT,
    created_by INT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_orders_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_orders_user FOREIGN KEY (created_by) REFERENCES users(id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created_by (created_by)
);

CREATE TABLE order_status_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    old_status VARCHAR(20) DEFAULT NULL,
    new_status VARCHAR(20) NOT NULL,
    changed_by INT NOT NULL,
    changed_at DATETIME NOT NULL,
    notes VARCHAR(255) DEFAULT '',
    CONSTRAINT fk_logs_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_logs_user FOREIGN KEY (changed_by) REFERENCES users(id)
);

INSERT INTO roles (name) VALUES ('manager'), ('petugas');

-- password: manager123
INSERT INTO users (role_id, name, email, phone, password_hash, is_active, created_at, updated_at)
VALUES (
    (SELECT id FROM roles WHERE name='manager'),
    'Manager Utama',
    'manager@jastipro.local',
    '081234567890',
    '$2y$10$ceflpwJcwwBu4myVF3hdJu5kL6OUG.bmYsqRdkqNxsfC53dBAyFDa',
    1,
    NOW(), NOW()
);

-- password: petugas123
INSERT INTO users (role_id, name, email, phone, password_hash, is_active, created_at, updated_at)
VALUES (
    (SELECT id FROM roles WHERE name='petugas'),
    'Petugas Satu',
    'petugas@jastipro.local',
    '081234567891',
    '$2y$10$g0m/t0IlN3QN8l8hpaE/xOFM6BFAx.3ILGtVgxq0x1PaWabY3p5xu',
    1,
    NOW(), NOW()
);

INSERT INTO customers (name, phone, address, notes, is_active, created_at, updated_at) VALUES
('Rina', '08111111111', 'Bandung', 'Customer rutin', 1, NOW(), NOW()),
('Budi', '08222222222', 'Jakarta', 'Minta cepat', 1, NOW(), NOW()),
('Salsa', '08333333333', 'Surabaya', '-', 1, NOW(), NOW());

INSERT INTO products (name, description, price, origin_city, eta_days, is_active, created_at, updated_at) VALUES
('Tas Branded A', 'Titip mall SG', 750000, 'Singapore', 7, 1, NOW(), NOW()),
('Sepatu Running B', 'Diskon seasonal', 1200000, 'Kuala Lumpur', 6, 1, NOW(), NOW()),
('Skincare C', 'Original duty-free', 450000, 'Bangkok', 5, 1, NOW(), NOW());

INSERT INTO orders (order_code, customer_id, product_id, quantity, unit_price, total_amount, status, notes, created_by, created_at, updated_at) VALUES
('ORD-20260330-001', 1, 1, 1, 750000, 750000, 'pending', 'Prioritas', 2, NOW(), NOW()),
('ORD-20260330-002', 2, 2, 2, 1200000, 2400000, 'diproses', 'DP sudah masuk', 2, NOW(), NOW()),
('ORD-20260330-003', 3, 3, 1, 450000, 450000, 'selesai', 'Sudah diterima', 1, NOW(), NOW());

INSERT INTO order_status_logs (order_id, old_status, new_status, changed_by, changed_at, notes) VALUES
(1, NULL, 'pending', 2, NOW(), 'Order dibuat'),
(2, NULL, 'pending', 2, NOW(), 'Order dibuat'),
(2, 'pending', 'diproses', 2, NOW(), 'Mulai proses'),
(3, NULL, 'pending', 1, NOW(), 'Order dibuat'),
(3, 'pending', 'selesai', 1, NOW(), 'Selesai');

