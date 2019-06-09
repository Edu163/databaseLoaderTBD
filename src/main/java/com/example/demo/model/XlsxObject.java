package com.example.demo.model;

import lombok.Data;

@Data
public class XlsxObject {
    private String teamName;
    private String playerFullName;

    @Override
    public String toString() {
        return "XlsxObject{" +
                "teamName='" + teamName + '\'' +
                ", playerFullName='" + playerFullName + '\'' +
                '}';
    }
}
