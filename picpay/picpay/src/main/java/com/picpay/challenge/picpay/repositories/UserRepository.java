package com.picpay.challenge.picpay.repositories;

import com.picpay.challenge.picpay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
