package com.example.tourismbooking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String userEmail;
    private LocalDate bookingDate;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "tourism_package_id")
    private TourismPackage tourismPackage;
    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TourismPackage getTourismPackage() {
        return tourismPackage;
    }

    public void setTourismPackage(TourismPackage tourismPackage) {
        this.tourismPackage = tourismPackage;
    }
}
