package com.example.demo.ElasticSearch;

import com.example.demo.dao.*;
import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Duplicates")
@Component
public class PlayerAnalysis implements CommandLineRunner {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private PlayerStatDao playerStatDao;

    @Autowired
    private PlayerNicknameDao playerNicknameDao;

    public void doAllPlayerAnalysis(){
        List<Player> players = playerDao.findAll();
        for (Player player: players
        ) {
            PlayerStat playerStat = this.doPlayerAnalysis(player.getFullName(), player);
            List<PlayerStat> playerStats = this.doPlayerAnalysisByKeyWords(player);
            PlayerStat playerStatUnion= this.makePlayerStatUnion(playerStat, playerStats);
            playerStatDao.save(playerStatUnion);
            //this.saveTeamStatList(teamStats);

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
    public PlayerStat doPlayerAnalysis(String search, Player player){
        String metric;
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        Date date = new Date();
        PlayerStat playerStat = new PlayerStat();
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
        playerStat.setPositiveTweets(positiveCounter);
        playerStat.setNegativeTweets(negativeCounter);
        playerStat.setNeutralTweets(neutralCounter);
        playerStat.setDate(date);
        playerStat.setPlayer(player);
        return playerStat;

    }
    public List<PlayerStat> doPlayerAnalysisByKeyWords(Player player){
        List<PlayerNickname> playerNicknames = playerNicknameDao.findPlayerNicknameByPlayer(player);
        List<PlayerStat> playerStats = new ArrayList<>();
        PlayerStat playerStat;
        for (PlayerNickname playerNickname: playerNicknames
        ) {
            playerStat = this.doPlayerAnalysis(playerNickname.getNickname(), player);
            playerStats.add(playerStat);
        }
        return playerStats;
    }
    public PlayerStat makePlayerStatUnion(PlayerStat playerStat, List<PlayerStat> playerStats){
        Integer positiveCounter = 0;
        Integer negativeCounter = 0;
        Integer neutralCounter = 0;
        for (PlayerStat playerStatArray: playerStats
        ) {
            positiveCounter = positiveCounter + playerStatArray.getPositiveTweets();
            negativeCounter = negativeCounter + playerStatArray.getNegativeTweets();
            neutralCounter = neutralCounter + playerStatArray.getNeutralTweets();
        }
        playerStat.setPositiveTweets(positiveCounter);
        playerStat.setNegativeTweets(negativeCounter);
        playerStat.setNeutralTweets(neutralCounter);

        return playerStat;
    }

    @Override
    public void run(String... args) throws Exception {
        this.doAllPlayerAnalysis();
    }
}
