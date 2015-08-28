package in.excogitation.zentone.library;

/**
 * @author Nishant Srivastava
 * @project Library-Zentone
 * @package in.excogitation.zentone.library
 * @date 19/04/15
 */
public class ZenTone {
    static PlayToneThread playToneThread;
    static boolean isThreadRunning = false;

    private static ZenTone INSTANCE = new ZenTone();

    public static ZenTone getInstance() {
        return INSTANCE;
    }

    private ZenTone() {
    }

    public static void generate(int freq, int duration) {
        if (!isThreadRunning) {
            playToneThread = new PlayToneThread(freq, duration);
            playToneThread.start();
            isThreadRunning = true;
        }
    }

    public static void stop() {
        if (playToneThread != null) {
            playToneThread.stopTone();
            playToneThread.interrupt();
            playToneThread = null;
            isThreadRunning = false;
        }
    }
}
