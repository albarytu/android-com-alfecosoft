package com.alfecosoft.utils.DataConversion;

public class IP {
    public static String ipToBinary(long ip)
    {
        StringBuilder sb = new StringBuilder();
        long r = ip;
        long b1 = r % 256;
        r = (r / 256);
        long b2 = r % 256;
        r = (r / 256);
        long b3 = r % 256;
        r = (r / 256);
        long b4 = r % 256;
        return String.format("%s.%s.%s.%s",Byte.byteToBinary(b4),Byte.byteToBinary(b3),Byte.byteToBinary(b2),Byte.byteToBinary(b1));
    }

    public static String ipToString(long ip)
    {
        StringBuilder sb = new StringBuilder();
        long r = ip;
        long b1 = r % 256;
        r = (r / 256);
        long b2 = r % 256;
        r = (r / 256);
        long b3 = r % 256;
        r = (r / 256);
        long b4 = r % 256;
        return String.format("%d.%d.%d.%d",b4,b3,b2,b1);
    }
}
