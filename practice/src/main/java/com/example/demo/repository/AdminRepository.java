package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // emailの存在チェック
    boolean existsByEmail(String email);

    // emailで管理者情報を取る
    List<Admin> findByEmail(String email);
}

