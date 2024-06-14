package com.cartoonbbs.cartoonbbs.servive;

import com.cartoonbbs.cartoonbbs.po.User;

import java.util.List;

public interface UserService {
    User checkUse(String username,String password);

    void changestatementOnline(User user);

    void changestatementOffline(User user);

    User saveUser(User user);
    User checkName(String username);
    List<User> checkState();
    void deleteByName(String name);
    void updateUser(User user);
    User findByid(Long id);
}
