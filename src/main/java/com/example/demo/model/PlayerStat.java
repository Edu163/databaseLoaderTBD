package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "player_stat")
public class PlayerStat  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable @Column(name = "positive_tweets")
    private Integer positiveTweets;

    @Nullable @Column(name = "negative_tweets")
    private Integer negativeTweets;

    @Nullable @Column(name = "neutral_tweets")
    private Integer neutralTweets;

    @Nullable @Temporal(TemporalType.DATE)
    private Date date;

    // PlayerStat -> Player
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;

    // CONSTRUCTOR

    public PlayerStat(@Nullable Integer positiveTweets, @Nullable Integer negativeTweets, @Nullable Integer neutralTweets, @Nullable Date date, Player player) {
        this.positiveTweets = positiveTweets;
        this.negativeTweets = negativeTweets;
        this.neutralTweets = neutralTweets;
        this.date = date;
        this.player = player;
    }
}
