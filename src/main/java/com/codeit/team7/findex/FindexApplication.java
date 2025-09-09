package com.codeit.team7.findex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
// @EnableScheduling
@SpringBootApplication
public class FindexApplication {

  public static void main(String[] args) {
    SpringApplication.run(FindexApplication.class, args);
    System.out.println("http://localhost:8080/swagger-ui/index.html");
    System.out.println("http://localhost:8080");
  }

}
