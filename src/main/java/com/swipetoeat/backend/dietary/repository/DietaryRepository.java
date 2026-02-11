package com.swipetoeat.backend.dietary.repository;

import com.swipetoeat.backend.dietary.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryRepository extends JpaRepository<UserPreference , String> {
}
