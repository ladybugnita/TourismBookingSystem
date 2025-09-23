package com.example.tourismbooking.repository;
import com.example.tourismbooking.entity.Booking;
import com.example.tourismbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    @Query("SELECT b From Booking b where b.id = :bookingId AND b.user.id = :userId")
    Optional<Booking> findByIdAndUserId(@Param("bookingId") Long bookingId, @Param("userId") Long userId);
     @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
     List<Booking> findByUserId(@Param("userId") Long userId);
     @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.id = :bookingId AND b.user.id =:userId")
     boolean existsByIdAndUserId(@Param("bookingId") Long bookingId, @Param("userId") Long userId);
    List<Booking> findByUser(User user);
}
