package com.cargosmart.mongodemo.annotation;

import java.lang.annotation.*;

/**
 * @author licankun
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {

}
