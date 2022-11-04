package com.budget.dao;

import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByUserPkAndAccount(UserPk userPk, String account);
    Optional<Integer> findTotalByUserPkAndAccount(UserPk userPk, String account);
    Optional<String> findAccountByUserPkAndAccount(UserPk userPk, String account);
    Optional<List<Integer>> findTotalByUserPk(UserPk userPk);
}
