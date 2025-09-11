package com.example.tourismbooking.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "tourism_package")
@JsonIgnoreProperties({"bookings"})
public class TourismPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Integer totalQuantity;
    private Integer availableQuantity;
    @NotBlank
    private String description;
    @NotNull
    private double price;
    @NotNull
    private LocalDate bookingDeadline;

    @OneToMany(mappedBy ="tourismPackage", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
    public TourismPackage(){}
    public Long getId(){
        return id;
    }
    public void setId(Long id){

        this.id =id;
    }
    public String getName(){

        return name;
    }
    public void setName(String name){

        this.name =name;
    }
    public double getPrice(){

        return price;
    }
    public void setPrice(double price){

        this.price = price;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){

        this.description = description;
    }
    public Integer getTotalQuantity(){
        return totalQuantity;
    }
    public void setTotalQuantity(Integer totalQuantity){
        this.totalQuantity=totalQuantity;
    }
    public Integer getAvailableQuantity(){
        return availableQuantity;
    }
    public void setAvailableQuantity(Integer availableQuantity){
        this.availableQuantity=availableQuantity;
    }
    public LocalDate getBookingDeadline(){
        return bookingDeadline;
    }
    public void setBookingDeadline(LocalDate bookingDeadline){
        this.bookingDeadline = bookingDeadline;
    }
    public List<Booking> getBookings(){
        return bookings;
    }
    public void setBookings(List<Booking> bookings){
        this.bookings = bookings;
    }
    @PrePersist
    public void prePersist(){
        if(this.availableQuantity == null && this.totalQuantity != null){
            this.availableQuantity = this.totalQuantity;
        }
    }
}
