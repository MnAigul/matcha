package com.example.matcha.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Data
@Table(name = "blacklist")
@Entity
@Getter
@Setter
public class BlackList {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public BlackList(Long user_id, String refreshToken) {
        this.userId = user_id;
        this.refreshToken = refreshToken;
    }
}
