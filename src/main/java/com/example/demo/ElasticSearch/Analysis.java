package com.example.demo.ElasticSearch;

import com.example.demo.dao.TeamDao;
import com.example.demo.dao.TeamNicknameDao;
import com.example.demo.dao.TeamStatDao;
import com.example.demo.model.Team;
import com.example.demo.model.TeamNickname;
import com.example.demo.model.TeamStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
@Order(4)
public class Analysis implements CommandLineRunner {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private TeamStatDao teamStatDao;

    @Autowired
    private TeamNicknameDao teamNicknameDao;

    public void doAllTeamAnalysis(){
        List<Team> teams = teamDao.findAll();
        for (Team team: teams
             ) {
            TeamStat teamStat = this.doTeamAnalysis(team.getName(), team);
            List<TeamStat> teamStats = this.doTeamAnalysisByKeyWords(team);
            TeamStat teamStatUnion = this.makeTeamStatUnion(teamStat, teamStats);
            teamStatDao.save(teamStatUnion);
            //this.saveTeamStatList(teamStats);

        }
    }
    public TeamStat makeTeamStatUnion(TeamStat teamStat, List<TeamStat> teamStats){
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        for (TeamStat teamStatInArray: teamStats
             ) {
            positiveCounter = positiveCounter + teamStatInArray.getPositiveTweets();
            negativeCounter = negativeCounter + teamStatInArray.getNegativeTweets();
            neutralCounter = neutralCounter + teamStatInArray.getNeutralTweets();
        }
        teamStat.setPositiveTweets(positiveCounter);
        teamStat.setNegativeTweets(negativeCounter);
        teamStat.setNeutralTweets(neutralCounter);

        return teamStat;
    }
    public TeamStat doTeamAnalysis(String search, Team team){
        String metric;
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        Date date = new Date();
        TeamStat teamStat = new TeamStat();
        List<Tweet> tweets = tweetRepository.findAllByText(search);
        for (Tweet tweet: tweets
             ) {
            metric = this.doMetrics(tweet);
            if(metric == "neutro"){
                neutralCounter = neutralCounter + 1;
            }
            else if(metric == "positivo"){
                positiveCounter = positiveCounter + 1;
            }
            else if(metric == "negativo"){
                negativeCounter = negativeCounter + 1;

            }
        }
        teamStat.setPositiveTweets(positiveCounter);
        teamStat.setNegativeTweets(negativeCounter);
        teamStat.setNeutralTweets(neutralCounter);
        teamStat.setDate(date);
        teamStat.setTeam(team);
        return teamStat;

    }
    public List<TeamStat> doTeamAnalysisByKeyWords(Team team){
        List<TeamNickname> teamNicknames = teamNicknameDao.findTeamNicknameByTeam(team);
        List<TeamStat> teamStats = new ArrayList<>();
        TeamStat teamStat;
        for (TeamNickname teamNick: teamNicknames
             ) {
            teamStat = this.doTeamAnalysis(teamNick.getNickname(), team);
            teamStats.add(teamStat);
        }
        return teamStats;
    }
    public void saveTeamStatList(List<TeamStat> teamStats){
        for (TeamStat teamStat: teamStats
             ) {
            teamStatDao.save(teamStat);
        }
    }


    public String doMetrics(Tweet tweet){
        Double positive;
        Double negative;
        positive = tweet.getPositive();
        negative = tweet.getNegative();
        if(tweet.getPositive() == null || tweet.getNegative() == null){
            return "neutro";
        }
        else{
            if(positive > negative){
                return "positivo";
            }
            else{
                return "negativo";
            }
        }


    }

    @Override
    public void run(String... args) throws Exception {
        this.doAllTeamAnalysis();
    }
}
