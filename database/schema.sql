-- Users Table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role ENUM('EMPLOYEE', 'MANAGER', 'ADMIN') DEFAULT 'EMPLOYEE',
    github_token VARCHAR(255),
    github_username VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Manager-Employee Mapping
CREATE TABLE manager_employee_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    manager_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES users(id),
    FOREIGN KEY (employee_id) REFERENCES users(id),
    UNIQUE KEY unique_mapping (manager_id, employee_id)
);

-- Daily Status Table
CREATE TABLE daily_status (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    status_date DATE NOT NULL,
    status_description TEXT,
    status_type ENUM('COMPLETED', 'IN_PROGRESS', 'BLOCKED') DEFAULT 'IN_PROGRESS',
    submitted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES users(id),
    UNIQUE KEY unique_daily_status (employee_id, status_date)
);

-- GitHub Commits Table
CREATE TABLE github_commits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    commit_hash VARCHAR(255),
    commit_message TEXT,
    repository_name VARCHAR(255),
    commit_date TIMESTAMP,
    author_name VARCHAR(100),
    fetch_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES users(id),
    INDEX idx_employee_date (employee_id, fetch_date)
);

-- Notification Tracking Table
CREATE TABLE notification_tracking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    manager_id BIGINT NOT NULL,
    missed_date DATE,
    notification_count INT DEFAULT 1,
    last_notification_date TIMESTAMP,
    status ENUM('PENDING', 'NOTIFIED', 'RESOLVED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES users(id),
    FOREIGN KEY (manager_id) REFERENCES users(id)
);

-- Email Logs Table
CREATE TABLE email_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    manager_id BIGINT NOT NULL,
    employee_id BIGINT,
    email_subject VARCHAR(255),
    email_body TEXT,
    sent_date TIMESTAMP,
    status ENUM('SENT', 'FAILED') DEFAULT 'SENT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES users(id)
);
