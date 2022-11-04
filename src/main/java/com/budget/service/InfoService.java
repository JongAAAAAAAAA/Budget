package com.budget.service;

import com.budget.dao.InfoRepository;
import com.budget.dao.UserAccountRepository;
import com.budget.dao.UserPkRepository;
import com.budget.dto.InfoDTO;
import com.budget.entity.Info;
import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class InfoService {
    private final UserPkRepository userPkRepository;
    private final InfoRepository infoRepository;
    private final UserAccountRepository userAccountRepository;

    public void moneyUpdate(InfoDTO infoDTO){
        Info info = new Info();

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        String content = infoDTO.getContent();

        Optional<String> getAccount = userAccountRepository.findAccountByAccount(account);

        info.setUserPk(new UserPk(userPk));
        info.setUserAccount(new UserAccount().getAccount(getAccount));
        info.setSpending(money);
        info.setLocalDateTime(localDateTime);
        info.setContent(content);

        infoRepository.save(info);
    }

    public void spendingUpdate(InfoDTO infoDTO){
        moneyUpdate(infoDTO);

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();


        Integer getTotal = userAccountRepository.findTotalByUserPkAndAccount(new UserPk(userPk), account).get();

        final int i = getTotal;

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(i-money);

            userAccountRepository.save(updateTotal);
        });
    }

    public void incomeUpdate(InfoDTO infoDTO){
        moneyUpdate(infoDTO);

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();

        Integer getTotal = userAccountRepository.findTotalByUserPkAndAccount(new UserPk(userPk), account).get();

        final int i = getTotal

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(i+money);

            userAccountRepository.save(updateTotal);
        });
    }
}
