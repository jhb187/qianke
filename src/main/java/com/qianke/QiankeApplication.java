package com.qianke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qianke"})
@EnableScheduling
//若要修改为本地内置tomcat启动，直接注释掉本类，释放下方注释的启动类
//外置tomcat  war包启动
/*
public class QiankeApplication extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(QiankeApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(QiankeApplication.class);
    }

}*/


//原先内置tomcat启动类
public class QiankeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QiankeApplication.class, args);
	}

}