package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "player")
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable
    private String name;

    @Nullable @Column(name = "last_name")
    private String lastName;

    @Nullable
    private String fullName;

    @Nullable @Temporal(TemporalType.DATE)
    private Date birthday;

    // RELATIONS

    // Player -> PlayerNickname
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @JsonIgnore
    private List<PlayerNickname> playerNicknameList;

    // Player -> PlayerStat
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    @JsonIgnore
    private List<PlayerStat> playerStatList;

    // Player -> Team
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_id")
    private Team team;

    // CONSTRUCTOR

    public Player(@Nullable String fullName, @Nullable String name, @Nullable String lastName, @Nullable Date birthday, Team team) {
        this.fullName = fullName;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.team = team;
    }
}
