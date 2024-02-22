package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.comment;
import com.shoeStore.Kha.shoeStore.Model.imageproduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface commentRepository extends CrudRepository<comment,Integer> {
    @Query("SELECT p FROM comment p WHERE p.IdProduct = :IdProduct ")
    Iterable<comment> findAll(Integer IdProduct);
}
