package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface productReporsitory extends CrudRepository<product,Integer> {

    @Query("SELECT p FROM product p WHERE p.titleProduct like %:titleProduct% and p.properti = 0")
    Page<product> findByNameContaining(String titleProduct, Pageable pageable);
    @Query("SELECT p FROM product p WHERE p.properti = 0")
    Page<product> findAll(Pageable pageable);
    @Query("SELECT p FROM product p WHERE p.titleProduct like %:titleProduct% and p.properti = 1")
    Page<product> findByNameContainingHistory(String titleProduct, Pageable pageable);
    @Query("SELECT p FROM product p WHERE p.properti = 1")
    Page<product> findAllHistopry(Pageable pageable);
    @Query("SELECT p FROM product p WHERE p.IdCategory= :Id and p.properti = 0")
    Page<product> findAllCategory(Integer Id, Pageable pageable);
    @Query("SELECT p FROM product p WHERE p.IdCategory = :Id and p.titleProduct like %:titleProduct% and p.properti = 0")
    Page<product> findByNameContainingCategory(Integer Id,String titleProduct, Pageable pageable);

}

