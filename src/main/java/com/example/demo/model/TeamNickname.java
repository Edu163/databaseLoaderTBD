package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "team_nickname")
public class TeamNickname  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable
    private String nickname;

    // TeamNickname -> Team
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "team_id")
    private Team team;
}
