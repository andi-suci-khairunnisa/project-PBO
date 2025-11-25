package wahdini.getajobcopy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/");
    }
}
// WebConfig: maps requests under `/uploads/**` to files inside the project's
// `uploads/` folder on disk (uses the JVM `user.dir` path). This enables
// serving user-uploaded files (images, attachments) via URLs like
// `/uploads/<filename>`.
