package net.bcsoft.library.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Questa classe di configurazione è per una libreria chiamata Swagger, che viene utilizzata per documentare e testare le API REST.
@Configuration
// Abilitiamo Swagger2
@EnableSwagger2
public class SwaggerConfig {

    //  Crea un bean Docket, che è un componente principale di Swagger che fornisce configurazioni per generare documentazione automatica per l'API.
    // La configurazione specifica che tutte le API (RequestHandlerSelectors.any())
    // e tutti i percorsi (PathSelectors.any()) devono essere inclusi nella documentazione generata

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.bcsoft.library.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    // Fornisce anche alcune informazioni di base sull'API come il titolo, la versione, la descrizione e i dettagli di contatto del proprietario dell'API.
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Library API")
                .description("API documentation for Library Application")
                .version("1.0.0")
                .build();
    }
}
