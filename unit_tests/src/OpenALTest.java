import java.nio.IntBuffer;

import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALFactory;
import net.java.games.joal.eax.EAX;
import net.java.games.joal.eax.EAXFactory;
import net.java.games.joal.util.BufferUtils;
import net.java.games.joal.util.WAVData;
import net.java.games.joal.util.WAVLoader;

/**
 * @author Athomas Goldberg
 *
 */
public class OpenALTest {
    public static void main(String[] args) {
        ALFactory.initialize();

        ALC alc = ALFactory.getALC();
        ALC.Device device = alc.alcOpenDevice("DirectSound3D");
        ALC.Context context = alc.alcCreateContext(device, null);
        alc.alcMakeContextCurrent(context);
        AL al = ALFactory.getAL();
        boolean eaxPresent = al.alIsExtensionPresent("EAX2.0");
        System.out.println("EAX present:" + eaxPresent);
        EAX eax = EAXFactory.getEAX();

        try {
            int[] buffers = new int[1];
            al.alGenBuffers(1, buffers);

            WAVData wd = WAVLoader.loadFromFile("lewiscarroll.wav");
            al.alBufferData(buffers[0], wd.format, wd.data, wd.size, wd.freq);

            int[] sources = new int[1];
            al.alGenSources(1, sources);
            al.alSourcei(sources[0], AL.AL_BUFFER, buffers[0]);
            System.out.println(
                "Looping 1: "
                    + (al.alGetSourcei(sources[0], AL.AL_LOOPING) == AL.AL_TRUE));
            int[] loopArray = new int[1];
            al.alGetSourcei(sources[0], AL.AL_LOOPING, loopArray);
            System.out.println("Looping 2: " + (loopArray[0] == AL.AL_TRUE));
            int[] loopBuffer = new int[1];
            al.alGetSourcei(sources[0], AL.AL_LOOPING, loopBuffer);
            System.out.println("Looping 3: " + (loopBuffer[0] == AL.AL_TRUE));

            if (eaxPresent) {
                IntBuffer env = BufferUtils.newIntBuffer(1);
                env.put(EAX.EAX_ENVIRONMENT_BATHROOM);
                eax.setListenerProperty(
                    EAX.DSPROPERTY_EAXLISTENER_ENVIRONMENT,
                    env);
            }

            al.alSourcePlay(sources[0]);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            al.alSource3f(sources[0], AL.AL_POSITION, 2f, 2f, 2f);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            al.alListener3f(AL.AL_POSITION, 3f, 3f, 3f);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            al.alSource3f(sources[0], AL.AL_POSITION, 0, 0, 0);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }

            al.alSourceStop(sources[0]);
            al.alDeleteSources(1, sources);
            alc.alcDestroyContext(context);
            alc.alcCloseDevice(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
