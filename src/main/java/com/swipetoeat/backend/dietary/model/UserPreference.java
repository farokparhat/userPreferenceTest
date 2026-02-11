package com.swipetoeat.backend.dietary.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_preference")
public class UserPreference {
    @Id
    private String userId;

    @Enumerated(EnumType.STRING)
    private DietType dietType;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Allergen> allergies = new ArrayList<>();

    public UserPreference() {}
    public UserPreference(String userId, DietType dietType, List<Allergen> allergies){
        this.userId = userId;
        this.dietType = dietType;
        this.allergies = (allergies != null) ? allergies : new ArrayList<>();
    }

    public DietType getDietType() {return dietType;}
    public String getUserId() {return userId;}
    public List<Allergen> getAllergies() {return allergies;}
    public List<String> getAllForbiddenItems(){
        List<String> allForbiddenItems = new ArrayList<>(this.dietType.getForbiddenItems());
        for (Allergen allergen : allergies) {
            allForbiddenItems.addAll(allergen.getForbiddenItems());
        }
        return allForbiddenItems.stream().distinct().toList();
    }
}
