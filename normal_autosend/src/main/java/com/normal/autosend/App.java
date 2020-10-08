package com.normal.autosend;

import com.normal.autosend.impl.AutoGoodSenderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author: fei.he
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    AutoGoodSenderManager autoSenderManager;

    @Override
    public void run(String... args) throws Exception {
        autoSenderManager.init();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
