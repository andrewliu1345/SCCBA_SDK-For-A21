/*    */
package com.joesmate.sdk.util;


import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ToolFun {
    final static String TAG = ToolFun.class.toString();


    /*    */
    public static int asc_hex(byte[] asc, byte[] hex, int asclen) {
        /* 5 */
        String ss = new String(asc);
        /* 6 */
        int string_len = ss.length();
        /* 7 */
        int len = asclen;
        /* 8 */
        if (string_len % 2 == 1) {
            /* 9 */
            ss = "0" + ss;
            /* 10 */
            len++;
            /*    */
        }
        /* 12 */
        for (int i = 0; i < len; i++) {
            /* 13 */
            hex[i] = ((byte) Integer.parseInt(ss.substring(2 * i, 2 * i + 2), 16));
            /*    */
        }
        /* 15 */
        return 0;
        /*    */
    }

    /*    */
    public static String hex_split(byte[] hex, int blen) {

        String strsplit = "";
        byte[] asc = new byte[blen * 2];
        hex_asc(hex, asc, blen);
        String strasc = new String(asc);
        char[] charasc = strasc.toCharArray();
        for (char c : charasc) {
            strsplit += "3" + c;
        }
        String str = new String(hexStringToBytes(strsplit));
        return str;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String printHexString(byte[] b) {
        String a = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            a = a + hex;
        }

        return a;
    }

    /*    */
    public static int hex_asc(byte[] hex, byte[] asc, int blen)
    /*    */ {
        /* 20 */
        for (int i = 0; i < blen; i++)
        /*    */ {
            /* 22 */
            byte temp = (byte) (hex[i] & 0xF0);
            /* 23 */
            if (temp < 0)
            /*    */ {
				/* 25 */
                temp = (byte) (hex[i] & 0x70);
				/* 26 */
                temp = (byte) ((byte) (temp >> 4) + 8);
				/*    */
            }
			/*    */
            else {
				/* 29 */
                temp = (byte) (hex[i] >> 4);
				/*    */
            }
			/* 31 */
            if ((temp >= 0) && (temp <= 9)) {
				/* 32 */
                asc[(i * 2 + 0)] = ((byte) (temp + 48));
				/* 33 */
            } else if ((temp >= 10) && (temp <= 15)) {
				/* 34 */
                asc[(i * 2 + 0)] = ((byte) (temp + 65 - 10));
				/*    */
            } else {
				/* 36 */
                asc[(i * 2 + 0)] = 48;
				/*    */
            }
			/* 38 */
            temp = (byte) (hex[i] & 0xF);
			/* 39 */
            if ((temp >= 0) && (temp <= 9)) {
				/* 40 */
                asc[(i * 2 + 1)] = ((byte) (temp + 48));
				/* 41 */
            } else if ((temp >= 10) && (temp <= 15)) {
				/* 42 */
                asc[(i * 2 + 1)] = ((byte) (temp + 65 - 10));
				/*    */
            } else/* 44 */ asc[(i * 2 + 1)] = 48;
			/*    */
        }
		/* 46 */
        return 0;
		/*    */
    }

    /*    */
	/*    */
    public static int hex_asc(byte[] hex, int hexPos, byte[] asc, int blen) {
		/* 50 */
        for (int i = hexPos; i < hexPos + blen; i++)
		/*    */ {
			/* 52 */
            byte temp = (byte) (hex[i] & 0xF0);
			/* 53 */
            if (temp < 0)
			/*    */ {
				/* 55 */
                temp = (byte) (hex[i] & 0x70);
				/* 56 */
                temp = (byte) ((byte) (temp >> 4) + 8);
				/*    */
            }
			/*    */
            else {
				/* 59 */
                temp = (byte) (hex[i] >> 4);
				/*    */
            }
			/* 61 */
            if ((temp >= 0) && (temp <= 9)) {
				/* 62 */
                asc[((i - hexPos) * 2 + 0)] = ((byte) (temp + 48));
				/* 63 */
            } else if ((temp >= 10) && (temp <= 15)) {
				/* 64 */
                asc[((i - hexPos) * 2 + 0)] = ((byte) (temp + 65 - 10));
				/*    */
            } else {
				/* 66 */
                asc[((i - hexPos) * 2 + 0)] = 48;
				/*    */
            }
			/* 68 */
            temp = (byte) (hex[i] & 0xF);
			/* 69 */
            if ((temp >= 0) && (temp <= 9)) {
				/* 70 */
                asc[((i - hexPos) * 2 + 1)] = ((byte) (temp + 48));
				/* 71 */
            } else if ((temp >= 10) && (temp <= 15)) {
				/* 72 */
                asc[((i - hexPos) * 2 + 1)] = ((byte) (temp + 65 - 10));
				/*    */
            } else/* 74 */ asc[((i - hexPos) * 2 + 1)] = 48;
			/*    */
        }
		/* 76 */
        return 0;
		/*    */
    }

    /*    */
	/*    */
    public static byte cr_bcc(int len, byte[] data) {
        byte temp = 0;

        for (int i = 0; i < len; i++)
            temp ^= data[i];

        return temp;

    }

    public static void Dalpey(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            Log.e(TAG, "Daley: ", ex);
        }
    }

    /*    */
	/*    */
	/*    */
    public static void splitFun(byte[] usplitdata, byte ulen, byte[] splitdata, byte slen)
	/*    */ {
		/* 90 */
        int nI = 0;
        for (int nJ = 0; nI < ulen * 2; nJ++)
		/*    */ {
			/* 92 */
            splitdata[nI] = ((byte) (((usplitdata[nJ] & 0xF0) >> 4) + 48));
			/* 93 */
            splitdata[(nI + 1)] = ((byte) ((usplitdata[nJ] & 0xF) + 48));
            nI += 2;
			/*    */
        }
		/*    */
    }
	/*    */

    public static void saveBitmap(String path, Bitmap bm) {
        // Log.e(TAG, "保存图片");
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            // Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static byte[] PackArg(byte[] head, byte[]... params) {
        int head_len = head.length;
        int len = 0;
        byte[] tmp = new byte[2048];
        for (byte[] item :
                params) {
            tmp[len] = (byte) item.length;
            ++len;
            System.arraycopy(item, 0, tmp, len, item.length);
            len += item.length;
        }
        byte[] cmd = new byte[len + head_len];
        int pos = 0;
        System.arraycopy(head, 0, cmd, pos, head_len);
        pos += head_len;
        System.arraycopy(tmp, 0, cmd, pos, len);
        return cmd;
    }
}
