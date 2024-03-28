package org.zerock.api01.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi restApi(){

        String[] pathToMatch = {"/api/**"};

        return GroupedOpenApi.builder()
                .pathsToMatch(pathToMatch)
                .group("REST API")
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi(){
        String[] pathsToExclude = {"/api/**", "/replies/**"};

        return GroupedOpenApi.builder()
                .pathsToMatch("/**/*")
                .pathsToExclude(pathsToExclude)  //api를 사용하는 경우는 common에서 제외함.
                .group("COMMON API")
                .build();
    }


}
