package com.example.tourismbooking.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "tourism_package")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","bookings"})
public class TourismPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message ="package name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "User name can contain only alphabets and spaces")
    private String name;
    @NotNull(message = "Total quantity is required")
    @Positive(message = "Total quantity must be greater than 0")
    private Integer totalQuantity;
    private Integer availableQuantity;
    @NotBlank(message ="Description is required")
    private String description;
    @Positive(message="price must be greater than 0")
    @NotNull(message = "Price is required")
    private double price;
    @NotNull(message =" Booking deadline is required")
    @FutureOrPresent(message = "Booking deadline must be today or in the future")
    private LocalDate bookingDeadline;

    @OneToMany(mappedBy ="tourismPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("tourismPackage")
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
