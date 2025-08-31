package com.moger.demo;

import com.moger.demo.util.QueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This class to perform a set of sample sql queries as a baseline for our project to run
 * */
@Component
public class MyRunner implements CommandLineRunner {

    private final QueryData queryData;;

    @Autowired
    public MyRunner(QueryData queryData){
        this.queryData = queryData;
    }

    @Override
    public void run(String... args) throws Exception {
        queryData.loadData();
    }
}
