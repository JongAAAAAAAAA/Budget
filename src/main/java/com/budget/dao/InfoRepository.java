package com.budget.dao;

import com.budget.entity.Info;
import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    Optional<List<Info>> findByUserPkAndIncome(UserPk userPk, Integer income);
}
