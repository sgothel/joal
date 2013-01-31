package com.jogamp.openal;

public class UnsupportedAudioFileException extends Exception {

  public UnsupportedAudioFileException (String message) {
    super(message);
  }

  public UnsupportedAudioFileException () {
    super();
  }
  
  public UnsupportedAudioFileException (String message, Throwable cause) {
    super(message, cause);
  }
  
  public UnsupportedAudioFileException (Throwable cause) {
    super(cause);
  }
  
}
