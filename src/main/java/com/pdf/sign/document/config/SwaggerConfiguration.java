package com.pdf.sign.document.config;

//import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableWebMvc
@Configuration
//@EnableSwagger2
//@Component
public class SwaggerConfiguration {/*

    // http://localhost:8089/NetProFaN/swagger-ui/
    // http://3.110.153.108:8083/NetProFaN/swagger-ui/

    @Bean
    public Docket cobirtApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pdf.sign.document.controller"))
                .paths(regex("/.*")).build().apiInfo(metaData());
    }

    private ApiInfo metaData(){
        return new ApiInfoBuilder().title("E Sign PDF Rest API")
                .description("\"PDF E-Sign Rest API\"")
                .version("1.0.0")
                .build();
    }

   // @Bean
    //@Profile({"local","dev","sit"})
    public CommonsRequestLoggingFilter loggingFilter(){
      CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
      loggingFilter.setIncludeQueryString(true);
      loggingFilter.setIncludePayload(true);
      loggingFilter.setMaxPayloadLength(10000);
      loggingFilter.setIncludeHeaders(false);
      loggingFilter.setAfterMessagePrefix("REQUEST DATA : ");

      return loggingFilter;
    }
*/}
