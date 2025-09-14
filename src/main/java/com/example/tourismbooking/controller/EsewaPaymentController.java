package com.example.tourismbooking.controller;
import com.example.tourismbooking.service.EsewaPaymentService;
import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.PaymentStatus;
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
    public EsewaPaymentController(EsewaPaymentService esewaPaymentService, BookingRepository bookingRepository){
        this.esewaPaymentService = esewaPaymentService;
        this.bookingRepository = bookingRepository;
    }
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, String>> initiatePayment(@RequestParam Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
        double pricePerUnit =booking.getTourismPackage().getPrice();
        double amount = pricePerUnit*booking.getQuantity();
        String pid ="BOOKING-" + bookingId;

        Map<String, String> payload = new HashMap<>();
        payload.put("tAmt", String.valueOf(amount));
        payload.put("amt",String.valueOf(amount));
        payload.put("psc", "0");
        payload.put("pdc", "0");
        payload.put("txAmt","0");
        payload.put("pid", pid);
        payload.put("scd",merchantCode);
        payload.put("su","http://localhost:8080/api/payment/success");
        payload.put("fu","http://localhost:8080/api/payment/failure");
        payload.put("esewaUrl",sandboxMainUrl);
        return ResponseEntity.ok(payload);
    }
    @GetMapping("/success")
    public ResponseEntity<String> esewaSuccess(@RequestParam String amt, @RequestParam String pid, @RequestParam String rid){
        boolean verified = esewaPaymentService.verifyPayment(amt, pid, rid);
        if(!verified){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
        }
        Long bookingId = parseBookingId(pid);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new RuntimeException("Booking not found"));
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setPaymentReference(rid);
        bookingRepository.save(booking);
        return ResponseEntity.ok("Payment verified and booking marked PAID");
    }
    @GetMapping("/failure")
    public ResponseEntity<String> esewaFailure(@RequestParam(required = false) String pid){
        if(pid != null){
            try {
                Long bookingId = parseBookingId(pid);
                Booking booking = bookingRepository.findById(bookingId).orElse(null);
                if (booking != null) {
                    booking.setPaymentStatus(PaymentStatus.FAILED);
                    bookingRepository.save(booking);
                }
            }catch (Exception ignored){}
        }
        return ResponseEntity.badRequest().body("Payment failed or cancelled");
    }
    private Long parseBookingId(String pid){
        if(pid != null && pid.startsWith("BOOKING-")){
            return Long.parseLong(pid.substring("BOOKING-".length()));
        }
        throw new IllegalArgumentException("Invalid pid format");
    }
}
