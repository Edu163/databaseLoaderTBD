package com.example.demo.ElasticSearch;

import com.example.demo.dao.TeamDao;
import com.example.demo.dao.TeamNicknameDao;
import com.example.demo.dao.TeamStatByDateDao;
import com.example.demo.dao.TeamStatDao;
import com.example.demo.model.Team;
import com.example.demo.model.TeamNickname;
import com.example.demo.model.TeamStat;
import com.example.demo.model.TeamStatByDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
@Order(6)
public class AnalysisByDate implements CommandLineRunner {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private TeamStatByDateDao teamStatByDateDao;

    @Autowired
    private TeamNicknameDao teamNicknameDao;

    private String[] dates = {"2019-07-01","2019-07-02","2019-07-03","2019-07-04","2019-07-05","2019-07-06","2019-07-07"
            ,"2019-07-08","2019-07-09","2019-07-10","2019-07-11","2019-07-12","2019-07-13","2019-07-14","2019-07-15"
            ,"2019-07-16"};

    /*public List<Tweet> findByDate(String date1, String date2, String text){
        String myDate1 = date1 + " 00:00:00";
        String myDate2 = date2 +  " 00:00:00";
        LocalDateTime localDateTime1 = LocalDateTime.parse(myDate1,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime localDateTime2 = LocalDateTime.parse(myDate2,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Long milliDate1 = localDateTime1
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        Long milliDate2 = localDateTime2
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        return tweetRepository.findByPublicationDateBetweenAndText(milliDate1, milliDate2, text);

    }*/

    public void doAllTeamAnalysis(){
        List<Team> teams = teamDao.findAll();
        for (Team team: teams
        ) {
            for(int i = 2; i < 16; i++) {
                TeamStatByDate teamStatByDate = this.doTeamAnalysis(team.getName(), team, dates[i-2],dates[i], dates[i]);
                List<TeamStatByDate> teamStatByDates = this.doTeamAnalysisByKeyWords(team, dates[i-2],dates[i], dates[i]);
                TeamStatByDate teamStatUnion = this.makeTeamStatUnion(teamStatByDate, teamStatByDates);
                teamStatByDateDao.save(teamStatUnion);
            }
            //this.saveTeamStatList(teamStats);
        }
    }
    public TeamStatByDate makeTeamStatUnion(TeamStatByDate teamStatByDate, List<TeamStatByDate> teamStatByDates){
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        for (TeamStatByDate teamStatInArray: teamStatByDates
        ) {
            positiveCounter = positiveCounter + teamStatInArray.getPositiveTweets();
            negativeCounter = negativeCounter + teamStatInArray.getNegativeTweets();
            neutralCounter = neutralCounter + teamStatInArray.getNeutralTweets();
        }
        teamStatByDate.setPositiveTweets(positiveCounter);
        teamStatByDate.setNegativeTweets(negativeCounter);
        teamStatByDate.setNeutralTweets(neutralCounter);

        return teamStatByDate;
    }
    public TeamStatByDate doTeamAnalysis(String search, Team team, String date1, String date2, String date3){
        //DATE TO LONG
        String myDate1 = date1 + " 00:00:00";
        String myDate3 = date3 +  " 00:00:00";
        String myDate2 = date2 +  " 00:00:00";
        LocalDateTime localDateTime1 = LocalDateTime.parse(myDate1,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime localDateTime3 = LocalDateTime.parse(myDate3,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Long milliDate1 = localDateTime1
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        Long milliDate2 = localDateTime3
                .atZone(ZoneId.systemDefault())
                .toInstant().toEpochMilli();
        //TWEETS
        String metric;
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        Date date = new Date();
        TeamStatByDate teamStatByDate = new TeamStatByDate();
        List<Tweet> tweets = tweetRepository.findByPublicationDateBetweenAndText(milliDate1, milliDate2, search);
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
        LocalDateTime localDateTime2 = LocalDateTime.parse(myDate2,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Date out = Date.from(localDateTime2.atZone(ZoneId.systemDefault()).toInstant());
        teamStatByDate.setPositiveTweets(positiveCounter);
        teamStatByDate.setNegativeTweets(negativeCounter);
        teamStatByDate.setNeutralTweets(neutralCounter);
        teamStatByDate.setDate(date);
        teamStatByDate.setTeam(team);
        teamStatByDate.setDate(out);
        return teamStatByDate;

    }
    public List<TeamStatByDate> doTeamAnalysisByKeyWords(Team team, String date1, String date2, String date3){
        List<TeamNickname> teamNicknames = teamNicknameDao.findTeamNicknameByTeam(team);
        List<TeamStatByDate> teamStatByDates = new ArrayList<>();
        TeamStatByDate teamStatByDate;
        for (TeamNickname teamNick: teamNicknames
        ) {
            teamStatByDate = this.doTeamAnalysis(teamNick.getNickname(), team, date1, date2, date3);
            teamStatByDates.add(teamStatByDate);
        }
        return teamStatByDates;
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
