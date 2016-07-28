package com.xys.down.tasklet;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;

import static com.xys.down.constant.Constant.UTF8;

/**
 * 聚合文件夹中所有文件
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/25
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class AggregatorFile implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AggregatorFile.class);

    /** 需要聚合文件夹 */
    private final File sourceDir;

    /** 聚合后文件路径 */
    private final File targetFile;

    public AggregatorFile(String sourceDir, String targetDir) {

        Assert.notNull(sourceDir);
        Assert.notNull(targetDir);

        this.sourceDir = new File(sourceDir);

        if (!this.sourceDir.isDirectory()) {
            throw new RuntimeException("读取路径不是文件夹");
        }

        File[] files = this.sourceDir.listFiles();

        if (files.length > 0) {
            String fileName = Files.getNameWithoutExtension(files[0].getName());
            fileName = fileName.substring(0, fileName.length() - 1);
            String targetFile = Joiner.on(File.separator)
                    .skipNulls()
                    .join(targetDir, new DateTime().toString("yyyyMMddHHmmsss"), fileName);
            this.targetFile = new File(targetFile);
            try {
                // 创建父目录
                Files.createParentDirs(this.targetFile);
            } catch (IOException e) {
                LOGGER.error("创建输出文件夹出现异常: ", e);
            }
        }
        else {
            this.targetFile = null;
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        for ( File file : this.sourceDir.listFiles() ) {
            Files.append(Files.toString(file, UTF8), targetFile, UTF8);
        }

        return RepeatStatus.FINISHED;
    }

}
