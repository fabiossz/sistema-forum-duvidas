package br.com.alura.forum.controller.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAnswerInputDTO {

    @NotBlank
    @Size(min = 10, max = 250)
    private String content;


    public Answer toAnswer(Topic topic, User user) {
        return new Answer(this.content, topic, user);
    }
}