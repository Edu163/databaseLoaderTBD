package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "zone")
public class Zone  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Nullable
    private String region;

    @Nullable
    private String city;

    @Nullable
    private String commune;


    // CONSTRUCTOR
    public Zone(@Nullable String region, @Nullable String city, @Nullable String commune) {
        this.region = region;
        this.city = city;
        this.commune = commune;
    }
}
