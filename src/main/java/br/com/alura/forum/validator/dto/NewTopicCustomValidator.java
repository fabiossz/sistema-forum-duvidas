package br.com.alura.forum.validator.dto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.com.alura.forum.controller.dto.input.NewTopicInputDTO;
import br.com.alura.forum.model.PossibleSpam;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;

public class NewTopicCustomValidator implements Validator {

	private final TopicRepository topicRepository;

	private User loggedUser;

	public NewTopicCustomValidator(TopicRepository topicRepository, User loggedUser) {
		this.topicRepository = topicRepository;
		this.loggedUser = loggedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {

		return NewTopicInputDTO.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		Instant minus = Instant.now().minus(1,ChronoUnit.HOURS);
		
		List<Topic> topics = topicRepository.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggedUser, minus);
		
		PossibleSpam possibleSpam = new PossibleSpam(topics);
		
		if(possibleSpam.hasTopicLimitExceeded()) {
			
			long minuitesToNextTopic = possibleSpam.minuitesToNextTopic(minus);
			errors.reject("newTopicInputDto.limit.exceeded", new Object[] {minuitesToNextTopic}, "O limite individual de novos tópicos por hora foi excedido");
			
		}

	}

}
