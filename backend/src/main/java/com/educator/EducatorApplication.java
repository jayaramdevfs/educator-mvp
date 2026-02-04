package com.educator;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EducatorApplication {

    public static void main(String[] args) {

       // Force JVM timezone early
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(EducatorApplication.class, args);
    }

}
