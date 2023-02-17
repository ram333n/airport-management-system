package edu.prokopchuk.springboottutorial.config;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "frontend")
@Data
public class FrontendProperties {

  @Min(value = 1, message = "Page must contain at least 1 record")
  private int crewPageSize = 5;

}
