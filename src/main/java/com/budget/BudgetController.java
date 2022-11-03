package com.budget;

import com.budget.dao.UserAccountRepository;
import com.budget.dao.UserPkRepository;
import com.budget.dto.InfoDTO;
import com.budget.dto.UserAccountDTO;
import com.budget.dto.UserDTO;
import com.budget.entity.Info;
import com.budget.entity.UserAccount;
import com.budget.entity.UserPk;
import com.budget.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BudgetController {
    private final UserPkRepository userPkRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountService userAccountService;

    @GetMapping
    String index(){
        return "index";
    }

    @ResponseBody
    @PostMapping("/register/user") // 유저 등록
    void userRegister(@RequestBody UserDTO userDTO){
        log.info("user 등록 : {}", userDTO.getUserPk());

        UserPk userPK = new UserPk();

        userPK.setUserPk(userPK.getUserPk());

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
        log.info("user 의 : {}, 계좌의 : {}, 예산 등록 : {}",
                userAccountDTO.getUserPk(), userAccountDTO.getAccount(), userAccountDTO.getTotal());



    }

    @ResponseBody
    @PostMapping("/update/spending") // 소비
    void spendingUpdate(@RequestBody InfoDTO infoDTO){
        log.info("user 의 : {}, 은행 : {}, 소비 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        Info info = new Info();

        String userPk = infoDTO.getUserPk();
        String account = infoDTO.getAccount();
        Integer money = infoDTO.getMoney();
        LocalDateTime localDateTime = infoDTO.getLocalDateTime();
        String content = infoDTO.getContent();

        info.setUserPk(new UserPk(userPk));
        info.setUserAccount(new UserAccount(account));
        info.setSpending(money);
        info.setLocalDateTime(localDateTime);
        info.setContent(content);

    }
}
