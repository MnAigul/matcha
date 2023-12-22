package com.example.matcha.services;

import com.example.matcha.entity.BlackList;
import com.example.matcha.repository.BlackListRepository;
import com.example.matcha.repository.RoleRepository;
import com.example.matcha.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class BlackListService {
    private final BlackListRepository blackListRepository;

    @Autowired
    public BlackListService(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    public boolean searchToken(Long id, String refreshToken) {
        BlackList obj = blackListRepository.findBlackListByIdAndRefreshToken(id, refreshToken);
        if (obj != null) {
            return true;
        }
        return false;
    }

    public void saveToken(Long user_id, String refreshToken) {
        blackListRepository.save(new BlackList(user_id, refreshToken));
    }

}
