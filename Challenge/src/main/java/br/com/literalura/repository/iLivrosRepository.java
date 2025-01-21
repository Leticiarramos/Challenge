package br.com.literalura.repository;

import br.com.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface iLivrosRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContains(String titulo);
    List<Livro> findByIdiomasContains(String idiomas);
}
