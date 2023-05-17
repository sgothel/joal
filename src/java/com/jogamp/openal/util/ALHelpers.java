/*
 * OpenAL Helpers
 *
 * Copyright (c) 2011 by Chris Robinson <chris.kcat@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/* This file contains routines to help with some menial OpenAL-related tasks,
 * such as opening a device and setting up a context, closing the device and
 * destroying its context, converting between frame counts and byte lengths,
 * finding an appropriate buffer format, and getting readable strings for
 * channel configs and sample types. */
package com.jogamp.openal.util;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALConstants;
import static com.jogamp.openal.ALConstants.*;
import com.jogamp.openal.ALExt;
// import com.jogamp.openal.ALExtConstants;
import static com.jogamp.openal.ALExtConstants.*;


/* This file contains routines to help with some menial OpenAL-related tasks,
 * such as converting between frame counts and byte lengths,
 * finding an appropriate buffer format, and getting readable strings for
 * channel configs and sample types. */
public class ALHelpers {

    /**
     * Returns a compatible AL buffer format given the AL channel layout and
     * AL sample type. If <code>hasSOFTBufferSamples</code> is true,
     * it will be called to find the closest-matching format from
     * <code>AL_SOFT_buffer_samples</code>.
     * <p>
     * Returns {@link ALConstants#AL_NONE} if no supported format can be found.
     * </p>
     *
     * @param alChannelLayout AL channel layout, see {@link #getDefaultALChannelLayout(int)}
     * @param alSampleType AL sample type, see {@link #getALSampleType(int, boolean, boolean)}.
     * @param hasSOFTBufferSamples true if having extension <code>AL_SOFT_buffer_samples</code>, otherwise false
     * @param hasEXTMcFormats true if having extension <code>AL_EXT_MCFORMATS</code>, otherwise false
     * @param hasEXTFloat32 true if having extension <code>AL_EXT_FLOAT32</code>, otherwise false
     * @param hasEXTDouble true if having extension <code>AL_EXT_DOUBLE</code>, otherwise false
     * @param al AL instance
     * @param alExt ALExt instance
     * @return AL buffer format
     */
    public static final int getALFormat(final int alChannelLayout, final int alSampleType,
                                        final boolean hasSOFTBufferSamples,
                                        final boolean hasEXTMcFormats,
                                        final boolean hasEXTFloat32,
                                        final boolean hasEXTDouble,
                                        final AL al, final ALExt alExt) {
        int format = AL_NONE;

        /* If using AL_SOFT_buffer_samples, try looking through its formats */
        if(hasSOFTBufferSamples)
        {
            /* AL_SOFT_buffer_samples is more lenient with matching formats. The
             * specified sample type does not need to match the returned format,
             * but it is nice to try to get something close. */
            if(alSampleType == AL_UNSIGNED_BYTE_SOFT || alSampleType == AL_BYTE_SOFT)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO8_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO8_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD8_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_8_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_8_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_8_SOFT;
            }
            else if(alSampleType == AL_UNSIGNED_SHORT_SOFT || alSampleType == AL_SHORT_SOFT)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO16_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO16_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD16_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_16_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_16_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_16_SOFT;
            }
            else if(alSampleType == AL_UNSIGNED_BYTE3_SOFT || alSampleType == AL_BYTE3_SOFT ||
                    alSampleType == AL_UNSIGNED_INT_SOFT || alSampleType == AL_INT_SOFT ||
                    alSampleType == AL_FLOAT_SOFT || alSampleType == AL_DOUBLE_SOFT)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO32F_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO32F_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD32F_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_32F_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_32F_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_32F_SOFT;
            }

            if(format != AL_NONE && !alExt.alIsBufferFormatSupportedSOFT(format))
                format = AL_NONE;

            /* A matching format was not found or supported. Try 32-bit float. */
            if(format == AL_NONE)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO32F_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO32F_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD32F_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_32F_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_32F_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_32F_SOFT;

                if(format != AL_NONE && !alExt.alIsBufferFormatSupportedSOFT(format))
                    format = AL_NONE;
            }
            /* 32-bit float not supported. Try 16-bit int. */
            if(format == AL_NONE)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO16_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO16_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD16_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_16_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_16_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_16_SOFT;

                if(format != AL_NONE && !alExt.alIsBufferFormatSupportedSOFT(format))
                    format = AL_NONE;
            }
            /* 16-bit int not supported. Try 8-bit int. */
            if(format == AL_NONE)
            {
                if(alChannelLayout == AL_MONO_SOFT) format = AL_MONO8_SOFT;
                else if(alChannelLayout == AL_STEREO_SOFT) format = AL_STEREO8_SOFT;
                else if(alChannelLayout == AL_QUAD_SOFT) format = AL_QUAD8_SOFT;
                else if(alChannelLayout == AL_5POINT1_SOFT) format = AL_5POINT1_8_SOFT;
                else if(alChannelLayout == AL_6POINT1_SOFT) format = AL_6POINT1_8_SOFT;
                else if(alChannelLayout == AL_7POINT1_SOFT) format = AL_7POINT1_8_SOFT;

                if(format != AL_NONE && !alExt.alIsBufferFormatSupportedSOFT(format))
                    format = AL_NONE;
            }

            return format;
        }

        /* We use the AL_EXT_MCFORMATS extension to provide output of Quad, 5.1,
         * and 7.1 channel configs, AL_EXT_FLOAT32 for 32-bit float samples, and
         * AL_EXT_DOUBLE for 64-bit float samples. */
        if(alSampleType == AL_UNSIGNED_BYTE_SOFT)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = AL_FORMAT_MONO8;
            else if(alChannelLayout == AL_STEREO_SOFT)
                format = AL_FORMAT_STEREO8;
            else if( hasEXTMcFormats )
            {
                if(alChannelLayout == AL_QUAD_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_QUAD8");
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_51CHN8");
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_61CHN8");
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_71CHN8");
            }
        }
        else if(alSampleType == AL_SHORT_SOFT)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = AL_FORMAT_MONO16;
            else if(alChannelLayout == AL_STEREO_SOFT)
                format = AL_FORMAT_STEREO16;
            else if( hasEXTMcFormats )
            {
                if(alChannelLayout == AL_QUAD_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_QUAD16");
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_51CHN16");
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_61CHN16");
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_71CHN16");
            }
        }
        else if(alSampleType == AL_FLOAT_SOFT && hasEXTFloat32)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = al.alGetEnumValue("AL_FORMAT_MONO_FLOAT32");
            else if(alChannelLayout == AL_STEREO_SOFT)
                format = al.alGetEnumValue("AL_FORMAT_STEREO_FLOAT32");
            else if( hasEXTMcFormats )
            {
                if(alChannelLayout == AL_QUAD_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_QUAD32");
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_51CHN32");
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_61CHN32");
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = al.alGetEnumValue("AL_FORMAT_71CHN32");
            }
        }
        else if(alSampleType == AL_DOUBLE_SOFT && hasEXTDouble)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = al.alGetEnumValue("AL_FORMAT_MONO_DOUBLE");
            else if(alChannelLayout == AL_STEREO_SOFT)
                format = al.alGetEnumValue("AL_FORMAT_STEREO_DOUBLE");
        }

        /* NOTE: It seems OSX returns -1 from alGetEnumValue for unknown enums, as
         * opposed to 0. Correct it. */
        if(format == -1) {
            format = AL_NONE;
        }

        return format;
    }

    /**
     * Returns the default AL channel layout matching the given channel count, or {@link ALConstants#AL_NONE}.
     * @param channelCount number of channels
     * @param signed true if signed number, false for unsigned
     * @param fixedP true for fixed point value, false for floating point value with a sampleSize of 32 (float) or 64 (double)
     */
    public static final int getDefaultALChannelLayout(final int channelCount) {
        switch(channelCount) {
            case 1: return AL_MONO_SOFT;
            case 2: return AL_STEREO_SOFT;
            // case 2: return AL_REAR_SOFT;
            case 4: return AL_QUAD_SOFT;
            case 6: return AL_5POINT1_SOFT;
            case 7: return AL_6POINT1_SOFT;
            case 8: return AL_7POINT1_SOFT;
        }
        return AL_NONE;
    }

    /**
     * Returns the readable name of the given AL channel layout
     */
    public static final String alChannelLayoutName(final int alChannelLayout) {
        switch(alChannelLayout) {
            case AL_MONO_SOFT: return "Mono";
            case AL_STEREO_SOFT: return "Stereo";
            case AL_REAR_SOFT: return "Rear";
            case AL_QUAD_SOFT: return "Quad";
            case AL_5POINT1_SOFT: return "5.1";
            case AL_6POINT1_SOFT: return "6.1";
            case AL_7POINT1_SOFT: return "7.1";
        }
        return "Unknown AL-Channel-Layout 0x"+Integer.toHexString(alChannelLayout);
    }

    /**
     * Returns the AL sample type matching the given audio type attributes, or {@link ALConstants#AL_NONE}.
     * @param sampleSize sample size in bits
     * @param signed true if signed number, false for unsigned
     * @param fixedP true for fixed point value, false for floating point value with a sampleSize of 32 (float) or 64 (double)
     */
    public static final int getALSampleType(final int sampleSize, final boolean signed, final boolean fixedP) {
        if( fixedP ) {
            if( signed ) {
                switch( sampleSize ) {
                    case  8: return AL_BYTE_SOFT;
                    case 16: return AL_SHORT_SOFT;
                    case 32: return AL_INT_SOFT;
                }
            } else {
                switch( sampleSize ) {
                    case  8: return AL_UNSIGNED_BYTE_SOFT;
                    case 16: return AL_UNSIGNED_SHORT_SOFT;
                    case 32: return AL_UNSIGNED_INT_SOFT;
                }
            }
        } else {
            if( signed ) {
                switch( sampleSize ) {
                    case 32: return AL_FLOAT_SOFT;
                    case 64: return AL_DOUBLE_SOFT;
                }
            }
        }
        return AL_NONE;
    }

    /**
     * Returns the readable name of the given AL sample type
     */
    public static final String alSampleTypeName(final int alSampleType) {
        switch(alSampleType) {
            case AL_BYTE_SOFT: return "s8";
            case AL_UNSIGNED_BYTE_SOFT: return "u8";
            case AL_SHORT_SOFT: return "s16";
            case AL_UNSIGNED_SHORT_SOFT: return "u16";
            case AL_INT_SOFT: return "s32";
            case AL_UNSIGNED_INT_SOFT: return "u32";
            case AL_FLOAT_SOFT: return "f32";
            case AL_DOUBLE_SOFT: return "f64";
        }
        return "Unknown AL-Type 0x"+Integer.toHexString(alSampleType);
    }

    /**
     * Returns the byte size of the given AL sample type
     * @throws IllegalArgumentException for unknown <code>alChannelLayout</code> or <code>alSampleType</code> values.
     */
    public static final int sizeOfALSampleType(final int alSampleType) throws IllegalArgumentException {
        switch(alSampleType) {
            case AL_BYTE_SOFT:
            case AL_UNSIGNED_BYTE_SOFT:
                return 1;
            case AL_SHORT_SOFT:
            case AL_UNSIGNED_SHORT_SOFT:
                return 2;
            case AL_INT_SOFT:
            case AL_UNSIGNED_INT_SOFT:
            case AL_FLOAT_SOFT:
                return 4;
            case AL_DOUBLE_SOFT:
                return 8;
            default:
                throw new IllegalArgumentException("Unknown al-type 0x"+Integer.toHexString(alSampleType));
        }
    }

    /**
     * @param sampleCount number of samples per channel
     * @param alChannelLayout AL channel layout
     * @param alSampleType AL sample type
     * @return bytes count required
     * @throws IllegalArgumentException for unknown <code>alChannelLayout</code> or <code>alSampleType</code> values.
     */
    public static final int samplesToByteCount(int sampleCount, final int alChannelLayout, final int alSampleType)
            throws IllegalArgumentException
    {
        switch(alChannelLayout) {
            case AL_MONO_SOFT:    sampleCount *= 1; break;
            case AL_STEREO_SOFT:  sampleCount *= 2; break;
            case AL_REAR_SOFT:    sampleCount *= 2; break;
            case AL_QUAD_SOFT:    sampleCount *= 4; break;
            case AL_5POINT1_SOFT: sampleCount *= 6; break;
            case AL_6POINT1_SOFT: sampleCount *= 7; break;
            case AL_7POINT1_SOFT: sampleCount *= 8; break;
            default: throw new IllegalArgumentException("Unknown al-channel-layout 0x"+Integer.toHexString(alChannelLayout));
        }

        switch(alSampleType) {
            case AL_BYTE_SOFT:
            case AL_UNSIGNED_BYTE_SOFT:
                break;
            case AL_SHORT_SOFT:
            case AL_UNSIGNED_SHORT_SOFT:
                sampleCount *= 2;
                break;
            case AL_INT_SOFT:
            case AL_UNSIGNED_INT_SOFT:
            case AL_FLOAT_SOFT:
                sampleCount *= 4;
                break;
            case AL_DOUBLE_SOFT:
                sampleCount *= 8;
                break;
            default:
                throw new IllegalArgumentException("Unknown al-type 0x"+Integer.toHexString(alSampleType));
        }

        return sampleCount;
    }

    public static final int bytesToSampleCount(final int byteCount, final int alChannelLayout, final int alSampleType) {
        return byteCount / samplesToByteCount(1, alChannelLayout, alSampleType);
    }
}
