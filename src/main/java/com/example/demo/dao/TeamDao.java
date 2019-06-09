package com.example.demo.dao;

import com.example.demo.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamDao extends JpaRepository<Team, Integer> {
    Team findByName(String name);
}
