package br.com.alura.forum.controller.dto.output;

import lombok.Getter;

@Getter
public class ErrorOutputDTO {

    private String code;
    private String message;

    public ErrorOutputDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

}