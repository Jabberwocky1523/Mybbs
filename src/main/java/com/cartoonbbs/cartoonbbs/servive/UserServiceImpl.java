package com.cartoonbbs.cartoonbbs.servive;

import com.cartoonbbs.cartoonbbs.dao.UserRepository;
import com.cartoonbbs.cartoonbbs.po.User;
import com.cartoonbbs.cartoonbbs.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUse(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username, Md5Utils.code(password));
        return user;
    }
    @Override
    public User checkName(String username) {
        User user=userRepository.findByUsername(username);
        return user;
    }
    @Override
    public void deleteByName(String name)
    {
        User a=userRepository.findByUsername(name);
        if(a==null)
        {
            return;
        }
        else{
            userRepository.deleteByUsername(name);
        }
    }

    @Override
    public void updateUser(User user)
    {
        User a=userRepository.findById(user.getId()).get();
        userRepository.deleteByUsername(a.getUsername());
        userRepository.save(user);
    }

    @Override
    public void changestatementOnline(User user)
    {
        userRepository.updateUserOnline(user.getUsername());
    }
    @Override
    public void changestatementOffline(User user)
    {
        userRepository.updateUserOffline(user.getUsername());
    }
    @Override
    public List<User> checkState(){
        return userRepository.checkState();
    }
    @Override
    public User findByid(Long id){
        return userRepository.findById(id).get();
    }
    @Transactional
    @Override
    public User saveUser(User user) {
        if(user.getId()==null){
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setAvatar("123");
            user.setType(2);
        }else {
            user.setUpdateTime(new Date());
        }
        return userRepository.save(user);
    }
}
