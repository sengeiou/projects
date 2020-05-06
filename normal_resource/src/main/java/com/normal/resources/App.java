package com.normal.resources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@Controller
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    @RequestMapping("testRedirect")
    public String redirect() {
        return "redirect:https://www.baidu.com/";
    }
}
