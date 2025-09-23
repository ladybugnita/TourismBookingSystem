package com.example.tourismbooking.controller;

import com.example.tourismbooking.service.EsewaPaymentService;
import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.Booking.PaymentStatus;
import com.example.tourismbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class EsewaPaymentController {
    private final EsewaPaymentService esewaPaymentService;
    private final BookingRepository bookingRepository;

    @Value("${esewa.merchant}")
    private String merchantCode;

    @Value("${esewa.sandbox-main-url}")
    private String sandboxMainUrl;

    public EsewaPaymentController(EsewaPaymentService esewaPaymentService, BookingRepository bookingRepository) {
        this.esewaPaymentService = esewaPaymentService;
        this.bookingRepository = bookingRepository;
    }
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestParam Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
            if (booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
                return ResponseEntity.badRequest().body("Booking is already paid");
            }

            double pricePerUnit = booking.getTourismPackage().getPrice();
            double amount = pricePerUnit * booking.getQuantity();
            String pid = "BOOKING-" + bookingId;

            Map<String, String> payload = new HashMap<>();
            payload.put("tAmt", String.valueOf(amount));
            payload.put("amt", String.valueOf(amount));
            payload.put("psc", "0");
            payload.put("pdc", "0");
            payload.put("txAmt", "0");
            payload.put("pid", pid);
            payload.put("scd", merchantCode);
            payload.put("su", "http://localhost:8080/api/payment/success");
            payload.put("fu", "http://localhost:8080/api/payment/failure");
            payload.put("esewaUrl", sandboxMainUrl);

            return ResponseEntity.ok(payload);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error initiating payment: " + e.getMessage());
        }
    }
    @GetMapping("/success")
    public ResponseEntity<?> esewaSuccess(@RequestParam String amt,
                                          @RequestParam String pid,
                                          @RequestParam String rid) {
        try {
            System.out.println("Received successful payment callback - PID: " + pid + ", RID: " + rid);
            boolean paymentSuccess = esewaPaymentService.verifyAndProcessPayment(amt, pid, rid);

            if (paymentSuccess) {
                Long bookingId = parseBookingId(pid);
                Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Booking not found"));

                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Payment verified and booking confirmed",
                        "bookingId", bookingId,
                        "referenceId", rid,
                        "amount", amt
                ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "status", "error",
                        "message", "Payment verification failed"
                ));
            }

        } catch (Exception e) {
            System.err.println("Error processing successful payment: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Internal server error: " + e.getMessage()
            ));
        }
    }
    @GetMapping("/failure")
    public ResponseEntity<?> esewaFailure(@RequestParam(required = false) String pid,
                                          @RequestParam(required = false) String amt,
                                          @RequestParam(required = false) String rid) {
        try {
            System.out.println("Received payment failure callback - PID: " + pid + ", RID: " + rid);

            if (pid != null) {
                Long bookingId = parseBookingId(pid);
                Booking booking = bookingRepository.findById(bookingId).orElse(null);

                if (booking != null) {
                    // Only update status if not already completed
                    if (booking.getPaymentStatus() != PaymentStatus.COMPLETED) {
                        booking.setPaymentStatus(PaymentStatus.FAILED);
                        bookingRepository.save(booking);
                        System.out.println("Booking " + bookingId + " marked as FAILED");
                    }
                }
            }

            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "message", "Payment failed or was cancelled",
                    "suggestion", "Please try again or contact support"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Error processing payment failure"
            ));
        }
    }
    @GetMapping("/status/{bookingId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            return ResponseEntity.ok(Map.of(
                    "bookingId", bookingId,
                    "paymentStatus", booking.getPaymentStatus(),
                    "paymentDate", booking.getPaymentDate(),
                    "amount", booking.getTourismPackage().getPrice() * booking.getQuantity()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving payment status: " + e.getMessage());
        }
    }
    @PostMapping("/manual-confirm/{bookingId}")
    public ResponseEntity<?> manualPaymentConfirm(@PathVariable Long bookingId) {
        try {
            esewaPaymentService.markBookingAsPaid(bookingId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Booking manually confirmed as paid",
                    "bookingId", bookingId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error manually confirming payment: " + e.getMessage());
        }
    }
    private Long parseBookingId(String pid) {
        if (pid != null && pid.startsWith("BOOKING-")) {
            return Long.parseLong(pid.substring("BOOKING-".length()));
        }
        throw new IllegalArgumentException("Invalid product ID format: " + pid);
    }
}