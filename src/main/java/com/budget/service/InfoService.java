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

    public void spendingUpdate(InfoDTO infoDTO){
        Info info = new Info();

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        String content = infoDTO.getContent();

        Optional<UserAccount> getAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        info.setUserPk(new UserPk(userPk));
        info.setAccount(getAccount.get().getAccount());
        info.setSpending(money);
        info.setLocalDateTime(localDateTime);
        info.setLocalDate(localDate);
        info.setContent(content);

        infoRepository.save(info);

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        final Integer total = getUserAccount.get().getTotal();

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(total-money);

            userAccountRepository.save(updateTotal);
        }); // else 문 로그 찍어야함
    }

    public void incomeUpdate(InfoDTO infoDTO){
        Info info = new Info();

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        String content = infoDTO.getContent();

        Optional<UserAccount> getAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        info.setUserPk(new UserPk(userPk));
        info.setAccount(getAccount.get().getAccount());
        info.setIncome(money);
        info.setLocalDateTime(localDateTime);
        info.setLocalDate(localDate);
        info.setContent(content);

        infoRepository.save(info);

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        final Integer total = getUserAccount.get().getTotal();

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userPk));
            updateTotal.setAccount(account);
            updateTotal.setTotal(total+money);

            userAccountRepository.save(updateTotal);
        });
    }

    public void detailUpdate(InfoDTO infoDTO){
        String userPk = infoDTO.getUserPk();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();

        Optional<Info> userPkAndLocalDateTime = infoRepository.findByUserPkAndLocalDateTime(new UserPk(userPk), localDateTime);

        userPkAndLocalDateTime.ifPresent(updateDetail ->{
            if (userPkAndLocalDateTime.get().getIncome() != null){
                updateDetail.setIncome(infoDTO.getMoney());
            }
            else{
                updateDetail.setSpending(infoDTO.getMoney());
            }


            infoRepository.save(updateDetail);
        });
    }

    public void detailDelete(InfoDTO infoDTO){
        String userPk = infoDTO.getUserPk();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();

        Optional<Info> userPkAndLocalDateTime = infoRepository.findByUserPkAndLocalDateTime(new UserPk(userPk), localDateTime);

        userPkAndLocalDateTime.ifPresent(deleteDetail ->{
            infoRepository.delete(userPkAndLocalDateTime.get());
        });
    }
}
