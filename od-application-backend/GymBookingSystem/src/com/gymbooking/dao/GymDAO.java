package com.gymbooking.dao;

import com.gymbooking.DatabaseConnection;
import com.gymbooking.model.Gym;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymDAO {
    public List<Gym> getAllGyms() {
        List<Gym> gyms = new ArrayList<>();
        String sql = "SELECT * FROM gyms";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                gyms.add(new Gym(rs.getInt("id"), rs.getString("name"), rs.getString("location"), rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gyms;
    }
}
