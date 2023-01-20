package com.example.StudentInfoBackend.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
        return args->{
            /*Student mariam= new Student(
                    "Merriam",
                    "19BCE2630",
                    "merriam.lok@gmail.com",
                    "+9779815709575",
                    LocalDate.of(2000, Month.JANUARY,5),
                    "absent",
                    "C:/Users/yraja/Downloads/images/server1.png"
            );
            Student bimal= new Student(
                    "Bimal",
                    "20BCE2630",
                    "bimalko.lok@gmail.com",
                    "+919658564856",
                    LocalDate.of(2002, Month.JANUARY,18),
                    "present",
                    "C:/Users/yraja/Downloads/images/server2.png"
            );
            Student sandesh= new Student(
                    "Sandesh",
                    "19BME0856",
                    "sandesh.pandey@gmail.com",
                    "+9779658564856",
                    LocalDate.of(1999, Month.FEBRUARY,8),
                    "present",
                    "C:/Users/yraja/Downloads/images/server3.png"
            );
            repository.saveAll(List.of(mariam,bimal,sandesh));*/

        };

    }
}
