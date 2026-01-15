package com.gymbooking.model;

import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private int gymId;
    private Date bookingDate;
    private String timeSlot;

    // Constructors, getters
    public Booking(int id, int userId, int gymId, Date bookingDate, String timeSlot) {
        this.id = id;
        this.userId = userId;
        this.gymId = gymId;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
