package github.nisrulz.zentone;

import android.os.Handler;

/**
 * The type Zen tone.
 */
public class ZenTone {
  private PlayToneThread playToneThread;
  private boolean isThreadRunning = false;
  private final Handler stopThread;

  private static final ZenTone INSTANCE = new ZenTone();

  private ZenTone() {
    stopThread = new Handler();
  }
  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static ZenTone getInstance() {
    return INSTANCE;
  }



  /**
   * Generate pure tone
   *
   * @param freq the freq
   * @param duration the duration
   * @param volumne the volumne
   * @param toneStoppedListener the tone stopped listener
   */
  public void generate(int freq, int duration, float volumne,
      ToneStoppedListener toneStoppedListener) {
    if (!isThreadRunning) {
      stop();
      playToneThread = new PlayToneThread(freq, duration, volumne, toneStoppedListener);
      playToneThread.start();
      isThreadRunning = true;
      stopThread.postDelayed(new Runnable() {
        @Override public void run() {
          stop();
        }
      }, duration * 1000);
    }
  }

  /**
   * Stop.
   */
  public void stop() {
    if (playToneThread != null) {
      playToneThread.stopTone();
      playToneThread.interrupt();
      playToneThread = null;
      isThreadRunning = false;
    }
  }
}
