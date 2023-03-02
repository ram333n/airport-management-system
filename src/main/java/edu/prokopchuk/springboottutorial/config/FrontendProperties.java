package edu.prokopchuk.springboottutorial.config;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "frontend")
@Data
public class FrontendProperties {

  @Min(value = 1, message = "Crew page must contain at least 1 record")
  private int crewPageSize = 5;

  @Min(value = 1, message = "Flight page must contain at least 1 record")
  private int flightsPageSize = 5;

  @Min(value = 1, message = "Flight page of crew member must contain at least 1 record")
  private int flightsOfCrewMemberPageSize = 5;

  @Min(value = 1, message = "Crew page of flight must contain at least 1 record")
  private int crewOfFlightPageSize = 5;

}
