package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.news;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface billReporsitory extends CrudRepository<bill,Integer> {
    @Query("SELECT n FROM bill n WHERE n.userName = :userName ")
    ArrayList<bill> findByNameContaining(String userName);
    @Query("SELECT n FROM bill n WHERE n.status = 0")
    ArrayList<bill> findByAll();
}
