package com.xys.down.tasklet;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 下载分割步骤
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/26
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class DownloadPartitioner implements Partitioner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadPartitioner.class);

    /** 文件列表 */
    private final List<String> files;

    /** 文件输出路径 */
    private final String outputFilePath;

    public DownloadPartitioner(String files, String outputFilePath) {

        this.files = Splitter
                .on(File.pathSeparator)
                .omitEmptyStrings()
                .trimResults()
                .splitToList(files);

        this.outputFilePath = outputFilePath;

    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> result = Maps.newHashMap();

        for (int i = 1; i < 4; i++) {

            ExecutionContext value = new ExecutionContext();

            String fileName = files.get(i - 1);

            value.put("inputFile", fileName);

            value.put("outputFilePath", outputFilePath);

            LOGGER.info("将文件{}迁移到{}", fileName, outputFilePath);

            result.put("partition" + i, value);

        }

        return result;
    }
}
