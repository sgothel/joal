/**
 * OpenAL Helpers
 *
 * Copyright (c) 2011 by Chris Robinson <chris.kcat@gmail.com>
 * Copyright (c) 2013-2023 JogAmp Community
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
package com.jogamp.openal.util;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALConstants;
import static com.jogamp.openal.ALConstants.*;
import com.jogamp.openal.ALExt;
import static com.jogamp.openal.ALExtConstants.*;

import com.jogamp.common.av.AudioFormat;


/* This class contains routines to help with some menial OpenAL-related tasks,
 * such as finding an audio format and getting readable strings for
 * channel configs and sample types. */
public class ALHelpers {
    /**
     * [openal-soft >= 1.18.0](https://github.com/kcat/openal-soft/blob/master/ChangeLog)
     * - Removed support for the AL_SOFT_buffer_samples and AL_SOFT_buffer_sub_data
     *   extensions. Due to conflicts with AL_EXT_SOURCE_RADIUS.
     */
    public static final String AL_SOFT_buffer_samples = "AL_SOFT_buffer_samples";
    // public static final String AL_SOFT_callback_buffer = "AL_SOFT_callback_buffer";
    public static final String AL_SOFT_events = "AL_SOFT_events";

    public static final String AL_EXT_MCFORMATS = "AL_EXT_MCFORMATS";
    public static final String AL_EXT_FLOAT32 = "AL_EXT_FLOAT32";
    public static final String AL_EXT_DOUBLE = "AL_EXT_DOUBLE";

    public static final String ALC_EXT_thread_local_context = "ALC_EXT_thread_local_context";

    /**
     * Returns a compatible {@link AudioFormat} based on given OpenAL channel-layout, sample-type and format,
     * as well as the generic sample-rate and sample-size.
     * <p>
     * The resulting {@link AudioFormat} uses {@link AudioFormat#planar} = false and {@link AudioFormat#littleEndian} = true.
     * </p>
     * @param alChannelLayout OpenAL channel layout
     * @param alSampleType OpenAL sample type
     * @param alFormat OpenAL format
     * @param sampleRate sample rate, e.g. 44100
     * @param sampleSize sample size in bits, e.g. 16
     * @return a new {@link AudioFormat} instance or null if parameter are not conclusive or invalid.
     */
    public static AudioFormat getAudioFormat(final int alChannelLayout, final int alSampleType, final int alFormat,
                                             final int sampleRate, final int sampleSize) {
        if( ALConstants.AL_NONE == alChannelLayout || ALConstants.AL_NONE == alSampleType ||
            ALConstants.AL_NONE == alFormat || 0 == sampleRate || 0 == sampleSize ) {
            return null;
        }
        final int channelCount = getALChannelLayoutChannelCount(alChannelLayout);
        if( 0 == channelCount ) {
            return null;
        }
        final boolean signed = isALSampleTypeSigned(alSampleType);
        final boolean fixedP = isALSampleTypeFixed(alSampleType);
        return new AudioFormat(sampleRate, sampleSize, channelCount, signed, fixedP,
                               false /* planar */, true /* littleEndian */);
    }

    /**
     * Returns a compatible AL buffer format given the {@link AudioFormat},
     * which determines the AL channel layout and AL sample type.
     * </p>
     * <p>
     * If <code>hasEXTMcFormats</code> or <code>hasSOFTBufferSamples</code> is true,
     * it will be called to find the closest-matching format from
     * <code>AL_EXT_MCFORMATS</code> or <code>AL_SOFT_buffer_samples</code>.
     * </p>
     * <p>
     * Returns {@link ALConstants#AL_NONE} if no supported format can be found.
     * </p>
     * <p>
     * Function uses {@link AL#alIsExtensionPresent(String)}, which might be context dependent,
     * otherwise function is context independent.
     * </p>
     *
     * @param audioFormat used to derive AL channel layout {@link #getDefaultALChannelLayout(int)}
     *                    and AL sample type {@link #getALSampleType(int, boolean, boolean)}
     * @param al AL instance
     * @param alExt ALExt instance
     * @return AL buffer format
     */
    public static int getALFormat(final AudioFormat audioFormat,
                                  final AL al, final ALExt alExt) {
        final int alChannelLayout = ALHelpers.getDefaultALChannelLayout(audioFormat.channelCount);
        final int alSampleType = ALHelpers.getALSampleType(audioFormat.sampleSize, audioFormat.signed, audioFormat.fixedP);
        if( ALConstants.AL_NONE != alChannelLayout && ALConstants.AL_NONE != alSampleType ) {
            return ALHelpers.getALFormat(alChannelLayout, alSampleType, al, alExt);
        } else {
            return ALConstants.AL_NONE;
        }
    }

    /**
     * Returns a compatible AL buffer format given the {@link AudioFormat},
     * which determines the AL channel layout and AL sample type.
     * </p>
     * <p>
     * If <code>hasEXTMcFormats</code> or <code>hasSOFTBufferSamples</code> is true,
     * it will be called to find the closest-matching format from
     * <code>AL_EXT_MCFORMATS</code> or <code>AL_SOFT_buffer_samples</code>.
     * </p>
     * <p>
     * Returns {@link ALConstants#AL_NONE} if no supported format can be found.
     * </p>
     * <p>
     * Function is context independent.
     * </p>
     *
     * @param audioFormat used to derive AL channel layout {@link #getDefaultALChannelLayout(int)}
     *                    and AL sample type {@link #getALSampleType(int, boolean, boolean)}
     * @param al AL instance
     * @param alExt ALExt instance
     * @param hasSOFTBufferSamples true if having extension <code>AL_SOFT_buffer_samples</code>, otherwise false
     * @param hasEXTMcFormats true if having extension <code>AL_EXT_MCFORMATS</code>, otherwise false
     * @param hasEXTFloat32 true if having extension <code>AL_EXT_FLOAT32</code>, otherwise false
     * @param hasEXTDouble true if having extension <code>AL_EXT_DOUBLE</code>, otherwise false
     * @return AL buffer format
     */
    public static int getALFormat(final AudioFormat audioFormat,
                                  final AL al, final ALExt alExt,
                                  final boolean hasSOFTBufferSamples,
                                  final boolean hasEXTMcFormats,
                                  final boolean hasEXTFloat32, final boolean hasEXTDouble) {
        final int alChannelLayout = ALHelpers.getDefaultALChannelLayout(audioFormat.channelCount);
        final int alSampleType = ALHelpers.getALSampleType(audioFormat.sampleSize, audioFormat.signed, audioFormat.fixedP);
        final int alFormat;
        if( ALConstants.AL_NONE != alChannelLayout && ALConstants.AL_NONE != alSampleType ) {
            alFormat = ALHelpers.getALFormat(alChannelLayout, alSampleType, al, alExt,
                                             hasSOFTBufferSamples, hasEXTMcFormats,
                                             hasEXTFloat32, hasEXTDouble);
        } else {
            alFormat = ALConstants.AL_NONE;
        }
        return alFormat;
    }

    /**
     * Returns a compatible AL buffer format given the AL channel layout and AL sample type.
     * <p>
     * If <code>hasEXTMcFormats</code> or <code>hasSOFTBufferSamples</code> is true,
     * it will be called to find the closest-matching format from
     * <code>AL_EXT_MCFORMATS</code> or <code>AL_SOFT_buffer_samples</code>.
     * </p>
     * <p>
     * Returns {@link ALConstants#AL_NONE} if no supported format can be found.
     * </p>
     * <p>
     * Function uses {@link AL#alIsExtensionPresent(String)}, which might be context dependent,
     * otherwise function is context independent.
     * </p>
     *
     * @param alChannelLayout AL channel layout, see {@link #getDefaultALChannelLayout(int)}
     * @param alSampleType AL sample type, see {@link #getALSampleType(int, boolean, boolean)}.
     * @param al AL instance
     * @param alExt ALExt instance
     * @return AL buffer format
     */
    public static final int getALFormat(final int alChannelLayout, final int alSampleType,
                                        final AL al, final ALExt alExt) {
        final boolean hasSOFTBufferSamples = al.alIsExtensionPresent(AL_SOFT_buffer_samples);
        final boolean hasEXTMcFormats = al.alIsExtensionPresent(AL_EXT_MCFORMATS);
        final boolean hasEXTFloat32 = al.alIsExtensionPresent(AL_EXT_FLOAT32);
        final boolean hasEXTDouble = al.alIsExtensionPresent(AL_EXT_DOUBLE);
        return ALHelpers.getALFormat(alChannelLayout, alSampleType, al, alExt,
                                     hasSOFTBufferSamples, hasEXTMcFormats,
                                     hasEXTFloat32, hasEXTDouble);
    }

    /**
     * Returns a compatible AL buffer format given the AL channel layout and AL sample type.
     * <p>
     * If <code>hasEXTMcFormats</code> or <code>hasSOFTBufferSamples</code> is true,
     * it will be called to find the closest-matching format from
     * <code>AL_EXT_MCFORMATS</code> or <code>AL_SOFT_buffer_samples</code>.
     * </p>
     * <p>
     * Returns {@link ALConstants#AL_NONE} if no supported format can be found.
     * </p>
     * <p>
     * Function is context independent.
     * </p>
     *
     * @param alChannelLayout AL channel layout, see {@link #getDefaultALChannelLayout(int)}
     * @param alSampleType AL sample type, see {@link #getALSampleType(int, boolean, boolean)}.
     * @param al AL instance
     * @param alExt ALExt instance
     * @param hasSOFTBufferSamples true if having extension <code>AL_SOFT_buffer_samples</code>, otherwise false
     * @param hasEXTMcFormats true if having extension <code>AL_EXT_MCFORMATS</code>, otherwise false
     * @param hasEXTFloat32 true if having extension <code>AL_EXT_FLOAT32</code>, otherwise false
     * @param hasEXTDouble true if having extension <code>AL_EXT_DOUBLE</code>, otherwise false
     * @return AL buffer format
     */
    public static final int getALFormat(final int alChannelLayout, final int alSampleType,
                                        final AL al, final ALExt alExt,
                                        final boolean hasSOFTBufferSamples,
                                        final boolean hasEXTMcFormats,
                                        final boolean hasEXTFloat32, final boolean hasEXTDouble) {
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
                    format = AL_FORMAT_QUAD8;
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = AL_FORMAT_51CHN8;
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = AL_FORMAT_61CHN8;
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = AL_FORMAT_71CHN8;
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
                    format = AL_FORMAT_QUAD16;
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = AL_FORMAT_51CHN16;
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = AL_FORMAT_61CHN16;
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = AL_FORMAT_71CHN16;
            }
        }
        else if(alSampleType == AL_FLOAT_SOFT && hasEXTFloat32)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = AL_FORMAT_MONO_FLOAT32;
            else if(alChannelLayout == AL_STEREO_SOFT)
                format = AL_FORMAT_STEREO_FLOAT32;
            else if( hasEXTMcFormats )
            {
                if(alChannelLayout == AL_QUAD_SOFT)
                    format = AL_FORMAT_QUAD32;
                else if(alChannelLayout == AL_5POINT1_SOFT)
                    format = AL_FORMAT_51CHN32;
                else if(alChannelLayout == AL_6POINT1_SOFT)
                    format = AL_FORMAT_61CHN32;
                else if(alChannelLayout == AL_7POINT1_SOFT)
                    format = AL_FORMAT_71CHN32;
            }
        }
        else if(alSampleType == AL_DOUBLE_SOFT && hasEXTDouble)
        {
            if(alChannelLayout == AL_MONO_SOFT)
                format = AL_FORMAT_MONO_DOUBLE_EXT;
            else if(alChannelLayout == AL_STEREO_SOFT) {
                format = AL_FORMAT_STEREO_DOUBLE_EXT;
            }
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
     * Returns the channel count of the given AL channel layout
     */
    public static final int getALChannelLayoutChannelCount(final int alChannelLayout) {
        switch(alChannelLayout) {
            case AL_MONO_SOFT: return 1;
            case AL_STEREO_SOFT: return 2;
            case AL_REAR_SOFT: return 2;
            case AL_QUAD_SOFT: return 4;
            case AL_5POINT1_SOFT: return 6;
            case AL_6POINT1_SOFT: return 7;
            case AL_7POINT1_SOFT: return 8;
        }
        return 0;
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
     * Returns whether the given AL sample type is signed
     */
    public static final boolean isALSampleTypeSigned(final int alSampleType) {
        switch(alSampleType) {
            case AL_BYTE_SOFT:
            case AL_SHORT_SOFT:
            case AL_INT_SOFT:
            case AL_FLOAT_SOFT:
            case AL_DOUBLE_SOFT:
                return true;
            case AL_UNSIGNED_BYTE_SOFT:
            case AL_UNSIGNED_SHORT_SOFT:
            case AL_UNSIGNED_INT_SOFT:
            default:
                return false;
        }
    }

    /**
     * Returns true if the given AL sample type is a fixed point (byte, short, int, ..)
     * or false if a floating point type (float, double).
     */
    public static final boolean isALSampleTypeFixed(final int alSampleType) {
        switch(alSampleType) {
            case AL_BYTE_SOFT:
            case AL_SHORT_SOFT:
            case AL_INT_SOFT:
            case AL_UNSIGNED_BYTE_SOFT:
            case AL_UNSIGNED_SHORT_SOFT:
            case AL_UNSIGNED_INT_SOFT:
                return true;
            case AL_FLOAT_SOFT:
            case AL_DOUBLE_SOFT:
            default:
                return false;
        }
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
