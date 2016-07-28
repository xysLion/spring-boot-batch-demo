package com.xys.down.conf;

import com.xys.down.listener.JobCompletionNotificationListener;
import com.xys.down.listener.StepCompletionNotificationListener;
import com.xys.down.tasklet.AggregatorFile;
import com.xys.down.tasklet.DownFileReader;
import com.xys.down.tasklet.DownFileWriter;
import com.xys.down.tasklet.DownloadPartitioner;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;

/**
 * 文件下载任务配置
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/25
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
@Configuration
@EnableBatchProcessing
public class DownJobConfig {

    /** 任务名 */
    public static final String JOBNAME = "downFile";

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

//    @Bean
//    @StepScope
//    public TransferFile transferFile(
//            @Value("#{stepExecutionContext[inputFile]}") String inputFile,
//            @Value("#{stepExecutionContext[outputFilePath]}") String outputFile
//    ) {
//        TransferFile transferFile = new TransferFile(inputFile, outputFile);
//        return transferFile;
//    }

    @Bean
    @StepScope
    public DownFileReader downFileReader(@Value("#{stepExecutionContext[inputFile]}") String inputFile){
        return new DownFileReader(inputFile);
    }

    @Bean
    @StepScope
    public DownFileWriter downFileWriter(@Value("#{stepExecutionContext[outputFilePath]}") String outputFile){
        return new DownFileWriter(outputFile);
    }

    @Bean
    @StepScope
    public Partitioner downloadPartitioner(
            @Value("#{jobParameters[inputFiles]}") String inputFiles,
            @Value("#{jobParameters[middle]}") String middle
    ){
        return new DownloadPartitioner(inputFiles, middle);
    }

    @Bean
    @StepScope
    public AggregatorFile aggregatorFile(
            @Value("#{jobParameters[middle]}") String sourceDir,
            @Value("#{jobParameters[output]}") String targetDir
    ){
        return new AggregatorFile(sourceDir, targetDir);
    }

    @Bean(name = JOBNAME)
    public Job downFile(){
        return jobs.get(JOBNAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionNotificationListener())
                .flow(masterStep())
                .on(ExitStatus.FAILED.getExitCode()).to(masterStep())
                .from(masterStep()).on(ExitStatus.COMPLETED.getExitCode()).to(aggregatorFileStep())
                .end()
                .build();
    }

//    public Flow pullFileFlow(){
//        return this.jobs.get("pullFileFlow").flow(masterStep()).on("COMPLETED").to(aggregatorFileStep());
//    }

    @Bean
    public Step masterStep() {
        return this.steps.get("masterStep")
                .partitioner("pullFile", downloadPartitioner(null, null))
                .step(downFileStep())
                .gridSize(3)
                .taskExecutor(taskExecutor())
                .listener(new StepCompletionNotificationListener())
                .build();
    }

//    @Bean
//    public Step downFileStep(){
//        return this.steps.get("pullFile")
//                .tasklet(transferFile(null, null))
//                .build();
//    }

    @Bean
    public Step downFileStep(){
        return this.steps.get("pullFile")
                .<File, File> chunk(1)
                .reader(downFileReader(null))
                .writer(downFileWriter(null))
                .listener(new StepCompletionNotificationListener())
//                .faultTolerant()
//                .retry(RuntimeException.class)
//                .retryLimit(3)
//                .listener(new StepCompletionNotificationListener())
//                .allowStartIfComplete(false)
                .build();
    }

    @Bean
    public Step aggregatorFileStep() {
        return this.steps.get("aggregatorFile").tasklet(aggregatorFile(null, null)).build();
    }

}