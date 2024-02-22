package com.shoeStore.Kha.shoeStore.Model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class about {
    @Id
    private int Id;
    private String title;
    private String Images;
    private String content;
}
