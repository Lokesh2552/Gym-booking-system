package com.gymbooking.dao;

import com.gymbooking.DatabaseConnection;
import com.gymbooking.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDAO {
    public boolean bookGym(int userId, int gymId, Date bookingDate, String timeSlot) {
        String sql = "INSERT INTO bookings (user_id, gym_id, booking_date, time_slot) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, gymId);
            pstmt.setDate(3, new java.sql.Date(bookingDate.getTime()));
            pstmt.setString(4, timeSlot);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(rs.getInt("id"), userId, rs.getInt("gym_id"),
                        rs.getDate("booking_date"), rs.getString("time_slot")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
