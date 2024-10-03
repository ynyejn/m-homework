package com.musinsa.homework.api;

import com.musinsa.homework.service.ItemApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ItemApiController {
    private final ItemApplicationService itemApplicationService;

}
