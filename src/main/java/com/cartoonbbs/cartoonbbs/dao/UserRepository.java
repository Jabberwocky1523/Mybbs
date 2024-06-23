package com.cartoonbbs.cartoonbbs.dao;

import com.cartoonbbs.cartoonbbs.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);

    User findByState(int state);

    User findByUsername(String username);
    void deleteByUsername(String username);
    @Query("select  c from User c where  c.state=1")
    List<User> checkState();
    @Transactional
    @Modifying
    @Query("update User  c set c.state=1 where c.state=0 and c.username like %:username%")
    int updateUserOnline(@Param("username")String username);
    @Transactional
    @Modifying
    @Query("update User  c set c.state=0 where c.state=1 and c.username like %:username%")
    int updateUserOffline(@Param("username")String username);

}
