package com.budget.RepositoryTest;

import com.budget.dao.UserAccountRepository;
import com.budget.dao.UserPkRepository;
import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserPkTest {
    @Autowired
    private UserPkRepository userPkRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void insert(){
        UserPk userPk = new UserPk();

        System.out.println(userPk);

        userPk.setUserPk("testUser");

        userPkRepository.save(userPk);

        System.out.println(userPk);
    }

    @Test
    //@Transactional
    void testest() {
        UserAccount userAccount = new UserAccount();

        userAccount.setId(0);
        userAccount.setUserPk(new UserPk("testUser"));
        userAccount.setAccount("123123");
        userAccount.setTotal(123123);

        userAccountRepository.save(userAccount);


//        Optional<List<Integer>> totalByUserPk = userAccountRepository.findTotalByUserPk(new UserPk("testUser"));

//        System.out.println("totalByUserPk = " + totalByUserPk.get().get(0));
    }
}
