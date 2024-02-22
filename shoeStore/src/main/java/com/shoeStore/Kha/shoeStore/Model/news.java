package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class news {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdNews;
    @NotNull
    @NotBlank(message = "Title news may not be blank")
    @Size(min = 5,max = 200)
    private String titleNews;
    private String images;
    @NotNull
    @NotBlank(message = "Content news may not be blank")
    @Size(min = 5,max = 500)
    private String contentNews;
    @NotNull
    @NotBlank(message = "Content news may not be blank")
    @Size(min = 100)
    private String detailNews;
    @Temporal(TemporalType.DATE)
    private Date dateNews = new Date();
    private int properti;
    @NotNull
    @Column(name = "idProduct", insertable = false, updatable = false)
    private int idProduct;
    @ManyToOne
    @JoinColumn(name = "idProduct")
    product product;
}
