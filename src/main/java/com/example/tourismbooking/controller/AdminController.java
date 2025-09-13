package com.example.tourismbooking.controller;
import com.example.tourismbooking.entity.TourismPackage;
import com.example.tourismbooking.service.TourismPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import java.util.List;
@RestController
@RequestMapping("/admin/packages")

public class AdminController {
    private final TourismPackageService service;

    @Autowired
    public AdminController(TourismPackageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createPackage(@Valid @RequestBody TourismPackage pkg, BindingResult result) {
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(service.createPackage(pkg));
    }

    @GetMapping
    public List<TourismPackage> getAllPackages() {
        return service.getAllPackages();
    }

    @GetMapping("/{id}")
    public TourismPackage getPackage(@PathVariable Long id) {
        return service.getPackageById(id);
    }

    @PutMapping("/{id}")
    public TourismPackage updatePackage(@PathVariable Long id, @RequestBody TourismPackage pkg) {
        return service.updatePackage(id,pkg);
    }

    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable Long id) {
        service.deletePackage(id);
    }
}

