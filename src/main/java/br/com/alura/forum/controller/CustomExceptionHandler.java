package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.output.ErrorOutputDTO;
import br.com.alura.forum.exception.ResourceNotFound;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.*;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "br.com.alura.forum.controller")
@AllArgsConstructor
@Slf4j
public class CustomExceptionHandler {

	private MessageSource messageSource;

	@ExceptionHandler({ Exception.class, NullPointerException.class })
	public ResponseEntity erro500(Exception ex) {
		log.error(ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.body(new ErrorOutputDTO("internal-error", "Unexpected error happens"));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity bad_credentials(AuthenticationException ex) {
		log.warn(ex.getMessage());
		return ResponseEntity.badRequest().body(new ErrorOutputDTO("credentials", "Revise suas credenciais"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity badRequest(MethodArgumentNotValidException ex) {
		List<ErrorOutputDTO> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> new ErrorOutputDTO(fieldError.getField(),
						messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())))
				.collect(Collectors.toList());
		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(ResourceNotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void notFound(ResourceNotFound ex) {
		log.info(ex.getMessage());
	}

}