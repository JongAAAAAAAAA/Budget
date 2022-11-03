package com.budget;

import com.budget.dao.UserPkRepository;
import com.budget.dto.UserAccountDTO;
import com.budget.dto.UserDTO;
import com.budget.entity.UserPk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BudgetController {
    private final UserPkRepository userPkRepository;

    @GetMapping
    String index(){
        return "index";
    }

    @ResponseBody
    @PostMapping("/register/user") // 유저 등록
    void userRegister(@RequestBody UserDTO userDTO){
        log.info("user 등록 : {}", userDTO.getUserPk());

        UserPk userPK = new UserPk();

        userPkRepository.save(userPK);
    }

    @ResponseBody
    @PostMapping("/register/useraccount") // 유저의 계좌 등록
    void userAccountRegister(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user 의 : {}, 계좌 등록 : {}", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        
    }



}
