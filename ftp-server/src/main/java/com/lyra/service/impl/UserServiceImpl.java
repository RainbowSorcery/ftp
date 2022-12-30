package com.lyra.service.impl;

import com.lyra.entity.User;
import com.lyra.mapper.UserRepository;
import com.lyra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean checkUserIsExits(String username) {
        return userRepository.countByUsername(username) > 0;
    }
}
