package de.semesterprojekt.quiz.database.controller;

import de.semesterprojekt.quiz.database.entity.Category;
import de.semesterprojekt.quiz.database.repository.CategoryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the Category-entity
 */
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Returns a list of all categories
     * @return List of categories
     */
    @GetMapping("/all")
    public List<Category> index(){

        return categoryRepository.findAll();
    }
}