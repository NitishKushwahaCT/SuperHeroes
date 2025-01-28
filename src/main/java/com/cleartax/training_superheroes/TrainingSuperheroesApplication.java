package com.cleartax.training_superheroes;

import com.cleartax.training_superheroes.services.SQSConsumerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrainingSuperheroesApplication implements CommandLineRunner {

	private final SQSConsumerService sqsConsumerService;

	public TrainingSuperheroesApplication(SQSConsumerService sqsConsumerService) {
		this.sqsConsumerService = sqsConsumerService;
	}
	public static void main(String[] args) {
		SpringApplication.run(TrainingSuperheroesApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Starting SQS listener...");
		sqsConsumerService.startListening();
	}

}
