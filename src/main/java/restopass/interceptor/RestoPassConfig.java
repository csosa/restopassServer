package restopass.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import restopass.utils.EnumToIntegerConverter;
import restopass.utils.IntegerToEnumConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestoPassConfig extends WebMvcConfigurerAdapter {

    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/users/login", "/users/refresh");
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new EnumToIntegerConverter());
        converterList.add(new IntegerToEnumConverter());
        return new CustomConversions(converterList);
    }
}
