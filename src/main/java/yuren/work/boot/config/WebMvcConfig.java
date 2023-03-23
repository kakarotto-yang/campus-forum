package yuren.work.boot.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import yuren.work.boot.fitter.AdminInterceptor;
import yuren.work.boot.fitter.GlobalInterceptor;
import yuren.work.boot.fitter.UserInterceptor;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 使用阿里 FastJson 作为JSON MessageConverter
     * @param converters
     */
    @Bean
    public UserInterceptor UserInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    public AdminInterceptor AdminInterceptor() {
        return new AdminInterceptor();
    }

    @Bean
    public GlobalInterceptor GlobalInterceptor() {
        return new GlobalInterceptor();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        converters.add(converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        //访问的路径    当访问/upload/**路径下，会去Locations的路径文件上找
        registry.addResourceHandler("/photo/**")
                .addResourceLocations("file:D:/WorkSpace_Extra/javaweb/CampusForum/public/photo/"); //本地路径
        registry.addResourceHandler("/romdomAva/**")
                .addResourceLocations("file:D:/WorkSpace_Extra/javaweb/CampusForum/public/romdomAva/"); //本地路径
        super.addResourceHandlers(registry);
    }


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //设置允许跨域的路径
//        registry.addMapping("/**")
//                //设置允许跨域请求的域名
//                .allowedOrigins("*")
//                .allowedHeaders("*")
//                //如果它不设置预期的请求头部参数key值的话，ajax请求头部就没办法正确解析，也就是token解析不出来
//                .exposedHeaders("token")
////                .allowCredentials(true)//是否允许证书 不再默认开启
//                //设置允许的方法
//                .allowedMethods("GET", "POST", "PUT", "DELETE");
////                .maxAge(3600);//跨域允许时间
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(GlobalInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger-resources/**","/webjars/**","/auth/**");
        registry.addInterceptor(UserInterceptor()).addPathPatterns("/auth/**");
        registry.addInterceptor(AdminInterceptor()).addPathPatterns("/admin/**");
        super.addInterceptors(registry);
    }

}