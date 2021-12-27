package de.semesterprojekt.quiz.data.controller;

import de.semesterprojekt.quiz.data.entity.Category;
import de.semesterprojekt.quiz.data.repository.CategoryRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The class controls the REST-mapping for the Category-entity
 */

@CrossOrigin(origins = "http://localhost:4001")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * TODO: DELETE
     * Returns a list of all categories
     * @return List of categories
     */
    @GetMapping("")
    public List<Category> index(){

        return categoryRepository.findAll();
    }
}