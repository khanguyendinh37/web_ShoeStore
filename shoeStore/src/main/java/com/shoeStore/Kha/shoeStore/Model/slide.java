package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdSlide;
    @NotBlank(message = "Title Can not blank!")
    @Length(min = 10,max = 150)
    private String Title;
    @NotBlank(message = "Content Can not blank!")
    @Length(min = 10,max = 300)
    private String Content;
    private String Images;
    @NotNull
    @Column(name = "IdProduct", insertable = false, updatable = false)
    private int IdProduct;
    @ManyToOne
    @JoinColumn(name = "IdProduct")
    product product;

}
