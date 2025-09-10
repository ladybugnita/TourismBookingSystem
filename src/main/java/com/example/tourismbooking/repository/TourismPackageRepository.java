package com.example.tourismbooking.repository;
import com.example.tourismbooking.entity.TourismPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TourismPackageRepository extends JpaRepository<TourismPackage, Long> {

}
