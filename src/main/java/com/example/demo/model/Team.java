package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "team")
public class Team  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable
    private String name;

    // RELATIONS

    // Team -> Player
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @JsonIgnore
    private List<Player> playerList;

    // Team -> TeamNickname
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @JsonIgnore
    private List<TeamNickname> teamNicknameList;

    // Team -> TeamNickname
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @JsonIgnore
    private List<TeamStat> teamStatList;

    // CONSTRUCTOR
    public Team(@Nullable String name) {
        this.name = name;
    }
}
