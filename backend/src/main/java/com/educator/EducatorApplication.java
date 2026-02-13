package com.educator;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EducatorApplication {

    public static void main(String[] args) {

       // Force JVM timezone early
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(EducatorApplication.class, args);
    }

}
