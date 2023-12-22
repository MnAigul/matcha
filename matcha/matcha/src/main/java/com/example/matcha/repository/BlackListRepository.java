package com.example.matcha.repository;

import com.example.matcha.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
    BlackList findBlackListByIdAndRefreshToken(Long id, String refreshToken);

}
