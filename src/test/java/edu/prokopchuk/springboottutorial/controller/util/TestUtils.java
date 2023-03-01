package edu.prokopchuk.springboottutorial.controller.util;

import java.io.UnsupportedEncodingException;
import org.springframework.test.web.servlet.ResultActions;

public final class TestUtils {

  private TestUtils() {}

  public static String extractContent(ResultActions resultActions) throws UnsupportedEncodingException {
    return resultActions
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

}
