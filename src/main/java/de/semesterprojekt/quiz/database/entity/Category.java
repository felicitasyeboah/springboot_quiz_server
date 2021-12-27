package de.semesterprojekt.quiz.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * The class represents a category for a question
 */
@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int categoryId;

    @Column(unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    List<Question> questionListM;
}
