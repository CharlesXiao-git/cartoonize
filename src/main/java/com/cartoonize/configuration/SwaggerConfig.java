package com.cartoonize.configuration;
import springfox.documentation.service.Parameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * set token as a global parameters
     * @return
     */
    public List<Parameter> getParameterList() {
        ParameterBuilder token = new ParameterBuilder();
        List<Parameter> params = new ArrayList<Parameter>();
        token.name("token")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        params.add((Parameter) token.build());
        return params;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cartoonize.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("Cartoonize Photo Webservice API")
                        .description("The api let users upload images and view them in a 'cartoonized' format.")
                        .version("1.0")
                        .contact(new Contact("Charles Xiao","","xiaobi808@yahoo.com"))
                        .license("Copyright by Charles Xiao")
                        .build()).globalOperationParameters(this.getParameterList());
    }
}
