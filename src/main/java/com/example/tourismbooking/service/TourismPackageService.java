package com.example.tourismbooking.service;

import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.repository.TourismPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TourismPackageService {
    private final TourismPackageRepository repository;

    @Autowired
    public TourismPackageService(TourismPackageRepository repository) {
        this.repository = repository;
    }
    public TourismPackage createPackage(TourismPackage pkg){
    return repository.save(pkg);
    }
    public List<TourismPackage> getAllPackages(){
    return repository.findAll();
    }
    public Optional <TourismPackage> getPackageById(Long id){
    return repository.findById(id);
    }
    public TourismPackage updatePackage(Long id, TourismPackage updatedpkg){
        TourismPackage existingpkg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id:" + id));
        if (updatedpkg.getName() != null && !updatedpkg.getName().isEmpty()){
            existingpkg.setName(updatedpkg.getName());
        }
        if (updatedpkg.getDescription() != null && !updatedpkg.getDescription().isEmpty()){
            existingpkg.setDescription(updatedpkg.getDescription());
        }
        if (updatedpkg.getPrice() > 0){
            existingpkg.setPrice(updatedpkg.getPrice());
        }
        if(updatedpkg.getTotalQuantity() != null) {
            int oldTotal = existingpkg.getTotalQuantity();
            int oldAvailable = existingpkg.getAvailableQuantity() != null ? existingpkg.getAvailableQuantity() : 0;
            int diff = updatedpkg.getTotalQuantity()- oldTotal;
            existingpkg.setTotalQuantity(updatedpkg.getTotalQuantity());
            existingpkg.setAvailableQuantity(oldAvailable + diff);
        }
        if (updatedpkg.getBookingDeadline() != null){
            existingpkg.setBookingDeadline(updatedpkg.getBookingDeadline());
        }
        return repository.save(existingpkg);
    }
    public void deletePackage(Long id){
        repository.deleteById(id);
    }
}
