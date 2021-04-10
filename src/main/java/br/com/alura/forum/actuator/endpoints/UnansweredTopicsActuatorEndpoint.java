package br.com.alura.forum.actuator.endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import br.com.alura.forum.model.OpenTopicByCategory;
import br.com.alura.forum.repository.TopicRepository;

@Component
@Endpoint(id = "open-topics-by-category")
public class UnansweredTopicsActuatorEndpoint {

	@Autowired
	private TopicRepository topicRepository;

	@ReadOperation
	public List<OpenTopicByCategory> execute() {
		return this.topicRepository.findOpenTopicsByCategory();
	}

}
