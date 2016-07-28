package com.xys.down.tasklet;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.File;

/**
 * 迁移文件操作
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/25
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class TransferFile implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferFile.class);

    private static int test = 0;

    /** 读取文件路径 */
    private final String inputFile;

    /** 输出文件路径 */
    private final String outputFilePath;

    public TransferFile(String inputFile, String outputFilePath) {
        this.inputFile = inputFile;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        LOGGER.info("迁移文件步骤：{}, {}", contribution, chunkContext);

        if (test == 0) {
            test = test + 1;
            throw new RuntimeException("模拟异常发生");
        }

        File source = new File(inputFile);

        File target = new File(outputFilePath + File.separator + source.getName());

        Files.copy(source, target);

        return RepeatStatus.FINISHED;
    }
}