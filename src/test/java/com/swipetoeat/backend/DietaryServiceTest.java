package com.swipetoeat.backend;

import com.swipetoeat.backend.dietary.model.Allergen;
import com.swipetoeat.backend.dietary.model.DietType;
import com.swipetoeat.backend.dietary.model.UserPreference;
import com.swipetoeat.backend.dietary.repository.DietaryRepository;
import com.swipetoeat.backend.dietary.service.DietaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//TDD tests for different scenarios

class DietaryServiceTest {
    private DietaryRepository dietaryRepository;
    private DietaryService dietaryService;

    @BeforeEach
    void setUp(){
        //set up a mock repository for testing purpose
        dietaryRepository = Mockito.mock(DietaryRepository.class);
        dietaryService = new DietaryService(dietaryRepository);
    }

    //1st TDD test, scene is that user have a new allergy
    @Test
    void testAddNewRestriction_HalalAddSeafood(){
        String userId = "user-add";
        DietType dietType = DietType.HALAL; // Includes NONHALAL
        List<Allergen> newAllergens = List.of(Allergen.SHELLFISH);

        when(dietaryRepository.save(any(UserPreference.class))).thenAnswer(i->i.getArguments()[0]);

        UserPreference result = dietaryService.updateUserPreference(userId, dietType, newAllergens);

        List<String> items = result.getAllForbiddenItems();
        assertTrue(items.contains("NONHALAL"),"NONHALAL tag is still exist since the user still require halal diet");
        assertTrue(items.contains("LOBSTER"),"Should now contains Lobster from Shellfish allergy");

    }

    //2nd TDD test, scene is that user converted from Vegan to Vegetarian//
    @Test
    void testRemoveRestriction_VeganToVegetarian(){
        String userId = "user-remove-resitriction";
        DietType newDiet = DietType.VEGETARIAN;
        List<Allergen> allergens = Collections.emptyList();

        when(dietaryRepository.save(any(UserPreference.class))).thenAnswer(i->i.getArguments()[0]);
        UserPreference result = dietaryService.updateUserPreference(userId, newDiet, allergens);

        List<String> items = result.getAllForbiddenItems();
        assertFalse(items.contains("DAIRY"),"DAIRY is no longer forbidden");
        assertFalse(items.contains("EGGS"),"EGG is no longer forbidden");
        assertTrue(items.contains("BEEF"),"Still can not eat beef");
    }

    //3rd TDD test scene is that user eats everything, should be valid with an empty forbidden list
    @Test
    void testOmnivore_NoAllergies_ShouldBeValid(){
        String userId = "user-omnivore";
        DietType diet = DietType.OMNIVORE;
        List<Allergen> allergens = Collections.emptyList();

        when(dietaryRepository.save(any(UserPreference.class))).thenAnswer((i->i.getArguments()[0]));

        UserPreference result = dietaryService.updateUserPreference(userId, diet, allergens);

        List<String> items = result.getAllForbiddenItems();

        assertNotNull(items,"result is not null");
        assertTrue(items.isEmpty(),"Forbidden list is empty for an omnivore with no allergy");
    }

    //4th TDD test, when user click save without changing anything
    @Test
    void testSave_NotChangingAnything_ShouldNotImportSameRecord(){
        String userId = "user-dupe-save";
        DietType diet = DietType.OMNIVORE;
        List<Allergen> allergens = List.of(Allergen.PEANUT);

        dietaryService.updateUserPreference(userId, diet, allergens);
        dietaryService.updateUserPreference(userId, diet, allergens);

        ArgumentCaptor<UserPreference> captor = ArgumentCaptor.forClass(UserPreference.class);
        verify(dietaryRepository,times(2)).save(captor.capture());

        List<UserPreference> savedPreferences = captor.getAllValues();
        UserPreference firstPreference = savedPreferences.get(0);
        UserPreference secondPreference = savedPreferences.get(1);

        assertEquals(firstPreference.getAllForbiddenItems().size(),secondPreference.getAllForbiddenItems().size());
        assertEquals(firstPreference.getAllergies(),secondPreference.getAllergies());
    }

    @Test
    void testUpdateUserPreference_NullDietType_ShouldThrowException(){
        String userId = "user-null_diettype";
        DietType dietType = null;
        List<Allergen> allergens = Collections.emptyList();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> {dietaryService.updateUserPreference(userId, dietType, allergens);});

        assertEquals("You must select a Diet Type", exception.getMessage());

        //make sure null data wouldn't be imported in to the database
        verify(dietaryRepository, never()).save(any());
    }
}


