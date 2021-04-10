package br.com.alura.forum.controller.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewTopicInputDTO {

	@NotBlank
	@Size(min = 10) 
	
	private String shortDescription;
	
	@NotBlank
	@Size(min = 10)
	private String content;
	
	@NotEmpty
	private String courseName;
	
    public Topic toTopic(CourseRepository courseRepository, User loggedUser) {
    	
        Course course = courseRepository.findByName(this.courseName).orElseThrow(() -> new IllegalArgumentException("curso não encontrado"));
        
        return new Topic(this.getShortDescription(), this.getContent(), loggedUser, course);
    }

}
