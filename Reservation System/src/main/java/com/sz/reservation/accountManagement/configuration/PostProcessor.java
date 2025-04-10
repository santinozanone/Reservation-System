package com.sz.reservation.accountManagement.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class PostProcessor implements BeanPostProcessor {
    private Logger logger = LogManager.getLogger(com.sz.reservation.propertyManagement.configuration.PostProcessor.class);
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.debug("DISERVLET, BEAN:{}",beanName);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
