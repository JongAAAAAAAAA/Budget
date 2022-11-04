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
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BudgetController {
    private final UserPkRepository userPkRepository;
    private final UserAccountRepository userAccountRepository;
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
        log.info("user 의 : {}, 계좌 등록 : {}", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        userAccountService.userAccountRegister(userAccountDTO);
    }

    @ResponseBody
    @PostMapping("/register/total") // 계좌의 예산 등록
    void totalRegister(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user 의 : {}, 계좌 : {}, 예산 등록 : {}",
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
        log.info("user 의 : {}, 은행 : {}, 소비 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.spendingUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/update/income") // 수입
    void incomeUpdate(@RequestBody InfoDTO infoDTO){
        log.info("user 의 : {}, 은행 : {}, 수입 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.incomeUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/search/total/all") // 총 금액 조회
    int totalSearch(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user 의 : {}, 총 금액 : {}", userAccountDTO.getUserPk(), userAccountDTO.getTotal());

        Optional<List<Integer>> totalByUserPk = userAccountRepository.findTotalByUserPk(new UserPk(userAccountDTO.getUserPk()));

        System.out.println("totalByUserPk = " + totalByUserPk.get().get(0));
        int total = 0;

        for(int i=0; i<totalByUserPk.get().size(); i++){
            total += totalByUserPk.get().get(i);
        }

        return 0;
    }
}
