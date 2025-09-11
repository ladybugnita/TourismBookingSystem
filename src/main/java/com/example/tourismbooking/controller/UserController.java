package com.example.tourismbooking.controller;

import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.repository.TourismPackageRepository;
import com.example.tourismbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourismPackageRepository tourismPackageRepository;

    @Transactional
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        if (booking.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be at least 1");
        }
        Long pkgId = booking.getTourismPackage()== null ? null : booking.getTourismPackage().getId();
        if (pkgId == null) {
            throw new RuntimeException("Tourism Package Id is required");
        }
        TourismPackage tourPackage = tourismPackageRepository.findById(pkgId)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        if(LocalDate.now().isAfter(tourPackage.getBookingDeadline())){
            throw new RuntimeException("Booking deadline has passed for this package");
        }
        if (tourPackage.getAvailableQuantity() == null || booking.getQuantity() > tourPackage.getAvailableQuantity()) {
            throw new RuntimeException("Not enough available quantity for this package");
        }
        booking.setTourismPackage(tourPackage);
        booking.setBookingDate(LocalDate.now());

        tourPackage.setAvailableQuantity(tourPackage.getAvailableQuantity() - booking.getQuantity());
        tourismPackageRepository.save(tourPackage);
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
    @Transactional
    @DeleteMapping("/bookings/{id}")
    public void cancelBooking(@PathVariable Long id) {
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID :" + id));
        TourismPackage tourPackage = existingBooking.getTourismPackage();
        tourPackage.setAvailableQuantity(tourPackage.getAvailableQuantity() + existingBooking.getQuantity());
        tourismPackageRepository.save(tourPackage);
        bookingRepository.delete(existingBooking);
    }

    @Transactional
    @PutMapping("/bookings/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (updatedBooking.getUserName() != null) {
            existingBooking.setUserName(updatedBooking.getUserName());
        }
        if (updatedBooking.getUserEmail() != null) {
            existingBooking.setUserEmail(updatedBooking.getUserEmail());
        }
        int newQuantity = (updatedBooking.getQuantity() >0) ? updatedBooking.getQuantity(): existingBooking.getQuantity();
        Long newpkgId = (updatedBooking.getTourismPackage() != null) ? updatedBooking.getTourismPackage().getId() : existingBooking.getTourismPackage().getId();
        TourismPackage newPackage = tourismPackageRepository.findById(newpkgId)
                .orElseThrow(() -> new RuntimeException("Booking deadline has passed for this package"));

        if (LocalDate.now().isAfter(newPackage.getBookingDeadline())) {
            throw new RuntimeException("Booking deadline has passed for this package");
        }
        TourismPackage oldPackage = existingBooking.getTourismPackage();

        if (!newPackage.getId().equals(oldPackage.getId())) {
            oldPackage.setAvailableQuantity(oldPackage.getAvailableQuantity() + existingBooking.getQuantity());
            tourismPackageRepository.save(oldPackage);
            if (newPackage.getAvailableQuantity() == null || newQuantity > newPackage.getAvailableQuantity()) {
                throw new RuntimeException("Not enough available quantity for this package");
            }
            newPackage.setAvailableQuantity(newPackage.getAvailableQuantity() - newQuantity);
            tourismPackageRepository.save(newPackage);
            existingBooking.setTourismPackage(newPackage);
    }else {
            int delta = newQuantity - existingBooking.getQuantity();
            if (delta >0){
                if (newPackage.getAvailableQuantity() == null || newPackage.getAvailableQuantity() < delta){
                    throw new RuntimeException("Not enough available quantity for this package");
                }
                newPackage.setAvailableQuantity(newPackage.getAvailableQuantity() - delta);
            }else if (delta < 0) {
                newPackage.setAvailableQuantity(newPackage.getAvailableQuantity()-delta);
            }
            tourismPackageRepository.save(newPackage);
        }
        existingBooking.setQuantity(newQuantity);
        return bookingRepository.save(existingBooking);
    }
}


