package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * The class represents a category for a question
 */
@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(unique = true)
    private String categoryName;
}
