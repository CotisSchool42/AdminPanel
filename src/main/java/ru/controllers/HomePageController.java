package ru.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class HomePageController {
    @GetMapping()
    public String homePage() {
        return "homePage/home";
    }


    @RequestMapping("500")
    String get500ErrorPage() {
        return "errors/500";
    }

    @RequestMapping("401")
    String get401ErrorPage() {
        return "errors/401";
    }

    @RequestMapping("404")
    String get404ErrorPage() {
        return "errors/404";
    }
}
