package com.library.managementprojectjava.entity.businnes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Boolean builtIn;

    @Column(nullable = false, unique = true)
    private int sequence;


    @OneToMany(mappedBy = "category")
    private List<Book> books;

}
