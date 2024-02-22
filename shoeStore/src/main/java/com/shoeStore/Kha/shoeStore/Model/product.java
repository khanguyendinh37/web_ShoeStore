package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jdk.jfr.Category;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdProduct;
    private String ImageProduct;
    @NotNull
    @NotBlank(message = "Title product may not be blank")
    @Size(min = 5,max = 100)
    @Column(name = "titleProduct")
    private String titleProduct;
    @NotNull
    @Column(name = "IdCategory", insertable = false, updatable = false)
    private int IdCategory;
    @NotNull
    @NotBlank(message = "Content product may not be blank")
    @Size(min = 5,max = 100)
    private String content;
    private int properti;
    @Temporal(TemporalType.DATE)
    private Date updateDay =  new Date();
    @NotNull
    @Min(value = 1000,message = "value must be more than 1000")
    private String price;

    private int productFor;
    @Min(value = 0, message = "Sale may > 0%")
    @Max(value = 100, message = "Sale may < 100%")
    private int sale;

    @Min(value = 0)
    @Max(value = 2000)
    private int quantity;
    @NotNull
    @NotBlank(message = "Size product may not be blank")
    private String size;
    @NotNull
    @NotBlank(message = "Color product may not be blank")
    private String color;
    private String detailProduct;
    private int likes;
    private int sold;
    @ManyToOne
    @JoinColumn(name = "IdCategory")
    category category;

    @Override
    public String toString() {

        return "product{" +
                "IdProduct=" + IdProduct +
                ", ImageProduct='" + ImageProduct + '\'' +
                ", titleProduct='" + titleProduct + '\'' +
                ", IdCategory=" + IdCategory +
                ", content='" + content + '\'' +
                ", properti=" + properti +
                ", updateDay=" + updateDay +
                ", price='" + price + '\'' +
                ", productFor=" + productFor +
                ", sale=" + sale +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", detailProduct='" + detailProduct + '\'' +
                ", likes=" + likes +
                ", sold=" + sold +
                ", category=" + category +
                '}';
    }
}
