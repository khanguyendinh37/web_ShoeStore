package com.shoeStore.Kha.shoeStore.Model;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdContact;
    @NotNull
    @NotBlank(message = "Address may not be blank!")
    private String addressContact;
    private String FB_url;
    private String intagram_url;
    private String yt_url;
    private String map_url;
    @NotNull
    @NotBlank(message = "Your phone number may not be blank")
    @Length(min = 7, max = 11, message = "Invalid your phone number")
    @Column(length = 11, unique = true)
    private String phoneNumber;
    @NotNull
    @Email(message = "Invalid your email address")
    @Column(length = 50, unique = true)
    private String emailAddress;
}
