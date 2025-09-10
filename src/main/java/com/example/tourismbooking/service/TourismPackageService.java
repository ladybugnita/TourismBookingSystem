package com.example.tourismbooking.service;

import com.example.tourismbooking.entity.TourismPackage;
import java.util.List;

public interface TourismPackageService {
    TourismPackage createPackage(TourismPackage pkg);
    List<TourismPackage> getAllPackages();
    TourismPackage getPackageById(Long id);
    TourismPackage updatePackage(Long id, TourismPackage pkg);
    void deletePackage(Long id);
}
