package com.antuansoft.init;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:com/antuansoft/init/mongo-config.xml")
public class MongoConfig {

}
