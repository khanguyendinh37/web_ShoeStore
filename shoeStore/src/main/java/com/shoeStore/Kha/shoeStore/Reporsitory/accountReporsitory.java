package com.shoeStore.Kha.shoeStore.Reporsitory;

import com.shoeStore.Kha.shoeStore.Model.account;
import com.shoeStore.Kha.shoeStore.Model.news;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface accountReporsitory extends CrudRepository<account,String> {
    @Query("SELECT a FROM account a WHERE a.userName = :username AND a.password = :password")
    Optional<account> GetAccount(String username, String password);

    @Query("SELECT a FROM account a WHERE a.email = :email")
    Optional<account> GetAccountByEmail(String email);

    @Query("SELECT a FROM account a WHERE a.phoneNumber = :phone")
    Optional<account> GetAccountByPhone(String phone);
    @Query("SELECT n FROM account n WHERE n.fullName like %:fullName% and n.properti = 0")
    Page<account> findByNameContaining(String fullName, Pageable pageable);

    @Query("SELECT n FROM account n WHERE n.properti = 0")
    Page<account> findAll(Pageable pageable);

    @Query("SELECT a FROM account a WHERE a.userName like %:username%")
    Optional<account> GetAccountByUsername(String username);
//
//    Page<account> findByNameContaining(String username, Pageable pageable);
//
//    @Query("SELECT a FROM account a WHERE a.properti=0")
//    Page<account> findAll(Pageable pageable);
//    @Query("SELECT a FROM account a WHERE a.properti=1")
//    Page<account> findAlldelete(Pageable pageable);
}
