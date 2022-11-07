package com.budget.controller;

import com.budget.dao.*;
import com.budget.dto.*;
import com.budget.entity.*;
import com.budget.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BudgetController {
    private final UserPkRepository userPkRepository;
    private final UserAccountRepository userAccountRepository;
    private final InfoRepository infoRepository;
    private final UserAccountService userAccountService;
    private final InfoService infoService;

    @GetMapping
    String index(){
        return "index";
    }

    @ResponseBody
    @PostMapping("/register/user") // 유저 등록
    void userRegister(@RequestBody UserDTO userDTO){
        log.info("user 등록 : {}", userDTO.getUserPk());

        UserPk userPK = new UserPk();

        userPK.setUserPk(userDTO.getUserPk());

        userPkRepository.save(userPK);
    }

    @ResponseBody
    @PostMapping("/register/useraccount") // 유저의 계좌 등록
    void userAccountRegister(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user {} 의, 계좌 등록 : {}", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        userAccountService.userAccountRegister(userAccountDTO);
    }

    @ResponseBody
    @PostMapping("/register/total") // 계좌의 예산 등록
    void totalRegister(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user {} 의, 계좌 : {}, 예산 등록 : {}",
                userAccountDTO.getUserPk(), userAccountDTO.getAccount(), userAccountDTO.getTotal());

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(
                new UserPk(userAccountDTO.getUserPk()), userAccountDTO.getAccount());

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setUserPk(new UserPk(userAccountDTO.getUserPk()));
            updateTotal.setAccount(userAccountDTO.getAccount());
            updateTotal.setTotal(userAccountDTO.getTotal());

            userAccountRepository.save(updateTotal);
        });
    }

    @ResponseBody
    @PostMapping("/update/spending") // 지출
    void spendingUpdate(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의, 은행 : {}, 소비 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.spendingUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/update/income") // 수입
    void incomeUpdate(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의, 은행 : {}, 수입 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.incomeUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/search/total/each") // 특정 계좌 예산 조회
    int totalSearchEach(@RequestBody InfoDTO infoDTO) {
        log.info("user {} 의 계좌 {} 예산 조회", infoDTO.getUserPk(), infoDTO.getAccount());

        Optional<UserAccount> getTotal = userAccountRepository.findByUserPkAndAccount(new UserPk(infoDTO.getUserPk()), infoDTO.getAccount());

        Integer total = getTotal.get().getTotal();

        return total;
    }

    @ResponseBody
    @PostMapping("/search/total/all") // 총 예산 조회
    int totalSearch(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 총 예산 조회", infoDTO.getUserPk());

        Optional<List<UserAccount>> totalByUserPk = userAccountRepository.findByUserPk(new UserPk(infoDTO.getUserPk()));

        Integer total = 0;

        for(int i=0; i<totalByUserPk.get().size(); i++){
            total += totalByUserPk.get().get(i).getTotal();
        }

        return total;
    }

    @ResponseBody
    @PostMapping("/search/spending/day") // 하루 지출 조회
    int daySpendingSearch(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 하루 지출 조회", infoDTO.getUserPk());

        Optional<List<Info>> getList = infoRepository.findByUserPkAndLocalDateAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getLocalDate(), null);

        System.out.println("infoDTO = " + infoDTO.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        Integer spending = 0;

        for (int i=0; i<getList.get().size(); i++){
            spending += getList.get().get(i).getSpending();
        }

        return spending;
    }

    @ResponseBody
    @PostMapping("/search/spending/month") // 한 달 지출 조회 , 한 달 조회시 년 월 일 까지 지정해야하는데 특정 달 클릭시 1일로 출력되게끔 설정해야함
    int monthSpendingSearch(@RequestBody InfoDTO infoDTO) {
        log.info("user {} 의 한 달 지출 조회", infoDTO.getUserPk());

        Optional<List<Info>> getList = infoRepository.findByUserPkAndLocalDateAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getLocalDate(), null);

        Integer spending = 0;

        for (int i = 0; i < getList.get().size(); i++) {
            log.info("1 : {}", getList.get().get(i).getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")));
            log.info("2 : {}", infoDTO.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")));

            if (infoDTO.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                    .equals(getList.get().get(i).getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")))
            ) {
                spending += getList.get().get(i).getSpending();
            }
        }
            return spending;
    }

    @ResponseBody
    @PostMapping("/search/spending/all") // 총 지출 조회
    int allSpendingSearch(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 총 지출 조회", infoDTO.getUserPk());

        Optional<List<Info>> userPk = infoRepository.findByUserPkAndIncome(new UserPk(infoDTO.getUserPk()), null);

        Integer spending = 0;

        for (int i=0; i<userPk.get().size(); i++){
            spending += userPk.get().get(i).getSpending();
        }

        return spending;
    }

    @ResponseBody
    @PostMapping("/search/spending/each") // 특정 계좌 지출 조회
    int spendingSearchEach(@RequestBody InfoDTO infoDTO) {
        log.info("user {} 의 계좌 {} 지출 조회", infoDTO.getUserPk(), infoDTO.getAccount());

        Optional<List<Info>> getSpending = infoRepository.findByUserPkAndAccountAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getAccount(), null);

        Integer spending = 0;

        for (int i=0; i<getSpending.get().size(); i++){
            spending += getSpending.get().get(i).getSpending();
        }

        return spending;
    }

    @ResponseBody
    @PostMapping("/search/balance") // 잔액 조회
    int balanceSearch(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 잔액 조회", infoDTO.getUserPk());

        Integer balance = totalSearch(infoDTO) - allSpendingSearch(infoDTO);

        return balance;
    }

    @ResponseBody
    @PostMapping("/update/detail") // 내역 수정
    void updateDetail(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 내역 수정", infoDTO.getUserPk());

        infoService.detailUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/delete/detail") // 내역 삭제
    void deleteDetail(@RequestBody InfoDTO infoDTO){
        log.info("user {} 의 내역 삭제", infoDTO.getUserPk());

        Optional<Info> getId = infoRepository.findById(infoDTO.getId());

        infoRepository.delete(getId.get());
    }
}
