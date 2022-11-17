package com.budget.dao;

import com.budget.entity.Info;
import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Integer> {
    Optional<List<Info>> findByUserPkAndIncome(UserPk userPk, Integer income);
    Optional<List<Info>> findByUserPkAndAccountAndIncome(UserPk userPk, String account, Integer income);
    Optional<List<Info>> findByUserPkAndLocalDateAndIncome(UserPk userPk, LocalDate localDate, Integer income);
    Optional<List<Info>> findByUserPkAndIncomeAndLocalDateBetween(UserPk userPk, Integer income, LocalDate start, LocalDate end);
    Optional<List<Info>> findByUserPkAndAccountAndIncomeAndLocalDateBetween(UserPk userPk, String account, Integer income, LocalDate start, LocalDate end);
    Optional<List<Info>> findByUserPkAndAccountAndSpendingAndLocalDateBetween(UserPk userPk, String account, Integer spending, LocalDate start, LocalDate end);
    Optional<List<Info>> findByUserPkAndAccount(UserPk userPk, String account);
}
