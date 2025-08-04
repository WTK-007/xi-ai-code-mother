package com.wtk.xiaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wtk.xiaicodemother.mapper")
public class XiAiCodeMotherApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiAiCodeMotherApplication.class, args);
	}

}
