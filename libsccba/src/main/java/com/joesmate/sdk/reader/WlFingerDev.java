package com.joesmate.sdk.reader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;


import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.util.CmdCode;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;
import com.joesmate.sdk.util.ToolFun;

import java.util.Arrays;

import static com.joesmate.sdk.mtreader.BlueToothClass.WriteBMP;

public class WlFingerDev extends FingerDev {

    private static final WlFingerDev sInstance = new WlFingerDev();

    protected WlFingerDev() {
    }

//    private static void ensureThreadLocked() {
//        if (sInstance == null) {
//            sInstance = new WlFingerDev();
//        }
//
//    }

    public static WlFingerDev getInstance() {

        synchronized (WlFingerDev.class) {
            //  ensureThreadLocked();
            return sInstance;
        }
    }

    @Override
    public void regFingerPrint(final ReturnListener listener, final int timeout) {

        synchronized (FingerDev.class) {
            m_ReturnListener = listener;
//            (new Thread(new Runnable() {
//                public void run() {
            LogMg.i("WlFingerDev", this + " regFingerPrint, timeout=" + timeout);
            int stBd = fingerSetBaudRate(0);
            LogMg.i("WlFingerDev", this + " fingerSetBaudRate, return=" + stBd);
            if (stBd != 0) {
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);
                listener.onError(stBd, RetCode.GetErrMsg(stBd));

            } else {
                byte[] temp = new byte[7];
                byte[] splitdata = new byte[14];
                byte[] writeBuf = new byte[16];
                temp[0] = 0;
                temp[1] = 4;
                temp[2] = 11;
                temp[3] = 0;
                temp[4] = 0;
                temp[5] = 0;
                temp[6] = (byte) ToolFun.cr_bcc(6, temp);
                ToolFun.splitFun(temp, (byte) 7, splitdata, (byte) 14);
                writeBuf[0] = 2;
                System.arraycopy(splitdata, 0, writeBuf, 1, 14);
                writeBuf[15] = 3;
                int st = fingerChannel(writeBuf, (byte) 0);
                LogMg.i("WlFingerDev", this + " regFingerPrint, send return=" + st);
                if (st == 0) {
                    final byte[] queryData = new byte[0];
                    final int[] recvLen = new int[1];
                    final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];

                    final long time1 = System.currentTimeMillis();
                    ReaderDev.getInstance().devBeep();
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                    while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                        int st11 = WlFingerDev.this.fingerChannel(queryData, (byte) 1);
                        if (st11 == 0) {
                            st11 = WlFingerDev.this.parseWelFingerData(WlFingerDev.this.recvBuffer.length,
                                    WlFingerDev.this.recvBuffer, recvLen, recvData);
                            if (st11 == 0) {
                                String finger = ToolFun.hex_split(recvData, recvLen[0]);
                                ReaderDev.getInstance().devBeep();
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                listener.onSuccess(finger);
//                                        Message message = new Message();
//                                        message.what = 2;
//                                        message.obj = finger;
//                                        myHandler.sendMessage(message);
                                return;
                            }
                            ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
//                                    Message message = new Message();
//                                    message.what = 3;
//                                    message.obj = RetCode.ERR_PARSEFINGER;
//                                    myHandler.sendMessage(message);
                            listener.onError(RetCode.ERR_PARSEFINGER, RetCode.GetErrMsg(RetCode.ERR_PARSEFINGER));
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
//                }
//            })).start();
        }
    }

    @Override
    public void sampFingerPrint(final ReturnListener listener, final int timeout) {

        synchronized (FingerDev.class) {
            m_ReturnListener = listener;
//            (new Thread(new Runnable() {
//                public void run() {
            LogMg.i("WlFingerDev", this + " sampFingerPrint, timeout=" + timeout);
            int stBd = fingerSetBaudRate(0);
            LogMg.i("WlFingerDev", this + " fingerSetBaudRate,return=" + stBd);
            if (stBd != 0) {
                listener.onError(stBd, RetCode.GetErrMsg(stBd));
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);

            } else {
                byte[] temp = new byte[7];
                byte[] splitdata = new byte[14];
                byte[] writeBuf = new byte[16];
                temp[0] = 0;
                temp[1] = 4;
                temp[2] = 12;
                temp[3] = 0;
                temp[4] = 0;
                temp[5] = 0;
                temp[6] = (byte) ToolFun.cr_bcc(6, temp);
                ToolFun.splitFun(temp, (byte) 7, splitdata, (byte) 14);
                writeBuf[0] = 2;
                System.arraycopy(splitdata, 0, writeBuf, 1, 14);
                writeBuf[15] = 3;
                int st = fingerChannel(writeBuf, (byte) 0);
                LogMg.i("WlFingerDev", this + " sampFingerPrint, send return=" + st);
                if (st == 0) {
                    final byte[] queryData = new byte[0];
                    final int[] recvLen = new int[1];
                    final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];

                    final long time1 = System.currentTimeMillis();
                    ReaderDev.getInstance().devBeep();
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                    while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                        int st11 = WlFingerDev.this.fingerChannel(queryData, (byte) 1);
                        if (st11 == 0) {
                            st11 = WlFingerDev.this.parseWelFingerData(WlFingerDev.this.recvBuffer.length,
                                    WlFingerDev.this.recvBuffer, recvLen, recvData);
                            if (st11 == 0) {

                                ReaderDev.getInstance().devBeep();
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                String finger = ToolFun.hex_split(recvData, recvLen[0]);
//                                        Message message = new Message();
//                                        message.what = 2;
//                                        message.obj = finger;
//                                        myHandler.sendMessage(message);
                                listener.onSuccess(finger);

                                return;

                            } else {
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                listener.onError(RetCode.ERR_PARSEFINGER, RetCode.GetErrMsg(RetCode.ERR_PARSEFINGER));
//                                        Message message = new Message();
//                                        message.what = 3;
//                                        message.obj = RetCode.ERR_PARSEFINGER;
//                                        myHandler.sendMessage(message);
                                return;
                            }

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
//                }
//            })).start();
        }
    }

    @Override
    public void imgFingerPrint(final ReturnListener listener, final int timeout) {

        synchronized (FingerDev.class) {
            m_ReturnListener = listener;
//            (new Thread(new Runnable() {
//                public void run() {
            LogMg.i("WlFingerDev", this + " imgFingerPrint, timeout=" + timeout);
            int stBd = fingerSetBaudRate(0);
            LogMg.i("WlFingerDev", this + " fingerSetBaudRate,return=" + stBd);
            if (stBd != 0) {
//                        Message message = new Message();
//                        message.what = 3;
//                        message.obj = stBd;
//                        myHandler.sendMessage(message);
                listener.onError(stBd, RetCode.GetErrMsg(stBd));

            } else {
                byte[] temp = new byte[7];
                byte[] splitdata = new byte[14];
                byte[] writeBuf = new byte[16];
                temp[0] = 0;
                temp[1] = 4;
                temp[2] = 24;
                temp[3] = 0;
                temp[4] = 1;
                temp[5] = 0;
                temp[6] = (byte) ToolFun.cr_bcc(6, temp);
                ToolFun.splitFun(temp, (byte) 7, splitdata, (byte) 14);
                writeBuf[0] = 2;
                System.arraycopy(splitdata, 0, writeBuf, 1, 14);
                writeBuf[15] = 3;
                int st = fingerChannel(writeBuf, (byte) 0);
                LogMg.i("WlFingerDev", this + " imgFingerSize, send return=" + st);
                if (st == 0) {
                    final byte[] queryData = new byte[0];
                    final int[] recvLen = new int[1];
                    final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                    final FingerPrint finger = new FingerPrint();
                    final long time1 = System.currentTimeMillis();

                    int st1 = 0;
                    ReaderDev.getInstance().devBeep();
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, true);
                    while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                        st1 = WlFingerDev.this.fingerChannel(queryData, (byte) 1);
                        if (st1 == 0) {
                            st1 = WlFingerDev.this.parseWelFingerData(WlFingerDev.this.recvBuffer.length,
                                    WlFingerDev.this.recvBuffer, recvLen, recvData);
                            WlFingerDev.this.parseFingerData(recvData, recvLen[0], finger);
                            break;
                        }
                    }

                    if (st1 == 0) {
                        st = WlFingerDev.this.Wel_Get_Image(timeout * 1000, finger);
                        if (st == 0) {
                            ReaderDev.getInstance().devBeep();
                            ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String dirPath = sdcardPath + "/finger.bmp";
//                                    Message message = new Message();
//                                    message.what = 2;
//                                    message.obj = dirPath;
//                                    myHandler.sendMessage(message);
                            listener.onSuccess(dirPath);
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
//                }
//            })).start();
        }
    }

    protected int Wel_Get_Image(int timeout, FingerPrint finger) {
        byte[] pBmp = new byte[CmdCode.MAX_SIZE_BUFFER * 11];
        int st = -1;
        if (finger != null) {
            st = this.Wel_Get_UploadImage(timeout, finger.getImageBufferData(), finger.getImageBufferLength());
            if (st == 0) {
                st = WriteBMP(finger.getImageBufferData(), pBmp, finger.getWidth(), finger.getHeight());
            }
        }

        return st;
    }

    private int Wel_Get_UploadImage(int timeout, byte[] imageBufferData, int[] imageBufferLength) {

        synchronized (FingerDev.class) {
            short num = 0;
            byte[] resp_hex = new byte[CmdCode.MAX_SIZE_BUFFER];
            int[] recvLen = new int[1];
            boolean isFinish = true;
            Arrays.fill(imageBufferData, (byte) 0);
            Arrays.fill(imageBufferLength, 0);
            int stBd = this.fingerSetBaudRate(4);
            LogMg.i("WlFingerDev", this + " Wel_Get_UploadImage, SetBaudRate return=" + stBd);
            if (stBd != 0) {
                return -202;
            } else {
                while (isFinish) {
                    int st = this.Wel_GetImageByNum(timeout, num, resp_hex, recvLen);
                    if (st != 0) {
                        break;
                    }

                    System.arraycopy(resp_hex, 0, imageBufferData, imageBufferLength[0], recvLen[0]);
                    imageBufferLength[0] += recvLen[0];
                    if (recvLen[0] < 1024) {
                        this.fingerSetBaudRate(0);
                        return 0;
                    }

                    ++num;
                }

                this.fingerSetBaudRate(0);
                return -2;
            }
        }
    }

    private int Wel_GetImageByNum(int timeout, short num, byte[] resp_hex, int[] recvLen) {
        LogMg.i("WlFingerDev", this + " Wel_GetImageByNum, timeout=" + timeout);
        byte[] temp = new byte[9];
        byte[] splitdata = new byte[18];
        byte[] writeBuf = new byte[20];
        temp[0] = 0;
        temp[1] = 6;
        temp[2] = 25;
        temp[3] = 0;
        temp[4] = 0;
        temp[5] = 0;
        temp[6] = (byte) (num / 256);
        temp[7] = (byte) (num % 256);
        temp[8] = (byte) ToolFun.cr_bcc(8, temp);
        ToolFun.splitFun(temp, (byte) 9, splitdata, (byte) 18);
        writeBuf[0] = 2;
        System.arraycopy(splitdata, 0, writeBuf, 1, 18);
        writeBuf[19] = 3;
        int st = this.fingerChannel(writeBuf, (byte) 0);
        ToolFun.Dalpey(160);
        LogMg.i("WlFingerDev", this + " Wel_GetImageByNum, send return=" + st);
        if (st == 0) {
            long time1 = System.currentTimeMillis();
            Arrays.fill(resp_hex, (byte) 0);
            Arrays.fill(recvLen, 0);
            byte[] queryData = new byte[0];

            while (System.currentTimeMillis() - time1 < (long) timeout) {

                synchronized (FingerDev.class) {
                    int st1 = this.fingerChannel(queryData, (byte) 1);
                    if (st1 == 0) {
                        LogMg.i("WlFingerDev", this + " Wel_GetImageByNum, query return=" + st1);
                        this.parseWelFingerData(this.recvBuffer.length, this.recvBuffer, recvLen, resp_hex);
                        return 0;
                    }
                    ToolFun.Dalpey(160);
                }
            }
        }

        LogMg.i("WlFingerDev", this + " Wel_GetImageByNum, query return=" + -203);
        return -203;
    }

    protected void parseFingerData(byte[] recvData, int recvLen, FingerPrint finger) {
        if (finger != null && recvLen > 3) {

            int len11;
            if (recvData[0] < 0) {
                len11 = recvData[0] + 256;
            } else {
                len11 = recvData[0];
            }

            int len21;
            if (recvData[1] < 0) {
                len21 = recvData[1] + 256;
            } else {
                len21 = recvData[1];
            }

            finger.setWidth(len11 * 256 + len21);
            LogMg.i("WlFingerDev", this + " imgFingerSize, width=" + len11 * 256 + len21);
            if (recvData[2] < 0) {
                len11 = recvData[2] + 256;
            } else {
                len11 = recvData[2];
            }

            if (recvData[3] < 0) {
                len21 = recvData[3] + 256;
            } else {
                len21 = recvData[3];
            }

            finger.setHeight(len11 * 256 + len21);
            LogMg.i("WlFingerDev", this + " imgFingerSize, height=" + len11 * 256 + len21);
        }

    }

    protected int parseWelFingerData(int readLen, byte[] readBuf, int[] revLen, byte[] revData) {
        byte[] tempBuf = new byte[1524];
        byte[] crcBuf = new byte[1524];
        int[] mergeLen = new int[1];
        int st = this.mergeData(readBuf, readLen, tempBuf, mergeLen);
        if (st != 0) {
            return st;
        } else {
            int allLen = mergeLen[0];
            if (tempBuf[0] != 2) {
                return -97;
            } else if (tempBuf[allLen - 1] != 3) {
                return -98;
            } else {

                int len11;
                if (tempBuf[1] < 0) {
                    len11 = tempBuf[1] + 256;
                } else {
                    len11 = tempBuf[1];
                }

                int len21;
                if (tempBuf[2] < 0) {
                    len21 = tempBuf[2] + 256;
                } else {
                    len21 = tempBuf[2];
                }

                if (len11 * 256 + len21 + 5 != allLen) {
                    return -100;
                } else if (tempBuf[3] != 0) {
                    LogMg.i("WlFingerDev", " WelFINGER tempBuf[3]" + tempBuf[3]);
                    return -tempBuf[3];
                } else {
                    System.arraycopy(tempBuf, 1, crcBuf, 0, allLen - 2);
                    if (ToolFun.cr_bcc(allLen - 2, crcBuf) != 0) {
                        return -101;
                    } else {
                        revLen[0] = allLen - 5 - 2;
                        System.arraycopy(tempBuf, 5, revData, 0, revLen[0]);
                        return 0;
                    }
                }
            }
        }
    }

    private int mergeData(byte[] readBuf, int allLen, byte[] mergeBuf, int[] mergeLen) {
        byte[] temp = new byte[CmdCode.MAX_SIZE_BUFFER];
        if (allLen < 2) {
            return -113;
        } else if (allLen % 2 != 0) {
            return -112;
        } else {
            System.arraycopy(readBuf, 1, temp, 0, allLen - 2);
            mergeBuf[0] = readBuf[0];

            for (int var7 = 0; var7 < (allLen - 2) / 2; ++var7) {
                mergeBuf[1 + var7] = (byte) ((temp[var7 * 2] - 48) * 16 + temp[var7 * 2 + 1] - 48);
            }

            mergeBuf[1 + (allLen - 2) / 2] = readBuf[allLen - 1];
            mergeLen[0] = 2 + (allLen - 2) / 2;
            return 0;
        }
    }

    @Override
    public String getFactory() {
        boolean st = ReaderDev.getInstance().isBTConnected();
        if (st) {
            return "2";
        } else {
            return "-1";
        }
    }

    public String sampFingerPrint(final int timeout) {

        Class var3 = FingerDev.class;
        synchronized (FingerDev.class) {
            LogMg.i("WlFingerDev", this + " sampFingerPrint, timeout="
                    + timeout);
            int stBd = this.fingerSetBaudRate(0);
            LogMg.i("WlFingerDev", this + " fingerSetBaudRate,return=" + stBd);
            if (stBd != 0) {
                return "-208";
                // + RetCode.GetErrMsg(-208);

            } else {
                byte[] temp = new byte[7];
                byte[] splitdata = new byte[14];
                byte[] writeBuf = new byte[16];
                temp[0] = 0;
                temp[1] = 4;
                temp[2] = 12;
                temp[3] = 0;
                temp[4] = 0;
                temp[5] = 0;
                temp[6] = (byte) ToolFun.cr_bcc(6, temp);
                ToolFun.splitFun(temp, (byte) 7, splitdata, (byte) 14);
                writeBuf[0] = 2;
                System.arraycopy(splitdata, 0, writeBuf, 1, 14);
                writeBuf[15] = 3;
                int st = this.fingerChannel(writeBuf, (byte) 0);
                LogMg.i("WlFingerDev", this + " sampFingerPrint, send return="
                        + st);
                if (st == 0) {
                    final byte[] queryData = new byte[0];
                    final int[] recvLen = new int[1];
                    final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                    final byte[] AscData = new byte[CmdCode.MAX_SIZE_BUFFER * 2];
                    final long time1 = System.currentTimeMillis();

                    Class var1 = FingerDev.class;
                    synchronized (FingerDev.class) {
                        boolean st1 = false;

                        while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                            int st11 = WlFingerDev.this.fingerChannel(
                                    queryData, (byte) 1);
                            ToolFun.Dalpey(200);
                            if (st11 == 0) {
                                WlFingerDev.this.parseWelFingerData(
                                        WlFingerDev.this.recvBuffer.length,
                                        WlFingerDev.this.recvBuffer, recvLen,
                                        recvData);
                                ToolFun.hex_asc(recvData, AscData, recvLen[0]);

                                String finger = new String(AscData);
                                return finger;

                            }
                        }
                        // + RetCode.GetErrMsg(-19)
                        return "-19";
                    }

                }

            }
            return "-201";
            // +RetCode.GetErrMsg(-201);

        }
    }

    public String imgFingerPrint(final int timeout) {
        Class var3 = FingerDev.class;
        synchronized (FingerDev.class) {
            isBigData = true;
            //  byte[] temp = new byte[7];
            byte[] splitdata = new byte[14];
            byte[] writeBuf = new byte[3];
            writeBuf[0] = 0x00;
            writeBuf[1] = 0x00;
            writeBuf[2] = (byte) timeout;
            int st = this.fingerChannel(writeBuf, (byte) 0x02);
            LogMg.i("WlFingerDev", this + " imgFingerSize, send return="
                    + st);
            if (st == 0) {
                Bitmap bm = BitmapFactory.decodeByteArray(this.recvBuffer, 0, this.recvBuffer.length);
                ToolFun.saveBitmap("/sdcard/finger.jpg", bm);
                return "0";
            } else {
                return "-201";
            }

        }

    }

    public String imgFingerPrint(final int timeout, String sdcardPath) {
        Class var3 = FingerDev.class;
        synchronized (FingerDev.class) {
            LogMg.i("WlFingerDev", this + " imgFingerPrint, timeout=" + timeout);
            int stBd = this.fingerSetBaudRate(0);
            LogMg.i("WlFingerDev", this + " fingerSetBaudRate,return=" + stBd);
            if (stBd != 0) {

                return "-208";

            } else {
                byte[] temp = new byte[7];
                byte[] splitdata = new byte[14];
                byte[] writeBuf = new byte[16];
                temp[0] = 0;
                temp[1] = 4;
                temp[2] = 24;
                temp[3] = 0;
                temp[4] = 1;
                temp[5] = 0;
                temp[6] = (byte) ToolFun.cr_bcc(6, temp);
                ToolFun.splitFun(temp, (byte) 7, splitdata, (byte) 14);
                writeBuf[0] = 2;
                System.arraycopy(splitdata, 0, writeBuf, 1, 14);
                writeBuf[15] = 3;
                int st = this.fingerChannel(writeBuf, (byte) 0);
                LogMg.i("WlFingerDev", this + " imgFingerSize, send return="
                        + st);
                if (st == 0) {
                    final byte[] queryData = new byte[0];
                    final int[] recvLen = new int[1];
                    final byte[] recvData = new byte[CmdCode.MAX_SIZE_BUFFER];
                    final FingerPrint finger = new FingerPrint();
                    final long time1 = System.currentTimeMillis();

                    Class var1 = FingerDev.class;
                    synchronized (FingerDev.class) {
                        int st1 = 0;

                        while (System.currentTimeMillis() - time1 < (long) (timeout * 1000)) {
                            st1 = WlFingerDev.this.fingerChannel(queryData,
                                    (byte) 1);
                            if (st1 == 0) {
                                WlFingerDev.this.parseWelFingerData(
                                        WlFingerDev.this.recvBuffer.length,
                                        WlFingerDev.this.recvBuffer, recvLen,
                                        recvData);
                                WlFingerDev.this.parseFingerData(recvData,
                                        recvLen[0], finger);
                                break;
                            }
                        }

                        if (st1 == 0) {
                            st = WlFingerDev.this.Wel_Get_Image(timeout * 1000,
                                    finger);
                            if (st == 0) {
                                // String sdcardPath =
                                // Environment.getExternalStorageDirectory().getAbsolutePath();
                                String dirPath = sdcardPath;

                                return dirPath;

                            }
                        }
                        return "-19";

                    }

                } else {
                    return "-201";
                }

            }
        }
    }
}