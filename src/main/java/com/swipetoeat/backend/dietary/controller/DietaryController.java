package com.swipetoeat.backend.dietary.controller;

import com.swipetoeat.backend.dietary.model.Allergen;
import com.swipetoeat.backend.dietary.model.DietType;
import com.swipetoeat.backend.dietary.model.UserPreference;
import com.swipetoeat.backend.dietary.service.DietaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class DietaryController {
    private final DietaryService dietaryService;

    public DietaryController(DietaryService dietaryService) {
        this.dietaryService = dietaryService;
    }

    @GetMapping("/options")
    public Map<String, List<String>> getOptions() {
        return dietaryService.getDietaryOptions();
    }

    @PutMapping("/users/{userId}")
    public UserPreference updatePreference(@PathVariable String userId, @RequestBody UpdateRequest requestBody){
        return dietaryService.updateUserPreference(userId, requestBody.getDietType(), requestBody.getAllergies());
    }


    public static class UpdateRequest{
        private DietType dietType;
        private List<Allergen> allergies;

        public DietType getDietType() { return dietType; }
        public void setDietType(DietType dietType) { this.dietType = dietType; }
        public List<Allergen> getAllergies() { return allergies; }
        public void setAllergies(List<Allergen> allergies) { this.allergies = allergies; }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNullDietType(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

}

