package howudoin.howudoin.auth;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Tüm endpoint'ler için geçerli
                .allowedOrigins("http://localhost:19006")  // Mobil frontend URL'si
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Hangi HTTP metodlarına izin verileceği
                .allowedHeaders("*");  // Hangi başlıklara izin verileceği
    }
}
