CREATE DATABASE IF NOT EXISTS framework_free_db;
USE framework_free_db;

CREATE USER IF NOT EXISTS 'java_app'@'localhost' IDENTIFIED BY 'secretpassword';
GRANT ALL PRIVILEGES ON framework_free_db.* TO 'java_app'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
