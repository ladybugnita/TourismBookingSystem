package com.example.tourismbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;
    @Min(value =1,message ="Quantity must be at least 1")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "tourism_package_id", nullable = false)
    @JsonIgnoreProperties("bookings")
    private TourismPackage tourismPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"bookings"})
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    public static PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_reference")
    private String paymentReference;

    private LocalDate paymentDate;

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    public boolean isEditable(){
        return paymentStatus != PaymentStatus.COMPLETED;
    }

    public Booking() {
    }
    public Booking(int quantity, LocalDate bookingDate,TourismPackage tourismPackage, User user){
        this.quantity = quantity;
        this.bookingDate = bookingDate;
        this.tourismPackage = tourismPackage;
        this.user = user;
    }
    @PrePersist
    public void prePersist(){
        if (this.bookingDate == null){
            this.bookingDate = LocalDate.now();
        }
    }
    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
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
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public PaymentStatus getPaymentStatus(){
        return paymentStatus;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }
    public String getPaymentReference(){
        return paymentReference;
    }
    public void setPaymentReference(String paymentReference){
        this.paymentReference = paymentReference;
    }
    public LocalDate getPaymentDate(){
        return paymentDate;
    }
    public void setPaymentDate(LocalDate paymentDate){
        this.paymentDate = paymentDate;
    }
}
