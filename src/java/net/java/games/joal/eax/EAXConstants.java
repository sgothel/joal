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

package net.java.games.joal.eax;
/**
 *
 * This class implements the basic EAX extension constants.
 *
 * @author Athomas Goldberg
 */
public interface EAXConstants {

    public final static int DSPROPERTY_EAXLISTENER_NONE                 = 0;
    public final static int DSPROPERTY_EAXLISTENER_ALLPARAMETERS        = 1;
    public final static int DSPROPERTY_EAXLISTENER_ROOM                 = 2;
    public final static int DSPROPERTY_EAXLISTENER_ROOMHF               = 3;
    public final static int DSPROPERTY_EAXLISTENER_ROOMROLLOFFFACTOR    = 4;
    public final static int DSPROPERTY_EAXLISTENER_DECAYTIME            = 5;
    public final static int DSPROPERTY_EAXLISTENER_DECAYHFRATIO         = 6;
    public final static int DSPROPERTY_EAXLISTENER_REFLECTIONS          = 7;
    public final static int DSPROPERTY_EAXLISTENER_REFLECTIONSDELAY     = 8;
    public final static int DSPROPERTY_EAXLISTENER_REVERB               = 9;
    public final static int DSPROPERTY_EAXLISTENER_REVERBDELAY          = 10;
    public final static int DSPROPERTY_EAXLISTENER_ENVIRONMENT          = 11;
    public final static int DSPROPERTY_EAXLISTENER_ENVIRONMENTSIZE      = 12;
    public final static int DSPROPERTY_EAXLISTENER_ENVIRONMENTDIFFUSION = 13;
    public final static int DSPROPERTY_EAXLISTENER_AIRABSORPTIONHF      = 14;
    public final static int DSPROPERTY_EAXLISTENER_FLAGS                = 15;
    
//     OR these flags with property id // 
    /** changes take effect immediately */
    public static final int DSPROPERTY_EAXLISTENER_IMMEDIATE  = 0x00000000;

    /** changes take effect later */
    public static final int DSPROPERTY_EAXLISTENER_DEFERRED   = 0x80000000;

    public static final int DSPROPERTY_EAXLISTENER_COMMITDEFERREDSETTINGS = 
            DSPROPERTY_EAXLISTENER_NONE |
            DSPROPERTY_EAXLISTENER_IMMEDIATE;

    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_GENERIC             = 0;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_PADDEDCELL          = 1;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_ROOM                = 2;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_BATHROOM            = 3;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_LIVINGROOM          = 4;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_STONEROOM           = 5;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_AUDITORIUM          = 6;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_CONCERTHALL         = 7;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_CAVE                = 8;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_ARENA               = 9;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_HANGAR              = 10;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_CARPETEDHALLWAY     = 11;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_HALLWAY             = 12;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_STONECORRIDOR       = 13;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_ALLEY               = 14;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_FOREST              = 15;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_CITY                = 16;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_MOUNTAINS           = 17;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_QUARRY              = 18;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_PLAIN               = 19;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_PARKINGLOT          = 20;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_SEWERPIPE           = 21;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_UNDERWATER          = 22;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_DRUGGED             = 23;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_DIZZY               = 24;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_PSYCHOTIC           = 25;
    /** used by DSPROPERTY_EAXLISTENER_ENVIRONMENT */
    public final static int EAX_ENVIRONMENT_COUNT               = 26;

//     These flags determine what properties are affected by environment size.
    /**  reverberation decay time */
    public final static int EAXLISTENERFLAGS_DECAYTIMESCALE        = 0x00000001; 
    /**  reflection level */
    public final static int EAXLISTENERFLAGS_REFLECTIONSSCALE      = 0x00000002;
    /**  initial reflection delay time */
    public final static int EAXLISTENERFLAGS_REFLECTIONSDELAYSCALE = 0x00000004; 
    /**  reflections level */
    public final static int EAXLISTENERFLAGS_REVERBSCALE           = 0x00000008; 
    /**  late reverberation delay time */
    public final static int EAXLISTENERFLAGS_REVERBDELAYSCALE      = 0x00000010; 

    /** This flag limits high-frequency decay time according to air absorption.*/
    public final static int EAXLISTENERFLAGS_DECAYHFLIMIT          = 0x00000020;
    /** reserved future use */
    public final static int EAXLISTENERFLAGS_RESERVED              = 0xFFFFFFC0;

//     property ranges and defaults:

    public final static int EAXLISTENER_MINROOM       = -10000;
    public final static int EAXLISTENER_MAXROOM       = 0;
    public final static int EAXLISTENER_DEFAULTROOM   = -1000;

    public final static int EAXLISTENER_MINROOMHF      = -10000;
    public final static int EAXLISTENER_MAXROOMHF      = 0;
    public final static int EAXLISTENER_DEFAULTROOMHF  = -100;

    public final static float EAXLISTENER_MINROOMROLLOFFFACTOR          = 0.0f;
    public final static float EAXLISTENER_MAXROOMROLLOFFFACTOR          = 10.0f;
    public final static float EAXLISTENER_DEFAULTROOMROLLOFFFACTOR      = 0.0f;

    public final static float EAXLISTENER_MINDECAYTIME                  = 0.1f;
    public final static float EAXLISTENER_MAXDECAYTIME                  = 20.0f;
    public final static float EAXLISTENER_DEFAULTDECAYTIME              = 1.49f;

    public final static float EAXLISTENER_MINDECAYHFRATIO               = 0.1f;
    public final static float EAXLISTENER_MAXDECAYHFRATIO               = 2.0f;
    public final static float EAXLISTENER_DEFAULTDECAYHFRATIO           = 0.83f;

    public final static int EAXLISTENER_MINREFLECTIONS                = -10000;
    public final static int EAXLISTENER_MAXREFLECTIONS                = 1000;
    public final static int EAXLISTENER_DEFAULTREFLECTIONS            = -2602;

    public final static float EAXLISTENER_MINREFLECTIONSDELAY          = 0.0f;
    public final static float EAXLISTENER_MAXREFLECTIONSDELAY          = 0.3f;
    public final static float EAXLISTENER_DEFAULTREFLECTIONSDELAY      = 0.007f;

    public final static int EAXLISTENER_MINREVERB                     = -10000;
    public final static int EAXLISTENER_MAXREVERB                     = 2000;
    public final static int EAXLISTENER_DEFAULTREVERB                 = 200;

    public final static float EAXLISTENER_MINREVERBDELAY               = 0.0f;
    public final static float EAXLISTENER_MAXREVERBDELAY               = 0.1f;
    public final static float EAXLISTENER_DEFAULTREVERBDELAY           = 0.011f;

    public final static int EAXLISTENER_MINENVIRONMENT                = 0;
    public final static int EAXLISTENER_MAXENVIRONMENT = EAX_ENVIRONMENT_COUNT-1;
    public final static int EAXLISTENER_DEFAULTENVIRONMENT = EAX_ENVIRONMENT_GENERIC;

    public final static float EAXLISTENER_MINENVIRONMENTSIZE            = 1.0f;
    public final static float EAXLISTENER_MAXENVIRONMENTSIZE            = 100.0f;
    public final static float EAXLISTENER_DEFAULTENVIRONMENTSIZE        = 7.5f;

    public final static float EAXLISTENER_MINENVIRONMENTDIFFUSION       = 0.0f;
    public final static float EAXLISTENER_MAXENVIRONMENTDIFFUSION       = 1.0f;
    public final static float EAXLISTENER_DEFAULTENVIRONMENTDIFFUSION   = 1.0f;

    public final static float EAXLISTENER_MINAIRABSORPTIONHF          = -100.0f;
    public final static float EAXLISTENER_MAXAIRABSORPTIONHF          = 0.0f;
    public final static float EAXLISTENER_DEFAULTAIRABSORPTIONHF      = -5.0f;

    public final static int EAXLISTENER_DEFAULTFLAGS = 
                                       EAXLISTENERFLAGS_DECAYTIMESCALE |      
                                       EAXLISTENERFLAGS_REFLECTIONSSCALE |    
                                       EAXLISTENERFLAGS_REFLECTIONSDELAYSCALE |
                                       EAXLISTENERFLAGS_REVERBSCALE |
                                       EAXLISTENERFLAGS_REVERBDELAYSCALE |
                                       EAXLISTENERFLAGS_DECAYHFLIMIT;

    public final static int DSPROPERTY_EAXBUFFER_NONE                   = 0;
    public final static int DSPROPERTY_EAXBUFFER_ALLPARAMETERS          = 1;
    public final static int DSPROPERTY_EAXBUFFER_DIRECT                 = 2;
    public final static int DSPROPERTY_EAXBUFFER_DIRECTHF               = 3;
    public final static int DSPROPERTY_EAXBUFFER_ROOM                   = 4;
    public final static int DSPROPERTY_EAXBUFFER_ROOMHF                 = 5;
    public final static int DSPROPERTY_EAXBUFFER_ROOMROLLOFFFACTOR      = 6;
    public final static int DSPROPERTY_EAXBUFFER_OBSTRUCTION            = 7;
    public final static int DSPROPERTY_EAXBUFFER_OBSTRUCTIONLFRATIO     = 8;
    public final static int DSPROPERTY_EAXBUFFER_OCCLUSION              = 9;
    public final static int DSPROPERTY_EAXBUFFER_OCCLUSIONLFRATIO       = 10;
    public final static int DSPROPERTY_EAXBUFFER_OCCLUSIONROOMRATIO     = 11;
    public final static int DSPROPERTY_EAXBUFFER_OUTSIDEVOLUMEHF        = 13;
    public final static int DSPROPERTY_EAXBUFFER_AIRABSORPTIONFACTOR    = 14;
    public final static int DSPROPERTY_EAXBUFFER_FLAGS                  = 15;

//     OR these flags with property id
    /**  changes take effect immediately */
    public final static int DSPROPERTY_EAXBUFFER_IMMEDIATE = 0x00000000;
    /** changes take effect later */ 
    public final static int DSPROPERTY_EAXBUFFER_DEFERRED  = 0x80000000;
    public final static int DSPROPERTY_EAXBUFFER_COMMITDEFERREDSETTINGS = 
            DSPROPERTY_EAXBUFFER_NONE |
            DSPROPERTY_EAXBUFFER_IMMEDIATE;


//     Used by DSPROPERTY_EAXBUFFER_FLAGS
//        TRUE:    value is computed automatically - property is an offset
//        FALSE:   value is used directly
//
//     Note: The number and order of flags may change in future EAX versions.
//           To insure future compatibility, use flag defines as follows:
//                  myFlags = EAXBUFFERFLAGS_DIRECTHFAUTO | EAXBUFFERFLAGS_ROOMAUTO;
//           instead of:
//                  myFlags = 0x00000003;
//
    /** affects DSPROPERTY_EAXBUFFER_DIRECTHF */
    public final static int EAXBUFFERFLAGS_DIRECTHFAUTO = 0x00000001; 
    /**  affects DSPROPERTY_EAXBUFFER_ROOM */
    public final static int EAXBUFFERFLAGS_ROOMAUTO     = 0x00000002;
    /** affects DSPROPERTY_EAXBUFFER_ROOMHF */
    public final static int EAXBUFFERFLAGS_ROOMHFAUTO   = 0x00000004;
    /** reserved future use */
    public final static int EAXBUFFERFLAGS_RESERVED     = 0xFFFFFFF8;

//     property ranges and defaults:

    public final static int EAXBUFFER_MINDIRECT                  = (-10000);
    public final static int EAXBUFFER_MAXDIRECT                  = 1000;
    public final static int EAXBUFFER_DEFAULTDIRECT              = 0;

    public final static int EAXBUFFER_MINDIRECTHF                = (-10000);
    public final static int EAXBUFFER_MAXDIRECTHF                = 0;
    public final static int EAXBUFFER_DEFAULTDIRECTHF            = 0;

    public final static int EAXBUFFER_MINROOM                    = (-10000);
    public final static int EAXBUFFER_MAXROOM                    = 1000;
    public final static int EAXBUFFER_DEFAULTROOM                = 0;

    public final static int EAXBUFFER_MINROOMHF                  = (-10000);
    public final static int EAXBUFFER_MAXROOMHF                  = 0;
    public final static int EAXBUFFER_DEFAULTROOMHF              = 0;

    public final static float EAXBUFFER_MINROOMROLLOFFFACTOR       = 0.0f;
    public final static float EAXBUFFER_MAXROOMROLLOFFFACTOR       = 10.f;
    public final static float EAXBUFFER_DEFAULTROOMROLLOFFFACTOR   = 0.0f;

    public final static int EAXBUFFER_MINOBSTRUCTION             = (-10000);
    public final static int EAXBUFFER_MAXOBSTRUCTION             = 0;
    public final static int EAXBUFFER_DEFAULTOBSTRUCTION         = 0;

    public final static float EAXBUFFER_MINOBSTRUCTIONLFRATIO      = 0.0f;
    public final static float EAXBUFFER_MAXOBSTRUCTIONLFRATIO      = 1.0f;
    public final static float EAXBUFFER_DEFAULTOBSTRUCTIONLFRATIO  = 0.0f;

    public final static int EAXBUFFER_MINOCCLUSION               = (-10000);
    public final static int EAXBUFFER_MAXOCCLUSION               = 0;
    public final static int EAXBUFFER_DEFAULTOCCLUSION           = 0;

    public final static float EAXBUFFER_MINOCCLUSIONLFRATIO        = 0.0f;
    public final static float EAXBUFFER_MAXOCCLUSIONLFRATIO        = 1.0f;
    public final static float EAXBUFFER_DEFAULTOCCLUSIONLFRATIO    = 0.25f;

    public final static float EAXBUFFER_MINOCCLUSIONROOMRATIO      = 0.0f;
    public final static float EAXBUFFER_MAXOCCLUSIONROOMRATIO      = 10.0f;
    public final static float EAXBUFFER_DEFAULTOCCLUSIONROOMRATIO  = 0.5f;

    public final static int EAXBUFFER_MINOUTSIDEVOLUMEHF         = (-10000);
    public final static int EAXBUFFER_MAXOUTSIDEVOLUMEHF         = 0;
    public final static int EAXBUFFER_DEFAULTOUTSIDEVOLUMEHF     = 0;

    public final static float EAXBUFFER_MINAIRABSORPTIONFACTOR     = 0.0f;
    public final static float EAXBUFFER_MAXAIRABSORPTIONFACTOR     = 10.0f;
    public final static float EAXBUFFER_DEFAULTAIRABSORPTIONFACTOR = 1.0f;

    public final static int EAXBUFFER_DEFAULTFLAGS =  
                                                EAXBUFFERFLAGS_DIRECTHFAUTO |
                                                EAXBUFFERFLAGS_ROOMAUTO |
                                                EAXBUFFERFLAGS_ROOMHFAUTO;

//     Material transmission presets
//     3 values in this order:
//         1: occlusion (or obstruction)
//         2: occlusion LF Ratio (or obstruction LF Ratio)
//         3: occlusion Room Ratio

//     Single window material preset
    public final static int EAX_MATERIAL_SINGLEWINDOW          = (-2800);
    public final static float EAX_MATERIAL_SINGLEWINDOWLF        = 0.71f;
    public final static float EAX_MATERIAL_SINGLEWINDOWROOMRATIO = 0.43f;

//     Double window material preset
    public final static int EAX_MATERIAL_DOUBLEWINDOW          = (-5000);
    public final static float EAX_MATERIAL_DOUBLEWINDOWHF        = 0.40f;
    public final static float EAX_MATERIAL_DOUBLEWINDOWROOMRATIO = 0.24f;

//     Thin door material preset
    public final static int EAX_MATERIAL_THINDOOR              = (-1800);
    public final static float EAX_MATERIAL_THINDOORLF            = 0.66f;
    public final static float EAX_MATERIAL_THINDOORROOMRATIO     = 0.66f;

//     Thick door material preset
    public final static int EAX_MATERIAL_THICKDOOR             = (-4400);
    public final static float EAX_MATERIAL_THICKDOORLF           = 0.64f;
    public final static float EAX_MATERIAL_THICKDOORROOMRTATION  = 0.27f;

//     Wood wall material preset
    public final static int EAX_MATERIAL_WOODWALL              = (-4000);
    public final static float EAX_MATERIAL_WOODWALLLF            = 0.50f;
    public final static float EAX_MATERIAL_WOODWALLROOMRATIO     = 0.30f;

//     Brick wall material preset
    public final static int EAX_MATERIAL_BRICKWALL             = (-5000);
    public final static float EAX_MATERIAL_BRICKWALLLF           = 0.60f;
    public final static float EAX_MATERIAL_BRICKWALLROOMRATIO    = 0.24f;

//     Stone wall material preset
    public final static int EAX_MATERIAL_STONEWALL             = (-6000);
    public final static float EAX_MATERIAL_STONEWALLLF           = 0.68f;
    public final static float EAX_MATERIAL_STONEWALLROOMRATIO    = 0.20f;

//     Curtain material preset
    public final static int EAX_MATERIAL_CURTAIN               = (-1200);
    public final static float EAX_MATERIAL_CURTAINLF             = 0.15f;
    public final static float EAX_MATERIAL_CURTAINROOMRATIO      = 1.00f;
}