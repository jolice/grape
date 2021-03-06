package com.github.jolice.exception.injection;

public class InjectionPointError extends RuntimeException {

    public InjectionPointError(Class<?> member, RuntimeException exception) {
        super("Injection failed in class " + member);
        initCause(exception);
    }
}