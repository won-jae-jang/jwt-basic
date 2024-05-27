package hello.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 파라미터 타입의 변환을 추가할 수 있도록 클래스 생성
 */
@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/files/**")
                .addResourceLocations("classpath:/static/");
    }
}
