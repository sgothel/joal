package com.jogamp.openal;

public class UnsupportedAudioFileException extends Exception {

  public UnsupportedAudioFileException (final String message) {
    super(message);
  }

  public UnsupportedAudioFileException () {
    super();
  }

  public UnsupportedAudioFileException (final String message, final Throwable cause) {
    super(message, cause);
  }

  public UnsupportedAudioFileException (final Throwable cause) {
    super(cause);
  }

}
