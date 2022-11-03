package com.budget.dao;

import com.budget.entity.Info;
import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    Optional<Info> findByUserPkAndSpending(UserPk userPk, Integer spending);

}
