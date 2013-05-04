package com.bot.modifiers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
	
    public String[] dependencies();

    
}