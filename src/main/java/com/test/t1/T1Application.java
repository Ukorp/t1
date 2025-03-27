package com.test.t1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class T1Application {

	public static void main(String[] args) {
		SpringApplication.run(T1Application.class, args);
	}

}
