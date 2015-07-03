package in.excogitation.zentone.library;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * @author Nishant Srivastava
 * @project Library-Zentone
 * @package in.excogitation.zentone.library
 * @date 19/04/15
 */
public class ZenTone {
	boolean isPlaying = false;

	public void play(double freqOfTone, double duration) {
		if (!isPlaying) {
			isPlaying = true;

			int sampleRate = 8000;              // a number

			double dnumSamples = duration * sampleRate;
			dnumSamples = Math.ceil(dnumSamples);
			int numSamples = (int) dnumSamples;
			double sample[] = new double[numSamples];
			byte generatedSnd[] = new byte[2 * numSamples];


			for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
				sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
			}

			// convert to 16 bit pcm sound array
			// assumes the sample buffer is normalized.
			// convert to 16 bit pcm sound array
			// assumes the sample buffer is normalised.
			int idx = 0;
			int i = 0;

			int ramp = numSamples / 20;                                    // Amplitude ramp as a percent of sample count


			for (i = 0; i < ramp; ++i) {                                     // Ramp amplitude up (to avoid clicks)
				// Ramp up to maximum
				final short val = (short) ((sample[i] * 32767 * i / ramp));
				// in 16 bit wav PCM, first byte is the low order byte
				generatedSnd[idx++] = (byte) (val & 0x00ff);
				generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
			}


			for (i = ramp; i < numSamples - ramp; ++i) {                        // Max amplitude for most of the samples
				// scale to maximum amplitude
				final short val = (short) ((sample[i] * 32767));
				// in 16 bit wav PCM, first byte is the low order byte
				generatedSnd[idx++] = (byte) (val & 0x00ff);
				generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
			}

			for (i =(numSamples-ramp); i < numSamples; ++i) {                               // Ramp amplitude down
				// Ramp down to zero
				final short val = (short) ((sample[i] * 32767 * (numSamples - i) / ramp));
				// in 16 bit wav PCM, first byte is the low order byte
				generatedSnd[idx++] = (byte) (val & 0x00ff);
				generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
			}

			AudioTrack audioTrack = null;                                   // Get audio track
			try {
				int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
						sampleRate, AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize,
						AudioTrack.MODE_STREAM);
				audioTrack.play();                                          // Play the track
				audioTrack.write(generatedSnd, 0, generatedSnd.length);     // Load the track
			} catch (Exception e) {
			}
			if (audioTrack != null) {
				audioTrack.release();
				isPlaying = false;
			}
		}
	}

	public void stop() {

		// Stop playing tone

	}

}
