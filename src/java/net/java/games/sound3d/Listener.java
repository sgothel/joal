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

package net.java.games.sound3d;

import net.java.games.joal.AL;


/**
 * DOCUMENT ME!
 *
 * @author Athomas Goldberg
 */
public class Listener {
    private final AL al;

    Listener(AL al) {
        this.al = al;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void setGain(float gain) {
        al.alListenerf(AL.AL_GAIN, gain);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGain() {
        float[] f = new float[1];
        al.alGetListenerf(AL.AL_GAIN, f);

        return f[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void setPosition(float x, float y, float z) {
        al.alListener3f(AL.AL_POSITION, x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param position DOCUMENT ME!
     */
    public void setPosition(Vec3f position) {
        al.alListener3f(AL.AL_POSITION, position.v1, position.v2, position.v3);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vec3f getPosition() {
        Vec3f result = null;
        float[] tmp = new float[3];
        al.alGetListenerfv(AL.AL_POSITION, tmp);
        result = new Vec3f(tmp[0], tmp[1], tmp[2]);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param velocity DOCUMENT ME!
     */
    public void setVelocity(Vec3f velocity) {
        al.alListener3f(AL.AL_VELOCITY, velocity.v1, velocity.v2, velocity.v3);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vec3f getVelocity() {
        Vec3f result = null;
        float[] tmp = new float[3];
        al.alGetListenerfv(AL.AL_VELOCITY, tmp);
        result = new Vec3f(tmp[0], tmp[1], tmp[2]);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orientation DOCUMENT ME!
     */
    public void setOrientation(float[] orientation) {
        al.alListenerfv(AL.AL_ORIENTATION, orientation);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float[] getOrientation() {
        float[] tmp = new float[6];
        al.alGetListenerfv(AL.AL_ORIENTATION, tmp);

        return tmp;
    }
}
