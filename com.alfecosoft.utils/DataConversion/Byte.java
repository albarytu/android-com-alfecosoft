package com.alfecosoft.utils.DataConversion;

public class Byte {
    public static String byteToBinary(long by)
    {
        long b = by;
        long b0 = b % 2; b=b/2;
        long b1 = b % 2; b=b/2;
        long b2 = b % 2; b=b/2;
        long b3 = b % 2; b=b/2;
        long b4 = b % 2; b=b/2;
        long b5 = b % 2; b=b/2;
        long b6 = b % 2; b=b/2;
        long b7 = b % 2;
        return String.format("%d%d%d%d%d%d%d%d",b7,b6,b5,b4,b3,b2,b1,b0);
    }

    public static String byteToHex(long by)
    {
        long b = by;
        long B0 = b % 16; b=b/16;
        long B1 = b % 16;
        return String.format("%s%s",Long.toString(B1,16).toUpperCase(),Long.toString(B0,16).toUpperCase());
    }

    public static long getByte(long bytes, int pos)
    {
        long result=0;
        long b = bytes;
        for (int i=0; i <= pos; i++) {
            result = b % 256;
            b = b / 256;
        }
        return result;
    }
}
