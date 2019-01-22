/*
package com.johndoeo.bootvueserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandRunner implements CommandLineRunner {
    private static Logger logger = LoggerFactory.getLogger(MyCommandRunner.class);
    @Value("${spring.web.visitUrl}")
    private String visitUrl;

    @Value("${spring.web.browserPath}")
    private String browserPath;

    @Value("${spring.auto.isAutoOpen}")
    private boolean isAutoOpen;

    @Override
    public void run(String... args) throws Exception {
        if(isAutoOpen){
            String cmd = browserPath +" "+ visitUrl;
            Runtime run = Runtime.getRuntime();
            try{
                run.exec(cmd);
                logger.debug("启动浏览器打开项目成功");
            }catch (Exception e){
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
*/
