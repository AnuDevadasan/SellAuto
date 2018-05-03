package com.sellauto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.sellauto.storage.*;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SellAutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellAutoApplication.class, args);
	}
	
    @Bean
    CommandLineRunner init(StorageRepository storageService) {
        return (args) -> {
        	//create the image storage folder if it did n't exists
          storageService.init();
        };
    }
}
