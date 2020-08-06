package me.mw.workoutisgood;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import me.mw.workoutisgood.service.jdbcService;
import me.mw.workoutisgood.service.CalendarService;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.*;
import java.net.URL;

@Configuration
public class Runner implements ApplicationRunner {

    @Autowired
    CalendarService cal;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    jdbcService jdbcService;
    @Autowired
    DataSource dataSource;

    @Autowired
    ServletContext servletContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}
