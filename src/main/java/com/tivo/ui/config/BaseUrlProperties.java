/**
 * 
 */
package com.tivo.ui.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "urlMap") // prefix app, find app.* values
public class BaseUrlProperties {
	private Map<String,Object> urlMap = new HashMap<String, Object>();

	public Map<String, Object> getUrlMap() {
		return urlMap;
	}

	public void setUrlMap(Map<String, Object> urlMap) {
		this.urlMap = urlMap;
	}
	
	

}
