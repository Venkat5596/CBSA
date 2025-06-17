package com.sks.tariff_01.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
//import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
//import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor defaultHeaderInterceptor = (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setAccept(List.of(MediaType.APPLICATION_XML));
            headers.setAcceptLanguageAsLocales(List.of(Locale.ENGLISH));
            return execution.execute(request, body);
        };

        restTemplate.getInterceptors().add(defaultHeaderInterceptor);
        return restTemplate;
    }


    @Bean
    public PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolverCustomizer() {

        PageableHandlerMethodArgumentResolver customizer = new PageableHandlerMethodArgumentResolver() {
            @Override
            public String getPageParameterName() {
                return super.getPageParameterName();

            }
        };
        customizer.setOneIndexedParameters(true);
        return customizer;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(pageableHandlerMethodArgumentResolverCustomizer());
    }
}
