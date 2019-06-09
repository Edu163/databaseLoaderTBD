package com.example.demo.model;

import lombok.Data;

@Data
public class KeyWord {
    private String type;
    private String name;
    private String keyWord;

    @Override
    public String toString() {
        return "KeyWord{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", keyWord='" + keyWord + '\'' +
                '}';
    }
}
