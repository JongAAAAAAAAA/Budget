package com.budget.RepositoryTest;

import com.budget.dao.UserPkRepository;
import com.budget.entity.UserPk;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserPkTest {
    @Autowired
    private UserPkRepository userPkRepository;

    @Test
    public void insert(){
        UserPk userPk = new UserPk();

        System.out.println(userPk);

        userPk.setUserPk("testUser");

        userPkRepository.save(userPk);

        System.out.println(userPk);
    }
}
