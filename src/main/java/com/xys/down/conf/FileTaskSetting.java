package com.xys.down.conf;

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件任务设定
 *
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/26
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
@Component
@ConfigurationProperties(prefix = "filetask")
public class FileTaskSetting {

    private Input input;

    private String midlle;

    private String output;

    public static int count = 0;

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getMidlle() {
        return midlle;
    }

    public void setMidlle(String midlle) {
        this.midlle = midlle;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public static class Input {

        List<String> files = Lists.newArrayList();

        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }
    }

}