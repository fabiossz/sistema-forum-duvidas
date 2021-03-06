package br.com.alura.forum.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alura.forum.model.User;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.alura.forum")).paths(PathSelectors.ant("/api/**"))
				.build().ignoredParameterTypes(User.class)
				.globalOperationParameters(Arrays.asList(new ParameterBuilder().name("Authorization")
						.description("Header para facilitar o envio do Authorization Bearer Token")
						.modelRef(new ModelRef("string")).parameterType("header").required(false).build()));
	}

	private ApiInfo apiInfo() {
		Contact contato = new Contact("Alura", "https://cursos.alura.com.br/", "contato@alura.com.br");
		return new ApiInfoBuilder().title("Alura Forum API Documentation").description(
				"Esta é a documentação interativa da Rest API do Fórum da Alura. Tente e nviar algum request ;)")
				.version("1.0").contact(contato).build();
	}

}
