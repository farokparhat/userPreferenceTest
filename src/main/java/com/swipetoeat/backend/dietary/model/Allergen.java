package com.swipetoeat.backend.dietary.model;

import java.util.List;

public enum Allergen {
    PEANUT(List.of("PEANUT", "PEANUT BUTTER", "SATAY SAUCE")),
    DAIRY(List.of("MILK", "CHEESE", "YOGURT", "CREAM", "BUTTER")),
    GLUTEN(List.of("WHEAT", "BARLEY", "RYE", "BREAD", "PASTA")),
    SHELLFISH(List.of("SHRIMP", "CRAB", "LOBSTER", "OYSTER", "CLAM")),
    SOY(List.of("SOY BEAN", "TOFU", "SOY SAUCE"));

    private final List<String> forbiddenItems;

    Allergen(List<String> forbiddenItems) {
        this.forbiddenItems = forbiddenItems;
    }
    public List<String> getForbiddenItems() {
        return forbiddenItems;
    }

}
