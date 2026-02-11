package com.swipetoeat.backend.dietary.service;

import com.swipetoeat.backend.dietary.model.Allergen;
import com.swipetoeat.backend.dietary.model.DietType;
import com.swipetoeat.backend.dietary.model.UserPreference;
import com.swipetoeat.backend.dietary.repository.DietaryRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DietaryService {
    private final DietaryRepository dietaryRepository;

    public DietaryService(DietaryRepository dietaryRepository) {
        this.dietaryRepository = dietaryRepository;
    }

    public Map<String , List<String>> getDietaryOptions(){
        Map<String , List<String>> options = new HashMap<>();
        options.put("dietType", Arrays.stream(DietType.values()).map(Enum::name).collect(Collectors.toList()));
        options.put("allergens", Arrays.stream(Allergen.values()).map(Enum::name).collect(Collectors.toList()));
        return options;
    }

    public UserPreference updateUserPreference(String userId, DietType dietType, List<Allergen> allergies){

        if(dietType == null){
            throw new IllegalArgumentException("You must select a Diet Type");
        }
        UserPreference preference = new UserPreference(userId, dietType, allergies);

        return dietaryRepository.save(preference);
    }
}
