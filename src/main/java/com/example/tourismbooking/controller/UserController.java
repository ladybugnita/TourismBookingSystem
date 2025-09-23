package com.example.tourismbooking.controller;

import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.entity.User;
import com.example.tourismbooking.service.UserService;
import com.example.tourismbooking.repository.BookingRepository;
import com.example.tourismbooking.repository.TourismPackageRepository;
import com.example.tourismbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourismPackageRepository tourismPackageRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new RuntimeException("User not authenticated");
        }
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    @PostMapping("/user/bookings")
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, Principal principal) {
        if (booking.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be at least 1");
        }
        Long pkgId = booking.getTourismPackage() == null ? null : booking.getTourismPackage().getId();
        if (pkgId == null) {
            return ResponseEntity.badRequest().body("Tourism Package Id is required");
        }

        TourismPackage tourPackage = tourismPackageRepository.findById(pkgId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        if (LocalDate.now().isAfter(tourPackage.getBookingDeadline())) {
            return ResponseEntity.badRequest().body("Booking deadline has passed for this package");
        }

        if (tourPackage.getAvailableQuantity() == null || booking.getQuantity() > tourPackage.getAvailableQuantity()) {
            return ResponseEntity.badRequest().body("Not enough available quantity for this package");
        }

        User user = getAuthenticatedUser(principal);
        booking.setUser(user);
        booking.setTourismPackage(tourPackage);
        booking.setBookingDate(LocalDate.now());
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        tourPackage.setAvailableQuantity(tourPackage.getAvailableQuantity() - booking.getQuantity());
        tourismPackageRepository.save(tourPackage);

        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/user/bookings")
    public ResponseEntity<?> getUserBookings(Principal principal) {
        User user = getAuthenticatedUser(principal);
        List<Booking> list = bookingRepository.findByUser(user);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id, Principal principal) {
        User user = getAuthenticatedUser(principal);
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
        if (!booking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to view this booking");
        }
        return ResponseEntity.ok(booking);
    }

    @Transactional
    @DeleteMapping("/user/bookings/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Principal principal) {
        User user = getAuthenticatedUser(principal);
        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID :" + id));

        if (!existingBooking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to delete this booking");
        }

        if(existingBooking.getPaymentStatus() == Booking.PaymentStatus.COMPLETED){
            System.out.println("After payment editing cannot be done.");
            return ResponseEntity.badRequest().body("After payment deletion cannot be done.");
        }

        TourismPackage tourPackage = existingBooking.getTourismPackage();
        tourPackage.setAvailableQuantity(tourPackage.getAvailableQuantity() + existingBooking.getQuantity());
        tourismPackageRepository.save(tourPackage);

        bookingRepository.delete(existingBooking);
        return ResponseEntity.ok("Booking deleted successfully");
    }

    @Transactional
    @PutMapping("/user/bookings/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id,
                                           @RequestBody Booking updatedBooking,
                                           Principal principal) {
        User user = getAuthenticatedUser(principal);

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!existingBooking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to update this booking");
        }

        if(existingBooking.getPaymentStatus() == Booking.PaymentStatus.COMPLETED){
            System.out.println("After payment editing cannot be done.");
            return ResponseEntity.badRequest().body("After payment editing cannot be done.");
        }

        int newQuantity = (updatedBooking.getQuantity() > 0)
                ? updatedBooking.getQuantity()
                : existingBooking.getQuantity();

        Long newpkgId = (updatedBooking.getTourismPackage() != null)
                ? updatedBooking.getTourismPackage().getId()
                : existingBooking.getTourismPackage().getId();

        TourismPackage newPackage = tourismPackageRepository.findById(newpkgId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        if (LocalDate.now().isAfter(newPackage.getBookingDeadline())) {
            return ResponseEntity.badRequest().body("Booking deadline has passed for this package");
        }

        TourismPackage oldPackage = existingBooking.getTourismPackage();

        if (!newPackage.getId().equals(oldPackage.getId())) {
            oldPackage.setAvailableQuantity(oldPackage.getAvailableQuantity() + existingBooking.getQuantity());
            tourismPackageRepository.save(oldPackage);

            if (newPackage.getAvailableQuantity() == null || newQuantity > newPackage.getAvailableQuantity()) {
                return ResponseEntity.badRequest().body("Not enough available quantity for this package");
            }
            newPackage.setAvailableQuantity(newPackage.getAvailableQuantity() - newQuantity);
            tourismPackageRepository.save(newPackage);
            existingBooking.setTourismPackage(newPackage);
        } else {
            int delta = newQuantity - existingBooking.getQuantity();
            if (delta > 0) {
                if (newPackage.getAvailableQuantity() == null || newPackage.getAvailableQuantity() < delta) {
                    return ResponseEntity.badRequest().body("Not enough available quantity for this package");
                }
                newPackage.setAvailableQuantity(newPackage.getAvailableQuantity() - delta);
            } else if (delta < 0) {
                newPackage.setAvailableQuantity(newPackage.getAvailableQuantity() - delta);
            }
            tourismPackageRepository.save(newPackage);
        }

        existingBooking.setQuantity(newQuantity);
        Booking saved = bookingRepository.save(existingBooking);
        return ResponseEntity.ok(saved);
    }
}
