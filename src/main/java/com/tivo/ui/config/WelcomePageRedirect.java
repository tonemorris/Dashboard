
package com.tivo.ui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WelcomePageRedirect extends WebMvcConfigurerAdapter {

@Value("${welcome.page}") // read from application.yml
private String welcomePage;
	
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/")
        .setViewName("forward:/" +welcomePage);
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    super.addViewControllers(registry);
  }
}
