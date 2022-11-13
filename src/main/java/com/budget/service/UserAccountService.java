package com.budget.service;

import com.budget.dao.UserAccountRepository;
import com.budget.dao.UserPkRepository;
import com.budget.dto.UserAccountDTO;
import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAccountService { // Account 중복 및 User 검사
    private final UserPkRepository userPkRepository;
    private final UserAccountRepository userAccountRepository;

    public void userAccountRegister(UserAccountDTO userAccountDTO){
        UserAccount userAccount = new UserAccount();

        String getUserPk = userAccountDTO.getUserPk();
        String account = userAccountDTO.getAccount();

        if(userPkRepository.findById(getUserPk).isPresent()) { // 입력한 UserPk가 DB에 존재하는지 검사
            if (userAccountRepository.findByUserPkAndAccount(new UserPk(getUserPk), account).isEmpty()){ // 유저의 동일 계좌 등록 방지
                userAccount.setUserPk(new UserPk(getUserPk));
                userAccount.setAccount(account);
                userAccount.setTotal(0);

                userAccountRepository.save(userAccount);
                log.info("등록에 성공했습니다.");
            }
            else {
                log.info("이미 등록된 계좌입니다.");
            }
        }
        else {
            log.info("등록되지 않은 유저입니다.");
        }
    }
}
