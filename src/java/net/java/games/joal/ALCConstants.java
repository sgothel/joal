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
*
* Created on Jun 6, 2003
*/

package net.java.games.joal;

/**
 * @author Athomas Goldberg
 *
 */
public interface ALCConstants {
    /* Boolean False. */
    public final static int  ALC_FALSE =                            0;

    /* Boolean True. */
    public final static int  ALC_TRUE =                             1;

    /** Errors: No Error. */
    public final static int  ALC_NO_ERROR =                         ALC_FALSE;

    public final static int  ALC_MAJOR_VERSION  =                   0x1000;
    public final static int  ALC_MINOR_VERSION  =                   0x1001;
    public final static int  ALC_ATTRIBUTES_SIZE =                  0x1002;
    public final static int  ALC_ALL_ATTRIBUTES =                   0x1003;

    public final static int  ALC_DEFAULT_DEVICE_SPECIFIER =         0x1004;
    public final static int  ALC_DEVICE_SPECIFIER =                 0x1005;
    public final static int  ALC_EXTENSIONS =                       0x1006;

    public final static int  ALC_FREQUENCY =                        0x1007;
    public final static int  ALC_REFRESH =                          0x1008;
    public final static int  ALC_SYNC =                             0x1009;

    /** 
     * The device argument does not name a valid dvice.
     */
    public final static int ALC_INVALID_DEVICE =                    0xA001;

    /** 
     * The context argument does not name a valid context.
     */
    public final static int ALC_INVALID_CONTEXT =                   0xA002;  

    /**
     * A function was called at inappropriate time,
     *  or in an inappropriate way, causing an illegal state.
     * This can be an incompatible ALenum, object ID,
     *  and/or function.
     */
    public final static int ALC_INVALID_ENUM =                      0xA003;

    /** 
     * Illegal value passed as an argument to an AL call.
     * Applies to parameter values, but not to enumerations.
     */
    public final static int ALC_INVALID_VALUE =                     0xA004;

    /**
     * A function could not be completed,
     * because there is not enough memory available.
     */
    public final static int ALC_OUT_OF_MEMORY =                     0xA005;

}
