package com.github.springboot.cache.autoconfigure;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * @author WangChen
 * @since 2021-12-24 17:34
 **/
public abstract class InstantiationAwareBeanAfterPostProcessorAdapter implements InstantiationAwareBeanPostProcessor {

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        doPostProcessAfterInstantiation(bean, beanName);
        return true;
    }

    /**
     * There is no return value
     * @param bean bean
     * @param beanName beanName
     */
    public abstract void doPostProcessAfterInstantiation(Object bean, String beanName);
}
