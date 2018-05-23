package com.cook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: cook-root
 * @description: 项目启动入口
 * @author: ziHeng
 * @create: 2018-05-03 15:18
 **/
@SpringBootApplication(scanBasePackages = "com")
@MapperScan({ "com.cook.dao" , "com.ziheng.dao" , "com.lin.dao" , "com.cai.dao" }) //dao层扫描
@EnableSwagger2
public class CookApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CookApplication.class);
    }



    public static void main(String[] args) {
        SpringApplication.run(CookApplication.class, args);
    }


}
