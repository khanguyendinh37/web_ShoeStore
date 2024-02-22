package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class billDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdBillDetail;
    private String nameProduct;
    private int IdBill;
    private String ImageProduct;
    @NotNull
    private int IdProduct;
    private int quantity;
    private String linePrice;
    private String price;
    private String colorBill;
    private String sizeBill;


}
