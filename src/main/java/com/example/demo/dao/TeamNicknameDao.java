package com.example.demo.dao;

import com.example.demo.model.Team;
import com.example.demo.model.TeamNickname;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamNicknameDao extends JpaRepository<TeamNickname, Integer> {
    List<TeamNickname> findTeamNicknameByTeam(Team team);
}
