package com.example.hackernewsscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackernewsscraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackernewsscraperApplication.class, args);
	}

}
