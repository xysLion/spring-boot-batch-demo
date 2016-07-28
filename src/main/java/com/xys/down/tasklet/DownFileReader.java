package com.xys.down.tasklet;

import com.xys.down.conf.FileTaskSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;

import java.io.File;

/**
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/26
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class DownFileReader implements ItemReader<File>, ItemStream {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownFileReader.class);

    /** 读取文件路径 */
    private final String inputFile;

    int currentIndex = 0;

    private static final String CURRENT_INDEX = "current.index";

    public DownFileReader(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public File read() throws Exception {

        if (currentIndex < 1) {

            if (FileTaskSetting.count <= 4) {
                FileTaskSetting.count++;
                throw new RuntimeException("模拟异常发生！");
            }

            currentIndex++;

            return new File(inputFile);
        }

        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        if(executionContext.containsKey(CURRENT_INDEX)){
            currentIndex = new Long(executionContext.getLong(CURRENT_INDEX)).intValue();
        }
        else{
            currentIndex = 0;
        }

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

        executionContext.putLong(CURRENT_INDEX, new Long(currentIndex).longValue());

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
