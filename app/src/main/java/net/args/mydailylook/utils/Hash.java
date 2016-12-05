
package net.args.mydailylook.utils;

import java.security.MessageDigest;
import java.util.Locale;

public class Hash {

    public static byte[] SHA256(String text) {
        byte[] shaHash = new byte[32];

        if (text != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                md.update(text.getBytes(), 0, text.length());
                shaHash = md.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return shaHash;
    }

    public static byte[] SHA1(String text) {
        byte[] shaHash = new byte[32];

        if (text != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");

                md.update(text.getBytes(), 0, text.length());
                shaHash = md.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return shaHash;
    }

    public static byte[] MD5(String text) {
        byte[] shaHash = new byte[32];

        if (text != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");

                md.update(text.getBytes(), 0, text.length());
                shaHash = md.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return shaHash;
    }

    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;

            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }

                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }

        return buf.toString().toUpperCase(Locale.getDefault());
    }
    
}
