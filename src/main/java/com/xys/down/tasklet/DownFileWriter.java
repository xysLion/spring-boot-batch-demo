package com.xys.down.tasklet;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.util.List;

/**
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/26
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class DownFileWriter implements ItemWriter<File> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownFileWriter.class);

    /** 输出文件路径 */
    private final String outputFilePath;

    public DownFileWriter(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void write(List<? extends File> items) throws Exception {

        for (File source : items) {

            if (source != null) {

                File target = new File(outputFilePath + File.separator + source.getName());

                Files.copy(source, target);

            }

        }

    }
}
