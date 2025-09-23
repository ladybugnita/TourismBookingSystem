package com.example.tourismbooking.service;

import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.Booking.PaymentStatus;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourismPackageService tourismPackageService;

    @Autowired
    private UserService userService;

    @Autowired
    private EsewaPaymentService esewaPaymentService;

    @Transactional
    public Booking createBooking(Booking booking, Long userId){
        TourismPackage tourismPackage = tourismPackageService.getPackageById(booking.getTourismPackage().getId())
                .orElseThrow(() -> new RuntimeException("Tourism Package not found"));

        booking.setUser(userService.getUserById(userId));
        booking.setPaymentStatus(PaymentStatus.PENDING);

        return bookingRepository.save(booking);
    }
    @Transactional
    public Booking updateBooking(Long bookingId, Booking updatedBooking, Long userId){
        Booking existingBooking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        validateBookingEditable(existingBooking);

        existingBooking.setQuantity(updatedBooking.getQuantity());
        existingBooking.setBookingDate(updatedBooking.getBookingDate());

        if(updatedBooking.getTourismPackage() != null) {
            TourismPackage tourismPackage = tourismPackageService.getPackageById(updatedBooking.getId())
                    .orElseThrow(() -> new RuntimeException("Tourism package not found"));
            existingBooking.setTourismPackage(updatedBooking.getTourismPackage());
        }
        return bookingRepository.save(existingBooking);
    }
    @Transactional
    public void deleteBooking(Long bookingId, Long userId){
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
        validateBookingEditable(booking);
        bookingRepository.delete(booking);
    }
    public Booking getBooking(Long bookingId, Long userId){
        return bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    public List<Booking> getUserBookings(Long userId){
        return bookingRepository.findByUserId(userId);
    }
    private void validateBookingEditable(Booking booking){
        if(booking.getPaymentStatus() == PaymentStatus.COMPLETED){
            log.warn("Attempt to modify booking {} after payment. User: {}", booking.getId(), booking.getUser().getEmail());
            throw new IllegalStateException("After payment editing cannot be done.");
        }
    }
    @Transactional
    public void markBookingAsPaid(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
        booking.setPaymentStatus(PaymentStatus.COMPLETED);
        booking.setPaymentDate(java.time.LocalDate.now());
        bookingRepository.save(booking);
        log.info("Booking {} marked as paid successfully", bookingId);
    }
    public boolean isBookingEditable (Long bookingId,Long userId){
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return booking.getPaymentStatus() != PaymentStatus.COMPLETED;
    }
}

