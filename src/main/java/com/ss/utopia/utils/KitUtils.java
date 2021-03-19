package com.ss.utopia.utils;

/**
 * Adds a quality of life library for commonly used static methods
 *
 * @author Joshua Tyler
 */
final class KitUtils {

  public static final String ANSI_RESET = "\033[0m";
  public static final String ANSI_TEST_NAME = "\033[33;4m";
  public static final String ANSI_TEST_PASS = "\033[32;1m";
  public static final String ANSI_TEST_FAIL = "\033[31;1m";

  public static final String ANSI_HIGH_INTENSITY = "\033[1m";
  public static final String ANSI_LOW_INTENSITY = "\033[2m";

  public static final String ANSI_ITALIC = "\033[3m";
  public static final String ANSI_UNDERLINE = "\033[4m";
  public static final String ANSI_BLINK = "\033[5m";
  public static final String ANSI_RAPID_BLINK = "\033[6m";

  public static final String ANSI_BLACK = "\033[30m";
  public static final String ANSI_RED = "\033[31m";
  public static final String ANSI_GREEN = "\033[32m";
  public static final String ANSI_YELLOW = "\033[33m";
  public static final String ANSI_BLUE = "\033[34m";
  public static final String ANSI_PURPLE = "\033[35m";
  public static final String ANSI_CYAN = "\033[36m";
  public static final String ANSI_WHITE = "\033[37m";

  private KitUtils() {
    throw new IllegalStateException("Utilility class 'KitUtils' is static and should not be instantiated.");
  }

  /**
   * A thread which sleeps for the specific time (ms), and then performs the
   * passed Runnable
   *
   * @param runnable - runnable to be executed after delay
   * @param delay    - int time in milliseconds to delay the runnable
   */
  public static void setTimeout(Runnable runnable, int delay) {
    new Thread(
      () -> {
        try {
          Thread.sleep(delay);
          runnable.run();
        } catch (Exception e) {
          System.err.println(e);
        }
      }
    )
    .start();
  }
}
