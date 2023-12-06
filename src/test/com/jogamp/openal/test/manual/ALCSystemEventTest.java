package com.jogamp.openal.test.manual;

import com.jogamp.openal.*;
import com.jogamp.openal.util.ALHelpers;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jogamp.openal.ALCConstants.*;
import static com.jogamp.openal.ALConstants.AL_FORMAT_MONO8;
import static com.jogamp.openal.ALExtConstants.*;

/**
 * Testing the OpenAL-Soft System Event Extension.
 */
public class ALCSystemEventTest {

    public static class ALCDeviceMetadata {
        public final String deviceIdentifier;
        public final String deviceName;

        public ALCDeviceMetadata(final String deviceIdentifier, final String deviceName) {
            this.deviceIdentifier = deviceIdentifier;
            this.deviceName = deviceName;
        }
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final ALC alc = ALFactory.getALC();

        if( !alc.alcIsExtensionPresent(null, ALHelpers.ALC_SOFT_system_events) ) {
            System.err.println("Current alc implementation has no "+ALHelpers.ALC_SOFT_system_events+" extension");
            return;
        }

        final JoalVersion jv = JoalVersion.getInstance();
        System.err.println(jv.toString(alc));
        System.err.println("-----------------------------------------------------------------------------------------------------");

        final ALExt alExt = ALFactory.getALExt();
        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final boolean enumerationExtIsPresent = alc.alcEnumerationExtIsPresent();
        final boolean enumerateAllExtIsPresent = alc.alcEnumerateAllExtIsPresent();
        final boolean noEnumerateExt = !enumerationExtIsPresent && !enumerateAllExtIsPresent;

        final Map<ALCdevice, ALCDeviceMetadata> playbackDeviceNameEnumerationExt = new HashMap<>();
        final Map<ALCdevice, ALCDeviceMetadata> captureDeviceNameEnumerationExt = new HashMap<>();

        if (enumerationExtIsPresent) {
            for (final String deviceIdentifier : alc.alcGetDeviceSpecifiers()) {
                final ALCdevice device = alc.alcOpenDevice(deviceIdentifier);
                String deviceName = alc.alcGetString(device, ALC_DEVICE_SPECIFIER);
                playbackDeviceNameEnumerationExt.put(device, new ALCDeviceMetadata(deviceIdentifier, deviceName));
            }
            for (final String deviceIdentifier : alc.alcGetCaptureDeviceSpecifiers()) {
                final ALCdevice device = alc.alcCaptureOpenDevice(deviceIdentifier, 1, AL_FORMAT_MONO8, 1);
                String deviceName = alc.alcGetString(device, ALC_CAPTURE_DEVICE_SPECIFIER);
                captureDeviceNameEnumerationExt.put(device, new ALCDeviceMetadata(deviceIdentifier, deviceName));
            }
        }

        final Map<ALCdevice, ALCDeviceMetadata> playbackDeviceNameEnumerateAllExt = new HashMap<>();

        if (enumerateAllExtIsPresent) {
            for (final String deviceIdentifier : alc.alcGetAllDeviceSpecifiers()) {
                final ALCdevice device = alc.alcOpenDevice(deviceIdentifier);
                String deviceName = alc.alcGetString(device, ALCConstants.ALC_ALL_DEVICES_SPECIFIER);
                playbackDeviceNameEnumerateAllExt.put(device, new ALCDeviceMetadata(deviceIdentifier, deviceName));
            }
        }

        final ALExt.ALCEVENTPROCTYPESOFT systemEventsCallback = (eventType, deviceType, device, message, userParam) -> {
            System.err.println("Event[eventType: "+Integer.toHexString(eventType)+
                    ", deviceType: "+Integer.toHexString(deviceType)+ ", device: "+device+ ", message: "+message+
                    ", userParam: "+userParam+"]");
            switch (eventType) {
                case ALC_EVENT_TYPE_DEFAULT_DEVICE_CHANGED_SOFT: {
                    switch (deviceType) {
                        case ALC_PLAYBACK_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Default playback device are changed");
                                if (enumerateAllExtIsPresent) {
                                    System.err.println("New default playback device name (With "+
                                            ALHelpers.ALC_ENUMERATE_ALL_EXT+"): "+
                                            alc.alcGetString(null, ALC_DEFAULT_ALL_DEVICES_SPECIFIER)
                                    );
                                }
                                if (enumerationExtIsPresent) {
                                    System.err.println("New default playback device name (With "+
                                            ALHelpers.ALC_ENUMERATION_EXT+"): "+
                                            alc.alcGetString(null, ALC_DEFAULT_DEVICE_SPECIFIER)
                                    );
                                }
                                if (noEnumerateExt) {
                                    System.err.println("Unable to retrieve default playback device name ("+
                                            ALHelpers.ALC_ENUMERATE_ALL_EXT+" and "+
                                            ALHelpers.ALC_ENUMERATION_EXT+" are missing)"
                                    );
                                }
                            });
                            break;
                        }
                        case ALC_CAPTURE_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Default capture device are changed");
                                if (enumerationExtIsPresent) {
                                    System.err.println("New default capture device name: "+
                                            alc.alcGetString(null, ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER)
                                    );
                                } else {
                                    System.err.println("Unable to retrieve default capture device name ("+
                                            ALHelpers.ALC_ENUMERATION_EXT+" is missing)"
                                    );
                                }
                            });
                            break;
                        }
                    }
                }
                case ALC_EVENT_TYPE_DEVICE_ADDED_SOFT: {
                    switch (deviceType) {
                        case ALC_PLAYBACK_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Playback device are added");
                                if (enumerateAllExtIsPresent) {
                                    for (final String deviceIdentifier : alc.alcGetAllDeviceSpecifiers()) {
                                        if (playbackDeviceNameEnumerateAllExt.values().stream()
                                                .map(m -> m.deviceName.equals(deviceIdentifier))
                                                .findAny()
                                                .isPresent()
                                        ) continue;

                                        final ALCdevice currentDevice = alc.alcOpenDevice(deviceIdentifier);
                                        final String deviceName = alc.alcGetString(currentDevice, ALCConstants.ALC_ALL_DEVICES_SPECIFIER);
                                        System.err.println("New playback device name (With "+
                                                ALHelpers.ALC_ENUMERATE_ALL_EXT+"): "+
                                                deviceName
                                        );
                                        playbackDeviceNameEnumerateAllExt.put(currentDevice, new ALCDeviceMetadata(deviceIdentifier, deviceName));
                                    }
                                }
                                if (enumerationExtIsPresent) {
                                    for (final String deviceIdentifier : alc.alcGetDeviceSpecifiers()) {
                                        if (playbackDeviceNameEnumerationExt.values().stream()
                                                .map(m -> m.deviceName.equals(deviceIdentifier))
                                                .findAny()
                                                .isPresent()
                                        ) continue;

                                        final ALCdevice currentDevice = alc.alcOpenDevice(deviceIdentifier);
                                        final String deviceName = alc.alcGetString(currentDevice, ALC_DEVICE_SPECIFIER);
                                        System.err.println("New playback device name (With "+
                                                ALHelpers.ALC_ENUMERATION_EXT+"): "+
                                                deviceName
                                        );
                                        playbackDeviceNameEnumerationExt.put(currentDevice, new ALCDeviceMetadata(deviceIdentifier, deviceName));
                                    }
                                }
                                if (noEnumerateExt) {
                                    System.err.println("Unable to retrieve new playback device name ("+
                                            ALHelpers.ALC_ENUMERATE_ALL_EXT+" and "+
                                            ALHelpers.ALC_ENUMERATION_EXT+" are missing)"
                                    );
                                }
                            });
                            break;
                        }
                        case ALC_CAPTURE_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Capture device are added");
                                if (enumerationExtIsPresent) {
                                    for (final String deviceIdentifier : alc.alcGetCaptureDeviceSpecifiers()) {
                                        if (captureDeviceNameEnumerationExt.values().stream()
                                                .map(m -> m.deviceName.equals(deviceIdentifier))
                                                .findAny()
                                                .isPresent()
                                        ) continue;

                                        final ALCdevice currentDevice = alc.alcCaptureOpenDevice(deviceIdentifier, 1, AL_FORMAT_MONO8, 1);
                                        final String deviceName = alc.alcGetString(currentDevice, ALC_CAPTURE_DEVICE_SPECIFIER);
                                        System.err.println("New capture device name (With "+
                                                ALHelpers.ALC_ENUMERATION_EXT+"): "+
                                                deviceName
                                        );
                                        captureDeviceNameEnumerationExt.put(currentDevice, new ALCDeviceMetadata(deviceIdentifier, deviceName));
                                    }
                                } else {
                                    System.err.println("Unable to retrieve new capture device name ("+
                                            ALHelpers.ALC_ENUMERATION_EXT+" is missing)"
                                    );
                                }
                            });
                            break;
                        }
                    }
                }
                case ALC_EVENT_TYPE_DEVICE_REMOVED_SOFT: {
                    switch (deviceType) {
                        case ALC_PLAYBACK_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Playback device are removed");
                                if (enumerateAllExtIsPresent) {
                                    final List<String> devicesNames = Arrays.asList(alc.alcGetAllDeviceSpecifiers());
                                    final List<ALCdevice> devicesToRemove = new ArrayList<>();
                                    for (final Map.Entry<ALCdevice, ALCDeviceMetadata> deviceEntry : playbackDeviceNameEnumerateAllExt.entrySet()) {
                                        if (!devicesNames.contains(deviceEntry.getValue().deviceIdentifier)) continue;

                                        System.err.println("Removed playback device name (With "+
                                                ALHelpers.ALC_ENUMERATE_ALL_EXT+"): "+
                                                deviceEntry.getValue().deviceName
                                        );
                                        devicesToRemove.add(deviceEntry.getKey());
                                    }
                                    devicesToRemove.forEach(d -> {
                                        alc.alcCloseDevice(d);
                                        playbackDeviceNameEnumerateAllExt.remove(d);
                                    });
                                }
                                if (enumerationExtIsPresent) {
                                    final List<String> devicesNames = Arrays.asList(alc.alcGetDeviceSpecifiers());
                                    final List<ALCdevice> devicesToRemove = new ArrayList<>();
                                    for (final Map.Entry<ALCdevice, ALCDeviceMetadata> deviceEntry : playbackDeviceNameEnumerationExt.entrySet()) {
                                        if (!devicesNames.contains(deviceEntry.getValue().deviceIdentifier)) continue;

                                        System.err.println("Removed playback device name (With "+
                                                ALHelpers.ALC_ENUMERATION_EXT+"): "+
                                                deviceEntry.getValue().deviceName
                                        );
                                        devicesToRemove.add(deviceEntry.getKey());
                                    }
                                    devicesToRemove.forEach(d -> {
                                        alc.alcCloseDevice(d);
                                        playbackDeviceNameEnumerationExt.remove(d);
                                    });
                                }
                                if (noEnumerateExt) {
                                    System.err.println("Unable to retrieve new playback device name ("+
                                            ALHelpers.ALC_ENUMERATE_ALL_EXT+" and "+
                                            ALHelpers.ALC_ENUMERATION_EXT+" are missing)"
                                    );
                                }
                            });
                            break;
                        }
                        case ALC_CAPTURE_DEVICE_SOFT: {
                            executorService.submit(() -> {
                                System.err.println("Capture device are removed");
                                if (enumerationExtIsPresent) {
                                    final List<String> devicesNames = Arrays.asList(alc.alcGetCaptureDeviceSpecifiers());
                                    final List<ALCdevice> devicesToRemove = new ArrayList<>();
                                    for (final Map.Entry<ALCdevice, ALCDeviceMetadata> deviceEntry : captureDeviceNameEnumerationExt.entrySet()) {
                                        if (!devicesNames.contains(deviceEntry.getValue().deviceIdentifier)) continue;

                                        System.err.println("Removed capture device name (With "+
                                                ALHelpers.ALC_ENUMERATION_EXT+"): "+
                                                deviceEntry.getValue().deviceName
                                        );
                                        devicesToRemove.add(deviceEntry.getKey());
                                    }
                                    devicesToRemove.forEach(d -> {
                                        alc.alcCaptureCloseDevice(d);
                                        captureDeviceNameEnumerationExt.remove(d);
                                    });
                                } else {
                                    System.err.println("Unable to retrieve new capture device name ("+
                                            ALHelpers.ALC_ENUMERATION_EXT+" is missing)"
                                    );
                                }
                            });
                            break;
                        }
                    }
                }
            }
        };

        alExt.alcEventControlSOFT(3, new int[] {
                ALC_EVENT_TYPE_DEFAULT_DEVICE_CHANGED_SOFT,
                ALC_EVENT_TYPE_DEVICE_ADDED_SOFT,
                ALC_EVENT_TYPE_DEVICE_REMOVED_SOFT
        }, 0, true);
        alExt.alcEventCallbackSOFT(systemEventsCallback, null);

        Thread.sleep(Duration.ofMinutes(2).toMillis());

        executorService.shutdown();

        alExt.alcEventControlSOFT(3, new int[] {
                ALC_EVENT_TYPE_DEFAULT_DEVICE_CHANGED_SOFT,
                ALC_EVENT_TYPE_DEVICE_ADDED_SOFT,
                ALC_EVENT_TYPE_DEVICE_REMOVED_SOFT
        }, 0, false);
        alExt.alcEventCallbackSOFT(null, null);

        if (enumerationExtIsPresent) {
            for (final ALCdevice device : playbackDeviceNameEnumerationExt.keySet()) {
                alc.alcCloseDevice(device);
            }
            for (final ALCdevice device : captureDeviceNameEnumerationExt.keySet()) {
                alc.alcCaptureCloseDevice(device);
            }
        }

        if (enumerateAllExtIsPresent) {
            for (final ALCdevice device : playbackDeviceNameEnumerateAllExt.keySet()) {
                alc.alcCloseDevice(device);
            }
        }
    }
}
