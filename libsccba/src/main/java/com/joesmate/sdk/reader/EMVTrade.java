/*     */
package com.joesmate.sdk.reader;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
/*     */
import android.text.format.Time;
import android.util.Log;


import com.joesmate.sdk.listener.ClipReturnListener;
import com.joesmate.sdk.listener.ReturnListener;
//import com.joesmate.sdk.reader.MT3YCmdMan;
//import com.joesmate.sdk.reader.MagCardDev;
//import com.joesmate.sdk.reader.MagCardMan;
import com.joesmate.sdk.mtreader.BlueToothClass;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;
import com.joesmate.sdk.util.ToolFun;

import org.json.JSONException;
import org.json.JSONObject;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("HandlerLeak")
public class EMVTrade {
    private static final EMVTrade sInstance = new EMVTrade();
//    ClipReturnListener m_ClipReturnListener;
//    ReturnListener m_ReturnListener;

    private EMVTrade() {
    }

//    private static void ensureThreadLocked() {
//        if (sInstance == null) {
//            sInstance = new EMVTrade();
//        }
//
//    }

    public static EMVTrade getInstance() {

        synchronized (EMVTrade.class) {
            //ensureThreadLocked();
            return sInstance;
        }
    }

    public void readCardInfo(final ClipReturnListener listener, final int timeout) {
        synchronized (EMVTrade.class) {
            //   m_ClipReturnListener = listener;
            (new Thread(new Runnable() {
                public void run() {

                    LogMg.i("EMVTrade", this + " readCardInfo, timeout=" + timeout);
                    int st = MagCardDev.getInstance().magReadStart();
                    LogMg.i("EMVTrade", this + " readCardInfo, magReadStart return=" + st);
                    if (st != 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put(RetCode.Key_ErrCode, st);
                            json.put(RetCode.Key_ErrMsg, RetCode.GetErrMsg(st));

                        } catch (JSONException var7) {
                            var7.printStackTrace();
                        }
                        listener.onError(json);
//                        Message message = myHandler.obtainMessage();
//                        message.what = 1;
//                        message.obj = json;
//                        myHandler.sendMessage(message);
                    } else {

                        JSONObject jsonobj = new JSONObject();
                        long time1 = System.currentTimeMillis();
                        ReaderDev.getInstance().devBeep();
                        ReaderDev.getInstance().devLCDControl(true, true, false, true, false);
                        while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                            int e = EMVTrade.this.readTrackData(jsonobj);
                            if (e == 0) {
                                ReaderDev.getInstance().devBeep();
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                listener.onSuccess(jsonobj);
//                                Message message = myHandler.obtainMessage();
//                                message.what = 0;
//                                message.obj = jsonobj;
//                                myHandler.sendMessage(message);
                                return;
                            }
                        }

                        try {
                            jsonobj.put("ErrCode", -75);
                            jsonobj.put("ErrMsg", RetCode.GetErrMsg(-75));
                            listener.onError(jsonobj);
//                            Message message = myHandler.obtainMessage();
//                            message.what = 1;
//                            message.obj = jsonobj;
//                            myHandler.sendMessage(message);
                        } catch (JSONException var6) {
                            var6.printStackTrace();
                        }

                    }
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }
            })).start();
        }

    }

    protected int readMagTrackData(JSONObject jsonobj) {


        int st = this.readMagData(jsonobj, 0);// 磁条
        try {

            if (st == 0) {
                return st;
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return st == 0 ? st : -76;

    }

    /*******读取磁条卡信息*****/
    public void readMagCardInfo(final ClipReturnListener listener, final int timeout) {
        synchronized (EMVTrade.class) {
            //   m_ClipReturnListener = listener;
            (new Thread(new Runnable() {
                public void run() {

                    LogMg.i("EMVTrade", this + " readCardInfo, timeout=" + timeout);
                    int st = MagCardDev.getInstance().magReadStart();
                    LogMg.i("EMVTrade", this + " readCardInfo, magReadStart return=" + st);
                    if (st != 0) {
                        JSONObject json = new JSONObject();
                        try {
                            json.put(RetCode.Key_ErrCode, st);
                            json.put(RetCode.Key_ErrMsg, RetCode.GetErrMsg(st));

                        } catch (JSONException var7) {
                            var7.printStackTrace();
                        }
                        listener.onError(json);
//                        Message message = myHandler.obtainMessage();
//                        message.what = 1;
//                        message.obj = json;
//                        myHandler.sendMessage(message);
                    } else {

                        JSONObject jsonobj = new JSONObject();
                        long time1 = System.currentTimeMillis();
                        ReaderDev.getInstance().devBeep();
                        ReaderDev.getInstance().devLCDControl(true, true, false, true, false);
                        while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                            int e = EMVTrade.this.readMagTrackData(jsonobj);
                            if (e == 0) {
                                ReaderDev.getInstance().devBeep();
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);

//                                Message message = myHandler.obtainMessage();
//                                message.what = 0;
//                                message.obj = jsonobj;
//                                myHandler.sendMessage(message);
                                listener.onSuccess(jsonobj);
                                return;
                            }
                        }

                        try {
                            jsonobj.put("ErrCode", -75);
                            jsonobj.put("ErrMsg", RetCode.GetErrMsg(-75));
                            listener.onError(jsonobj);
//                            Message message = myHandler.obtainMessage();
//                            message.what = 1;
//                            message.obj = jsonobj;
//                            myHandler.sendMessage(message);
                        } catch (JSONException var6) {
                            var6.printStackTrace();
                        }

                    }
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }
            })).start();
        }

    }

    public void getFileld55(final ReturnListener listener, final String amount, final Time transTime) {
        synchronized (EMVTrade.class) {
            //m_ReturnListener = listener;
            (new Thread(new Runnable() {
                public void run() {

                    LogMg.i("EMVTrade", this + " getFileld55, amount=" + amount);

                    int iAuthAmt1;
                    try {
                        iAuthAmt1 = (int) (Float.valueOf(amount).floatValue() * 100.0F);
                    } catch (Exception var7) {
                        LogMg.e("EMVTrade", var7.getMessage());
                        listener.onError(RetCode.ERR_AMOUNT, RetCode.GetErrMsg(RetCode.ERR_AMOUNT));
//                        Message message = myHandler.obtainMessage();
//                        message.what = 3;
//                        message.obj = RetCode.ERR_AMOUNT;
//                        myHandler.sendMessage(message);

                        return;
                    }
                    int st = 0;
                    if (iAuthAmt1 > 0) {
                        st = sendAuthedAmount(iAuthAmt1);
                        // LogMg.i("EMVTrade", this + " getFileld55,
                        // sendAuthedAmount
                        // return=" + st);
                    }
                    //

                    if (st == 0) {
                        st = sendTransData(transTime);
                        LogMg.i("EMVTrade", this + " getFileld55, sendTransData return=" + st);
                        if (st == 0) {
                            String strf55 = getPbocF55();
                            ;

                            if (!strf55.isEmpty()) {
//                                Message message = myHandler.obtainMessage();
//                                message.what = 2;
//                                message.obj = strf55;
//                                myHandler.sendMessage(message);
                                listener.onSuccess(strf55);
                            }
                        } else {
                            listener.onError(RetCode.ERR_SETTRANTIME, RetCode.GetErrMsg(RetCode.ERR_SETTRANTIME));
//                            Message message = myHandler.obtainMessage();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_SETTRANTIME;
//                            myHandler.sendMessage(message);
                        }
                        // } else if (listener != null) {
                        // listener.onError(-205, RetCode.GetErrMsg(-205));
                        // }

                    }
                }
            })).start();
        }
    }

    public String getFileld55(final String amount, final Time transTime) {

        LogMg.i("EMVTrade", this + " getFileld55, amount=" + amount);

        int iAuthAmt1;
        try {
            iAuthAmt1 = (int) (Float.valueOf(amount).floatValue() * 100.0F);
        } catch (Exception var7) {
            LogMg.e("EMVTrade", var7.getMessage());
            return "";
        }
        int st = 0;
        if (iAuthAmt1 > 0) {
            st = sendAuthedAmount(iAuthAmt1);

        }
        if (st == 0) {
            st = sendTransData(transTime);
            LogMg.i("EMVTrade", this + " getFileld55, sendTransData return=" + st);
            if (st == 0) {
                String strf55 = getPbocF55();
                if (!strf55.isEmpty()) {
                    return strf55;
                }
            } else {
                return "";
            }
        }
        return "";

    }

    private String getPbocF55() {
        byte[] cmd = new byte[]{(byte) 80, (byte) 2};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("EMVTrade", this + " getPbocF55, sendRecv return=" + st);

        if (st == 0) {
            String temp = new String(cmdMan.getRecvData());
            String[] strf55s = temp.split("\\|");
            String strf55 = strf55s[0];
            if (strf55s.length > 1) {
                strf55 = strf55s[1] + strf55s[0];
            }
            return strf55;
        } else {
            return "";
        }

    }

    private int sendTransData(Time transTime) {
        int year = transTime.year % 100;
        int month = transTime.month;
        int day = transTime.monthDay;
        byte[] cmd = new byte[]{(byte) 80, (byte) 1, this.toBCD(year), this.toBCD(month + 1), this.toBCD(day)};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        return st;
    }

    private byte toBCD(int src) {
        return (byte) (src / 10 * 16 + src % 10);
    }

    private int sendAuthedAmount(int iAuthAmt) {
        byte[] cmd = {(byte) 80, (byte) 3, (byte) (int) ((iAuthAmt & 0xFF) >> 40),
                (byte) (int) ((iAuthAmt & 0xFF) >> 32), (byte) (int) ((iAuthAmt & 0xFF) >> 24),
                (byte) (int) ((iAuthAmt & 0xFF) >> 16), (byte) (int) ((iAuthAmt & 0xFF) >> 8),
                (byte) (int) (iAuthAmt & 0xFF)};
        CmdManagement cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        return st;
    }

    protected int readTrackData(JSONObject jsonobj) {

        Time tmNow = new Time("GMT+8");
        tmNow.setToNow();
        int st = this.readMagData(jsonobj, 0);// 磁条
        try {
            jsonobj.put("Fileld55", "");
            if (st == 0) {
                return st;
            }
            st = this.readPbocTrackData(0, jsonobj);// IC
            if (st == 0) {

                jsonobj.put("Fileld55", getFileld55("0", tmNow));

                return st;
            }
            st = this.readPbocTrackData(1, jsonobj);// 非接

            jsonobj.put("Fileld55", getFileld55("0", tmNow));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return st == 0 ? st : -76;

    }

    public int readPbocTrackData(int mode, JSONObject jsonobj) {
        LogMg.i("EMVTrade", this + " readPbocTrackData enter, mode=" + mode);
        byte[] cmd = new byte[]{(byte) 80, (byte) 0, (byte) mode, (byte) 1};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            byte[] tmpTrack2Data = new byte[256];
            ToolFun.hex_asc(recvBuffer, tmpTrack2Data, recvBuffer.length);
            byte[] track2Data = new byte[256];
            byte[] flag = new byte[]{(byte) 53, (byte) 55, (byte) 0, (byte) 0, (byte) 0};
            int index = BlueToothClass.ParseTLV(tmpTrack2Data, flag, track2Data);

            try {
                if (jsonobj != null) {
                    jsonobj.put("CardNO", "");
                    jsonobj.put("Track2", "");
                    jsonobj.put("Track3", "");
                }

                if (index > -1 && jsonobj != null) {
                    String e = new String(track2Data);
                    jsonobj.put("Track2", e);
                    int indexOfD = e.indexOf("D");
                    if (indexOfD > -1) {
                        String cardNo = e.substring(0, indexOfD);
                        jsonobj.put("CardNO", cardNo);
                    }
                }
            } catch (JSONException var14) {
                var14.printStackTrace();
            }
        }

        LogMg.i("EMVTrade", this + " readPbocTrackData return=" + st);
        return st;
    }

    private int readMagData(JSONObject jsonobj, int timeout) {
        LogMg.i("EMVTrade", this + " readMagData enter");
        MagCardMan magMan = new MagCardMan(timeout);
        int st = magMan.execCmd();
        if (st == 0 && jsonobj != null) {
            JSONObject json = magMan.getResult();

            try {
                String e = json.getString("Track2");
                int index = e.indexOf("=");
                if (index <= 0) {
                    jsonobj.put("CardNO", e);
                    Log.e("readMagData", e + "");
                } else {
                    int start = e.charAt(0) >= 48 && e.charAt(0) <= 57 ? 0 : 1;
                    String cardNo = e.substring(start, index);
                    jsonobj.put("CardNO", cardNo);
                }
                jsonobj.put("ErrCode", "0");
                jsonobj.put("ErrMsg", "成功");
                jsonobj.put("Track1", json.getString("Track1"));
                jsonobj.put("Track2", e);
                jsonobj.put("Track3", json.getString("Track3"));
            } catch (JSONException var9) {
                var9.printStackTrace();
            }
        }

        LogMg.i("EMVTrade", this + " readMagData return = " + st);
        return st;
    }

    //    final Handler myHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:// ClipReturnListener.onSuccess
//                    if (m_ClipReturnListener != null) {
//                        m_ClipReturnListener.onSuccess((JSONObject) msg.obj);
//                    }
//                    break;
//                case 1:// ClipReturnListener.onError
//                    if (m_ClipReturnListener != null) {
//                        m_ClipReturnListener.onError((JSONObject) msg.obj);
//                    }
//                    break;
//                case 2:// ReturnListener.onSuccess
//                    if (m_ReturnListener != null) {
//                        m_ReturnListener.onSuccess((String) msg.obj);
//                    }
//                    break;
//                case 3:// ReturnListener.onError
//                    int err_code = (Integer) msg.obj;
//                    if (m_ReturnListener != null) {
//                        m_ReturnListener.onError(err_code, RetCode.GetErrMsg(err_code));
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    //读取磁条卡
    // 磁条卡的读取
    String mag;

    public JSONObject readMg(final int timeout) {
        JSONObject json = new JSONObject();
        Class var3 = EMVTrade.class;
        synchronized (EMVTrade.class) {
            LogMg.i("EMVTrade", this + " readCardInfo, timeout=" + timeout);
            int st = MagCardDev.getInstance().magReadStart();
            ToolFun.Dalpey(200);
            LogMg.i("EMVTrade", this + " readCardInfo, magReadStart return="
                    + st);
            if (st != 0) {

                try {
                    json.put("ErrMsg", "读取磁条卡失败");
                    json.put("ErrCode", String.valueOf(st));
                } catch (JSONException e) {
                }
                return json;

            }

        }

        synchronized (EMVTrade.class) {

            long time1 = System.currentTimeMillis();

            while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                int e = EMVTrade.this.readMagData(json, timeout);

                if (e == 0) {
                    return json;
                }
                //ToolFun.Dalpey(500);
            }

            try {
                json.put("ErrCode", "-75");
                json.put("ErrMsg", RetCode.GetErrMsg(-75));

                return json;
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return json;
        }

    }


    public String[] GetICCInfo(int ICType, String AIDList, String TagList, short strTimeout) {
        String[] arrRet = null;
        byte[] icType = new byte[1];
        icType[0] = (byte) ICType;
        byte[] aidList = AIDList.getBytes();

        byte[] tagList = TagList.getBytes();

        byte[] timeout = {(byte) strTimeout};

        byte[] cmd = mcCreatData((byte) 0x00, icType, aidList, tagList, timeout);


        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    public String[] GetARQC(int ICType, String TxData, String AIDList, short strTimeout) {
        String[] arrRet = null;

        byte[] icType = new byte[1];
        icType[0] = (byte) ICType;
        byte[] txtdata = TxData.getBytes();

        byte[] aidList = AIDList.getBytes();

        byte[] timeout = new byte[1];
        timeout[0] = (byte) strTimeout;
        byte[] cmd = mcCreatData((byte) 0x01, icType, txtdata, aidList, timeout);
//        byte[] cmd = new byte[data.length + 3];
//        int pos = 0;
//        cmd[pos] = (byte) 0x50;
//        pos++;
//        cmd[pos] = (byte) 0x04;
//        pos++;
//        cmd[pos] = (byte) 0x01;
//        pos++;
//        System.arraycopy(data, 0, cmd, pos, data.length);

        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    public String[] ARPCExeScript(int ICType, String TxData, String ARPC, String CDol2) {
        String[] arrRet = null;

        byte[] icType = {(byte) ICType};

        byte[] tagList = TxData.getBytes();

        byte[] arpc = ARPC.getBytes();

        byte[] cdol = CDol2.getBytes();

        byte[] cmd = mcCreatData((byte) 0x02, icType, tagList, arpc, cdol);

        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        //cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    public String[] GetTrDetail(int ICType, int NOLog, short strTimeout) {
        String[] arrRet = null;

        byte[] icType = {(byte) ICType};

        byte[] nolog = {(byte) NOLog};

        byte[] timeout = {(byte) strTimeout};

        byte[] cmd = mcCreatData((byte) 0x03, icType, nolog, timeout);
//        byte[] cmd = new byte[data.length + 3];
//        int pos = 0;
//        cmd[pos] = (byte) 0x50;
//        pos++;
//        cmd[pos] = (byte) 0x04;
//        pos++;
//        cmd[pos] = (byte) 0x02;
//        pos++;
//        System.arraycopy(data, 0, cmd, pos, data.length);

        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    public String[] GetLoadLog(int ICType, int NOLog, String AIDList, short strTimeout) {
        String[] arrRet = null;

        byte[] icType = {(byte) ICType};

        byte[] nolog = {(byte) NOLog};

        byte[] aidList = AIDList.getBytes();

        byte[] timeout = {(byte) strTimeout};

        byte[] cmd = mcCreatData((byte) 0x04, icType, nolog, aidList, timeout);
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    public String[] SCCBA_GetICAndARQCInfo(int ICType, String AIDList, String TagList, String TxData, short strTimeout) {
        String[] arrRet = null;
        byte[] icType = {(byte) ICType};

        byte[] aidList = AIDList.getBytes();

        byte[] tagList = TagList.getBytes();

        byte[] txtdata = TxData.getBytes();

        byte[] timeout = {(byte) strTimeout};

        byte[] cmd = mcCreatData((byte) 0x00, icType, aidList, tagList, txtdata, timeout);

        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setCommTimeouts(strTimeout * 1000);
        int st = cmdMan.SendRecv();
        if (st == 0) {
            byte[] recvBuffer = cmdMan.getRecvData();
            arrRet = new String(recvBuffer).split("\\|");
            if (arrRet[0].equals("0")) {
                return arrRet;
            }

        }
        return arrRet;
    }

    private byte[] mcCreatData(byte port, byte[]... params) {
        int len = 0;
        byte[] tmp = new byte[2048];
        for (byte[] item :
                params) {
            tmp[len] = (byte) item.length;
            len++;
            System.arraycopy(item, 0, tmp, len, item.length);
            len += item.length;
        }
        byte[] cmd = new byte[len + 3];
        int pos = 0;
        cmd[pos] = (byte) 0x50;
        pos++;
        cmd[pos] = (byte) 0x04;
        pos++;
        cmd[pos] = port;
        pos++;
        System.arraycopy(tmp, 0, cmd, pos, len);
        return cmd;
    }
}
