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

        //Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        final Integer total = getAccount.get().getTotal();

        getAccount.ifPresent(updateTotal ->{
            //updateTotal.setUserPk(new UserPk(userPk));
            //updateTotal.setAccount(account);
            updateTotal.setTotal(total - money);

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

        //Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

        final Integer total = getAccount.get().getTotal();

        getAccount.ifPresent(updateTotal ->{
            //updateTotal.setUserPk(new UserPk(userPk));
            //updateTotal.setAccount(account);
            updateTotal.setTotal(total + money);

            userAccountRepository.save(updateTotal);
        });
    }

    public void detailUpdate(InfoDTO infoDTO) {
        Optional<Info> getId = infoRepository.findById(infoDTO.getId());

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        String content = infoDTO.getContent();

        getId.ifPresent(updateDetail -> {
            if (getId.get().getIncome() != null) { // 수입인 경우의 update
                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

                final Integer total = getUserAccount.get().getTotal();

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total - getId.get().getIncome());

                    userAccountRepository.save(updateTotal);
                });

                updateDetail.setAccount(getUserAccount.get().getAccount());
                updateDetail.setContent(content);
                updateDetail.setIncome(money);
                updateDetail.setLocalDateTime(localDateTime);
                updateDetail.setLocalDate(localDate);

                infoRepository.save(updateDetail);

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total + money);

                    userAccountRepository.save(updateTotal);
                });
            //하나의 로우를 바꿔버려야함. 거기서 바뀌는 income spending의 변화에 따른 total이 업데이트 돼야하고
                // 이걸 incomeupdate를 쓰면서 야무지게 할 수 있는 방법이 없을까 고민해야함.

//                incomeUpdate(infoDTO);
            } else { // 지출인 경우의 update
                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);

                final Integer total = getUserAccount.get().getTotal();

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total + getId.get().getIncome());

                    userAccountRepository.save(updateTotal);
                });

                updateDetail.setAccount(getUserAccount.get().getAccount());
                updateDetail.setContent(content);
                updateDetail.setSpending(money);
                updateDetail.setLocalDateTime(localDateTime);
                updateDetail.setLocalDate(localDate);

                infoRepository.save(updateDetail);

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total - money);

                    userAccountRepository.save(updateTotal);
                });

//                spendingUpdate(infoDTO);
            }

//            infoRepository.save(updateDetail);
        });
    }

//    public void detailUpdate(InfoDTO infoDTO) {
//        Optional<Info> getId = infoRepository.findById(infoDTO.getId());
//
//        UserAccount userAccount = new UserAccount();
//
//        String userPk = infoDTO.getUserPk();
//        String account = infoDTO.getAccount();
//
//        getId.ifPresent(updateDetail -> {
//            if (getId.get().getIncome() != null) { // 수입인 경우의 update
//                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);
//
//                final Integer total = getUserAccount.get().getTotal();
//
//                userAccount.setTotal(total - getId.get().getIncome());
//
//                userAccountRepository.save(userAccount);
//
//                incomeUpdate(infoDTO);
//            } else { // 지출인 경우의 update
//                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(new UserPk(userPk), account);
//
//                final Integer total = getUserAccount.get().getTotal();
//
//                userAccount.setTotal(total + getId.get().getIncome());
//
//                userAccountRepository.save(userAccount);
//
//                spendingUpdate(infoDTO);
//            }
//
//            infoRepository.save(updateDetail);
//        });
//    }

    public void detailDelete(InfoDTO infoDTO){
        Optional<Info> getId = infoRepository.findById(infoDTO.getId());

        getId.ifPresent(deleteDetail ->{
            infoRepository.delete(getId.get());
        });
    }

//    public void detailDelete(InfoDTO infoDTO){
//        String userPk = infoDTO.getUserPk();
//        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
//
//        Optional<Info> userPkAndLocalDateTime = infoRepository.findByUserPkAndLocalDateTime(new UserPk(userPk), localDateTime);
//
//
//        userPkAndLocalDateTime.ifPresent(deleteDetail ->{
//            infoRepository.delete(userPkAndLocalDateTime.get());
//        });
//    }
}
