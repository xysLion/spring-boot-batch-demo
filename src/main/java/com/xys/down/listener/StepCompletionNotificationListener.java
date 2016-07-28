package com.xys.down.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

/**
 * @author 摇光
 * @version 1.0
 * @Created on 2016/7/26
 * @Copyright 杭州安存网络科技有限公司 Copyright (c) 2016
 */
public class StepCompletionNotificationListener extends StepExecutionListenerSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepCompletionNotificationListener.class);

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        LOGGER.info("id : {}, name : {}, status : {}, exitStatus : {}, skipCount : {}, commitCount : {}, startTime : {}, endTime : {}",
                stepExecution.getId(),
                stepExecution.getStepName(),
                stepExecution.getStatus(),
                stepExecution.getExitStatus(),
                stepExecution.getSkipCount(),
                stepExecution.getCommitCount(),
                stepExecution.getStartTime(),
                stepExecution.getEndTime()
                );

        return null;
    }

}