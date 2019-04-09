package com.ylchen;

import com.ylchen.storm.TopologyApp;
import com.ylchen.util.GetSpringBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class StromApplication {

    public static void main(String[] args) {
        //SpringApplication.run(StromApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(StromApplication.class, args);
        GetSpringBean springBean=new GetSpringBean();
        springBean.setApplicationContext(context);
        TopologyApp app = context.getBean(TopologyApp.class);
        app.runStorm(args);
    }

}
