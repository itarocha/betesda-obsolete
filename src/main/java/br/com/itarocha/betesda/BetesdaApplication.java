package br.com.itarocha.betesda;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


// Olha só de onde eu tirei: https://github.com/callicoder/spring-security-react-ant-design-polls-app
// https://java.jsonwebtoken.io/
// https://github.com/jwtk/jjwt

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan({"br.com.itarocha.betesda.controller",
				"br.com.itarocha.betesda.config", 
				"br.com.itarocha.betesda.service", 
				"br.com.itarocha.betesda.payload", 
				"br.com.itarocha.betesda.security", 
				"br.com.itarocha.betesda.repository"})
@EntityScan(basePackageClasses = { 
		BetesdaApplication.class,
		Jsr310JpaConverters.class 
})
public class BetesdaApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(BetesdaApplication.class, args);
	}
}
