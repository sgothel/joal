/**
* Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* -Redistribution of source code must retain the above copyright notice, 
* this list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduce the above copyright notice, 
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* Neither the name of Sun Microsystems, Inc. or the names of contributors may 
* be used to endorse or promote products derived from this software without 
* specific prior written permission.
* 
* This software is provided "AS IS," without a warranty of any kind.
* ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
* ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
* LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
* RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
* IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
* OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
* PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
* ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
* BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that this software is not designed or intended for use in the
* design, construction, operation or maintenance of any nuclear facility.
*/
import java.nio.IntBuffer;

import net.java.games.joal.AL;
import net.java.games.joal.ALC;
import net.java.games.joal.ALFactory;
import net.java.games.joal.OpenALException;
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
    	try {
			ALFactory.initialize();

			ALC alc = ALFactory.getALC();
			ALC.Device device = alc.alcOpenDevice(null);
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
		} catch (OpenALException e) {
		    e.printStackTrace();
		}
    }
}
