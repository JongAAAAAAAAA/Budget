package com.budget.service;

import com.budget.dao.InfoRepository;
import com.budget.dao.UserPkRepository;
import com.budget.dto.InfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class InfoService {
    private final UserPkRepository userPkRepository;
    private final InfoRepository infoRepository;

    public void spendingUpdate(InfoDTO infoDTO){

    }
}
