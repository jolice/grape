package com.github.jolice.bean.registry;

import com.github.jolice.bean.matcher.AnyMatcher;
import com.github.jolice.bean.matcher.BeanMatcher;
import com.github.jolice.bean.matcher.policy.BindingPolicy;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Represents a bean request.
 */
@Data
@Setter(AccessLevel.NONE)
public class BeanQuery {

    /**
     * Type of the bean. If target bean implements an interface,
     * the interface type must be specified. Implementation injection
     * is not currently supported, i.e you may only use an interface
     * at the injection point.
     */
    private Class<?> type;

    /**
     * The class that requests a bean. Used by the container internals to provide
     * better debug information. This attribute may be ignored by container clients.
     */
    private Class<?> querySource;

    /**
     * A matcher specifying the bean to be obtained. If there's only bean of the
     * certain type, use {@link AnyMatcher}
     */
    private BeanMatcher beanMatcher;
    private BindingPolicy bindingPolicy;

    public BeanQuery(Class<?> type, BeanMatcher beanMatcher, BindingPolicy bindingPolicy) {
        this.type = type;
        this.beanMatcher = beanMatcher;
        this.bindingPolicy = bindingPolicy;
    }

    public BeanQuery(Class<?> type, Class<?> querySource, BeanMatcher beanMatcher, BindingPolicy bindingPolicy) {
        this(type, beanMatcher, bindingPolicy);
        this.querySource = querySource;
    }
}
