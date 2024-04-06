package org.zerock.api01.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

//@Configuration
//public class OpenAPIConfig {
//
//    @Bean
//    public GroupedOpenApi restApi(){
//
//        String[] pathToMatch = {"/api/**"};
//
//        return GroupedOpenApi.builder()
//                .pathsToMatch(pathToMatch)
//                .group("REST API")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi commonApi(){
//        String[] pathsToExclude = {"/api/**", "/replies/**"};
//
//        return GroupedOpenApi.builder()
//                .pathsToMatch("/**/*")
//                .pathsToExclude(pathsToExclude)  //api를 사용하는 경우는 common에서 제외함.
//                .group("COMMON API")
//                .build();
//    }
//
//    @Bean
//    public OpenAPI customOpenAPI() {    // OpenAPI 타입의 객체를 반환
//        final String securitySchemeName = "BearerAuth"; // API 보안 스키마를 참조할 때 사용되는 보안 스키마의 이름을 "BearerAuth"
//
//        return new OpenAPI() //OpenAPI 객체 인스턴스 생성
//                .components(new Components().addSecuritySchemes(securitySchemeName, // API의 재사용 가능한 구성요소들을 설정,  여기서는 Components 객체를 생성하고, 이 객체의 addSecuritySchemes 메소드를 사용해 보안 스키마를 추가함. 보안 스키마의 이름으로 앞서 설정한 "BearerAuth"를 사용
//                        new SecurityScheme() //SecurityScheme 객체 생성 이 객체로 HTTP Bearer 인증 방식을 정의
//                                .type(SecurityScheme.Type.HTTP) //인증 스키마의 타입을 HTTP로 설정
//                                .scheme("bearer")   //"bearer" 방식을 사용함을 나타냄
//                                .bearerFormat("JWT")    //토큰의 형식이 JWT임을 지정
//                                .in(SecurityScheme.In.HEADER)   //인증 정보가 HTTP 헤더에 위치한다는 것을 의미
//                                .name("Authorization")))    //해당 정보가 "Authorization" 헤더에 포함될 것이라는 것을 의미
//                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)); //addSecurityItem 메소드를 사용하여 보안 요구 사항을 추가, SecurityRequirement 객체 생성, addList 메소드를 통해 앞서 정의한 보안 스키마 이름("BearerAuth")을 참조. 이것으로 API가 해당 보안 스키마를 사용함을 명시
//    }
//
//
//}


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

    @Bean
    public OpenAPI openAPI() {

// Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme() //SecurityScheme 타입의 객체 bearerAuth 생성 -> HTTP Bearer 인증 방식에 대한 설정
                .type(SecurityScheme.Type.HTTP) //보안 스키마의 타입을 HTTP로 설정
                .scheme("bearer") //인증 스키마로 "bearer"사용함을 나타냄, ->  Bearer 토큰 방식 사용을 의미
                .bearerFormat("Authorization") //bearerFormat "Authorization"으로 설정 ( bearerFormat은 일반적으로 토큰 타입을 명시함 ex)"JWT")
                .in(SecurityScheme.In.HEADER) //인증 정보를 HTTP 헤더에 위치시킴
                .name(HttpHeaders.AUTHORIZATION); //HTTP 헤더에서 사용될 필드의 이름을 "Authorization"으로 설정 ->HttpHeaders.AUTHORIZATION으로 이를 명시

// Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();//보안 요구 사항을 정의하기 위한 SecurityRequirement 객체 addSecurityItem 생성
        addSecurityItem.addList("Authorization"); //"Authorization" 보안 스키마 설정 -> 앞서 설정한 bearerAuth와 일치해야 함

        return new OpenAPI()
                .components(
                        new Components() //구성요소에 보안 스키마 추가
                                .addSecuritySchemes("Authorization", bearerAuth) // "Authorization" 이름으로 bearerAuth 보안 스키마 등록

                )
                // API 마다 Security 인증 컴포넌트 설정
                .addSecurityItem(addSecurityItem) //전역 보안 요구 사항으로 addSecurityItem을 추가 -> API 호출할 때 해당 보안 스키마를 충족해야 함을 의미

                .info(new Info().title("SpringDoc SwaggerUI example") //OpenAPI 문서의 정보를 설정
                        .description("Test SwaggerUI application") //문서의 설명 "Test SwaggerUI application"으로 설정
                        .version("v0.0.1"));    //문서 버전 설정  "v0.0.1"로 설정 ->  API의 버전을 나타내는 데 사용됨

    }

//    @Bean
//    public GroupedOpenApi group2() {
//        return GroupedOpenApi.builder()
//                .group("API")
//                .pathsToMatch("/api/**")
//                .build();
//    }
}
