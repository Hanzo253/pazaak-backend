package com.capstone.pazaak.repository;

import com.capstone.pazaak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends JpaRepository<User, Long> {
    // to register
    boolean existsByEmailAddress(String userEmailAddress);

    // to login
    User findUserByEmailAddress(String userEmailAddress);

    // get username
    User findUserByUserName(String username);
}
