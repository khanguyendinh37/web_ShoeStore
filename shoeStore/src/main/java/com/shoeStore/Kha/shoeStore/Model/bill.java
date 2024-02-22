package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdBill;
    @Temporal(TemporalType.DATE)
    private Date billDate = new Date();
    private String totalPrice;
    private String userName;
    private int properti;
    private String note;
    private String phone;
    private String email;
    private String address;
    private String fullName;
    private int pay;
    private int quantity;
    private int status;
}
