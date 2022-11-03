package com.budget.dao;

import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUserPkAndAccount(UserPk userPk, String account);
}
