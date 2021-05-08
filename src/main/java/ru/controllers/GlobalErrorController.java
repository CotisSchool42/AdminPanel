package ru.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController


public class GlobalErrorController implements ErrorController {

    private static final String PATH = "/error";

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value = PATH)
    public void error(HttpServletResponse response) throws IOException {
        int status = response.getStatus();
        switch (status) {
            case 500:
                response.sendRedirect("/500");
                break;
            case 401:
                response.sendRedirect("/401");
                break;
            case 403:
                response.sendRedirect("/403");
                break;
            default:
                response.sendRedirect("/404");
        }

    }
}

