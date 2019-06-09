package com.example.demo.dao;

import com.example.demo.model.Player;
import com.example.demo.model.PlayerStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface PlayerStatDao extends JpaRepository<PlayerStat, Integer> {

    PlayerStat findFirstByOrderByIdDesc();

    @Query("select ps from PlayerStat ps where ps.player = ?1 and ps.date > ?2 and ps.date < ?3")
    List<PlayerStat> findByBetweenDates (Player player, Date startDate, Date endDate);
    PlayerStat findFirstByPlayer(Player player);
}
