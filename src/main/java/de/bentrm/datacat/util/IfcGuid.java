package de.bentrm.datacat.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class IfcGuid {

    private final static char[] IFC_GUID_CHARSET = new char[64];
    static {
        int i = 0;
        for (; i < 10; i++) {
            IFC_GUID_CHARSET[i] = Character.forDigit(i, 10);
        }
        for (char c = 'A'; c <= 'Z'; c++, i++) {
            IFC_GUID_CHARSET[i] = c;
        }
        for (char c = 'a'; c <= 'z'; c++, i++) {
            IFC_GUID_CHARSET[i] = c;
        }
        IFC_GUID_CHARSET[62] = '_';
        IFC_GUID_CHARSET[63] = '$';
    }

    private final static List<Character> charsetLookupList = new ArrayList<>();
    static {
        for (char c : IFC_GUID_CHARSET) {
            charsetLookupList.add(c);
        }
    }

    private IfcGuid() {}

    public static String compress(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        byte[] byteArr = byteBuffer.array();

        StringBuilder buffer = new StringBuilder(24);
        buffer.append(encode(Byte.toUnsignedInt(byteArr[0]), 2));
        for (int i = 1; i < 16; i += 3) {
            int a = (Byte.toUnsignedInt(byteArr[i])) << 16;
            int b = Byte.toUnsignedInt(byteArr[i + 1]) << 8;
            int c = Byte.toUnsignedInt(byteArr[i + 2]);
            buffer.append(encode(a + b + c, 4));
        }
        return buffer.toString();
    }

    public static UUID decompress(String value) {
        byte[] digits = new byte[16];

        digits[0] = (byte) decode(value.substring(0, 2));

        int digitIndex = 1;
        for (int i = 0; i < 5; i++) {
            int beginIndex = 2 + 4 * i;
            int endIndex = 6 + 4 * i;
            int digit = decode(value.substring(beginIndex, endIndex));
            for (int j = 0; j < 3; j++) {
                digits[digitIndex] = (byte) ((digit >> (8 * (2 - j))) % 256);
                digitIndex++;
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(digits);
        return new UUID(byteBuffer.getLong(0), byteBuffer.getLong(8));
    }

    private static String encode(int value, int length) {
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = Math.floorMod(Math.floorDiv(value, (int) Math.pow(64, i)), 64);
            buffer.append(IFC_GUID_CHARSET[index]);
        }
        return buffer.reverse().toString();
    }

    private static int decode(String value) {
        int length = value.length();
        int[] charsetIndexes = new int[length];
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            charsetIndexes[i] = charsetLookupList.indexOf(c);
        }
        return Arrays.stream(charsetIndexes)
                .reduce((left, right) -> (left * 64) + right)
                .getAsInt();

    }
}
