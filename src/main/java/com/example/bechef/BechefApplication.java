package com.example.bechef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class BechefApplication {

	public static void main(String[] args) {
		SpringApplication.run(BechefApplication.class, args);

		String password = "admin1111";
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		System.out.println("Hashed password: " + hashedPassword);

	}
}
