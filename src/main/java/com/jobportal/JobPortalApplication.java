package com.jobportal;

import com.jobportal.entity.UsersType;
import com.jobportal.repository.UsersTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class JobPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(UsersTypeRepository usersTypeRepository) {
		return args -> {
			if (usersTypeRepository.findAll().isEmpty()) {
				UsersType recruiter = new UsersType();
				recruiter.setUserTypeId(1);
				recruiter.setUserTypeName("Recruiter");

				UsersType jobSeeker = new UsersType();
				jobSeeker.setUserTypeId(2);
				jobSeeker.setUserTypeName("Job Seeker");

				usersTypeRepository.saveAll(Arrays.asList(recruiter, jobSeeker));
			}
		};
	}
}
