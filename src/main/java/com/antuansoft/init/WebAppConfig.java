package com.antuansoft.init;

import java.net.UnknownHostException;
import java.util.List;

import com.antuansoft.herospace.AuthenticationUtil;
import com.antuansoft.herospace.HistoryAwarePerson;
import com.antuansoft.herospace.Person;
import com.antuansoft.herospace.PersonDAO;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@EnableAsync
@ImportResource("classpath:com/antuansoft/init/appconfig.xml")
@ComponentScan("com.antuansoft")
public class WebAppConfig extends WebMvcConfigurerAdapter {

    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";


    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";


    @Bean
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = this.getMappingJackson2HttpMessageConverter();
        converters.add(converter);
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = this.getObjectMapper();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // this is what you need
        objectMapper.setSerializationInclusion(Include.NON_NULL); // this is to not serialize unset properties
        return objectMapper;
    }

    @Bean
    public Thing thing() {
        Thing thing = new Thing();
        thing.name = "Hero";
        return thing;
    }

    @Bean
    public PersonDAO personDAO() throws UnknownHostException {
        //PersonDAO personDAO = new PersonDAO("localhost:27017", "anothertest", "person",Person.class);
        PersonDAO personDAO = null;
        return personDAO;
    }
    @Bean
    AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();


}




    @Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {


		registry.addResourceHandler("/assets*//**")
	    .addResourceLocations("classpath:WEB-INF/views/assets/");
	  registry.addResourceHandler("/css*//**")
	    .addResourceLocations("/css/");
	  registry.addResourceHandler("/images*//**")
	    .addResourceLocations("/images/");
	  registry.addResourceHandler("/js*//**")
	    .addResourceLocations("/js/");

		}



}