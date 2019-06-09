package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "player_nickname")
public class PlayerNickname  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable
    private String nickname;

    // PlayerNickname -> Player
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;
}
