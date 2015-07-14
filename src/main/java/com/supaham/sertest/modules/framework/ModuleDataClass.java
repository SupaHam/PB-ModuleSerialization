package com.supaham.sertest.modules.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to reference the {@link ModuleData} class used to process the annotated class.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDataClass {
    
    Class<? extends ModuleData> value();
}
