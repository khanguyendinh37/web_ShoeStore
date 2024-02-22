package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.imageproduct;
import com.shoeStore.Kha.shoeStore.Model.product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface imageProductReporsitory extends CrudRepository<imageproduct,Integer> {
    @Query("SELECT p FROM imageproduct p WHERE p.IdProduct = :IdProduct ")
    Iterable<imageproduct> findAll(Integer IdProduct);

}
