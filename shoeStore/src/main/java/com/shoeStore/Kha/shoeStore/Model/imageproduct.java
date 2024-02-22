package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class imageproduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int IdImage;
    private int IdProduct;
    @NotNull
    @NotBlank(message = "Images Product may not be blank!")
    private String Image;
}
