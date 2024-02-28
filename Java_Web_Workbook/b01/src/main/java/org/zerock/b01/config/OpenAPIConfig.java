package org.zerock.b01.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi restApi(){
        String[] pathToMatch =  {"/replies/**", "/api/**"};

        return GroupedOpenApi.builder()
                .pathsToMatch(pathToMatch)//api로 시작하면 이걸 사용도록 함
                .group("REST API")
                .build();
    }


    @Bean
    public GroupedOpenApi commonApi(){

        String[] pathsToExclude = {"/api/**", "/replies/**"};

        return GroupedOpenApi.builder()
                .pathsToMatch("/**/*")    //경로에 api가 없는 애들은 이 설정을 사용하도록 함 + path형태 /**/*
                .pathsToExclude(pathsToExclude)  //api를 사용하는 경우는 common에서 제외함.
                .group("COMMON API")
                .build();
    }

//    @Bean
//    public GroupedOpenApi replyApi(){
//
//        return GroupedOpenApi.builder()
//                .pathsToMatch("/replies/**")    //경로에 api가 없는 애들은 이 설정을 사용하도록 함 + path형태 /**/*
//                .group("REPLY API")
//                .build();
//    }



}
