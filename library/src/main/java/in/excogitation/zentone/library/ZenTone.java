package in.excogitation.zentone.library;


import android.os.Handler;

/**
 * @author Nishant Srivastava
 * @project Zentone
 */
public class ZenTone {
    private static PlayToneThread playToneThread;
    private static boolean isThreadRunning = false;
    private static Handler stopThread;

    private static ZenTone INSTANCE = new ZenTone();

    public static ZenTone getInstance() {
        return INSTANCE;
    }

    private ZenTone() {
        stopThread = new Handler();
    }

    public void generate(int freq, int duration, ToneStoppedListener toneStoppedListener) {
        if (!isThreadRunning) {
            stop();
            playToneThread = new PlayToneThread(freq, duration, toneStoppedListener);
            playToneThread.start();
            isThreadRunning = true;
            stopThread.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, duration * 1000);
        }
    }

    public void stop() {
        if (playToneThread != null) {
            playToneThread.stopTone();
            playToneThread.interrupt();
            playToneThread = null;
            isThreadRunning = false;
        }
    }
}
