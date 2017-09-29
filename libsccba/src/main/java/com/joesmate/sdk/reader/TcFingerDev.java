package com.joesmate.sdk.reader;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.util.Base64;
import android.util.Log;


import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.util.CmdCode;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;
import com.joesmate.sdk.util.ToolFun;

import java.io.File;
import java.io.FileOutputStream;

public class TcFingerDev extends FingerDev {

    private static final TcFingerDev sInstance = new TcFingerDev();

    protected TcFingerDev() {
    }


    public static TcFingerDev getInstance() {

        synchronized (TcFingerDev.class) {
            //ensureThreadLocked();
            return sInstance;
        }
    }

    public void regFingerPrint(final ReturnListener listerner, final int timeout) {
        synchronized (TcFingerDev.class) {
            m_ReturnListener = listerner;
            (new Thread(new Runnable() {
                public void run() {
                    LogMg.i("TcFingerDev", this + " regFingerPrint, timeout=" + timeout);
                    int stBd = fingerSetBaudRate(0);
                    LogMg.i("TcFingerDev", this + " fingerSetBaudRate, return=" + stBd);
                    if (stBd != 0) {
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);
                        listerner.onError(stBd, RetCode.GetErrMsg(stBd));

                    } else {
                        byte[] writeBuf = new byte[9];
                        byte[] temp = new byte[7];
                        writeBuf[0] = 126;
                        writeBuf[1] = 66;
                        writeBuf[2] = 99;
                        writeBuf[3] = 0;
                        writeBuf[4] = 0;
                        writeBuf[5] = 0;
                        writeBuf[6] = 1;
                        writeBuf[7] = 0;
                        System.arraycopy(writeBuf, 1, temp, 0, 7);
                        writeBuf[8] = (byte) ToolFun.cr_bcc(7, temp);
                        int st = fingerChannel(writeBuf, (byte) 0);
                        LogMg.i("TcFingerDev", this + " regFingerPrint, send return=" + st);
                        if (st == 0) {
                            final byte[] queryData = new byte[0];
                            final int[] recvLen = new int[1];
                            final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                            final byte[] AscData = new byte[CmdCode.MAX_SIZE_BUFFER * 2];
                            final long time1 = System.currentTimeMillis();
                            ReaderDev.getInstance().devBeep();
                            ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                            while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                int st11 = TcFingerDev.this.fingerChannel(queryData, (byte) 1);
                                if (st11 == 0) {
                                    ReaderDev.getInstance().devBeep();
                                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                    TcFingerDev.this.parseTcFingerData(TcFingerDev.this.recvBuffer.length,
                                            TcFingerDev.this.recvBuffer, recvLen, recvData);
                                    ToolFun.hex_asc(recvData, AscData, recvLen[0]);
                                    int fpdatalen = recvLen[0];
                                    byte[] fpdata = new byte[fpdatalen];
                                    java.lang.System.arraycopy(recvData, 0, fpdata, 0, fpdatalen);
                                    String str = Base64.encodeToString(fpdata, 0, fpdatalen, Base64.NO_PADDING);
                                    String string = str.replace("\n", "");
                                    Log.e("天成注册", string);
                                    listerner.onSuccess(string);
//									Message message = new Message();
//									message.what = 2;
//									message.obj = new String(string);
//									myHandler.sendMessage(message);

                                    return;
                                }
                            }
                            listerner.onError(RetCode.ERR_TIMEOUT, RetCode.GetErrMsg(RetCode.ERR_TIMEOUT));
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_TIMEOUT;
//                            myHandler.sendMessage(message);

                        } else {
                            listerner.onError(RetCode.ERR_CHALSEND, RetCode.GetErrMsg(RetCode.ERR_CHALSEND));
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_CHALSEND;
//                            myHandler.sendMessage(message);
                        }

                    }
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }
            })).start();
        }
    }

    public void sampFingerPrint(final ReturnListener listener, final int timeout) {

        LogMg.i("TcFingerDev", this + " sampFingerPrint, timeout=" + timeout);
        synchronized (TcFingerDev.class) {
            m_ReturnListener = listener;
            (new Thread(new Runnable() {
                public void run() {
                    int stBd = fingerSetBaudRate(0);
                    LogMg.i("TcFingerDev", this + " fingerSetBaudRate,return=" + stBd);
                    if (stBd != 0) {
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);
                        listener.onError(stBd, RetCode.GetErrMsg(stBd));

                    } else {
                        byte[] writeBuf = new byte[9];
                        byte[] temp = new byte[7];
                        writeBuf[0] = 126;
                        writeBuf[1] = 66;
                        writeBuf[2] = 100;
                        writeBuf[3] = 0;
                        writeBuf[4] = 0;
                        writeBuf[5] = 0;
                        writeBuf[6] = 1;
                        writeBuf[7] = 2;
                        System.arraycopy(writeBuf, 1, temp, 0, 7);
                        writeBuf[8] = (byte) ToolFun.cr_bcc(7, temp);
                        int st = fingerChannel(writeBuf, (byte) 0);
                        LogMg.i("TcFingerDev", this + " sampFingerPrint, send return=" + st);
                        if (st == 0) {
                            final byte[] queryData = new byte[0];
                            final int[] recvLen = new int[1];
                            final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                            final byte[] AscData = new byte[CmdCode.MAX_SIZE_BUFFER * 2];
                            final long time1 = System.currentTimeMillis();
                            final byte[] tempBuf = new byte[1024];
                            final int[] tempLen = new int[1];
                            ReaderDev.getInstance().devBeep();
                            ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                            while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                int st11 = fingerChannel(queryData, (byte) 1);
                                if (st11 == 0) {
                                    st11 = parseTcFingerData(recvBuffer.length, recvBuffer, tempLen, tempBuf);
                                    if (st11 != 0) {
                                        ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                        Message message = new Message();
//                                        message.what = 3;
//                                        message.obj = st11;
//                                        myHandler.sendMessage(message);
                                        listener.onError(st11, RetCode.GetErrMsg(st11));
                                        return;
                                    }

                                    int allLen = tempLen[0];
                                    if (allLen < 3) {
                                        ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                        Message message = new Message();
//                                        message.what = 3;
//                                        message.obj = RetCode.ERR_LEN;
//                                        myHandler.sendMessage(message);
                                        listener.onError(RetCode.ERR_LEN, RetCode.GetErrMsg(RetCode.ERR_LEN));
                                        return;
                                    }

                                    int userLen1;
                                    if (tempBuf[0] < 0) {
                                        userLen1 = tempBuf[0] + 256;
                                    } else {
                                        userLen1 = tempBuf[0];
                                    }

                                    int fingerLen1;
                                    if (tempBuf[1] < 0) {
                                        fingerLen1 = tempBuf[1] + 256;
                                    } else {
                                        fingerLen1 = tempBuf[1];
                                    }

                                    if (userLen1 + fingerLen1 + 2 != allLen) {
                                        ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                        Message message = new Message();
//                                        message.what = 3;
//                                        message.obj = RetCode.ERR_LEN;
//                                        myHandler.sendMessage(message);
                                        listener.onError(RetCode.ERR_LEN, RetCode.GetErrMsg(RetCode.ERR_LEN));
                                        return;
                                    }
                                    ReaderDev.getInstance().devBeep();
                                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                    recvLen[0] = fingerLen1;
                                    int fpdatalen = recvLen[0];
                                    byte[] fpdata = new byte[fpdatalen];


                                    ToolFun.hex_asc(recvData, AscData, recvLen[0]);
                                    System.arraycopy(tempBuf, 2 + userLen1, recvData, 0, fingerLen1);
//									Log.e("天诚指纹111",new String(recvData)+"天诚指纹+++"+new String(recvData).length());
                                    String str_in = Base64.encodeToString(recvData, 0, fpdatalen, Base64.NO_WRAP);
                                    String string = str_in.replace("\n", "");
//									String base64Str = string.replace('-', '+');
//									base64Str = base64Str.replace('_', '/');
//									Log.e("天诚指纹",string+"天诚指纹+++"+string.trim().length());


//                                    Message message = new Message();
//                                    message.what = 2;
//                                    message.obj = new String(string);
//                                    myHandler.sendMessage(message);
                                    listener.onSuccess(string);
                                    return;
                                }
                            }
                            listener.onError(RetCode.ERR_TIMEOUT, RetCode.GetErrMsg(RetCode.ERR_TIMEOUT));
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_TIMEOUT;
//                            myHandler.sendMessage(message);

                        } else {
                            listener.onError(RetCode.ERR_CHALSEND, RetCode.GetErrMsg(RetCode.ERR_CHALSEND));
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_CHALSEND;
//                            myHandler.sendMessage(message);
                        }

                    }
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }
            })).start();
        }
    }


    public void imgFingerPrint(final ReturnListener listener, final int timeout) {
        LogMg.i("TcFingerDev", this + " imgFingerPrint, timeout=" + timeout);
        synchronized (TcFingerDev.class) {

            m_ReturnListener = listener;
            (new Thread(new Runnable() {
                public void run() {
                    int stBd = fingerSetBaudRate(0);
                    LogMg.i("TcFingerDev", this + " fingerSetBaudRate, return=" + stBd);
                    if (stBd != 0) {
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);
                        listener.onError(stBd, RetCode.GetErrMsg(stBd));

                    } else {
                        byte[] writeBuf = new byte[12];
                        byte[] temp = new byte[10];
                        writeBuf[0] = 126;
                        writeBuf[1] = 66;
                        writeBuf[2] = -128;
                        writeBuf[3] = 0;
                        writeBuf[4] = 0;
                        writeBuf[5] = 0;
                        writeBuf[6] = 4;
                        writeBuf[7] = 0;
                        writeBuf[8] = 0;
                        writeBuf[9] = 1;
                        writeBuf[10] = 0;
                        System.arraycopy(writeBuf, 1, temp, 0, 10);
                        writeBuf[11] = (byte) ToolFun.cr_bcc(10, temp);
                        int st = fingerChannel(writeBuf, (byte) 0);
                        LogMg.i("TcFingerDev", this + " imgFingerPrint, send return=" + st);
                        if (st == 0) {
                            final byte[] queryData = new byte[0];
                            final int[] recvLen = new int[1];
                            final byte[] recvData = new byte[CmdCode.MAX_FINGER_SIZE];
                            final long time1 = System.currentTimeMillis();

                            TcFingerDev.this.isBigData = true;
                            ReaderDev.getInstance().devBeep();
                            ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                            while (true) {
                                try {
                                    if (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                                        int st11 = TcFingerDev.this.fingerChannel(queryData, (byte) 1);
                                        if (st11 != 0) {
                                            continue;
                                        }

                                        int ex = TcFingerDev.this.parseTcFingerData(TcFingerDev.this.recvBuffer.length,
                                                TcFingerDev.this.recvBuffer, recvLen, recvData);
                                        if (ex == 0) {
                                            String sdcardPath = Environment.getExternalStorageDirectory()
                                                    .getAbsolutePath();
                                            String dirPath = sdcardPath + "/tcFinger.bmp";
                                            int stSave = TcFingerDev.this.saveTcFigerImage(recvData, recvLen[0],
                                                    dirPath);
                                            if (stSave != 0) {
                                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                                Message message = new Message();
//                                                message.what = 3;
//                                                message.obj = stSave;
//                                                myHandler.sendMessage(message);
                                                listener.onError(stSave, RetCode.GetErrMsg(stSave));
                                                return;
                                            }
                                            ReaderDev.getInstance().devBeep();
                                            ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                            Message message = new Message();
//                                            message.what = 2;
//                                            message.obj = dirPath;
//                                            myHandler.sendMessage(message);
                                            listener.onSuccess(dirPath);
                                            return;
                                        }
//                                        Message message = new Message();
//                                        message.what = 3;
//                                        message.obj = st11;
//                                        myHandler.sendMessage(message);
                                        // if (listener != null) {
                                        // listener.onError(st11,
                                        // RetCode.GetErrMsg(-209) + " " +
                                        // RetCode.GetErrMsg(st11));
                                        // }
                                        listener.onError(st11, RetCode.GetErrMsg(st11));
                                        break;
                                    }

                                } catch (Exception var9) {
                                    LogMg.e("TcFingerDev", "imgFingerPrint error:" + var9.getMessage());
                                } finally {
                                    TcFingerDev.this.isBigData = false;
                                }

                            }
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_TIMEOUT;
//                            myHandler.sendMessage(message);
                            listener.onError(RetCode.ERR_TIMEOUT, RetCode.GetErrMsg(RetCode.ERR_TIMEOUT));
                        } else {
//                            Message message = new Message();
//                            message.what = 3;
//                            message.obj = RetCode.ERR_CHALSEND;
//                            myHandler.sendMessage(message);
                            listener.onError(RetCode.ERR_CHALSEND, RetCode.GetErrMsg(RetCode.ERR_CHALSEND));
                        }

                    }
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }
            })).start();
        }

    }

    private int saveTcFigerImage(byte[] recvData, int recvLen, String dirPath) {
        LogMg.i("TcFingerDev", "saveTcFigerImage enter");
        Bitmap bm = BitmapFactory.decodeByteArray(recvData, 0, recvLen);
        File f = new File(dirPath);
        if (f.exists()) {
            f.delete();
        }

        try {
            FileOutputStream ex = new FileOutputStream(f);
            bm.compress(CompressFormat.PNG, 90, ex);
            ex.flush();
            ex.close();
        } catch (Exception var7) {
            LogMg.e("TcFingerDev", "saveTcFigerImage error:" + var7.getMessage());
            return -211;
        }

        LogMg.i("TcFingerDev", "saveTcFigerImage return=0");
        return 0;
    }

    protected int parseTcFingerData(int readLen, byte[] readBuf, int[] revLen, byte[] revData) {

        byte[] tempBuf1;
        if (this.isBigData) {
            tempBuf1 = new byte[CmdCode.MAX_FINGER_SIZE];
        } else {
            tempBuf1 = new byte[1024];
        }

        if (readLen < 8) {
            return -96;
        } else if (readBuf[0] != 126) {
            return -97;
        } else if (readBuf[1] != 66) {
            return -98;
        } else {
            System.arraycopy(readBuf, 1, tempBuf1, 0, readLen - 1);
            if (ToolFun.cr_bcc(readLen - 1, tempBuf1) != 0) {
                return -99;
            } else if (readBuf[3] != 0) {
                LogMg.e("TcFingerDev", " readBuf[3]" + readBuf[3]);
                return -readBuf[3];
            } else {

                int len11;
                if (readBuf[4] < 0) {
                    len11 = readBuf[4] + 256;
                } else {
                    len11 = readBuf[4];
                }

                int len21;
                if (readBuf[5] < 0) {
                    len21 = readBuf[5] + 256;
                } else {
                    len21 = readBuf[5];
                }

                int len31;
                if (readBuf[6] < 0) {
                    len31 = readBuf[6] + 256;
                } else {
                    len31 = readBuf[6];
                }

                int len41;
                if (readBuf[7] < 0) {
                    len41 = readBuf[7] + 256;
                } else {
                    len41 = readBuf[7];
                }

                if ((len11 << 24) + (len21 << 16) + (len31 << 8) + len41 + 9 != readLen) {
                    return -100;
                } else {
                    revLen[0] = readLen - 9;
                    System.arraycopy(readBuf, 8, revData, 0, revLen[0]);
                    return 0;
                }
            }
        }
    }

    @Override
    public String getFactory() {
        boolean st = ReaderDev.getInstance().isBTConnected();
        if (st) {
            return "北京天诚指纹仪";
        } else {
            return "-1";
        }

    }

    public String sampFingerPrint(final int timeout) {
        LogMg.i("TcFingerDev", this + " sampFingerPrint, timeout=" + timeout);
        int stBd = this.fingerSetBaudRate(0);
        LogMg.i("TcFingerDev", this + " fingerSetBaudRate,return=" + stBd);
        if (stBd != 0) {

            return "-208";
            // + RetCode.GetErrMsg(-208);

        } else {
            byte[] writeBuf = new byte[9];
            byte[] temp = new byte[7];
            writeBuf[0] = 126;
            writeBuf[1] = 66;
            writeBuf[2] = 100;
            writeBuf[3] = 0;
            writeBuf[4] = 0;
            writeBuf[5] = 0;
            writeBuf[6] = 1;
            writeBuf[7] = 2;
            System.arraycopy(writeBuf, 1, temp, 0, 7);
            writeBuf[8] = (byte) ToolFun.cr_bcc(7, temp);
            int st = this.fingerChannel(writeBuf, (byte) 0);
            LogMg.i("TcFingerDev", this + " sampFingerPrint, send return=" + st);
            if (st == 0) {
                final byte[] queryData = new byte[0];
                final int[] recvLen = new int[1];
                final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                final byte[] AscData = new byte[CmdCode.MAX_SIZE_BUFFER * 2];
                final long time1 = System.currentTimeMillis();
                final byte[] tempBuf = new byte[1024];
                final int[] tempLen = new int[1];

                boolean st1 = false;

                while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                    int st11 = TcFingerDev.this.fingerChannel(queryData,
                            (byte) 1);
                    if (st11 == 0) {
                        st11 = TcFingerDev.this.parseTcFingerData(
                                TcFingerDev.this.recvBuffer.length,
                                TcFingerDev.this.recvBuffer, tempLen, tempBuf);
                        if (st11 != 0) {

                            Log.e("天诚", String.valueOf(st11));
                            return String.valueOf(st11);
                        }

                        int allLen = tempLen[0];
                        if (allLen < 3) {

                            return "-100";
                        }

                        boolean userLen = false;
                        int userLen1;
                        if (tempBuf[0] < 0) {
                            userLen1 = tempBuf[0] + 256;
                        } else {
                            userLen1 = tempBuf[0];
                        }

                        boolean fingerLen = false;
                        int fingerLen1;
                        if (tempBuf[1] < 0) {
                            fingerLen1 = tempBuf[1] + 256;
                        } else {
                            fingerLen1 = tempBuf[1];
                        }

                        if (userLen1 + fingerLen1 + 2 != allLen) {
                            return "-100";

                        }

                        recvLen[0] = fingerLen1;
                        System.arraycopy(tempBuf, 2 + userLen1, recvData, 0,
                                fingerLen1);
                        ToolFun.hex_asc(recvData, AscData, recvLen[0]);

                        ReaderDev.getInstance().devBeep();
                        ReaderDev.getInstance().devLCDControl(false, false,
                                false, false, false);

                        return new String(AscData);
                    }
                }

                return "-19";

            } else {
                return "201";
            }

        }
    }
}
