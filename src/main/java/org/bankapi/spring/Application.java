package org.bankapi.spring;

import org.bankapi.sql.PostgreSQLDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PostgreSQLDatabase db() throws IOException {
        // Загружаем параметры из файла config.properties
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("src/main/resources/config.properties");
        props.load(in);
        in.close();

        // Получаем значения параметров
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        return new PostgreSQLDatabase(url, user, password);
    }
}
