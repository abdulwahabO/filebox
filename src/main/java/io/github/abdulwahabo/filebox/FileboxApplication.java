package io.github.abdulwahabo.filebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FileboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileboxApplication.class, args);
	}

}
