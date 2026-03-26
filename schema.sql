-- SocialGraph Database Schema
-- Author: Vinay Bisht
-- Run: mysql -u root -p < schema.sql

CREATE DATABASE IF NOT EXISTS socialgraph_db;
USE socialgraph_db;

-- ─── UserNodes ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS UserNodes (
    id         INT          PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- ─── EdgeConnections ──────────────────────────────────────────────────────────
-- Stores undirected edges in canonical order (user_id_1 < user_id_2)
CREATE TABLE IF NOT EXISTS EdgeConnections (
    user_id_1  INT NOT NULL,
    user_id_2  INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id_1, user_id_2),
    FOREIGN KEY (user_id_1) REFERENCES UserNodes(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_2) REFERENCES UserNodes(id) ON DELETE CASCADE
);

-- ─── Demo Seed Data ───────────────────────────────────────────────────────────
INSERT IGNORE INTO UserNodes (id, name) VALUES
    (1,'Shifa'),(2,'Pihu'),(3,'Tanishka'),(4,'Shaurya'),(5,'Vinay'),
    (6,'Gourvansh'),(7,'Priyanshu'),(8,'Aaliya'),(9,'Yushra'),(10,'Zain');

INSERT IGNORE INTO EdgeConnections (user_id_1, user_id_2) VALUES
    (1,2),(1,3),(1,4),(2,3),(2,5),(3,6),(4,5),(4,7),(5,6),(5,8),
    (6,9),(7,8),(7,10),(8,9),(9,10),(3,7),(2,8),(1,9);
