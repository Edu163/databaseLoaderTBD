package com.example.demo.dao;

import com.example.demo.model.PlayerNickname;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PlayerNicknameDao extends JpaRepository<PlayerNickname, Integer> {
}
