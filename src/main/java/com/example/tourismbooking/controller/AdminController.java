package com.example.tourismbooking.controller;

import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.entity.User;
import com.example.tourismbooking.repository.BookingRepository;
import com.example.tourismbooking.repository.UserRepository;
import com.example.tourismbooking.service.TourismPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final TourismPackageService service;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(TourismPackageService service,
                           BookingRepository bookingRepository,
                           UserRepository userRepository) {
        this.service = service;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/packages")
    public List<TourismPackage> getAllPackages() {
        return service.getAllPackages();
    }

    @GetMapping("/packages/{id}")
    public Optional<TourismPackage> getPackage(@PathVariable Long id) {
        return service.getPackageById(id);
    }

    @PostMapping("/packages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPackage(@Valid @RequestBody TourismPackage pkg, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(service.createPackage(pkg));
    }

    @PutMapping("/packages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TourismPackage updatePackage(@PathVariable Long id, @RequestBody TourismPackage pkg) {
        return service.updatePackage(id, pkg);
    }

    @DeleteMapping("/packages/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePackage(@PathVariable Long id) {
        service.deletePackage(id);
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> allBookings = bookingRepository.findAll();
        return ResponseEntity.ok(allBookings);
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
        return ResponseEntity.ok("Booking deleted Successfully");
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
