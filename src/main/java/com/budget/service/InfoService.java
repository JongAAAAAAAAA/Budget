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

    public void spendingUpdate(InfoDTO infoDTO){ // 지출 내역의 수정
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

        final Integer total = getAccount.get().getTotal();

        getAccount.ifPresent(updateTotal ->{
            updateTotal.setTotal(total - money);

            userAccountRepository.save(updateTotal);
        });
    }

    public void incomeUpdate(InfoDTO infoDTO){ // 수압 내역의 수정
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

        final Integer total = getAccount.get().getTotal();

        getAccount.ifPresent(updateTotal ->{
            updateTotal.setTotal(total + money);

            userAccountRepository.save(updateTotal);
        });
    }

    public void detailUpdate(InfoDTO infoDTO) { // 세부 내역 수정
        Optional<Info> getId = infoRepository.findById(infoDTO.getId());

        UserPk userPk = getId.get().getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        String content = infoDTO.getContent();

        getId.ifPresent(updateDetail -> {
            if (getId.get().getIncome() != null) { // 수입인 경우의 update
                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(userPk, account);

                final Integer total = getUserAccount.get().getTotal();
                System.out.println("total = " + total);

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total - getId.get().getIncome());

                    userAccountRepository.save(updateTotal);

                    System.out.println("updateTotal = " + updateTotal.getTotal());
                });

                final Integer updatedTotal = getUserAccount.get().getTotal();

                updateDetail.setAccount(getUserAccount.get().getAccount());
                updateDetail.setContent(content);
                updateDetail.setIncome(money);
                System.out.println("money = " + money);
                updateDetail.setLocalDateTime(localDateTime);
                updateDetail.setLocalDate(localDate);

                infoRepository.save(updateDetail);

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(updatedTotal + money);

                    userAccountRepository.save(updateTotal);
                    System.out.println("updateTotal = " + updateTotal.getTotal());
                });
            //하나의 로우를 바꿔버려야함. 거기서 바뀌는 income spending의 변화에 따른 total이 업데이트 돼야하고
                // 이걸 incomeupdate를 쓰면서 야무지게 할 수 있는 방법이 없을까 고민해야함. --> 구현 완료

            } else { // 지출인 경우의 update
                Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(userPk, account);

                final Integer total = getUserAccount.get().getTotal();

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(total + getId.get().getSpending());

                    userAccountRepository.save(updateTotal);
                });

                final Integer updatedTotal = getUserAccount.get().getTotal();

                updateDetail.setAccount(getUserAccount.get().getAccount());
                updateDetail.setContent(content);
                updateDetail.setSpending(money);
                updateDetail.setLocalDateTime(localDateTime);
                updateDetail.setLocalDate(localDate);

                infoRepository.save(updateDetail);

                getUserAccount.ifPresent(updateTotal -> {
                    updateTotal.setTotal(updatedTotal - money);

                    userAccountRepository.save(updateTotal);
                });

            }

        });
    }

    public void detailDelete(InfoDTO infoDTO){
        Optional<Info> getId = infoRepository.findById(infoDTO.getId());

        String account = getId.get().getAccount();
        UserPk userPk = getId.get().getUserPk();

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(userPk, account);

        final Integer total = getUserAccount.get().getTotal();

        if (getId.get().getIncome() != null){ // 수입인 경우의 delete
            Integer income = getId.get().getIncome();

            getUserAccount.ifPresent(updateTotal -> {
                updateTotal.setTotal(total - income);

                userAccountRepository.save(updateTotal);
            });
        } else { // 지출인 경우의 delete
            Integer spending = getId.get().getSpending();

            getUserAccount.ifPresent(updateTotal -> {
                updateTotal.setTotal(total + spending);

                userAccountRepository.save(updateTotal);
            });
        }

        infoRepository.delete(getId.get());
    }
}
