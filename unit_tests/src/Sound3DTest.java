/*
 * Created on May 1, 2003
 *
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import net.java.games.sound3d.AudioSystem3D;
import net.java.games.sound3d.Context;
import net.java.games.sound3d.Device;
import net.java.games.sound3d.Listener;
import net.java.games.sound3d.Source;

/**
 * @author Athomas Goldberg
 *
 */
public class Sound3DTest {

	public static float lerp(float v1, float v2, float t) {
		float result = 0;
		result = v1 + ((v2-v1) * t);
		return result;
	}

	public static void main(String[] args) {
		AudioSystem3D.init();
		
		// create the initial context - this can be collapsed into the init.
		Device device = AudioSystem3D.openDevice("DirectSound3D");
		Context context = AudioSystem3D.createContext(device);
		AudioSystem3D.makeContextCurrent(context);
		
		// get the listener object
		Listener listener = AudioSystem3D.getListener();
		listener.setPosition(0,0,0);
		
		// load a source and play it
        try {
            Source source1 = AudioSystem3D.loadSource("lewiscarroll.wav");
            source1.setPosition(0,0,0);
            source1.setLooping(true);
            source1.play();
		
    		try {
    			Thread.sleep(10000);
    		} catch (InterruptedException e) {
    		
    		}
    		
    		// move the source
    		source1.setPosition(1,1,1);
    		
    		// move the listener
    		for(int i = 0; i < 1000; i++) {
    			float t = ((float)i)/1000f;
    			float lp = lerp(0f,2f,t);
    			listener.setPosition(lp,lp,lp);
    			try {
    				Thread.sleep(10);
    			} catch (InterruptedException e) {
    
    			}
    		}
    		
    		// fade listener out.
    		for(int i = 0; i < 1000; i++) {
    			float t = ((float)i)/1000f;
    			float lp = lerp(1f,0f,t);
    			listener.setGain(lp);
    			try {
    				Thread.sleep(10);
    			} catch (InterruptedException e) {
    			}
    		}
    				
    		source1.stop();
    		source1.delete();
    		context.destroy();
    		device.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
	}
}
