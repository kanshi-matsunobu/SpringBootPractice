package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.form.AdminForm;
import com.example.demo.repository.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void register(AdminForm form) {
        Admin admin = new Admin();
        admin.setLastName(form.getLastName());
        admin.setFirstName(form.getFirstName());
        admin.setEmail(form.getEmail());

        admin.setPassword(form.getPassword());

        adminRepository.save(admin);
    }
}