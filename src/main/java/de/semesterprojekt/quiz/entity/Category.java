package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    @Column(unique = true)
    private String categoryName;

    /* work in progress
    @OneToMany(mappedBy = "category")
    Set<Question> questions;
    */
}
