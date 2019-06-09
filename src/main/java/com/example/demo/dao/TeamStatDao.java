package com.example.demo.dao;

import com.example.demo.model.Team;
import com.example.demo.model.TeamStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface TeamStatDao extends JpaRepository<TeamStat, Integer> {

    TeamStat findFirstByOrderByIdDesc();

    @Query("select ts from TeamStat ts where ts.team = ?1 and ts.date > ?2 and ts.date < ?3")
    List<TeamStat> findByBetweenDates (Team team, Date startDate, Date endDate);
}

