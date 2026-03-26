package com.socialgraph.db;

import com.socialgraph.core.Graph;

import java.sql.*;

/**
 * DatabaseConnector — JDBC implementation for persistent MySQL storage.
 * Schema: UserNodes(id, name) | EdgeConnections(user_id_1, user_id_2)
 * Author: Vinay Bisht
 */
public class DatabaseConnector {

    private static final String URL      = "jdbc:mysql://localhost:3306/socialgraph_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "your_password_here"; // ← change this

    private Connection connection;

    // ─── Connect ─────────────────────────────────────────────────────────────

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("[DB] Connected to MySQL: socialgraph_db");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("[DB] Disconnected.");
        }
    }

    // ─── Schema Initialisation ───────────────────────────────────────────────

    public void initSchema() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS UserNodes (
                id   INT PRIMARY KEY,
                name VARCHAR(100) NOT NULL
            )
        """);
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS EdgeConnections (
                user_id_1 INT NOT NULL,
                user_id_2 INT NOT NULL,
                PRIMARY KEY (user_id_1, user_id_2),
                FOREIGN KEY (user_id_1) REFERENCES UserNodes(id),
                FOREIGN KEY (user_id_2) REFERENCES UserNodes(id)
            )
        """);
        System.out.println("[DB] Schema ready.");
    }

    // ─── Persist Graph to DB ─────────────────────────────────────────────────

    public void saveUser(int id, String name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT IGNORE INTO UserNodes (id, name) VALUES (?, ?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.executeUpdate();
    }

    public void saveEdge(int u, int v) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT IGNORE INTO EdgeConnections (user_id_1, user_id_2) VALUES (?, ?)");
        ps.setInt(1, Math.min(u, v)); // Canonical order to avoid duplicates
        ps.setInt(2, Math.max(u, v));
        ps.executeUpdate();
    }

    // ─── Load Graph from DB ───────────────────────────────────────────────────

    /**
     * Reconstructs the full Graph object from MySQL.
     * Maps DB rows → Java Graph objects (for the Core Engine to process).
     */
    public Graph loadGraph() throws SQLException {
        Graph graph = new Graph();

        // Load users
        ResultSet rsUsers = connection.createStatement()
                .executeQuery("SELECT id, name FROM UserNodes");
        while (rsUsers.next()) {
            graph.addUser(rsUsers.getInt("id"), rsUsers.getString("name"));
        }

        // Load edges
        ResultSet rsEdges = connection.createStatement()
                .executeQuery("SELECT user_id_1, user_id_2 FROM EdgeConnections");
        while (rsEdges.next()) {
            graph.addConnection(rsEdges.getInt("user_id_1"), rsEdges.getInt("user_id_2"));
        }

        System.out.printf("[DB] Loaded %d users, %d edges from MySQL.%n",
                graph.getVertexCount(), graph.getEdgeCount());
        return graph;
    }

    // ─── Delete ──────────────────────────────────────────────────────────────

    public void deleteUser(int id) throws SQLException {
        connection.createStatement()
                .executeUpdate("DELETE FROM UserNodes WHERE id = " + id);
    }

    public Connection getConnection() { return connection; }
}
