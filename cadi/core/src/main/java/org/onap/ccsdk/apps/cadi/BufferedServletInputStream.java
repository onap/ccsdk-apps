/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END====================================================
 *
 */

package org.onap.ccsdk.apps.cadi;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

/**
 * BufferedServletInputStream
 *
 * There are cases in brain-dead middleware (SOAP) where they store routing information in the content.
 *
 * In HTTP, this requires reading the content from the InputStream which, of course, cannot be re-read.
 *
 * BufferedInputStream exists to implement the "Mark" protocols for Streaming, which will enable being
 * re-read.  Unfortunately, J2EE chose to require a "ServletInputStream" as an abstract class, rather than
 * an interface, which requires we create a delegating pattern, rather than the preferred inheriting pattern.
 *
 * Unfortunately, the standard "BufferedInputStream" cannot be used, because it simply creates a byte array
 * in the "mark(int)" method of that size.  This is not appropriate for this application, because the Header
 * can be potentially huge, and if a buffer was allocated to accommodate all possibilities, the cost of memory
 * allocation would be too large for high performance transactions.
 *
 *
 *
 */
public class BufferedServletInputStream extends ServletInputStream {
    private static final int NONE = 0;
    private static final int STORE = 1;
    private static final int READ = 2;

    private InputStream is;
    private int state = NONE;
    private Capacitor capacitor;

    public BufferedServletInputStream(InputStream is) {
        this.is = is;
        capacitor = null;
    }


    public int read() throws IOException {
        int value=-1;
        if (capacitor==null) {
            value=is.read();
        } else {
            switch(state) {
                case STORE:
                    value = is.read();
                    if (value>=0) {
                        capacitor.put((byte)value);
                    }
                    break;
                case READ:
                    value = capacitor.read();
                    if (value<0) {
                        capacitor.done();
                        capacitor=null; // all done with buffer
                        value = is.read();
                    }
            }
        }
        return value;
    }
    @Override
    public int read(byte[] b) throws IOException {
        return read(b,0,b.length);
    }
    
  @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = -1;
        if (capacitor==null) {
            count = is.read(b,off,len);
        } else {
            switch(state) {
                case STORE:
                    count = is.read(b, off, len);
                    if (count>0) {
                        capacitor.put(b, off, count);
                    }
                    break;
                case READ:
                    count = capacitor.read(b, off, len);
                    if (count<=0) {
                        capacitor.done();
                        capacitor=null; // all done with buffer
                    }
                    if (count<len) {
                        int temp = is.read(b, count, len-count);
                        if (temp>0) { // watch for -1
                            count+=temp;
                        } else if (count<=0) {
                            count = temp; // must account for Stream coming back -1
                        }
                    }
                    break;
            }
        }
        return count;
    }

    public long skip(long n) throws IOException {
        long skipped = capacitor.skip(n);
        if (skipped<n) {
            skipped += is.skip(n-skipped);
        }
        return skipped;
    }


    public int available() throws IOException {
        int count = is.available();
        if (capacitor!=null)count+=capacitor.available();
        return count;
    }

    /**
     * Return just amount buffered (for debugging purposes, mostly)
     * @return
     */
    public int buffered() {
        return capacitor.available();
    }


    public void close() throws IOException {
        if (capacitor!=null) {
            capacitor.done();
            capacitor=null;
        }
        is.close();
    }


    /**
     * Note: Readlimit is ignored in this implementation, because the need was for unknown buffer size which wouldn't
     * require allocating and dumping huge chunks of memory every use, or risk overflow.
     */
    public synchronized void mark(int readlimit) {
        switch(state) {
            case NONE:
                capacitor = new Capacitor();
                break;
            case READ:
                capacitor.done();
                break;
        }
        state = STORE;
    }


    /**
     * Reset Stream
     *
     * Calling this twice is not supported in typical Stream situations, but it is allowed in this service.  The caveat is that it can only reset
     * the data read in since Mark has been called.  The data integrity is only valid if you have not continued to read past what is stored.
     *
     */
    public synchronized void reset() throws IOException {
        switch(state) {
            case STORE:
                capacitor.setForRead();
                state = READ;
                break;
            case READ:
                capacitor.reset();
                break;
            case NONE:
                throw new IOException("InputStream has not been marked");
        }
    }


    public boolean markSupported() {
        return true;
    }


    @Override
    public boolean isFinished() {
        
        try {
            return (this.is.available() == 0);
        } catch (IOException e) {
            return true;
        }
    }


    @Override
    public boolean isReady() {
        return true;
    }


    @Override
    public void setReadListener(ReadListener arg0) {
        throw new UnsupportedOperationException("Unimplemented method 'setReadListener'");
    }
}
