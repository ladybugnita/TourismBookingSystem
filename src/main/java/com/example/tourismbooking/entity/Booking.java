package com.example.tourismbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "User name can contain only alphabets and spaces")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String userEmail;

    @NotNull(message = "Booking date is required")
    private LocalDate bookingDate;
    @Min(value =1,message ="Quantity must be at least 1")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "tourism_package_id", nullable = false)
    @JsonIgnoreProperties("bookings")
    private TourismPackage tourismPackage;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_reference")
    private String paymentReference;

    public Booking() {
    }
    public Booking(String userName, String userEmail,int quantity, TourismPackage tourismPackage){
        this.userName = userName;
        this.userEmail = userEmail;
        this.quantity = quantity;
        this.tourismPackage = tourismPackage;
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
}
