package com.jogamp.openal.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.common.os.MachineDataInfo;
import com.jogamp.common.os.Platform;

class IOHelpers {
    /* modified from com.jogamp.common.util.IOUtil - always respect initialCapacity */
    public static ByteBuffer copyFromStream2ByteBuffer(InputStream stream, int bytesToCopy) throws IOException {
        if( !(stream instanceof BufferedInputStream) ) {
            stream = new BufferedInputStream(stream);
        }
        final MachineDataInfo machine = Platform.getMachineDataInfo();
        ByteBuffer data = Buffers.newDirectByteBuffer( machine.pageAlignedSize( bytesToCopy ) );
        final byte[] chunk = new byte[machine.pageSizeInBytes()];
        int avail = Math.min(stream.available(), bytesToCopy);
        int chunk2Read = Math.min(machine.pageSizeInBytes(), avail);
        int numRead = 0;
        do {
            if (avail > data.remaining()) {
                final ByteBuffer newData = Buffers.newDirectByteBuffer(
                        machine.pageAlignedSize(data.position() + avail) );
                newData.put(data);
                data = newData;
            }

            numRead = stream.read(chunk, 0, chunk2Read);
            if (numRead > 0) {
                data.put(chunk, 0, numRead);
            }
            avail -= numRead;
            chunk2Read = Math.min(machine.pageSizeInBytes(), avail);
        } while ( numRead > 0 ); // EOS: -1 == numRead, EOF maybe reached earlier w/ 0 == numRead

        data.flip();
        return data;
    }

}
