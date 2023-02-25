package edu.prokopchuk.springboottutorial.config;

import java.util.function.BiFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Configuration
public class ViewConfig {

  @Bean
  public BiFunction<String, String, String> replaceOrAddParam() {
    return (paramName, value) -> ServletUriComponentsBuilder.fromCurrentRequest()
        .replaceQueryParam(paramName, value)
        .toUriString();
  }

}
