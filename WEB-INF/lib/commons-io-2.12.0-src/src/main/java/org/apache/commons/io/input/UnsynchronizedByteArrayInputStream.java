/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import static java.lang.Math.min;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.build.AbstractStreamBuilder;

/**
 * This is an alternative to {@link java.io.ByteArrayInputStream} which removes the synchronization overhead for non-concurrent access; as such this class is
 * not thread-safe.
 *
 * @see ByteArrayInputStream
 * @since 2.7
 */
//@NotThreadSafe
public class UnsynchronizedByteArrayInputStream extends InputStream {

    /**
     * Builds a new {@link UnsynchronizedByteArrayInputStream} instance.
     * <p>
     * Using a Byte Array:
     * </p>
     *
     * <pre>{@code
     * UnsynchronizedByteArrayInputStream s = UnsynchronizedByteArrayInputStream.builder()
     *   .setByteArray(byteArray)
     *   .setOffset(0)
     *   .setLength(byteArray.length)
     *   .get()}
     * </pre>
     * <p>
     * Using File IO:
     * </p>
     *
     * <pre>{@code
     * UnsynchronizedByteArrayInputStream s = UnsynchronizedByteArrayInputStream.builder()
     *   .setFile(file)
     *   .setOffset(0)
     *   .setLength(byteArray.length)
     *   .get()}
     * </pre>
     * <p>
     * Using NIO Path:
     * </p>
     *
     * <pre>{@code
     * UnsynchronizedByteArrayInputStream s = UnsynchronizedByteArrayInputStream.builder()
     *   .setPath(path)
     *   .setOffset(0)
     *   .setLength(byteArray.length)
     *   .get()}
     * </pre>
     */
    public static class Builder extends AbstractStreamBuilder<UnsynchronizedByteArrayInputStream, Builder> {

        private int offset;
        private int length;

        /**
         * Constructs a new instance.
         *
         * @throws UnsupportedOperationException if the origin cannot be converted to a byte array.
         */
        @Override
        public UnsynchronizedByteArrayInputStream get() throws IOException {
            return new UnsynchronizedByteArrayInputStream(getOrigin().getByteArray(), offset, length);
        }

        @Override
        public Builder setByteArray(final byte[] origin) {
            length = Objects.requireNonNull(origin, "origin").length;
            return super.setByteArray(origin);
        }

        public Builder setLength(final int length) {
            if (length < 0) {
                throw new IllegalArgumentException("length cannot be negative");
            }
            this.length = length;
            return this;
        }

        public Builder setOffset(final int offset) {
            if (offset < 0) {
                throw new IllegalArgumentException("offset cannot be negative");
            }
            this.offset = offset;
            return this;
        }

    }

    /**
     * The end of stream marker.
     */
    public static final int END_OF_STREAM = -1;

    /**
     * Constructs a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * The underlying data buffer.
     */
    private final byte[] data;

    /**
     * End Of Data.
     *
     * Similar to data.length, i.e. the last readable offset + 1.
     */
    private final int eod;

    /**
     * Current offset in the data buffer.
     */
    private int offset;

    /**
     * The current mark (if any).
     */
    private int markedOffset;

    /**
     * Creates a new byte array input stream.
     *
     * @param data the buffer
     * @deprecated Use {@link #builder()}.
     */
    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data) {
        this.data = Objects.requireNonNull(data, "data");
        this.offset = 0;
        this.eod = data.length;
        this.markedOffset = this.offset;
    }

    /**
     * Creates a new byte array input stream.
     *
     * @param data   the buffer
     * @param offset the offset into the buffer
     *
     * @throws IllegalArgumentException if the offset is less than zero
     * @deprecated Use {@link #builder()}.
     */
    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset) {
        Objects.requireNonNull(data, "data");
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        this.data = data;
        this.offset = min(offset, data.length > 0 ? data.length : offset);
        this.eod = data.length;
        this.markedOffset = this.offset;
    }

    /**
     * Creates a new byte array input stream.
     *
     * @param data   the buffer
     * @param offset the offset into the buffer
     * @param length the length of the buffer
     *
     * @throws IllegalArgumentException if the offset or length less than zero
     * @deprecated Use {@link #builder()}.
     */
    @Deprecated
    public UnsynchronizedByteArrayInputStream(final byte[] data, final int offset, final int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length cannot be negative");
        }
        this.data = Objects.requireNonNull(data, "data");
        this.offset = min(offset, data.length > 0 ? data.length : offset);
        this.eod = min(this.offset + length, data.length);
        this.markedOffset = this.offset;
    }

    @Override
    public int available() {
        return offset < eod ? eod - offset : 0;
    }

    @SuppressWarnings("sync-override")
    @Override
    public void mark(final int readlimit) {
        this.markedOffset = this.offset;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() {
        return offset < eod ? data[offset++] & 0xff : END_OF_STREAM;
    }

    @Override
    public int read(final byte[] dest) {
        Objects.requireNonNull(dest, "dest");
        return read(dest, 0, dest.length);
    }

    @Override
    public int read(final byte[] dest, final int off, final int len) {
        Objects.requireNonNull(dest, "dest");
        if (off < 0 || len < 0 || off + len > dest.length) {
            throw new IndexOutOfBoundsException();
        }

        if (offset >= eod) {
            return END_OF_STREAM;
        }

        int actualLen = eod - offset;
        if (len < actualLen) {
            actualLen = len;
        }
        if (actualLen <= 0) {
            return 0;
        }
        System.arraycopy(data, offset, dest, off, actualLen);
        offset += actualLen;
        return actualLen;
    }

    @SuppressWarnings("sync-override")
    @Override
    public void reset() {
        this.offset = this.markedOffset;
    }

    @Override
    public long skip(final long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Skipping backward is not supported");
        }

        long actualSkip = eod - offset;
        if (n < actualSkip) {
            actualSkip = n;
        }

        offset = Math.addExact(offset, Math.toIntExact(n));
        return actualSkip;
    }
}
