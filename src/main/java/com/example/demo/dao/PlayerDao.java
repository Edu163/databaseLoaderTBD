package com.example.demo.dao;

import com.example.demo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlayerDao extends JpaRepository<Player, Integer> {
    Player findPlayerById(Integer id);
    Player findPlayerByFullName(String fullName);
}
