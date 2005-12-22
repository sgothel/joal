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
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MICROSYSTEMS, INC. ("SUN") AND ITS
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

package net.java.games.sound3d;

import net.java.games.joal.AL;


/**
 * This class represents the human listener in the Sound3D environment. It 
 * provides methods for controlling the position, orientation as well as other
 * properties associated with the listener.
 *
 * @author Athomas Goldberg
 */
public class Listener {
    private final AL al;

    Listener(AL al) {
        this.al = al;
    }

    /**
     * Sets the Gain, or volume of the audio in the environment relative to the
     * listener
     *
     * @param gain the gain, or volume
     */
    public void setGain(float gain) {
        al.alListenerf(AL.AL_GAIN, gain);
    }

    /**
     * Gets the value of the gain, or volume of the audio in the environment
     * relative to the listener.
     *
     * @return the gain value.
     */
    public float getGain() {
        float[] f = new float[1];
        al.alGetListenerf(AL.AL_GAIN, f, 0);

        return f[0];
    }

    /**
     * Sets the position in (x-y-z coordinates) of the Listener in the Sound3D
     * environment.
     *
     * @param x The position of the listener along the X-axis in the Sound3D
     * environment
     * @param y The position of the listener along the Y-axis in the Sound3D
     * environment
     * @param z The position of the listener along the Z-axis in the Sound3D
     * environment
     */
    public void setPosition(float x, float y, float z) {
        al.alListener3f(AL.AL_POSITION, x, y, z);
    }

    /**
     * Sets the position in (x-y-z coordinates) of the Listener in the Sound3D
     * environment.
     *
     * @param position a Vec3f object containing the x,y and z coordinates of 
     * Listener.
     */
    public void setPosition(Vec3f position) {
        al.alListener3f(AL.AL_POSITION, position.v1, position.v2, position.v3);
    }

    /**
     * Gets the position in (x-y-z coordinates) of the Listener in the Sound3D
     * environment.
     *
     * @return a Vec3f object containing the x,y and z coordinates of 
     * Listener.
     */
    public Vec3f getPosition() {
        Vec3f result = null;
        float[] tmp = new float[3];
        al.alGetListenerfv(AL.AL_POSITION, tmp, 0);
        result = new Vec3f(tmp[0], tmp[1], tmp[2]);

        return result;
    }

    /**
     * Sets the velocity in (x-y-z coordinates) of the Listener in the Sound3D
     * environment. Used in determining doppler shift.
     *
     * @param velocity a Vec3f object containing the velicity in
     * x,y and z coordinates of Listener.
     */
    public void setVelocity(Vec3f velocity) {
        al.alListener3f(AL.AL_VELOCITY, velocity.v1, velocity.v2, velocity.v3);
    }

    /**
     * Gets the velocity in (x-y-z coordinates) of the Listener in the Sound3D
     * environment. Used in determining doppler shift.
     *
     * @return a Vec3f object containing the velicity in
     * x,y and z coordinates of Listener.
     */
    public Vec3f getVelocity() {
        Vec3f result = null;
        float[] tmp = new float[3];
        al.alGetListenerfv(AL.AL_VELOCITY, tmp, 0);
        result = new Vec3f(tmp[0], tmp[1], tmp[2]);

        return result;
    }

    /**
     * Sets the orientation of the Listener in the Sound3D environment. 
     * Orientation is expressed as "up" and "at" vectors. 
     *
     * @param orientation The first 3 elements of the array should contain
     * the x,y,z up-vector, the second 3 elements should contain the x,z,z
     * look-at vector.
     */
    public void setOrientation(float[] orientation) {
        al.alListenerfv(AL.AL_ORIENTATION, orientation, 0);
    }

    /**
     * Gets the orientation of the Listener in the Sound3D environment. 
     * Orientation is expressed as "up" and "at" vectors. 
     *
     * @return an array containing the orientation of the listener.
     * The first 3 elements of the array contain
     * the x,y,z up-vector, the second 3 elements contain the x,z,z
     * look-at vector.
     */
    public float[] getOrientation() {
        float[] tmp = new float[6];
        al.alGetListenerfv(AL.AL_ORIENTATION, tmp, 0);
        return tmp;
    }
}
