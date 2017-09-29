//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.joesmate.sdk.reader;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;


import java.util.Arrays;

import org.json.JSONObject;

import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.mtreader.BlueToothClass;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.ToolFun;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class KeyboardDev {
    private static final String TAG = "KeyboardDev";
    private static final KeyboardDev sInstance = new KeyboardDev();

//	static {
//		System.loadLibrary("mt3x32");
//		sInstance = null;
//	}
//
//	public static native int mt8desencrypt(byte[] var0, byte[] var1, int var2,
//			byte[] var3);
//
//	public static native int mt8desdecrypt(byte[] var0, byte[] var1, int var2,
//			byte[] var3);

    private KeyboardDev() {
    }

//    private static void ensureThreadLocked() {
//        if (sInstance == null) {
//            sInstance = new KeyboardDev();
//        }
//
//    }

    public static KeyboardDev getInstance() {
        Class var0 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
//            ensureThreadLocked();
            return sInstance;
        }
    }

    public String loadMasterKey(String masterKey, String checkValue) {
        JSONObject json = new JSONObject();
        LogMg.i("KeyboardDev", this + " LoadMasterkey,masterKey=" + masterKey
                + ",checkValue=" + checkValue);
        if (masterKey.isEmpty()) {

            return this.Check_kb_status(-80) + " errorCode:" + -80;

        } else if (masterKey.length() % 2 == 1) {

            return this.Check_kb_status(-82) + " errorCode:" + -82;

        } else {
            masterKey = masterKey.replaceAll(" ", "");
            checkValue = checkValue.replaceAll(" ", "");
            if (masterKey.length() > 120) {

                return this.Check_kb_status(-82) + " errorCode:" + -82;
            } else {
                byte[] masterKeyHex = new byte[masterKey.length() / 2];
                byte[] checkValueHex = new byte[checkValue.length() / 2];
                ToolFun.asc_hex(masterKey.getBytes(), masterKeyHex,
                        masterKey.length() / 2);
                ToolFun.asc_hex(checkValue.getBytes(), checkValueHex,
                        checkValue.length() / 2);
                int st = this.Download_EnMainkey(masterKeyHex,
                        masterKey.length() / 2, checkValueHex,
                        checkValue.length() / 2);
                LogMg.i("KeyboardDev", this + " Download_EnMainkey, return="
                        + st);
                if (st != 0) {
                    try {
                        return this.Check_kb_status(st) + " errorCode:" + st;
                    } catch (Exception var18) {
                        var18.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var17) {
                    var17.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this + " Check_KB_Cache, return=" + st2);
                if (st2 != 0) {
                    return this.Check_kb_status(st) + " errorCode:" + st;

                } else {
                    LogMg.i("KeyboardDev", this + " Check_KB_Cache, status="
                            + status[0]);
                    if (status[0] != 0) {
                        return this.Check_kb_status(status[0]) + " errorCode:"
                                + status[0];

                    } else {
                        return "0";
                    }
                }

            }
        }
    }

    /*** 主密钥明文 **/
    public void LoadMasterkey(ReturnListener listener, int masterIndex,
                              int mode, String masterKey) {
        LogMg.i("KeyboardDev", this + " LoadMasterkey, masterIndex="
                + masterIndex + ",mode=" + mode + ",masterKey=" + masterKey);
        if (masterKey.isEmpty()) {
            if (listener != null) {
                listener.onError(-80, this.Check_kb_status(-80));
            }

        } else if (masterKey.length() % 2 == 1) {
            if (listener != null) {
                listener.onError(-82, this.Check_kb_status(-82));
            }

        } else {
            masterKey = masterKey.replaceAll(" ", "");
            byte[] hexData = new byte[masterKey.length() / 2];
            ToolFun.asc_hex(masterKey.getBytes(), hexData,
                    masterKey.length() / 2);
            int st = this.Download_Mainkey(mode, 0, hexData,
                    masterKey.length() / 2);
            LogMg.i("KeyboardDev", this + " Download_Mainkey, return=" + st);
            if (st != 0) {
                if (listener != null) {
                    listener.onError(st, this.Check_kb_status(st));
                }
            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var10) {
                    var10.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this + " Check_KB_Cache, return=" + st2);
                if (st2 != 0) {
                    if (listener != null) {
                        listener.onError(st2, this.Check_kb_status(st2));
                    }
                } else {
                    LogMg.i("KeyboardDev", this + " Check_KB_Cache, status="
                            + status[0]);
                    if (status[0] != 0) {
                        if (listener != null) {
                            listener.onError(status[0],
                                    this.Check_kb_status(status[0]));
                        }
                    } else if (listener != null) {
                        listener.onSuccess("OK");
                    }
                }
            }

        }
    }

    public void LoadMackey(ReturnListener listener, int masterIndex,
                           String macKey) {
        LogMg.i("KeyboardDev", this + " LoadMackey, masterIndex=" + masterIndex
                + ",macKey=" + macKey);
        if (macKey.isEmpty()) {
            if (listener != null) {
                listener.onError(-80, this.Check_kb_status(-80));
            }

        } else if (macKey.length() % 2 == 1) {
            if (listener != null) {
                listener.onError(-82, this.Check_kb_status(-82));
            }

        } else {
            macKey = macKey.replaceAll(" ", "");
            byte[] hexData = new byte[macKey.length() / 2];
            ToolFun.asc_hex(macKey.getBytes(), hexData, macKey.length() / 2);
            int st = this.Download_Mackey(masterIndex, hexData,
                    macKey.length() / 2);
            LogMg.i("KeyboardDev", this + " Download_Mackey, return=" + st);
            if (st != 0) {
                if (listener != null) {
                    listener.onError(st, this.Check_kb_status(st));
                }
            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this + " Check_KB_Cache, return=" + st2);
                if (st2 != 0) {
                    if (listener != null) {
                        listener.onError(st2, this.Check_kb_status(st2));
                    }
                } else {
                    LogMg.i("KeyboardDev", this + " Check_KB_Cache, status="
                            + status[0]);
                    if (status[0] != 0) {
                        if (listener != null) {
                            listener.onError(status[0],
                                    this.Check_kb_status(status[0]));
                        }
                    } else if (listener != null) {
                        listener.onSuccess("OK");
                    }
                }
            }

        }
    }

    public void LoadWorkkey(ReturnListener listener, int masterIndex,
                            String workKey) {
        LogMg.i("KeyboardDev", this + " LoadMackey, masterIndex=" + masterIndex
                + ",workKey=" + workKey);
        if (workKey.isEmpty()) {
            if (listener != null) {
                listener.onError(-80, this.Check_kb_status(-80));
            }

        } else if (workKey.length() % 2 == 1) {
            if (listener != null) {
                listener.onError(-82, this.Check_kb_status(-82));
            }

        } else {
            workKey = workKey.replaceAll(" ", "");
            byte[] hexData = new byte[workKey.length() / 2];
            ToolFun.asc_hex(workKey.getBytes(), hexData, workKey.length() / 2);
            int st = this.Download_Workkey(0, hexData, workKey.length() / 2);
            LogMg.i("KeyboardDev", this + " Download_Workkey, return=" + st);
            if (st != 0) {
                if (listener != null) {
                    listener.onError(st, this.Check_kb_status(st));
                }
            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this + " Check_KB_Cache, return=" + st2);
                if (st2 != 0) {
                    if (listener != null) {
                        listener.onError(st2, this.Check_kb_status(st2));
                    }
                } else {
                    LogMg.i("KeyboardDev", this + " Check_KB_Cache, status="
                            + status[0]);
                    if (status[0] != 0) {
                        if (listener != null) {
                            listener.onError(status[0],
                                    this.Check_kb_status(status[0]));
                        }
                    } else if (listener != null) {

                        listener.onSuccess("OK");
                    }
                }
            }

        }
    }

    public String LoadPinkey(int masterIndex, String pinKey) {
        LogMg.i("KeyboardDev", this + " LoadMackey, masterIndex=" + masterIndex
                + ",pinKey=" + pinKey);
        if (pinKey.isEmpty()) {

            return -80 + this.Check_kb_status(-80);

        } else if (pinKey.length() % 2 == 1) {

            return -82 + this.Check_kb_status(-82);

        } else {
            pinKey = pinKey.replaceAll(" ", "");
            byte[] hexData = new byte[pinKey.length() / 2];
            ToolFun.asc_hex(pinKey.getBytes(), hexData, pinKey.length() / 2);
            int st = this.Download_Pinkey(0, hexData, pinKey.length() / 2);
            LogMg.i("KeyboardDev", this + " Download_Pinkey, return=" + st);
            if (st != 0) {

                return st + this.Check_kb_status(st);

            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this + " Check_KB_Cache, return=" + st2);
                if (st2 != 0) {

                    return st2 + this.Check_kb_status(st2);

                } else {
                    LogMg.i("KeyboardDev", this + " Check_KB_Cache, status="
                            + status[0]);
                    if (status[0] != 0) {

                        return status[0] + this.Check_kb_status(status[0]);

                    } else {
                        return "0";
                    }
                }
            }

        }
    }

    public void GetMac(final ReturnListener listener, int masterIndex,
                       final int mode, String macSrc) {
        Class var5 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetMac, masterIndex=" + masterIndex
                    + ",mode=" + mode + ",macSrc=" + macSrc);
            if (macSrc.isEmpty()) {
                if (listener != null) {
                    listener.onError(-81, this.Check_kb_status(-81));
                }

            } else {
                byte[] ascdata = new byte[macSrc.getBytes().length];
                final byte[] hexData = new byte[macSrc.getBytes().length / 2];
                System.arraycopy(macSrc.getBytes(), 0, ascdata, 0,
                        macSrc.getBytes().length);
                ToolFun.asc_hex(ascdata, hexData, ascdata.length / 2);
                int st1 = this.Close_KB();
                LogMg.i("KeyboardDev", this + " GetMac, Close_KB return ="
                        + st1);
                if (st1 != 0) {
                    if (listener != null) {
                        listener.onError(st1, this.Check_kb_status(st1));
                    }

                } else {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var10) {
                        var10.printStackTrace();
                    }

                    (new Thread(new Runnable() {
                        public void run() {
                            Class var1 = KeyboardDev.class;
                            synchronized (KeyboardDev.class) {
                                int len = hexData.length;
                                int st = KeyboardDev.this.Get_KB_MAC(0, mode,
                                        hexData, len);
                                LogMg.i("KeyboardDev", this
                                        + " GetMac, Get_KB_MAC return=" + st);
                                if (st != 0) {
                                    if (listener != null) {
                                        listener.onError(st, KeyboardDev.this
                                                .Check_kb_status(st));
                                    }
                                } else {
                                    try {
                                        Thread.sleep(100L);
                                    } catch (InterruptedException var10) {
                                        var10.printStackTrace();
                                    }

                                    int[] status = new int[1];
                                    int[] datalen = new int[1];
                                    st = KeyboardDev.this.Check_KB_Cache(
                                            status, datalen);
                                    LogMg.i("KeyboardDev", this
                                            + " GetMac, Check_KB_Cache return="
                                            + st);
                                    if (st == 0) {
                                        if (status[0] != 0) {
                                            if (listener != null) {
                                                listener.onError(
                                                        status[0],
                                                        KeyboardDev.this
                                                                .Check_kb_status(status[0]));
                                            }

                                            KeyboardDev.this.Close_KB();
                                            return;
                                        }

                                        try {
                                            Thread.sleep(200L);
                                        } catch (InterruptedException var9) {
                                            var9.printStackTrace();
                                        }

                                        byte[] readdata = new byte[200];
                                        st = KeyboardDev.this.Read_KB_Buffer(
                                                datalen[0], readdata);
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetMac, Read_KB_Buffer return="
                                                        + st);
                                        if (st == 0) {
                                            byte[] macdata = new byte[datalen[0]];
                                            System.arraycopy(readdata, 0,
                                                    macdata, 0, datalen[0]);
                                            if (listener != null) {
                                                byte[] asc = new byte[macdata.length * 2];
                                                ToolFun.hex_asc(macdata, asc,
                                                        macdata.length);
                                                listener.onSuccess(new String(
                                                        asc));
                                            }
                                        }
                                    }
                                }

                                KeyboardDev.this.Close_KB();
                            }
                        }
                    })).start();
                }
            }
        }
    }

    public void GetTDEA(final ReturnListener listener, int masterIndex,
                        String inputData0) {
        Class var4 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetTDEA, masterIndex="
                    + masterIndex + ",inputData=" + inputData0);
            final String inputData = inputData0.replaceAll(" ", "");
            if (inputData.isEmpty()) {
                if (listener != null) {
                    listener.onError(-81, this.Check_kb_status(-81));
                }

            } else if (inputData.length() % 2 == 1) {
                if (listener != null) {
                    listener.onError(-83, this.Check_kb_status(-83));
                }

            } else {
                final byte[] hexData = new byte[inputData.length() / 2];
                ToolFun.asc_hex(inputData.getBytes(), hexData,
                        inputData.length() / 2);
                (new Thread(new Runnable() {
                    public void run() {
                        Class var1 = KeyboardDev.class;
                        synchronized (KeyboardDev.class) {
                            int st = KeyboardDev.this.Close_KB();
                            LogMg.i("KeyboardDev", this
                                    + " GetTDEA, Close_KB return=" + st);
                            if (st != 0) {
                                if (listener != null) {
                                    listener.onError(st, KeyboardDev.this
                                            .Check_kb_status(st));
                                }

                            } else {
                                st = KeyboardDev.this.Get_KB_TDEA(0, hexData,
                                        inputData.length() / 2);
                                LogMg.i("KeyboardDev", this
                                        + " GetTDEA, Get_KB_TDEA return=" + st);
                                if (st == 0) {
                                    try {
                                        Thread.sleep(100L);
                                    } catch (InterruptedException var9) {
                                        var9.printStackTrace();
                                    }

                                    int[] status = new int[1];
                                    int[] datalen = new int[1];
                                    st = KeyboardDev.this.Check_KB_Cache(
                                            status, datalen);
                                    LogMg.i("KeyboardDev",
                                            this
                                                    + " GetTDEA, Check_KB_Cache return="
                                                    + st);
                                    if (st == 0) {
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetTDEA, Check_KB_Cache status="
                                                        + status[0]);
                                        if (status[0] != 0) {
                                            if (listener != null) {
                                                listener.onError(
                                                        status[0],
                                                        KeyboardDev.this
                                                                .Check_kb_status(status[0]));
                                            }

                                            KeyboardDev.this.Close_KB();
                                            return;
                                        }

                                        try {
                                            Thread.sleep(200L);
                                        } catch (InterruptedException var8) {
                                            var8.printStackTrace();
                                        }

                                        byte[] readdata = new byte[1024];
                                        st = KeyboardDev.this.Read_KB_Buffer(
                                                datalen[0], readdata);
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetTDEA, Read_KB_Buffer return="
                                                        + st);
                                        if (st == 0) {
                                            byte[] outdata = new byte[datalen[0]];
                                            System.arraycopy(readdata, 0,
                                                    outdata, 0, datalen[0]);
                                            if (listener != null) {
                                                byte[] asc = new byte[outdata.length * 2];
                                                ToolFun.hex_asc(outdata, asc,
                                                        outdata.length);
                                                listener.onSuccess(new String(
                                                        asc));
                                            }
                                        }
                                    } else if (listener != null) {
                                        listener.onError(st, KeyboardDev.this
                                                .Check_kb_status(st));
                                    }
                                } else if (listener != null) {
                                    listener.onError(st, KeyboardDev.this
                                            .Check_kb_status(st));
                                }

                                KeyboardDev.this.Close_KB();
                            }
                        }
                    }
                })).start();
            }
        }
    }

    public String GetPIN(int masterIndex, final String CardNo, final int timeout) {
        Class var5 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetPIN, masterIndex=" + masterIndex
                    + ",CardNo=" + CardNo + ",timeout=" + timeout);
            int st = this.Open_KB();
            LogMg.i("KeyboardDev", this + " GetPIN, Open_KB return=" + st);
            if (st != 0) {

                return st + "密码键盘打开失败";

            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }

                final long time1 = System.currentTimeMillis();

                Class var1 = KeyboardDev.class;
                synchronized (KeyboardDev.class) {
                    byte[] pinResNull = new byte[8];
                    Arrays.fill(pinResNull, (byte) -1);
                    int pinLen = 0;
                    String psCardNo = "";
                    String pattern = "0000000000000000";
                    if (CardNo.length() > 0) {
                        int hexCardNo = CardNo.length() - 13;
                        if (hexCardNo < 0) {
                            hexCardNo = 0;
                        }

                        psCardNo = CardNo.substring(hexCardNo,
                                CardNo.length() - 1);
                    }

                    psCardNo = pattern.substring(0,
                            pattern.length() - psCardNo.length())
                            + psCardNo;
                    byte[] hexCardNo1 = new byte[10];
                    ToolFun.asc_hex(psCardNo.getBytes(), hexCardNo1,
                            psCardNo.length() / 2);
                    int pressRes = -1;

                    while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                        pressRes = KeyboardDev.this.pressKeyNum();
                        if (pressRes >= 0) {
                            break;
                        }
                    }

                    LogMg.i("KeyboardDev", this
                            + " GetPIN, pressKeyNum return=" + pressRes);
                    if (pressRes == 0) {
                        KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                        int st1 = KeyboardDev.this.Get_ClientPreInfo(0,
                                hexCardNo1, CardNo.length() / 2);
                        LogMg.i("KeyboardDev", this
                                + " GetPIN, Get_ClientPreInfo return=" + st1);
                        if (st1 == 0) {
                            int[] status = new int[1];
                            int[] datalen = new int[1];

                            try {
                                Thread.sleep(200L);
                            } catch (InterruptedException var15) {
                                var15.printStackTrace();
                            }

                            st1 = KeyboardDev.this.Check_KB_Cache(status,
                                    datalen);
                            LogMg.i("KeyboardDev", this
                                    + " GetPIN, Check_KB_Cache return=" + st1);
                            if (st1 == 0) {
                                if (status[0] != 0 && status[0] != 81) {

                                    KeyboardDev.this.Close_KB();
                                    Log.e("mima键盘",
                                            status[0]
                                                    + KeyboardDev.this
                                                    .Check_kb_status(status[0]));
                                    return status[0]
                                            + KeyboardDev.this
                                            .Check_kb_status(status[0]);
                                }

                                try {
                                    Thread.sleep(100L);
                                } catch (InterruptedException var14) {
                                    var14.printStackTrace();
                                }

                                byte[] readdata = new byte[200];
                                st1 = KeyboardDev.this.Read_KB_Buffer(
                                        datalen[0], readdata);
                                LogMg.i("KeyboardDev", this
                                        + " GetPIN, Read_KB_Buffer return="
                                        + st1);
                                if (st1 == 0) {
                                    if (datalen[0] > 0) {
                                        pinLen = datalen[0];
                                    }

                                    byte[] pinRes = new byte[pinLen];
                                    System.arraycopy(readdata, 0, pinRes, 0,
                                            pinLen);
                                    byte[] asc = new byte[pinLen * 2];
                                    ToolFun.hex_asc(pinRes, asc, pinLen);

                                    KeyboardDev.this.Close_KB();

                                    return "ok" + new String(asc);
                                }
                            }
                        }
                    } else if (pressRes > 0) {

                        KeyboardDev.this.Close_KB();
                        KeyboardDev.this.Set_LCD_LIGHT((byte) 0);

                        return String.valueOf(pressRes);
                    }
                    KeyboardDev.this.Close_KB();
                    KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                    return "FFFFFFFFFFFFFFFF";

                }

            }
        }
    }

    public void GetPINSM2(final ReturnListener listener, final String CardNo,
                          final int timeout) {
        Class var4 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetPINSM2, CardNo=" + CardNo
                    + ",timeout=" + timeout);
            int st = this.Open_KB();
            if (st != 0) {
                if (listener != null) {
                    listener.onError(st, "Open KB error");
                }

            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }

                final long time1 = System.currentTimeMillis();
                (new Thread(new Runnable() {
                    public void run() {
                        Class var1 = KeyboardDev.class;
                        synchronized (KeyboardDev.class) {
                            byte[] pinResNull = new byte[8];
                            Arrays.fill(pinResNull, (byte) -1);
                            int pinLen = 0;
                            String psCardNo = "";
                            String pattern = "0000000000000000";
                            if (CardNo.length() > 0) {
                                int hexCardNo = CardNo.length() - 13;
                                if (hexCardNo < 0) {
                                    hexCardNo = 0;
                                }

                                psCardNo = CardNo.substring(hexCardNo,
                                        CardNo.length() - 1);
                            }

                            psCardNo = pattern.substring(0, pattern.length()
                                    - psCardNo.length())
                                    + psCardNo;
                            byte[] hexCardNo1 = new byte[10];
                            ToolFun.asc_hex(psCardNo.getBytes(), hexCardNo1,
                                    psCardNo.length() / 2);
                            int pressRes = -1;

                            while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                pressRes = KeyboardDev.this.pressKeyNum();
                                if (pressRes >= 0) {
                                    break;
                                }
                            }

                            LogMg.i("KeyboardDev", this
                                    + " GetPIN, pressKeyNum return=" + pressRes);
                            if (pressRes == 0) {
                                KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                                int st1 = KeyboardDev.this.Get_ClientPreInfo(
                                        48, hexCardNo1, CardNo.length() / 2);
                                LogMg.i("KeyboardDev", this
                                        + " GetPIN, Get_ClientPreInfo return="
                                        + st1);
                                if (st1 == 0) {
                                    int[] status = new int[1];
                                    int[] datalen = new int[1];

                                    try {
                                        Thread.sleep(500L);
                                    } catch (InterruptedException var15) {
                                        var15.printStackTrace();
                                    }

                                    st1 = KeyboardDev.this.Check_KB_Cache(
                                            status, datalen);
                                    LogMg.i("KeyboardDev", this
                                            + " GetPIN, Check_KB_Cache return="
                                            + st1);
                                    if (st1 == 0) {
                                        if (status[0] != 0 && status[0] != 81) {
                                            if (listener != null) {
                                                listener.onError(
                                                        status[0],
                                                        KeyboardDev.this
                                                                .Check_kb_status(status[0]));
                                            }

                                            KeyboardDev.this.Close_KB();
                                            return;
                                        }

                                        try {
                                            Thread.sleep(100L);
                                        } catch (InterruptedException var14) {
                                            var14.printStackTrace();
                                        }

                                        byte[] readdata = new byte[200];
                                        st1 = KeyboardDev.this.Read_KB_Buffer(
                                                datalen[0], readdata);
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetPIN, Read_KB_Buffer return="
                                                        + st1);
                                        if (st1 == 0) {
                                            if (datalen[0] > 0) {
                                                pinLen = datalen[0];
                                            }

                                            byte[] pinRes = new byte[pinLen];
                                            System.arraycopy(readdata, 0,
                                                    pinRes, 0, pinLen);
                                            if (listener != null) {
                                                byte[] asc = new byte[pinLen * 2];
                                                ToolFun.hex_asc(pinRes, asc,
                                                        pinLen);
                                                listener.onSuccess(new String(
                                                        asc));
                                            }

                                            KeyboardDev.this.Close_KB();
                                            return;
                                        }
                                    }
                                } else if (pressRes > 0) {
                                    if (listener != null) {
                                        listener.onError(
                                                pressRes,
                                                KeyboardDev.this
                                                        .Check_kb_status(pressRes));
                                    }

                                    KeyboardDev.this.Close_KB();
                                    KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                                    return;
                                }
                            }

                            if (listener != null) {
                                listener.onSuccess("FFFFFFFFFFFFFFFF");
                            }

                            KeyboardDev.this.Close_KB();
                            KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                        }
                    }
                })).start();
            }
        }
    }

    public void LoadSM2Publickey(ReturnListener listener, String SM2Publickey) {
        LogMg.i("KeyboardDev", this + " LoadSM2Publickey, SM2Publickey="
                + SM2Publickey);
        if (!SM2Publickey.isEmpty() && SM2Publickey.length() == 128) {
            byte[] hexData = new byte[SM2Publickey.length() / 2];
            ToolFun.asc_hex(SM2Publickey.getBytes(), hexData,
                    SM2Publickey.length() / 2);
            int st = this.Download_SM2Publickey(hexData, 64);
            LogMg.i("KeyboardDev", this
                    + " LoadSM2Publickey, Download_SM2Publickey return=" + st);
            if (st != 0) {
                if (listener != null) {
                    listener.onError(st, this.Check_kb_status(st));
                }
            } else {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var8) {
                    var8.printStackTrace();
                }

                int[] status = new int[1];
                int[] datalen = new int[1];
                int st2 = this.Check_KB_Cache(status, datalen);
                LogMg.i("KeyboardDev", this
                        + " LoadSM2Publickey, Check_KB_Cache return=" + st2);
                if (st2 != 0) {
                    if (listener != null) {
                        listener.onError(st2, this.Check_kb_status(st2));
                    }
                } else {
                    LogMg.i("KeyboardDev", this
                            + " LoadSM2Publickey, Check_KB_Cache status="
                            + status[0]);
                    if (status[0] != 0) {
                        if (listener != null) {
                            listener.onError(status[0],
                                    this.Check_kb_status(status[0]));
                        }
                    } else if (listener != null) {
                        listener.onSuccess("OK");
                    }
                }
            }

        } else {
            if (listener != null) {
                listener.onError(-80, this.Check_kb_status(-80));
            }

        }
    }

    public void GetSM3HashValue(final ReturnListener listener,
                                final String dataSrc) {
        Class var3 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetSM3HashValue, dataSrc="
                    + dataSrc);
            if (dataSrc.isEmpty()) {
                if (listener != null) {
                    listener.onError(-81, this.Check_kb_status(-81));
                }

            } else {
                final byte[] hexData = new byte[dataSrc.length() / 2];
                ToolFun.asc_hex(dataSrc.getBytes(), hexData,
                        dataSrc.length() / 2);
                int st1 = this.Close_KB();
                LogMg.i("KeyboardDev", this
                        + " GetSM3HashValue, Close_KB return=" + st1);
                if (st1 != 0) {
                    if (listener != null) {
                        listener.onError(st1, "Close KB ERR");
                    }

                } else {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var7) {
                        var7.printStackTrace();
                    }

                    (new Thread(new Runnable() {
                        public void run() {
                            Class var1 = KeyboardDev.class;
                            synchronized (KeyboardDev.class) {
                                int st = KeyboardDev.this.SM3_HashValue(
                                        hexData, dataSrc.length() / 2);
                                LogMg.i("KeyboardDev",
                                        this
                                                + " GetSM3HashValue, SM3_HashValue return="
                                                + st);
                                if (st != 0) {
                                    if (listener != null) {
                                        listener.onError(st, KeyboardDev.this
                                                .Check_kb_status(st));
                                    }
                                } else {
                                    try {
                                        Thread.sleep(100L);
                                    } catch (InterruptedException var9) {
                                        var9.printStackTrace();
                                    }

                                    int[] status = new int[1];
                                    int[] datalen = new int[1];
                                    st = KeyboardDev.this.Check_KB_Cache(
                                            status, datalen);
                                    LogMg.i("KeyboardDev",
                                            this
                                                    + " GetSM3HashValue, Check_KB_Cache return="
                                                    + st);
                                    if (st == 0) {
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetSM3HashValue, Check_KB_Cache status="
                                                        + status[0]);
                                        if (status[0] != 0) {
                                            if (listener != null) {
                                                listener.onError(
                                                        status[0],
                                                        KeyboardDev.this
                                                                .Check_kb_status(status[0]));
                                            }

                                            KeyboardDev.this.Close_KB();
                                            return;
                                        }

                                        try {
                                            Thread.sleep(200L);
                                        } catch (InterruptedException var8) {
                                            var8.printStackTrace();
                                        }

                                        byte[] readdata = new byte[200];
                                        st = KeyboardDev.this.Read_KB_Buffer(
                                                datalen[0], readdata);
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetSM3HashValue, Read_KB_Buffer return="
                                                        + st);
                                        if (st == 0) {
                                            byte[] macdata = new byte[datalen[0]];
                                            System.arraycopy(readdata, 0,
                                                    macdata, 0, datalen[0]);
                                            if (listener != null) {
                                                byte[] asc = new byte[datalen[0] * 2];
                                                ToolFun.hex_asc(macdata, asc,
                                                        datalen[0]);
                                                listener.onSuccess(new String(
                                                        asc));
                                            }
                                        }
                                    }
                                }

                                KeyboardDev.this.Close_KB();
                            }
                        }
                    })).start();
                }
            }
        }
    }

    public String GetPINPlain(final int timeout) {

        Class var3 = KeyboardDev.class;
        synchronized (KeyboardDev.class) {
            LogMg.i("KeyboardDev", this + " GetPINPlain, timeout=" + timeout);
            final byte[] Masterkey = new byte[]{18, 52, 86, 120, 18, 52, 86,
                    120};
            int st = this.DownMainKey(0, (byte) 28, Masterkey, (int) 8);
            LogMg.i("KeyboardDev", this + " GetPINPlain, DownMainKey return="
                    + st);
            if (st != 0) {
                // Log.e("面膜1",st+this.Check_kb_status(st));
                return st + this.Check_kb_status(st);

            } else {
                byte[] Pinkey = new byte[]{115, -127, -119, 19, -98, 11, 102,
                        -31};
                st = this.DownPinkey((byte) 28, Pinkey, (int) 8);
                LogMg.i("KeyboardDev", this
                        + " GetPINPlain, DownPinkey return=" + st);
                if (st != 0) {
                    // Log.e("面膜2",st+this.Check_kb_status(st));
                    return st + this.Check_kb_status(st);

                } else {
                    st = this.Open_KB();
                    LogMg.i("KeyboardDev", this
                            + " GetPINPlain, Open_KB return=" + st);
                    if (st != 0) {
                        // Log.e("面膜3",st+this.Check_kb_status(st));
                        return st + this.Check_kb_status(st);

                    } else {
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException var9) {
                            var9.printStackTrace();
                        }

                        final long time1 = System.currentTimeMillis();

                        Class var1 = KeyboardDev.class;
                        synchronized (KeyboardDev.class) {
                            byte[] pinResNull = new byte[8];
                            Arrays.fill(pinResNull, (byte) 70);
                            int pinLen = 0;
                            byte[] hexCardNo = new byte[8];
                            int pressRes = -1;

                            while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                pressRes = KeyboardDev.this.pressKeyNum();
                                if (pressRes >= 0) {
                                    break;
                                }
                            }

                            LogMg.i("KeyboardDev", this
                                    + " GetPINPlain, pressKeyNum return="
                                    + pressRes);
                            if (pressRes == 0) {
                                KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                                int st1 = KeyboardDev.this.Get_ClientPreInfo(
                                        28, hexCardNo, 8);
                                LogMg.i("KeyboardDev",
                                        this
                                                + " GetPINPlain, Get_ClientPreInfo return="
                                                + st1);
                                if (st1 == 0) {
                                    int[] status = new int[1];
                                    int[] datalen = new int[1];

                                    try {
                                        Thread.sleep(200L);
                                    } catch (InterruptedException var19) {
                                        var19.printStackTrace();
                                    }

                                    st1 = KeyboardDev.this.Check_KB_Cache(
                                            status, datalen);
                                    LogMg.i("KeyboardDev",
                                            this
                                                    + " GetPINPlain, Check_KB_Cache return="
                                                    + st1);
                                    if (st1 == 0) {
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetPINPlain, Check_KB_Cache status="
                                                        + status[0]);
                                        if (status[0] != 0 && status[0] != 81) {
                                            int i = status[0];
                                            String check = Check_kb_status(status[0]);

                                            KeyboardDev.this.Close_KB();
                                            // Log.e("面膜4",i+check);
                                            return i + check;
                                        }

                                        try {
                                            Thread.sleep(100L);
                                        } catch (InterruptedException var18) {
                                            var18.printStackTrace();
                                        }

                                        byte[] readdata = new byte[200];
                                        st1 = KeyboardDev.this.Read_KB_Buffer(
                                                datalen[0], readdata);
                                        LogMg.i("KeyboardDev",
                                                this
                                                        + " GetPINPlain, Read_KB_Buffer return="
                                                        + st1);
                                        if (st1 == 0) {
                                            if (datalen[0] > 0) {
                                                pinLen = datalen[0];
                                            }

                                            byte[] pinRes = new byte[pinLen];
                                            System.arraycopy(readdata, 0,
                                                    pinRes, 0, pinLen);
                                            byte[] key = new byte[128];
                                            System.arraycopy(Masterkey, 0, key,
                                                    0, 8);
                                            byte[] szsource = new byte[512];
                                            System.arraycopy(pinRes, 0,
                                                    szsource, 0, pinLen);
                                            byte[] szdest = new byte[512];
                                            BlueToothClass.mt8desdecrypt(key,
                                                    szsource, pinLen, szdest);
                                            byte pLen = szdest[0];
                                            byte[] pKeys = new byte[8];
                                            System.arraycopy(szdest, 1, pKeys,
                                                    0, 7);
                                            byte[] pAscKeys = new byte[20];
                                            ToolFun.hex_asc(pKeys, pAscKeys, 7);
                                            byte[] resKey = new byte[pLen];
                                            System.arraycopy(pAscKeys, 0,
                                                    resKey, 0, pLen);
                                            // 成功

                                            return "ok" + new String(resKey);

                                        }
                                    }
                                }
                            } else if (pressRes > 0) {
                                KeyboardDev.this.Close_KB();
                                KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                                // Log.e("面膜6121",pressRes+KeyboardDev.this.Check_kb_status(pressRes));
                                return String.valueOf(pressRes);

                            }
                            KeyboardDev.this.Close_KB();
                            KeyboardDev.this.Set_LCD_LIGHT((byte) 0);
                            // Log.e("面膜7",new String(pinResNull));
                            return new String(pinResNull);

                        }
                    }
                }
            }
        }
    }

    // public void GetPINPlain(final ReturnListener listener, final int timeout)
    // {
    // Class var3 = KeyboardDev.class;
    // synchronized(KeyboardDev.class) {
    // LogMg.i("KeyboardDev", this + " GetPINPlain, timeout=" + timeout);
    // final byte[] Masterkey = new byte[]{18, 52, 86, 120, 18, 52, 86, 120};
    // int st = this.DownMainKey(0, (byte)28, Masterkey,(int) 8);
    // LogMg.i("KeyboardDev", this + " GetPINPlain, DownMainKey return=" + st);
    // if(st != 0) {
    // if(listener != null) {
    // listener.onError(st, this.Check_kb_status(st));
    // }
    //
    // } else {
    // byte[] Pinkey = new byte[]{115, -127, -119, 19, -98, 11, 102, -31};
    // st = this.DownPinkey((byte)28, Pinkey,(int) 8);
    // LogMg.i("KeyboardDev", this + " GetPINPlain, DownPinkey return=" + st);
    // if(st != 0) {
    // if(listener != null) {
    // listener.onError(st, this.Check_kb_status(st));
    // }
    //
    // } else {
    // st = this.Open_KB();
    // LogMg.i("KeyboardDev", this + " GetPINPlain, Open_KB return=" + st);
    // if(st != 0) {
    // if(listener != null) {
    // listener.onError(st, "Open KB error");
    // }
    //
    // } else {
    // try {
    // Thread.sleep(100L);
    // } catch (InterruptedException var9) {
    // var9.printStackTrace();
    // }
    //
    // final long time1 = System.currentTimeMillis();
    // (new Thread(new Runnable() {
    // public void run() {
    // Class var1 = KeyboardDev.class;
    // synchronized(KeyboardDev.class) {
    // byte[] pinResNull = new byte[8];
    // Arrays.fill(pinResNull,(byte) 70);
    // int pinLen = 0;
    // byte[] hexCardNo = new byte[8];
    // int pressRes = -1;
    //
    // while(System.currentTimeMillis() - time1 < (long)(timeout * 1000)) {
    // pressRes = KeyboardDev.this.pressKeyNum();
    // if(pressRes >= 0) {
    // break;
    // }
    // }
    //
    // LogMg.i("KeyboardDev", this + " GetPINPlain, pressKeyNum return=" +
    // pressRes);
    // if(pressRes == 0) {
    // KeyboardDev.this.Set_LCD_LIGHT((byte)0);
    // int st1 = KeyboardDev.this.Get_ClientPreInfo(28, hexCardNo, 8);
    // LogMg.i("KeyboardDev", this + " GetPINPlain, Get_ClientPreInfo return=" +
    // st1);
    // if(st1 == 0) {
    // int[] status = new int[1];
    // int[] datalen = new int[1];
    //
    // try {
    // Thread.sleep(200L);
    // } catch (InterruptedException var19) {
    // var19.printStackTrace();
    // }
    //
    // st1 = KeyboardDev.this.Check_KB_Cache(status, datalen);
    // LogMg.i("KeyboardDev", this + " GetPINPlain, Check_KB_Cache return=" +
    // st1);
    // if(st1 == 0) {
    // LogMg.i("KeyboardDev", this + " GetPINPlain, Check_KB_Cache status=" +
    // status[0]);
    // if(status[0] != 0 && status[0] != 81) {
    // if(listener != null) {
    // listener.onError(status[0], KeyboardDev.this.Check_kb_status(status[0]));
    // }
    //
    // KeyboardDev.this.Close_KB();
    // return;
    // }
    //
    // try {
    // Thread.sleep(100L);
    // } catch (InterruptedException var18) {
    // var18.printStackTrace();
    // }
    //
    // byte[] readdata = new byte[200];
    // st1 = KeyboardDev.this.Read_KB_Buffer(datalen[0], readdata);
    // LogMg.i("KeyboardDev", this + " GetPINPlain, Read_KB_Buffer return=" +
    // st1);
    // if(st1 == 0) {
    // if(datalen[0] > 0) {
    // pinLen = datalen[0];
    // }
    //
    // byte[] pinRes = new byte[pinLen];
    // System.arraycopy(readdata, 0, pinRes, 0, pinLen);
    // byte[] key = new byte[128];
    // System.arraycopy(Masterkey, 0, key, 0, 8);
    // byte[] szsource = new byte[512];
    // System.arraycopy(pinRes, 0, szsource, 0, pinLen);
    // byte[] szdest = new byte[512];
    // KeyboardDev.mt8desdecrypt(key, szsource, pinLen, szdest);
    // byte pLen = szdest[0];
    // byte[] pKeys = new byte[8];
    // System.arraycopy(szdest, 1, pKeys, 0, 7);
    // byte[] pAscKeys = new byte[20];
    // ToolFun.hex_asc(pKeys, pAscKeys, 7);
    // byte[] resKey = new byte[pLen];
    // System.arraycopy(pAscKeys, 0, resKey, 0, pLen);
    // listener.onSuccess(new String(resKey));
    // KeyboardDev.this.Close_KB();
    // return;
    // }
    // }
    // }
    // } else if(pressRes > 0) {
    // if(listener != null) {
    // listener.onError(pressRes, KeyboardDev.this.Check_kb_status(pressRes));
    // }
    //
    // KeyboardDev.this.Close_KB();
    // KeyboardDev.this.Set_LCD_LIGHT((byte)0);
    // return;
    // }
    //
    // listener.onSuccess(new String(pinResNull));
    // KeyboardDev.this.Close_KB();
    // KeyboardDev.this.Set_LCD_LIGHT((byte)0);
    // }
    // }
    // })).start();
    // }
    // }
    // }
    // }
    // }

    public int SetUpgrade() {
        byte[] cmd = new byte[]{0, -47};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("KeyboardDev", this + " SetUpgrade return " + st);
        return st;
    }

    private int DownPinkey(byte masterIndex, byte[] Workkey, int keylen) {
        int st = this.Download_Pinkey(masterIndex, Workkey, keylen);
        if (st == 0) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }

            int[] status = new int[1];
            int[] datalen = new int[1];
            int st2 = this.Check_KB_Cache(status, datalen);
            if (st2 != 0) {
                return st2;
            }

            if (status[0] != 0) {
                return status[0];
            }
        }

        return st;
    }

    private int DownMainKey(int mode, byte keyNo, byte[] keyData, int keyLen) {
        int st = this.Download_Mainkey(mode, keyNo, keyData, keyLen);
        if (st == 0) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException var9) {
                var9.printStackTrace();
            }

            int[] status = new int[1];
            int[] datalen = new int[1];
            int st2 = this.Check_KB_Cache(status, datalen);
            if (st2 != 0) {
                return st2;
            }

            if (status[0] != 0) {
                return status[0];
            }
        }

        return st;
    }

    protected int SM3_HashValue(byte[] inputData, int dataLen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] revLen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) dataLen;
        Send_Data[3] = -75;
        Send_Data[4] = -1;
        System.arraycopy(inputData, 0, Send_Data, 5, dataLen);
        int st = this.PinPadChannel((byte) 0, 5 + dataLen, Send_Data, revLen,
                Recv_Data);
        return st;
    }

    private int Download_SM2Publickey(byte[] sM2Publickey, int keylen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] revLen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) keylen;
        Send_Data[3] = -87;
        Send_Data[4] = -1;
        System.arraycopy(sM2Publickey, 0, Send_Data, 5, keylen);
        int st = this.PinPadChannel((byte) 0, 5 + keylen, Send_Data, revLen,
                Recv_Data);
        return st;
    }

    protected int Get_ClientPreInfo(int masterIndex, byte[] cardNo,
                                    int cardNoLen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (cardNoLen + 1);
        Send_Data[3] = -93;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        System.arraycopy(cardNo, 0, Send_Data, 6, cardNoLen);
        int st = this.PinPadChannel((byte) 0, 6 + cardNoLen, Send_Data, relen,
                Recv_Data);
        return st;
    }

    protected int pressKeyNum() {
        LogMg.i("KeyboardDev", this + " pressKeyNum");
        int st2 = this.Get_ClientPreNum();
        LogMg.i("KeyboardDev", this + " pressKeyNum, Get_ClientPreNum return="
                + st2);
        if (st2 == 0) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException var5) {
                var5.printStackTrace();
            }

            byte[] readdata = new byte[200];
            int st1 = this.Read_KB_Buffer(2, readdata);
            LogMg.i("KeyboardDev", this
                    + " pressKeyNum, Read_KB_Buffer return=" + st1);
            if (st1 == 0) {
                this.Set_LCD_LIGHT(readdata[0]);
                if (readdata[0] > 12) {
                    return 97;
                }

                byte keyStatus = readdata[1];
                System.out
                        .println("keyStatus-1:"
                                + (keyStatus - 1)
                                + "//////////////////////////////////////////////////////////////////////");
                if (keyStatus == 1) {
                    if (readdata[0] >= 6 && readdata[0] <= 12) {
                        return 0;
                    }

                    return 97;
                }

                if (keyStatus == 2) {
                    return 98;
                }
            }
        }

        return -1;
    }

    private int Set_LCD_LIGHT(byte lightnum) {
        LogMg.i("KeyboardDev", this + " Set_LCD_LIGHT lightnum=" + lightnum);
        if (lightnum > 12) {
            return -72;
        } else {
            byte[] Send_Data = new byte[]{-64, 14, lightnum};
            MT3YCmdMan cmdMan = new MT3YCmdMan(Send_Data);
            int st = cmdMan.SendRecv();
            LogMg.i("KeyboardDev", this + " Set_LCD_LIGHT return=" + st);
            return st;
        }
    }

    private int Get_ClientPreNum() {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = 0;
        Send_Data[3] = -94;
        Send_Data[4] = -1;
        int st = this.PinPadChannel((byte) 0, 5, Send_Data, relen, Recv_Data);
        return st;
    }

    private int Open_KB() {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = 0;
        Send_Data[3] = -96;
        Send_Data[4] = -1;
        int st = this.PinPadChannel((byte) 0, 5, Send_Data, relen, Recv_Data);
        return st;
    }

    protected int Get_KB_TDEA(int masterIndex, byte[] inputdata, int datalen) {
        int[] nRLen = new int[1];
        byte[] Send_Data = new byte[datalen + 20];
        byte[] Recv_Data = new byte[200];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (datalen + 1);
        Send_Data[3] = -62;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        System.arraycopy(inputdata, 0, Send_Data, 6, datalen);
        int st = this.PinPadChannel((byte) 0, 6 + datalen, Send_Data, nRLen,
                Recv_Data);
        return st;
    }

    protected int Read_KB_Buffer(int readlen, byte[] readdata) {
        byte[] Send_Data = new byte[readlen + 200];
        byte[] Recv_Data = new byte[readlen + 200];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = 0;
        Send_Data[3] = -14;
        Send_Data[4] = -1;
        int st = this.PinPadChannel((byte) 0, 5, Send_Data, relen, Recv_Data);
        if (st != 0) {
            return st;
        } else {
            Arrays.fill(Send_Data, (byte) 0);
            Arrays.fill(Recv_Data, (byte) 0);

            for (int e1 = 0; e1 < readlen; ++e1) {
                Send_Data[e1] = -1;
            }

            try {
                Thread.sleep(20L);
            } catch (InterruptedException var8) {
                var8.printStackTrace();
            }

            st = this.PinPadChannel((byte) 0, readlen, Send_Data, relen,
                    Recv_Data);
            if (st == 0) {
                System.arraycopy(Recv_Data, 0, readdata, 0, relen[0]);
            }

            return st;
        }
    }

    protected int Get_KB_MAC(int masterIndex, int mode, byte[] macdata,
                             int maclen) {
        int[] nRLen = new int[1];
        byte[] Send_Data = new byte[maclen + 200];
        byte[] Recv_Data = new byte[200];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (maclen + 2);
        Send_Data[3] = -63;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        Send_Data[6] = (byte) mode;
        System.arraycopy(macdata, 0, Send_Data, 7, maclen);
        int st = this.PinPadChannel((byte) 0, 7 + maclen, Send_Data, nRLen,
                Recv_Data);
        return st;
    }

    private int Close_KB() {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = 0;
        Send_Data[3] = -95;
        Send_Data[4] = -1;
        int st = this.PinPadChannel((byte) 0, 5, Send_Data, relen, Recv_Data);
        return st;
    }

    private int Download_Pinkey(int masterIndex, byte[] workKeyData,
                                int workKeyLen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] revLen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (workKeyLen + 1);
        Send_Data[3] = -88;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        System.arraycopy(workKeyData, 0, Send_Data, 6, workKeyLen);
        int st = this.PinPadChannel((byte) 0, 6 + workKeyLen, Send_Data,
                revLen, Recv_Data);
        return st;
    }

    private int Download_Workkey(int masterIndex, byte[] workKeyData,
                                 int workKeyLen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] revLen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (workKeyLen + 1);
        Send_Data[3] = -90;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        System.arraycopy(workKeyData, 0, Send_Data, 6, workKeyLen);
        int st = this.PinPadChannel((byte) 0, 6 + workKeyLen, Send_Data,
                revLen, Recv_Data);
        return st;
    }

    private int Download_Mackey(int masterIndex, byte[] KeyData, int KeyLen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] revLen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = (byte) (KeyLen + 1);
        Send_Data[3] = -89;
        Send_Data[4] = -1;
        Send_Data[5] = (byte) masterIndex;
        System.arraycopy(KeyData, 0, Send_Data, 6, KeyLen);
        int st = this.PinPadChannel((byte) 0, 6 + KeyLen, Send_Data, revLen,
                Recv_Data);
        return st;
    }

    private int Check_KB_Cache(int[] status, int[] datalen) {
        byte[] Send_Data = new byte[200];
        byte[] Recv_Data = new byte[400];
        int[] relen = new int[1];
        Send_Data[0] = -86;
        Send_Data[1] = 0;
        Send_Data[2] = 3;
        Send_Data[3] = -16;
        Send_Data[4] = -1;
        Send_Data[5] = -1;
        Send_Data[6] = -1;
        Send_Data[7] = -1;
        int st = this.PinPadChannel((byte) 0, 8, Send_Data, relen, Recv_Data);
        if (st == 0) {
            status[0] = Recv_Data[5];
            datalen[0] = Recv_Data[6] * 256 + Recv_Data[7];
        }

        return st;
    }

    private int Download_EnMainkey(byte[] keyEnData, int keyEnDataLen,
                                   byte[] checkValue, int checkValueLen) {
        byte[] SendData = new byte[50];
        byte[] revData = new byte[50];
        int[] revLen = new int[1];
        boolean slen = false;
        SendData[0] = -86;
        SendData[1] = 0;
        SendData[2] = (byte) (keyEnDataLen + checkValueLen);
        SendData[3] = -84;
        SendData[4] = -1;
        byte slen1 = 5;
        System.arraycopy(keyEnData, 0, SendData, slen1, keyEnDataLen);
        int slen2 = slen1 + keyEnDataLen;
        System.arraycopy(checkValue, 0, SendData, slen2, checkValueLen);
        slen2 += checkValueLen;
        return this.PinPadChannel((byte) 0, slen2, SendData, revLen, revData);
    }

    private int Download_Mainkey(int mode, int masterIndex, byte[] keyData,
                                 int keyLen) {
        byte[] SendData = new byte[50];
        byte[] revData = new byte[50];
        int[] revLen = new int[1];
        SendData[0] = -86;
        SendData[1] = 0;
        SendData[2] = (byte) (keyLen + 3);
        SendData[3] = -91;
        SendData[4] = -1;
        SendData[5] = (byte) masterIndex;
        SendData[6] = (byte) keyLen;
        SendData[7] = (byte) mode;
        System.arraycopy(keyData, 0, SendData, 8, keyLen);
        return this.PinPadChannel((byte) 0, 8 + keyLen, SendData, revLen,
                revData);
    }

    private int PinPadChannel(byte mode, int sendLen, byte[] sendData,
                              int[] recvLen, byte[] readdata) {
        byte[] pSendData = new byte[sendLen + 3];
        pSendData[0] = -58;
        pSendData[1] = 3;
        pSendData[2] = mode;
        System.arraycopy(sendData, 0, pSendData, 3, sendLen);
        MT3YCmdMan cmdMan = new MT3YCmdMan(pSendData);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            recvLen[0] = recvBuffer.length;
            System.arraycopy(recvBuffer, 0, readdata, 0, recvLen[0]);
        }

        return st;
    }

    private String Check_kb_status(int status) {
        switch (status) {
            case -83:
                return "输入数据长度有误";
            case -82:
                return "输入密钥长度有误";
            case -81:
                return "输入数据为空, 请检查";
            case -80:
                return "输入密钥为空, 请检查";
            case -31:
                return "不支持该指令";
            case -30:
                return "密钥不一致";
            case 0:
                return "成功";
            case 1:
                return "失败";
            case 2:
                return "校验出错";
            case 3:
                return "参数据错误";
            case 4:
                return "长度错误";
            case 81:
                return "设备空闲";
            case 82:
                return "指令正在执行";
            case 83:
                return "数据传输";
            case 84:
                return "键盘已关闭";
            case 85:
                return "无密钥";
            case 96:
                return "SM2 公钥长度错误";
            case 97:
                return "Pin 长度应该是6-12位";
            case 98:
                return "用户取消";
            default:
                return "未知错误";
        }
    }


    /**************************************************
     *
     * @param iEncryType
     * @param iTimes
     * @param iLength
     * @param strVoice
     * @param EndType
     * @param Timeout
     * @return
     */
    public byte[] getPassword(int iEncryType, int iTimes, int iLength, String strVoice, int EndType,
                              int Timeout) {
        byte[] enctytype = new byte[]{(byte) iEncryType};
        byte[] times = new byte[]{(byte) iTimes};
        byte[] pwdlength = new byte[]{(byte) iLength};
        byte[] voice = strVoice.getBytes();
        byte[] endtype = new byte[]{(byte) EndType};
        byte[] timeout = new byte[]{(byte) Timeout};

        byte[] head = new byte[]{(byte) 0xc6, (byte) 0x03, (byte) 0x01};
        byte[] cmd = ToolFun.PackArg(head, enctytype, times, pwdlength, voice, endtype, timeout);
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(Timeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {

            byte[] recvData = cmdMan.getRecvData();
            return recvData;
            //new String(recvData).replace("\0", "");
        }
        return null;
    }

    /**
     * @return
     */
    public int InitPinPad() {
        byte[] head = new byte[]{(byte) 0xc6, (byte) 0x03, (byte) 0x02};
        byte[] cmd = head;
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(10 * 1000);
        return cmdMan.SendRecv();
    }

    /**
     * @param ZmkIndex
     * @param ZmkLength
     * @param Zmk
     * @param CheckValue
     * @return
     */
    public int UpdateMKey(int ZmkIndex, int ZmkLength, String Zmk, String CheckValue) {
        byte[] mindex = new byte[]{(byte) ZmkIndex};
        byte[] mlength = new byte[]{(byte) ZmkLength};
        byte[] zmk = ToolFun.hexStringToBytes(Zmk);
        byte[] checkvalue = ToolFun.hexStringToBytes(CheckValue);
        byte[] head = new byte[]{(byte) 0xc6, (byte) 0x03, (byte) 0x03};
        byte[] cmd = ToolFun.PackArg(head, mindex, mlength, zmk, checkvalue);
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(10 * 1000);
        return cmdMan.SendRecv();
    }

    public int DownLoadWKey(int MKeyIndex, int WKeyIndex, int WKeyLength, String Key, String CheckValue) {
        byte[] mindex = new byte[]{(byte) MKeyIndex};
        byte[] windex = new byte[]{(byte) WKeyIndex};
        byte[] wlength = new byte[]{(byte) WKeyLength};
        byte[] zwk = ToolFun.hexStringToBytes(Key);
        byte[] checkvalue = ToolFun.hexStringToBytes(CheckValue);
        byte[] head = new byte[]{(byte) 0xc6, (byte) 0x03, (byte) 0x04};
        byte[] cmd = ToolFun.PackArg(head, mindex, windex, wlength, zwk, checkvalue);
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(10 * 1000);
        return cmdMan.SendRecv();
    }

    public int ActiveWKey(int MKeyIndex, int WKeyIndex) {
        byte[] mindex = new byte[]{(byte) MKeyIndex};
        byte[] windex = new byte[]{(byte) WKeyIndex};
        byte[] head = new byte[]{(byte) 0xc6, (byte) 0x03, (byte) 0x05};
        byte[] cmd = ToolFun.PackArg(head, mindex, windex);
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(10 * 1000);
        return cmdMan.SendRecv();
    }
}
