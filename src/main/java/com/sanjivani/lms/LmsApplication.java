package com.sanjivani.lms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(servers = {@Server(url = "/sanjivani", description = "Sanjivani LMS")}, info = @Info(title = "Sanjivani", version = "1.0.0"))
@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {

				registry
						.addResourceHandler("sanjivani/swagger-ui.html")
						.addResourceLocations("classpath:/META-INF/resources/");

				registry
						.addResourceHandler("/sanjivani/webjars/**")
						.addResourceLocations("classpath:/META-INF/resources/webjars/");
			}
		};
	}

}
