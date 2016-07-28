package com.xys.down;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.xys.down.conf.FileTaskSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

import static com.xys.down.conf.DownJobConfig.JOBNAME;

/**
 * 文件下载app
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/25
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
@SpringBootApplication
public class DownApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownApp.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(DownApp.class, args);
    }

    @Component
    public static class CommandLine implements CommandLineRunner {

        @Resource
        private JobLauncher jobLauncher;

        @Resource(name = JOBNAME)
        private Job job;

        @Resource
        private FileTaskSetting setting;

        @Override
        public void run(String... strings) throws Exception {

            Map<String, JobParameter> parameterMap = Maps.newHashMap();

            String inputFiles = Joiner.on(File.pathSeparator).skipNulls().join(setting.getInput().getFiles());
            parameterMap.put("inputFiles", new JobParameter(inputFiles));
            parameterMap.put("middle", new JobParameter(setting.getMidlle()));
            parameterMap.put("output", new JobParameter(setting.getOutput()));

            JobParameters jobParameters = new JobParameters(parameterMap);

            JobExecution execution = jobLauncher.run(job, jobParameters);

            LOGGER.info(execution.toString());

        }
    }

}