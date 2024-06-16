package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Recipe;
import org.example.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository repository;

    public List<Recipe> getRecipes() {
        return repository.findAll();
    }
}
