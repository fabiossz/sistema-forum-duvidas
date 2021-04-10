package br.com.alura.forum.controller;

import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDTO;
import br.com.alura.forum.controller.dto.input.NewTopicInputDTO;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.AnswerOutputDTO;
import br.com.alura.forum.controller.dto.output.DashboardItemInfoDTO;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDTO;
import br.com.alura.forum.exception.ResourceNotFound;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.model.topic.domain.TopicStatus;
import br.com.alura.forum.repository.AnswerRepository;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.TopicService;
import br.com.alura.forum.validator.dto.NewTopicCustomValidator;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/topics")
@AllArgsConstructor
public class TopicController {

	@Autowired 
	private TopicRepository topicRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	private AnswerRepository answerRepository;

	private TopicService topicService;

	@GetMapping("dashboard")
    public List<DashboardItemInfoDTO> dash() {
        List<Category> categories = categoryRepository.findByCategoryIsNull();

        return categories.stream().map(category -> {
            Integer allTopics = topicRepository.countTopicsByCategoryId(category.getId());
            Instant lastWeekStartDate = Instant.now().minus(7, ChronoUnit.DAYS);
            Integer lastWeek = topicRepository.countLastWeekTopicsByCategoryId(category.getId(), lastWeekStartDate);
            Integer notAnswered = topicRepository.countTopicsByCategoryIdAndStatus(category.getId(), TopicStatus.NOT_ANSWERED);
            return new DashboardItemInfoDTO(category, allTopics, lastWeek, notAnswered);
        }).collect(Collectors.toList());

    }
	
	@GetMapping(value = "/api/topics", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch,
			@PageableDefault(sort = "creationInstant", direction = Sort.Direction.DESC) Pageable pageRequest) {

		Specification<Topic> topicSearchSpecification = topicSearch.build();

		Page<Topic> topics = topicRepository.findAll(topicSearchSpecification, pageRequest);

		return TopicBriefOutputDto.listFromTopics(topics);
	}



	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TopicOutputDTO> createTopic(@Valid @RequestBody NewTopicInputDTO newTopic,
			@AuthenticationPrincipal User user, UriComponentsBuilder uriComponentsBuilder) {

		Topic savedTopic = newTopic.toTopic(courseRepository, user);

		topicRepository.save(savedTopic);

		URI uri = uriComponentsBuilder.path("api/topics/{id}").buildAndExpand(savedTopic.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new TopicOutputDTO(savedTopic));

	}

	@InitBinder("newTopicInputDto")
	public void initBinder(WebDataBinder binder, @AuthenticationPrincipal User loggedUser) {
		binder.addValidators(new NewTopicCustomValidator(this.topicRepository, loggedUser));
	}

	@GetMapping("{id}")
	public ResponseEntity<TopicOutputDTO> findById(@PathVariable("id") Long topicId) {
		Topic topic = topicService.findById(topicId);
		return ResponseEntity.ok(new TopicOutputDTO(topic));
	}

	@GetMapping("{idDoTopico}/answers/{idAnswer}")
	public ResponseEntity<AnswerOutputDTO> findAnswerById(@PathVariable("idDoTopico") Long topicId,
			@PathVariable("idAnswer") Long answerId) {
		Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFound("answer not found"));
		return ResponseEntity.ok(new AnswerOutputDTO(answer));
	}

	@PostMapping("{idDoTopico}/answers")
	public ResponseEntity<AnswerOutputDTO> createAnswer(@PathVariable("idDoTopico") Long topicId,
			@Valid @RequestBody NewAnswerInputDTO input, @AuthenticationPrincipal User loggedUser,
			UriComponentsBuilder uriBuilder) {
		Topic topic = topicService.findById(topicId);
		Answer answer = input.toAnswer(topic, loggedUser);
		answerRepository.save(answer);
		Map<String, Long> pathVariables = new HashMap<>();
		pathVariables.put("idDoTopico", topic.getId());
		pathVariables.put("answerId", answer.getId());

		URI uri = uriBuilder.path("api/topics/{idDoTopico}/answers/{answerId}").buildAndExpand(pathVariables).toUri();

		return ResponseEntity.created(uri).body(new AnswerOutputDTO(answer));

	}

}
