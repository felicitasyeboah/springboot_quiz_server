package de.semesterprojekt.quiz.controller;

import de.semesterprojekt.quiz.entity.Category;
import de.semesterprojekt.quiz.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the Category-entity
 */
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
    @GetMapping("")
    public List<Category> Index(){
        return categoryRepository.findAll();
    }
}
