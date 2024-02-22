package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.news;
import com.shoeStore.Kha.shoeStore.Model.product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface newsReporsitory extends CrudRepository<news,Integer> {
    @Query("SELECT n FROM news n WHERE n.titleNews like %:title% and n.properti = 0")
    Page<news> findByNameContaining(String title, Pageable pageable);
    @Query("SELECT n FROM news n WHERE n.properti = 0")
    Page<news> findAll(Pageable pageable);
    @Query("SELECT p FROM news p WHERE p.titleNews like %:title% and p.properti = 1")
    Page<news> findByNameContainingHistory(String title, Pageable pageable);
    @Query("SELECT p FROM news p WHERE p.properti = 1")
    Page<news> findAllHistopry(Pageable pageable);
}
