package com.github.jolice.bean;

import com.github.jolice.annotation.Primary;

import java.lang.reflect.AnnotatedElement;

public interface BeanMeta {

    AnnotatedElement getAnnotationData();

    Class<?> getBeanClass();

    default boolean isPrimary() {
        return getAnnotationData().isAnnotationPresent(Primary.class);
    }
}
