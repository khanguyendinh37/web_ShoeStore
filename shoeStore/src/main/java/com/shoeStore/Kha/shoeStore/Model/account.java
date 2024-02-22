package com.shoeStore.Kha.shoeStore.Model;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
public class account {
    @Id
    @NotNull
    @NotBlank(message = "Username cannot be blank!")
    @Length(min = 5,max = 50,message = "Username must be at least 5 characters and at most 50 characters!")
    @Column(length = 50)
    private String userName;
    @NotNull
    @NotBlank(message = "Password cannot be blank!")
    @Length(min = 6, max = 250, message = "Password must be at least 6 characters and at most 50 characters ")
    @Column(length = 250)
    private String password;
    private String Avatar;
    private int postion;
    @NotBlank(message = "Your full name may not be blank!")
    private String fullName;
    @NotNull
    @NotBlank(message = "Your phone number may not be blank")
    @Length(min = 7, max = 11, message = "Invalid your phone number")
    @Column(length = 11, unique = true)
    private String phoneNumber;
    @NotNull
    @Email(message = "Invalid your email address")
    @Column(length = 50, unique = true)
    private String email;
    private String address;
    @NotNull
    private int properti;
    @Temporal(TemporalType.DATE)
    private Date updateDay = new Date();
    private String birthDay;
    private int sex;

}
