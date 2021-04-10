package br.com.alura.forum.service;

import org.springframework.stereotype.Service;

import br.com.alura.forum.exception.ResourceNotFound;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TopicService {

    private TopicRepository topicRepository;

    public Topic findById(Long topicId) {
        return topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFound("topic not found"));
    }
}