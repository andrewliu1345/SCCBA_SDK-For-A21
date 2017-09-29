package com.joesmate.sdk.reader;


import android.util.Log;

import com.joesmate.sdk.util.CmdCode;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.ToolFun;

import java.io.IOException;

import java.util.Arrays;

public class MT3YCommMan extends CommManagement {
    static final String TAG = MT3YCommMan.class.toString();

    public static boolean isCancel = false;

    public MT3YCommMan() {
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            sInstance = new MT3YCommMan();
            sInstance.start();
        }

    }

    public static CommManagement getInstance() {

        synchronized (CommManagement.class) {
            ensureThreadLocked();
            return sInstance;
        }
    }

    public int SendRecv(byte[] writedata, int writeLen, byte[] readdata, int[] readLen) {

        synchronized (CommManagement.class) {

            int[] len1 = new int[3];
            byte[] Writebuffer = new byte[5 + writeLen];

            byte[] tmp;// = new byte[CmdCode.MAX_SIZE_BUFFER];
            byte[] var29;
            byte[] var30;
            if (this.isBigData) {
                var29 = new byte[CmdCode.MAX_FINGER_SIZE];

            } else {
                var29 = new byte[CmdCode.MAX_SIZE_BUFFER];
                // var30 = new byte[CmdCode.MAX_SIZE_BUFFER];
            }

            long time = 0L;
            long timeD = 0L;

            if (this.socket != null && this.socket.isConnected()) {
                LogMg.i("MT3YCommManagement", this + " SendRecv socket =" + this.socket + ", socket.isConnected()=" + this.socket.isConnected());
                Writebuffer[0] = 2;
                Writebuffer[1] = (byte) (writeLen >> 8);
                Writebuffer[2] = (byte) (writeLen & 0xff);

//                int var26;
//                for (var26 = 0; var26 < writeLen; ++var26) {
//                    Writebuffer[3 + var26] = writedata[var26];
//                }
                System.arraycopy(writedata, 0, Writebuffer, 3, writeLen);
                Writebuffer[3 + writeLen] = (byte) ToolFun.cr_bcc(writeLen, writedata);
                Writebuffer[4 + writeLen] = 3;
                String sendStr = "";

                for (int var26 = 0; var26 < 5 + writeLen; ++var26) {
                    sendStr = sendStr + String.format("%02X", new Object[]{Byte.valueOf(Writebuffer[var26])}) + " ";
                }

                LogMg.e("MT3YCommManagement", "SendData:%s", sendStr);

                try {
                    isCancel = false;
                    InputFulsh();//清空输入缓存
                    this.mOutputStream.flush();
                    this.mOutputStream.write(Writebuffer, 0, 5 + writeLen);
                    ToolFun.Dalpey(32);
                } catch (Exception var23) {
                    this.bluetoothBroken();
                    var23.printStackTrace();
                    LogMg.e("MT3YCommManagement", var23.getMessage());
                    return -17;
                }


                time = System.currentTimeMillis();
                int var27 = 0;

                while (System.currentTimeMillis() - time < (long) this.ulTotalTimeOuts || !isCancel) {
                    int var31 = 0;

                    while (var31 == 0) {
                        try {
                            var31 = this.mInputStream.available();
                            LogMg.e(TAG, "SendRecv: available=%d", var31);
                            ToolFun.Dalpey(10);
                        } catch (IOException var22) {
                            this.bluetoothBroken();
                            var22.printStackTrace();
                            LogMg.e("MT3YCommManagement", var22.getMessage());
                            return -17;
                        }

                        timeD = System.currentTimeMillis();
                        if (timeD - time >= (long) this.ulTotalTimeOuts || isCancel) {
                            return -19;
                        }
                    }

                    LogMg.e("MT3YCommManagement", "Received number:" + var31);

                    int var25;
                    for (int var28 = 0; var28 < var31 && System.currentTimeMillis() - time < (long) this.ulTotalTimeOuts; var28 += var25) {


                        try {
                            if (isCancel) {
                                return -2;
                            }
                            //Arrays.fill(tmp, (byte) 0x00);
                            tmp = new byte[var31];
                            var25 = this.mInputStream.read(tmp);
                            Log.e(TAG, String.format("SendRecv: read=%d", var25));
                            System.arraycopy(tmp, 0, var29, var27, var25);
                            var27 += var25;
                            ToolFun.Dalpey(100);
                        } catch (Exception ex) {
                            this.bluetoothBroken();
                            ex.printStackTrace();
                            LogMg.e("MT3YCommManagement", ex.getMessage());
                            return -17;
                        }

//                        String strRev = "";
//
//                        for (var26 = 0; var26 < var25; ++var26) {
//                            strRev = strRev + String.format("%02X", new Object[]{Byte.valueOf(var29[var26])}) + " ";
//                        }
//
//                        LogMg.d("MT3YCommManagement", "RecvData:" + strRev);

                    }

                    if (var27 > 3) {
                        if (var29[1] < 0) {
                            len1[0] = var29[1] + 256;
                        } else {
                            len1[0] = var29[1];
                        }

                        if (var29[2] < 0) {
                            len1[1] = var29[2] + 256;
                        } else {
                            len1[1] = var29[2];
                        }

                        if (len1[0] * 256 + len1[1] + 5 == var27) {
                            break;
                        }
                    }
                }

                if (var27 < 7) {
                    return -100;
                } else if (var29[0] != 2) {
                    return -97;
                } else if (var29[var27 - 1] != 3) {
                    return -98;
                } else {
                    //Arrays.fill(var30, (byte) 0);
                    var30 = new byte[var27 - 5];
                    System.arraycopy(var29, 3, var30, 0, var27 - 5);
                    byte x = ToolFun.cr_bcc(var27 - 5, var30);
                    byte y = var29[var27 - 2];
                    if (x != y) {
                        Log.d(TAG, String.format("CRC: CRC1=%02X,CRC2=%02X", x, y));
                        return -101;
                    } else if (var29[3] == 0 && var29[4] == 0) {
                        readLen[0] = len1[0] * 256 + len1[1] - 2;
                        if (readdata.length < readLen[0]) {
                            LogMg.d("MT3YCommManagement", "readdata.length:%d,readLen[0]=%d", readdata.length, readLen);
                            LogMg.e("MT3YCommManagement", "接收缓冲区溢出");
                            return -210;
                        } else {
                            System.arraycopy(var30, 2, readdata, 0, readLen[0]);
//                            for (var26 = 0; var26 < readLen[0]; ++var26) {
//                                readdata[var26] = var29[5 + var26];
//                            }

                            LogMg.i("MT3YCommManagement", "Received data length :" + readLen[0]);
                            return 0;
                        }
                    } else {
                        LogMg.i("MT3YCommManagement", "ReadBuffer[3]=" + var29[3]);
                        LogMg.i("MT3YCommManagement", "ReadBuffer[4]=" + var29[4]);
                        LogMg.i("MT3YCommManagement", "Status word error");
                        return -(var29[3] * 256 + var29[4]);
                    }
                }
            } else {
                this.bluetoothBroken();
                return -17;
            }
        }
    }
}