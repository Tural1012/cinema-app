package cinema.cinema_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableCaching
public class CinemaAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaAppApplication.class, args);
	}

}
