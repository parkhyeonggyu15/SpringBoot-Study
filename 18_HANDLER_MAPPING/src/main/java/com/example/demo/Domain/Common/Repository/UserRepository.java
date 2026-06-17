package com.example.demo.Domain.Common.Repository;

import com.example.demo.Domain.Common.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT u FROM User as u where u.role=?1")
    List<User> selectAllByRole(String role);

    @Query("SELECT u FROM User as u where u.role=?1 and u.password=?2")
    List<User> selectAllByRoleAndPwd(String role, String password);

    @Query("SELECT u FROM User as u where u.role=:role")
    List<User> selectAllByRole_2(@Param("role") String r);

    @Query("SELECT u FROM User as u where u.username like concat('%',:user,'%')")
    List<User> selectAllLikeUsername(@Param("user") String username);

}
