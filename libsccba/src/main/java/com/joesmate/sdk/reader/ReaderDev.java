package com.joesmate.sdk.reader;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;

import android.text.format.Time;

import com.joesmate.sdk.listener.ClipReturnListener;
import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;
import com.joesmate.sdk.util.ToolFun;

import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("HandlerLeak")
public class ReaderDev {

    private static final ReaderDev sInstance = new ReaderDev();
    private static MagCardDev magCardDev = null;

    ReturnListener m_ReturnListener;
    ClipReturnListener m_ClipReturnListener;

    private ReaderDev() {
    }

//    private static void ensureThreadLocked() {
//        if (sInstance == null) {
//            sInstance = new ReaderDev();
//        }
//
//    }

    public static ReaderDev getInstance() {

        synchronized (ReaderDev.class) {
            // ensureThreadLocked();
            return sInstance;
        }
    }

    public int openDevice(BluetoothDevice device) {
        CommManagement commMan = MT3YCommMan.getInstance();
        try {
            return commMan.deviceConnect(device);
        } catch (Exception var3) {
            var3.printStackTrace();
            LogMg.e("ReaderDev", "openDevice:" + var3.getMessage());
            return -17;
        }
    }

    public void openDevice(final ReturnListener listener) {
        synchronized (ReaderDev.class) {
            m_ReturnListener = listener;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    // TODO Auto-generated method stub
                    CommManagement commMan = MT3YCommMan.getInstance();
                    int st = -1;
                    try {
                        st = commMan.deviceConnect();
                    } catch (IOException var3) {
                        var3.printStackTrace();
                        LogMg.e("ReaderDev", "openDevice:" + var3.getMessage());
                        st = -17;
                    }
                    if (st == 0) {
                        listener.onSuccess("连接成功");
//                        Message message = new Message();
//                        message.what = 2;
//                        message.obj = "连接成功";
//                        handler.sendMessage(message);
                        devBeep();

                    } else {
                        listener.onError(st, RetCode.GetErrMsg(st));
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = st;
//                        handler.sendMessage(message);
                    }

                }
            }).start();
        }

    }

    public int openDevice() {
        CommManagement commMan = MT3YCommMan.getInstance();

        try {
            return commMan.deviceConnect();
        } catch (IOException var3) {
            var3.printStackTrace();
            LogMg.e("ReaderDev", "openDevice:" + var3.getMessage());
            return -17;
        }
    }

    public int closeDevice() {
        CommManagement commMan = MT3YCommMan.getInstance();
        return commMan.closeDevice();
    }

    public boolean isBTConnected() {
        CommManagement commMan = MT3YCommMan.getInstance();
        return commMan.isConnected();
    }

    public void PlayVoice(String voice) {
        byte[] voicebyte = voice.getBytes();
        byte[] send = new byte[voicebyte.length + 2];
        send[0] = (byte) 0xc0;
        send[1] = (byte) 0x0e;
        System.arraycopy(voicebyte, 0, send, 2, voicebyte.length);
        StrDataMan strDataMan = new StrDataMan(send);
        strDataMan.execCmd();
        ToolFun.Dalpey(400);
    }

    public String getSnr() {
        byte[] cmd = new byte[]{(byte) -64, (byte) 9, (byte) 20};
        StrDataMan strDataMan = new StrDataMan(cmd);
        int st = strDataMan.execCmd();
        LogMg.i("ReaderDev", this + " getSnr return " + st);
        if (st == 0) {
            JSONObject jsonObj = strDataMan.getResult();

            try {
                return jsonObj.getString("Value");
            } catch (JSONException var6) {
                var6.printStackTrace();
                LogMg.e("ReaderDev", var6.getMessage());
            }
        }

        return null;
    }

    public String getVersion() {
        CommManagement comMan = MT3YCommMan.getInstance();
        return comMan.getVersion();
    }

    public void getPower(final ReturnListener listener) {
        synchronized (ReaderDev.class) {
            m_ReturnListener = listener;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    byte[] cmd = new byte[]{(byte) -64, (byte) 12};
                    NumDataMan numDataMan = new NumDataMan(cmd);
                    int st = numDataMan.execCmd();
                    LogMg.i("ReaderDev", this + " getPower return " + st);
                    if (st == 0) {
                        JSONObject jsonObj = numDataMan.getResult();

                        try {
                            int e = jsonObj.getInt("Value");
                            listener.onSuccess("" + e);
//                            Message message = new Message();
//                            message.what = 2;
//                            message.obj = "" + e;
//                            handler.sendMessage(message);
                        } catch (JSONException var7) {
                            var7.printStackTrace();
                            LogMg.e("ReaderDev", var7.getMessage());
                        }
                    } else {
                        listener.onError(st, RetCode.GetErrMsg(st));
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = st;
//                        handler.sendMessage(message);
                    }

                }
            }).start();
        }

    }

    public void getDevStatus(final ClipReturnListener listener) {
        synchronized (ReaderDev.class) {
            m_ClipReturnListener = listener;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] cmd = new byte[]{(byte) 48, (byte) 8};
                    DevStatusDataMan devStatusDataMan = new DevStatusDataMan(cmd);
                    int st = devStatusDataMan.execCmd();
                    LogMg.i("ReaderDev", this + " getDevStatus return " + st);

                    if (st == 0) {
                        JSONObject Obj = devStatusDataMan.getResult();
                        listener.onSuccess(Obj);
//                        Message message = new Message();
//                        message.what = 0;
//                        message.obj = Obj;
//                        handler.sendMessage(message);
                    } else {
                        JSONObject errObj = new JSONObject();

                        try {
                            errObj.put("ErrCode", st);
                            errObj.put("ErrMsg", RetCode.GetErrMsg(st));
                            listener.onError(errObj);
//                            Message message = new Message();
//                            message.what = 1;
//                            message.obj = errObj;
//                            handler.sendMessage(message);
                        } catch (JSONException var7) {
                            var7.printStackTrace();
                            LogMg.e("ReaderDev", "getDevStatus:" + var7.getMessage());
                        }
                    }
                }
            }).start();
        }
    }

    public void getChannelStatus(ClipReturnListener listener) {
        byte[] cmd = new byte[]{(byte) 48, (byte) 7};
        DevStatusDataMan devStatusDataMan = new DevStatusDataMan(cmd);
        int st = devStatusDataMan.execCmd();
        LogMg.i("ReaderDev", this + " getChannelStatus return " + st);
        JSONObject errObj;
        if (st == 0) {
            errObj = devStatusDataMan.getResult();
            if (listener != null) {
                listener.onSuccess(errObj);
            }
        } else {
            errObj = new JSONObject();

            try {
                errObj.put("ErrCode", st);
                errObj.put("ErrMsg", RetCode.GetErrMsg(st));
                if (listener != null) {
                    listener.onError(errObj);
                }
            } catch (JSONException var7) {
                var7.printStackTrace();
                LogMg.e("ReaderDev", "getChannelStatus:" + var7.getMessage());
            }
        }

    }

    public int devBeep() {
        byte[] cmd = new byte[]{(byte) 49, (byte) 19, (byte) 1, (byte) 1, (byte) 1};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("ReaderDev", this + " devBeep return " + st);
        return st;
    }

    public int devLCDControl(boolean cont, boolean contless, boolean idcard, boolean mag, boolean finger) {
        byte[] cmd = new byte[]{(byte) -64, (byte) 15, (byte) (cont ? 1 : 0), (byte) (contless ? 1 : 0),
                (byte) (idcard ? 1 : 0), (byte) (mag ? 1 : 0), (byte) (finger ? 1 : 0)};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("ReaderDev", this + " devLCDControl return " + st);
        return st;
    }

    public void findCard(final ClipReturnListener listener, int mode, final int timeout) {

        synchronized (ReaderDev.class) {
            switch (mode) {
                case 0:
                case 1:
                case 8:
                    magCardDev = MagCardDev.getInstance();
                    int findCardDataMan = magCardDev.magReadStart();
                    if (findCardDataMan != 0) {
                        if (listener != null) {
                            JSONObject json = new JSONObject();

                            try {
                                json.put("ErrCode", findCardDataMan);
                                json.put("ErrMsg", "magReadStart fail");
                                listener.onError(json);
                            } catch (JSONException var8) {
                                var8.printStackTrace();
                                LogMg.e("ReaderDev", "findCard:" + var8.getMessage());
                            }
                        }

                        return;
                    }
                default:
                    final FindCardDataMan findCardDataMan1 = new FindCardDataMan(mode);
                    (new Thread(new Runnable() {
                        public void run() {

                            synchronized (ReaderDev.class) {
                                long time1 = System.currentTimeMillis();

                                while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                    int st = findCardDataMan1.execCmd();
                                    if (st == 0) {
                                        if (listener != null) {
                                            listener.onSuccess(findCardDataMan1.getResult());
                                        }

                                        return;
                                    }
                                }

                                if (listener != null) {
                                    listener.onError(findCardDataMan1.getResult());
                                }

                            }
                        }
                    })).start();
            }
        }
    }

    public int setSleepTime(int sleepTime) {
        byte[] cmd = new byte[]{(byte) 48, (byte) 17, (byte) sleepTime};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("ReaderDev", this + " setSleepTime return " + st);
        return st;
    }

    public int devSleepStart() {
        byte[] cmd = new byte[]{(byte) 48, (byte) 18};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("ReaderDev", this + " devSleepStart return " + st);
        return st;
    }

    public int syncRTCTime() {
        Calendar calendar = Calendar.getInstance();

        // Time tmNow = new Time();
        // tmNow.setToNow();
        byte year = (byte) calendar.get(Calendar.YEAR);//  (tmNow.year % 100);
        byte month = (byte) calendar.get(Calendar.MONTH);// (tmNow.month + 1);
        byte day = (byte) calendar.get(Calendar.DATE); //tmNow.monthDay;
        byte hour = (byte) calendar.get(Calendar.HOUR);// tmNow.hour;
        byte min = (byte) calendar.get(Calendar.MINUTE);// tmNow.minute;
        byte sec = (byte) calendar.get(Calendar.SECOND);//tmNow.second;
        byte[] cmd = new byte[]{(byte) -64, (byte) 13, year, month, day, hour, min, sec};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        LogMg.i("ReaderDev", this + "setRTCTime return " + st);
        return st;
    }

    public int Cancel() {
        byte[] cmd = new byte[]{(byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0xC7, (byte) 0x02, (byte) 0xc5, (byte) 0x03};
        MT3YCmdMan cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        return st;
    }
//    final Handler handler = new Handler() {
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
}