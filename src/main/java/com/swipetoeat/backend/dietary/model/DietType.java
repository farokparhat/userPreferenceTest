package com.swipetoeat.backend.dietary.model;

import java.util.Collections;
import java.util.List;

public enum DietType {
    OMNIVORE(Collections.emptyList()),
    VEGETARIAN(List.of("BEEF", "PORK", "CHICKEN", "LAMB", "FISH", "SEAFOOD")),
    VEGAN(List.of("BEEF", "PORK", "CHICKEN", "LAMB", "FISH", "SEAFOOD", "DAIRY", "EGGS", "HONEY")),
    HALAL(List.of("NONHALAL")),
    NOPORKNOLARD(List.of("PORK","LARD"));

    private final List<String> forbiddenItems;

    DietType(List<String> forbiddenItems){
        this.forbiddenItems = forbiddenItems;
    }

    public List<String> getForbiddenItems() {
        return forbiddenItems;
    }
}
