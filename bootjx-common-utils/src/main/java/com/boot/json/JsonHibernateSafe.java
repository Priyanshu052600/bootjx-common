package com.boot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * If this annotation is assigned to any method, output of that method will be
 * cached, for user scope using userId as unique ID
 * 
 * @author lalittanwar
 *
 */
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public @interface JsonHibernateSafe {

}