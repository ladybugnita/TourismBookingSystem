package com.example.tourismbooking.controller;

import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.repository.TourismPackageRepository;
import com.example.tourismbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourismPackageRepository tourismPackageRepository;

    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        TourismPackage tourPackage = tourismPackageRepository.findById(booking.getTourismPackage().getId())
                .orElseThrow(() -> new RuntimeException("Package not found"));
        booking.setTourismPackage(tourPackage);
        return bookingRepository.save(booking);
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("/bookings/{id}")
    public Booking getBookingsById(@PathVariable Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }

    @DeleteMapping("/bookings/{id}")
    public void cancelBooking(@PathVariable Long id) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID :" + id));
        bookingRepository.delete(existingBooking);
    }

    @PutMapping("/bookings/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (updatedBooking.getUserName() != null) {
            existingBooking.setUserName(updatedBooking.getUserName());
        }
        if (updatedBooking.getUserEmail() != null) {
            existingBooking.setUserEmail(updatedBooking.getUserEmail());
        }
        if (updatedBooking.getBookingDate() != null) {
            existingBooking.setBookingDate(updatedBooking.getBookingDate());
        }
        if (updatedBooking.getQuantity() != 0) {
            existingBooking.setQuantity(updatedBooking.getQuantity());
        }
        if (updatedBooking.getTourismPackage() != null) {
            TourismPackage tourPackage = tourismPackageRepository.findById(updatedBooking.getTourismPackage().getId())
                    .orElseThrow(() -> new RuntimeException("package not found"));
            existingBooking.setTourismPackage(tourPackage);
        }
        return bookingRepository.save(existingBooking);
    }
}


