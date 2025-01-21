package br.com.literalura;

import br.com.literalura.repository.iAutorRepository;
import br.com.literalura.repository.iLivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeApplication implements CommandLineRunner {
	@Autowired
	private iLivrosRepository livrosRepositorio;

	@Autowired
	private iAutorRepository autorRepositorio;


	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livrosRepositorio, autorRepositorio);
		principal.exibeMenu();
	}
}
