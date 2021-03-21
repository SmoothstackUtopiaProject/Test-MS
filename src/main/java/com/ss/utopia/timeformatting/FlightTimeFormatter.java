package com.ss.utopia.timeformatting;

import java.time.format.DateTimeFormatter;

public final class FlightTimeFormatter {
  
  private static DateTimeFormatter formatter;

  private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  private FlightTimeFormatter() {
    throw new IllegalStateException("Utilility class 'FlightTimeFormatter' is static and should not be instantiated.");
  }

  public static DateTimeFormatter getInstance() {
    if(formatter == null) {
      formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    }
    return formatter;
  }
}
