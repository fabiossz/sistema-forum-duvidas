package br.com.alura.forum.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import br.com.alura.forum.model.Answer;

public interface AnswerRepository extends Repository<Answer, Long> {

	void save(Answer answer);

	Optional<Answer> findById(Long answerId);
}
