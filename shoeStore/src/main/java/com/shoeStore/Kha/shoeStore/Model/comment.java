package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class comment {
    @Id
    private int IdComment;
    @NotNull
    @Column(name = "userName", insertable = false, updatable = false)
    private  String userName;
    @NotNull
    @NotBlank(message = "star may not be blank")
    private int  star;
    private String content;
    @NotNull
    @Column(name = "IdProduct", insertable = false, updatable = false)
    private int IdProduct;
    private int properti;
    @Temporal(TemporalType.DATE)
    private Date updateDay =  new Date();
    private int alert;
    @ManyToOne
    @JoinColumn(name = "userName")
    account account;
    @ManyToOne
    @JoinColumn(name = "IdProduct")
    product product;
}
