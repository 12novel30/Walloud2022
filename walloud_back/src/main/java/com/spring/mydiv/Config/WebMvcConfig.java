package com.spring.mydiv.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOrigins("http://localhost:3000")
//            .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name());
//    }

    // for APPLICATION_FORM_URLENCODED
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // add converter suport Content-Type: 'application/x-www-form-urlencoded'
//        converters.stream()
//                .filter(AllEncompassingFormHttpMessageConverter.class::isInstance)
//                .map(AllEncompassingFormHttpMessageConverter.class::cast)
//                .findFirst()
//                .ifPresent(converter -> converter.addSupportedMediaTypes(MediaType.APPLICATION_FORM_URLENCODED));
//    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(converter());
//    }
//
//    private MyObjectConverter converter() {
//        MyObjectConverter converter = new MyObjectConverter();
//        MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", Charset.forName("UTF-8"));
//        converter.setSupportedMediaTypes(Arrays.asList(mediaType));
//        return converter;
//    }

}