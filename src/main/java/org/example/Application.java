package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        System.out.println("Starting Task Application!!!!");

        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        System.out.println("------------------------------------------------");
        System.out.println("-----Printing Bean Definition Names ------------");
        System.out.println("------------------------------------------------");
        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }
        System.out.println("------------------------------------------------");
        System.out.println("--------------------END------------------------");
        System.out.println("------------------------------------------------");

    }

}
