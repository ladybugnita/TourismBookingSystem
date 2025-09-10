package com.example.tourismbooking.service;

import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.repository.TourismPackageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TourismPackageServiceImpl implements TourismPackageService {
    private final TourismPackageRepository repository;
    public TourismPackageServiceImpl(TourismPackageRepository repository){
        this.repository = repository;
    }
    @Override
    public TourismPackage createPackage(TourismPackage pkg){
        return repository.save(pkg);
    }
    @Override
    public List<TourismPackage> getAllPackages(){
        return repository.findAll();
    }
    @Override
    public TourismPackage getPackageById(Long id){
        return repository.findById(id).orElse(null);
    }
    @Override
    public TourismPackage updatePackage(Long id, TourismPackage pkg){
        TourismPackage existing = repository.findById(id).orElseThrow(()-> new RuntimeException("Package not found with id:" + id));
        existing.setName(pkg.getName());
        existing.setPrice(pkg.getPrice());
        existing.setDescription(pkg.getDescription());

        return repository.save(pkg);
    }
    @Override
    public void deletePackage(Long id){
        repository.deleteById(id);
    }
}
