package com.alegs3.modemsim.parser;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;

public final class RawBytes {
    private static final RawBytes EMPTY = new RawBytes(new byte[0]);
    private static final HexFormat HEX = HexFormat.of().withUpperCase();

    private final byte[] bytes;

    private RawBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public static RawBytes empty() {
        return EMPTY;
    }

    public static RawBytes copyOf(byte[] source) {
        Objects.requireNonNull(source, "source");
        return source.length == 0 ? EMPTY : new RawBytes(Arrays.copyOf(source, source.length));
    }

    public static RawBytes ascii(String value) {
        return copyOf(value.getBytes(StandardCharsets.US_ASCII));
    }

    public static RawBytes hex(String value) {
        return copyOf(HEX.parseHex(value.replace(" ", "")));
    }

    public RawBytes append(RawBytes other) {
        byte[] joined = Arrays.copyOf(bytes, bytes.length + other.bytes.length);
        System.arraycopy(other.bytes, 0, joined, bytes.length, other.bytes.length);
        return copyOf(joined);
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public int length() {
        return bytes.length;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }

    public String toHex() {
        return HEX.formatHex(bytes);
    }

    public String ascii() {
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    @Override
    public String toString() {
        return toHex();
    }
}
