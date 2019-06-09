package com.example.demo.dao;

import com.example.demo.model.Player;
import com.example.demo.model.PlayerNickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlayerNicknameDao extends JpaRepository<PlayerNickname, Integer> {
    List<PlayerNickname> findPlayerNicknameByPlayer(Player player);
}
