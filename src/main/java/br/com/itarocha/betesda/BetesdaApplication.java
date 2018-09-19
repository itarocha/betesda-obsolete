package br.com.itarocha.betesda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
//@EnableTransactionManagement
@ComponentScan({"br.com.itarocha.betesda.controller","br.com.itarocha.betesda.service", "br.com.itarocha.betesda.repository"})
public class BetesdaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetesdaApplication.class, args);
	}
}
