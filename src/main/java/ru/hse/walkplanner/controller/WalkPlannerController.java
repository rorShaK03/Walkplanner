package ru.hse.walkplanner.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalkPlannerController {

    @GetMapping("hello")
    public String ahaha() {
        return "Hello";
    }
}
