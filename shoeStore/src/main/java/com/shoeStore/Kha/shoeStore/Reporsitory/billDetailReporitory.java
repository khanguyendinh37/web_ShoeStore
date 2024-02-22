package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.bill;
import com.shoeStore.Kha.shoeStore.Model.billDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface billDetailReporitory extends CrudRepository<billDetail,Integer> {
    @Query("SELECT n FROM billDetail n WHERE n.IdBill = :IdBill ")
    ArrayList<billDetail> findByNameContaining(Integer IdBill);
}
