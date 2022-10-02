package com.seg-vault.ocr;

import com.seg-vault.ocr.service.OcrService;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.seg-vault.ocr.storage.StorageProperties;
import com.seg-vault.ocr.service.StorageService;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@Import(OcrConfiguration.class)
public class OcrApplication {
        @Autowired
        private JobScheduler jobScheduler;

        @Autowired
        private OcrService ocrService;
        
	public static void main(String[] args) {
		SpringApplication.run(OcrApplication.class, args);
	}
        @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
                        jobScheduler.enqueue(() -> ocrService.execute());
			storageService.deleteAll("tmp");//delete contents of temp directory
			storageService.init();
		};
	}
        
        /*
        @Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}

		};
	}
        */
}
