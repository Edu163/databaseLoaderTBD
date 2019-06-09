package com.example.demo.dao;

import com.example.demo.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneDao extends JpaRepository<Zone, Integer> {
}
