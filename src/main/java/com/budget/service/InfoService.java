package com.budget.service;

import com.budget.dao.InfoRepository;
import com.budget.dao.UserAccountRepository;
import com.budget.dto.InfoDTO;
import com.budget.entity.Info;
import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InfoService {
    private final InfoRepository infoRepository;
    private final UserAccountRepository userAccountRepository;

    public void moneyUpdate(InfoDTO infoDTO){
        Info info = new Info();

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        System.out.println("account = " + account);
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        String content = infoDTO.getContent();

        Optional<UserAccount> getAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        info.setUserPk(new UserPk(userPk));
        System.out.println("getAccount = " + getAccount);
        info.setAccount(getAccount.get().getAccount());
        System.out.println("getAccount.get().getAccount() = " + getAccount.get().getAccount());
        info.setSpending(money);
        info.setLocalDateTime(localDateTime);
        info.setLocalDate(localDate);
        info.setContent(content);

        infoRepository.save(info);
    }

    public void spendingUpdate(InfoDTO infoDTO){
        moneyUpdate(infoDTO);

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();

        UserAccount getTotal = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account).get();

        final int i = getTotal.getTotal();

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(i-money);

            userAccountRepository.save(updateTotal);
        }); // else 문 로그 찍어야함
    }

    public void incomeUpdate(InfoDTO infoDTO){
        moneyUpdate(infoDTO);

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();

        UserAccount getTotal = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account).get();

        final int i = getTotal.getTotal();

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(i+money);

            userAccountRepository.save(updateTotal);
        });
    }
}
