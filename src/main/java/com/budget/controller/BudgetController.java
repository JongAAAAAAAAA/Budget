package com.budget.controller;

import com.budget.dao.*;
import com.budget.dto.*;
import com.budget.entity.*;
import com.budget.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @GetMapping("login.html")
    String login() {
        return "login";
    }

    @GetMapping("accounts.html")
    String accounts() {
        return "accounts";
    }

    @GetMapping("contact.html")
    String contact() {
        return "contact";
    }

    @GetMapping("404.html")
    String error() {
        return "404";
    }

    @GetMapping("test.html")
    String test() {
        return "test";
    }

    @ResponseBody
    @PostMapping("/register/user") // 유저 등록
    void userRegister(UserDTO userDTO){
        log.info("user 등록 : {}", userDTO.getUserPk());

        UserPk userPK = new UserPk();

        userPK.setUserPk(userDTO.getUserPk());

        userPkRepository.save(userPK);
    }

    @PostMapping("/register/useraccount") // 유저의 계좌 등록
    String userAccountRegister(UserAccountDTO userAccountDTO){
        log.info("user {} 의 계좌 등록 : {}", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        userAccountService.userAccountRegister(userAccountDTO);

        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/search/useraccount") // 유저가 가진 계좌 목록 조회
    Map userAccountSearch(UserAccountDTO userAccountDTO){
        log.info("user {} 의 보유 계좌 목록 조회", userAccountDTO.getUserPk());

        HashMap<String, List<String>> stringIntegerHashMap = new HashMap<>();

        Optional<List<UserAccount>> userPk = userAccountRepository.findByUserPk(new UserPk(userAccountDTO.getUserPk()));

        List<String> account = new ArrayList<>();

        for (int i=0; i<userPk.get().size(); i++){
            account.add(userPk.get().get(i).getAccount());
        }

        stringIntegerHashMap.put("userAccountAll", account);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/register/total") // 계좌의 예산 등록
    void totalRegister(@RequestBody UserAccountDTO userAccountDTO){
        log.info("user {} 의, 계좌 : {}, 예산 등록 : {}",
                userAccountDTO.getUserPk(), userAccountDTO.getAccount(), userAccountDTO.getTotal());

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(
                new UserPk(userAccountDTO.getUserPk()), userAccountDTO.getAccount());

        getUserAccount.ifPresent(updateTotal ->{
            updateTotal.setTotal(userAccountDTO.getTotal());

            userAccountRepository.save(updateTotal);
        });
    }

    @PostMapping("/update/spending") // 지출
    String spendingUpdate(InfoDTO infoDTO){
        log.info("user {} 의 계좌 : {}, 소비 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.spendingUpdate(infoDTO);

        return "redirect:/";
    }

    @PostMapping("/update/income") // 수입
    String incomeUpdate(InfoDTO infoDTO){
        log.info("user {} 의 계좌 : {}, 수입 금액 : {}, 날짜 : {}, 내용 : {}",
                infoDTO.getUserPk(), infoDTO.getAccount(), infoDTO.getMoney(), infoDTO.getLocalDateTime(), infoDTO.getContent());

        infoService.incomeUpdate(infoDTO);

        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/search/total/each") // 특정 계좌 예산 조회
    Map totalSearchEach(InfoDTO infoDTO) {
        log.info("user {} 의 계좌 {} 예산 조회", infoDTO.getUserPk(), infoDTO.getAccount());

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        Optional<UserAccount> getTotal = userAccountRepository.findByUserPkAndAccount(new UserPk(infoDTO.getUserPk()), infoDTO.getAccount());

        Integer total = getTotal.get().getTotal();

        log.info("totalEach:{}",total);

        stringIntegerHashMap.put("totalEach", total);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/search/total/all") // 총 예산 조회
    Map totalSearch(InfoDTO infoDTO){
        log.info("user {} 의 총 예산 조회", infoDTO.getUserPk());

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        Optional<List<UserAccount>> totalByUserPk = userAccountRepository.findByUserPk(new UserPk(infoDTO.getUserPk()));

        Integer total = 0;

        for(int i=0; i<totalByUserPk.get().size(); i++){
            total += totalByUserPk.get().get(i).getTotal();
        }

        log.info("total:{}", total);

        stringIntegerHashMap.put("totalAll", total);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/search/spending/day") // 하루 지출 조회
    Map daySpendingSearch(InfoDTO infoDTO){
        log.info("user {} 의 {} 하루 지출 조회", infoDTO.getUserPk(), infoDTO.getLocalDate());

        String userPk = infoDTO.getUserPk();

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        Optional<List<Info>> getList = infoRepository.findByUserPkAndLocalDateAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getLocalDate(), null);

        Integer spending = 0;

        for (int i=0; i<getList.get().size(); i++){
            spending += getList.get().get(i).getSpending();
        }

        stringIntegerHashMap.put("spendingDay", spending);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/search/spending/month") // 한 달 지출 조회
    Map monthSpendingSearch(InfoDTO infoDTO) {
        log.info("user {} 의 {} 한 달 지출 조회", infoDTO.getUserPk(), infoDTO.getLocalDate());

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        YearMonth baseMonth = YearMonth.from(infoDTO.getLocalDate());

        LocalDate start = baseMonth.atDay(1);
        LocalDate end = baseMonth.atEndOfMonth();

        log.info(start.toString());
        log.info(end.toString());

        Optional<List<Info>> getList = infoRepository.findByUserPkAndIncomeAndLocalDateBetween(
                new UserPk(infoDTO.getUserPk()), null, start, end);

        Integer spending = 0;

        for (int i=0; i<getList.get().size(); i++){
            spending += getList.get().get(i).getSpending();
        }

        stringIntegerHashMap.put("spendingMonth", spending);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/search/spending/all") // 총 지출 조회
    Map allSpendingSearch(InfoDTO infoDTO){
        log.info("user {} 의 총 지출 조회", infoDTO.getUserPk());

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        Optional<List<Info>> userPk = infoRepository.findByUserPkAndIncome(new UserPk(infoDTO.getUserPk()), null);

        Integer spending = 0;

        for (int i=0; i<userPk.get().size(); i++){
            spending += userPk.get().get(i).getSpending();
        }

        log.info("spending:{}", spending);

        stringIntegerHashMap.put("spendingAll", spending);

        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/search/spending/each") // 특정 계좌 지출 조회
    Map spendingSearchEach(InfoDTO infoDTO) {
        log.info("user {} 의 계좌 {} 지출 조회", infoDTO.getUserPk(), infoDTO.getAccount());
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        Optional<List<Info>> getSpending = infoRepository.findByUserPkAndAccountAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getAccount(), null);

        Integer spending = 0;

        for (int i=0; i<getSpending.get().size(); i++){
            spending += getSpending.get().get(i).getSpending();
        }

        log.info("spendingEach:{}", spending);

//        model.addAttribute("spendingEach", spending);

        stringIntegerHashMap.put("spendingEach", spending);


        return stringIntegerHashMap;
    }

    @ResponseBody
    @PostMapping("/update/detail") // 내역 수정
    void updateDetail(@RequestBody InfoDTO infoDTO){
        log.info("id {} 의 내역 수정", infoDTO.getId());

        infoService.detailUpdate(infoDTO);
    }

    @ResponseBody
    @PostMapping("/delete/detail") // 내역 삭제
    void deleteDetail(@RequestBody InfoDTO infoDTO){
        log.info("id {} 의 내역 삭제", infoDTO.getId());

        infoService.detailDelete(infoDTO);
    }

    @PostMapping("/delete/account") // 특정 계좌 삭제
    String deleteAccount(UserAccountDTO userAccountDTO){
        log.info("user {} 의 계좌 {} 삭제", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        Optional<UserAccount> getUserAccount = userAccountRepository.findByUserPkAndAccount(
                new UserPk(userAccountDTO.getUserPk()), userAccountDTO.getAccount());

        Optional<List<Info>> getInfo = infoRepository.findByUserPkAndAccount(
                new UserPk(userAccountDTO.getUserPk()), userAccountDTO.getAccount());

        userAccountRepository.delete(getUserAccount.get());

        for (int i=0; i<getInfo.get().size(); i++){
            infoRepository.delete(getInfo.get().get(i));
        }

        return "redirect:/";
    }

    @PostMapping("/accounts.html") // 목록에 있는 계좌 클릭 시 데이터 전송
    String bridge(UserAccountDTO userAccountDTO, Model model){
        log.info("user {} 의 계좌 {} 선택", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

        String userPk = userAccountDTO.getUserPk();
        String account = userAccountDTO.getAccount();

        model.addAttribute("userPk", userPk);
        model.addAttribute("account", account);

        return "accounts";
    }

    @PostMapping("/test.html") // 목록에 있는 계좌 클릭 시 데이터 전송
    String thymeleafTest1(UserAccountDTO userAccountDTO, Model model){
        log.info("user {} 의 계좌 {} 선택", userAccountDTO.getUserPk(), userAccountDTO.getAccount());

//        String userPk = userAccountDTO.getUserPk();
        String account = userAccountDTO.getAccount();

//        model.addAttribute("userPk", userPk);
        model.addAttribute("account", account);

        return "test";
    }

    @PostMapping("/test2.html") // 특정 계좌 지출 조회
    String thymeleafTest2(InfoDTO infoDTO, Model model) {
        log.info("user {} 의 계좌 {} 지출 조회", infoDTO.getUserPk(), infoDTO.getAccount());

        Optional<List<Info>> getSpending = infoRepository.findByUserPkAndAccountAndIncome(
                new UserPk(infoDTO.getUserPk()), infoDTO.getAccount(), null);

        Integer spending = 0;

        for (int i=0; i<getSpending.get().size(); i++){
            spending += getSpending.get().get(i).getSpending();
        }

        log.info("spendingEach:{}", spending);

        model.addAttribute("spendingEach", spending);

        return "test";
    }

}
