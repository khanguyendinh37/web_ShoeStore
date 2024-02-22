package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
public class category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdCategory;
    @NotNull
    @NotBlank(message = "Category name may not be blank!")
    @Column(length = 100)
    private String Title;
//    @NotNull
//    @NotBlank(message = "Catalog photo may not be blank!")
    private String imageCategory;
    @Column(length = 200)
    private String Content;
    @OneToMany(mappedBy = "category")
    private List<product> products;
}
