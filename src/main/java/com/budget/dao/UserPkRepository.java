package com.budget.dao;

import com.budget.entity.UserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPkRepository extends JpaRepository<UserPk, String> {
}
