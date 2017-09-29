package com.joesmate.sccba;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.reader.EMVTrade;

import com.joesmate.sdk.reader.IDCardDev;
import com.joesmate.sdk.reader.KeyboardDev;
import com.joesmate.sdk.reader.MT3YCommMan;
import com.joesmate.sdk.reader.ReaderDev;
import com.joesmate.sdk.reader.SignatureDev;
import com.joesmate.sdk.reader.TcFingerDev;
import com.joesmate.sdk.reader.WlFingerDev;
import com.joesmate.sdk.reader.ZzFingerDev;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.ToolFun;
import com.siro.touch.lib.drivers.api.SccbaTouchDriversApi;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import static com.joesmate.sdk.util.ToolFun.saveBitmap;

public class SccbaReader implements SccbaTouchDriversApi {

    static final String TAG = SccbaReader.class.toString();
    KeyboardDev key = KeyboardDev.getInstance();
    EMVTrade emv = EMVTrade.getInstance();
    IDCardDev id = IDCardDev.getInstance();
    ReaderDev reader = ReaderDev.getInstance();

    public SccbaReader() {
        LogMg.e(TAG, "中航 API 加载成功");
    }

    /***
     * 连接设备
     *
     **/

    public String[] SCCBA_Connect(BluetoothDevice device) {
        String[] str = new String[2];
        LogMg.e(TAG, "A21 正在连接");
        int i = reader.openDevice(device);
        if (i == 0) {
            str[0] = String.valueOf(i);
            str[1] = "连接设备成功  ";
            ToolFun.Dalpey(160);
            LogMg.e(TAG, "A21 连接成功");
            // reader.PlayVoice("设备已连接");
        } else {
            str[0] = String.valueOf(i);

            str[1] = "设备连接失败  ";
            LogMg.e(TAG, "A21 连接失败");
        }
        return str;
    }

    /***
     * 断开设备
     *
     **/
    public String[] SCCBA_Disconnect() {
        String[] str = new String[2];
        String a;
        int i = reader.closeDevice();
        if (i == 0) {
            str[0] = String.valueOf(i);

            str[1] = "断开设备成功  ";
        } else {
            str[0] = String.valueOf(i);

            str[1] = "设备连接失败  ";
        }
        return str;
    }

    /***
     * 获取密码
     *
     * 输入参数 iEncryType 加密方式 1-不加密 5 -SM4 加密 iTimes 输入密码次数 iLength 密码长度,最小 1 位，最大
     * 12 位 strTimeout 超时时间，单位为秒 strVoice 要播放的语音 EndType 密码输入的结束方式： 0-
     * 按【确认】键或到达指定的密码长度，开始加密处理并上送密码； 1- 按【确认】键，开始加密处理并上送密码； 2-
     * 到达指定的密码长度才开始加密处理并上送密码，即使按【确认】键也不上送密码； 3- 到达指定的密码长度并按【确认】键，开始加密处理上送密码； *
     */
    String[] pin = new String[3];

    public String[] SCCBA_ReadPin(int iEncryType, int iTimes, int iLength, String strVoice, int EndType,
                                  String strTimeout) {
        if (strTimeout == null || strTimeout.equals(""))
            return new String[]{"-120", "超时不可为空"};

        int timeout = Integer.parseInt(strTimeout);
        String pinplain = "";
        byte[] pinbuff = KeyboardDev.getInstance().getPassword(iEncryType, iTimes, iLength, strVoice, EndType, timeout);


        Arrays.fill(pin, "");
        if (pinbuff != null && pinbuff.length > 0) {
            if (iEncryType == 1)
                pinplain = ToolFun.printHexString(pinbuff).substring(2, pinbuff[0] + 2);
            else if (iEncryType == 5)
                pinplain = ToolFun.printHexString(pinbuff);
            pin[0] = "0";
            pin[1] = pinplain;
        } else {
            pin[0] = "-120";
            pin[1] = "密码获取失败";
        }
        return pin;
    }

    String idnum = null;
    String startDate = null;
    String endDate = null;
    String name = null;
    String cardno = null;
    String track2 = null;
    String track3 = null;

    public String[] SCCBA_GetICCInfo(int ICType, String AIDList, String TagList, String strTimeout) {
        if (strTimeout == null || strTimeout == "") {
            return new String[]{"-120", "请填写超时"};
        }
        short timeout = Short.parseShort(strTimeout);
        return emv.GetICCInfo(ICType, AIDList, TagList, timeout);
    }

    private String getData(String string, String str, String str1) {
        boolean data = string.contains(str);
        String returnData = null;
        if (data) {

            returnData = string.replace(str, "");

            int i2 = returnData.indexOf(str1);

            returnData = returnData.substring(0, i2);

        }
        return returnData;
    }

    String[] strMag = new String[4];
    String[] track2data = new String[1];
    String[] track3data = new String[1];
    String[] track23data = new String[1];

    String magString[] = new String[8];

    /**
     * 获取磁条卡数据 输入参数 readModel：2-表示读取二磁道数据3-标识读取三磁道数据23-表示读取二三磁道数据 iTimeout
     * 超时时间，单位为妙 返回值 aryRet[0]：0，成功。其它，失败，具体错误代码:……。
     * aryRet[1]：读取的磁道数据二三磁数据用”<”分隔 aryRet[2]：二磁数据 aryRet[3]：三磁数据
     * 如果aryRet[0]为其它，则aryRet[1]为错误描述。
     **/
    @SuppressWarnings("unused")
    public String[] SCCBA_MsfRead(String readModel, String strTimeout) {
        if (strTimeout == null || strTimeout.equals(""))
            return new String[]{"-120", "超时不可为空"};
        JSONObject json = new JSONObject();
        try {
            int timeout = Integer.parseInt(strTimeout);
            //reader.PlayVoice("请刷卡");
            //  ToolFun.Dalpey(200);
            json = emv.readMg(timeout);
            String errcode = json.getString("ErrCode");

            String ErrMsg = json.getString("ErrMsg");

            strMag[0] = errcode;
            strMag[1] = ErrMsg;
            String track2 = json.getString("Track2");

            String track3 = json.getString("Track3");
            if (readModel.equals("2")) {
                strMag[0] = errcode;
                strMag[2] = track2;

                return strMag;
            } else if (readModel.equals("3")) {
                strMag[0] = errcode;
                strMag[3] = track3;

            } else if (readModel.equals("23")) {
                strMag[0] = errcode;
                strMag[1] = track2 + "<" + track3;

            }

        } catch (Exception e) {
        }

        return strMag;

    }

    String stri[] = new String[8];

    private int getFinger(String finger) {
        if (finger.equals("-19")) {
            stri[0] = "-19";
            stri[1] = "通讯超时";
            return -19;
        } else if (finger.equals("-201")) {
            stri[0] = "-201";
            stri[1] = "下发指令失败";
            return -201;
        } else if (finger.equals("-208")) {
            stri[0] = "-208";
            stri[1] = "设置波特率失败";
            return -208;
        } else {
            stri[0] = "0";
            stri[1] = finger;
            stri[2] = "";
            stri[3] = finger;
            long i = (long) finger.length();

            String s = getPrintSize(i);
            stri[4] = s;
            return 0;
        }
    }

    /**
     * 指纹数据
     **/
    public String[] SCCBA_ReadFinger(String fingerImgPath, String fingerType, String stringtimeout) {
        if (stringtimeout == null || stringtimeout.equals(""))
            return new String[]{"-120", "超时不可为空"};
        // reader.PlayVoice("请按指纹");
        int timeout = Integer.parseInt(stringtimeout);
        String finger;
        Arrays.fill(stri, "");
        LogMg.d(TAG, "SCCBA_ReadFinger.fingerType =%s", fingerType);
        // if (fingerType.equals("1")) {
        finger = WlFingerDev.getInstance().sampFingerPrint(timeout);
        if (getFinger(finger) == 0) {
            String s = WlFingerDev.getInstance().imgFingerPrint(timeout);
            if (s == "0") {
                Bitmap bm = BitmapFactory.decodeFile("/sdcard/finger.jpg");
                saveBitmap(fingerImgPath, bm);
            }
        }


//        }
//        if (fingerType.equals("3")) {
//            finger = ZzFingerDev.getInstance().sampFingerPrint(timeout);
//            Log.e("指纹", finger);
//            getFinger(finger);
//        } else if (fingerType.equals("2")) {
//            finger = TcFingerDev.getInstance().sampFingerPrint(timeout);
//            getFinger(finger);
//            return stri;
//        }

        return stri;

    }

    private static String getPrintSize(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }

    /**
     * *身份证
     **/

    public String[] SCCBA_ReadIDFullInfo(String strPhotoPath, String strTiemout) {
        if (strTiemout == null || strTiemout.equals(""))
            return new String[]{"-120", "超时不可为空"};
        int timeout = Integer.parseInt(strTiemout) * 1000;
        Bitmap photo = null;
        String[] idinfo = new String[15];
        //reader.PlayVoice("请刷身份证");
        IDCardDev idmessage = IDCardDev.getInstance();
        try {
            int i = idmessage.Read(0, timeout);
            if (i == 0) {

                if (idmessage.getID_Type() == "I") {
                    idinfo[0] = "-1";
                    idinfo[1] = "身份证读取失败";
                    return idinfo;
                }
                idinfo[0] = "0";
                idinfo[1] = idmessage.getName();

                idinfo[2] = idmessage.getSex();

                idinfo[3] = idmessage.getNation();

                idinfo[4] = idmessage.getBirth();

                idinfo[5] = idmessage.getAddr();

                idinfo[6] = idmessage.getIDNum();
                idinfo[7] = idmessage.getGrantDepart();

                idinfo[8] = idmessage.getDateStart() + "-" + idmessage.getDateEnd();

                photo = idmessage.getPhoto();
                saveBitmap(strPhotoPath, photo);
            } else {
                idinfo[0] = String.valueOf(i);
                idinfo[1] = "身份证读取失败";
            }
        } catch (Exception e) {
        }
        return idinfo;
    }


    /**
     * 初始化密码键盘
     **/
    int i = 0;

    public int SCCBA_InitPinPad() {
        return key.InitPinPad();
    }

    /**
     * 更新主密钥
     **/
    public int SCCBA_UpdateMKey(int ZmkIndex, int ZmkLength, String Zmk, String CheckValue) {
        return key.UpdateMKey(ZmkIndex, ZmkLength, Zmk, CheckValue);
    }

    int str1;

    /****
     *
     * 下载工作密钥
     *
     ***/
    public int SCCBA_DownLoadWKey(int MKeyIndex, int WKeyIndex, int WKeyLength, String Key, String CheckValue) {
        return key.DownLoadWKey(MKeyIndex, WKeyIndex, WKeyLength, Key, CheckValue);
    }

    int returnpin;

    /***
     *
     * 激活工作密钥
     ***/
    public int SCCBA_ActiveWKey(int MKeyIndex, int WKeyIndex) {
        return key.ActiveWKey(MKeyIndex, WKeyIndex);
    }

    @Override
    public String[] SCCBA_ARPCExeScript(int arg0, String arg1, String arg2, String arg3) {
        return emv.ARPCExeScript(arg0, arg1, arg2, arg3);
    }

    @Override
    public String[] SCCBA_GetARQC(int arg0, String arg1, String arg2, String arg3) {
        if (arg3 == null || arg3 == "") {
            return new String[]{"-120", "请填写超时"};
        }
        short timeout = Short.parseShort(arg3);
        return emv.GetARQC(arg0, arg1, arg2, timeout);

    }

    @Override
    public String[] SCCBA_GetICAndARQCInfo(int arg0, String arg1, String arg2, String arg3, String arg4) {
        if (arg4 == null || arg4 == "") {
            return new String[]{"-120", "请填写超时"};
        }
        short timeout = Short.parseShort(arg4);
        return emv.SCCBA_GetICAndARQCInfo(arg0, arg1, arg2, arg3, timeout);
    }

    @Override
    public String[] SCCBA_GetLoadLog(int arg0, int arg1, String arg2, String arg3) {
        if (arg3 == null || arg3 == "") {
            return new String[]{"-120", "请填写超时"};
        }
        short timeout = Short.parseShort(arg3);
        return emv.GetLoadLog(arg0, arg1, arg2, timeout);
    }

    @Override
    public String[] SCCBA_GetTrDetail(int arg0, int arg1, String arg2) {
        if (arg2 == null || arg2.equals("")) {
            return new String[]{"-120", "请填写超时"};
        }
        short timeout = Short.parseShort(arg2);
        return emv.GetTrDetail(arg0, arg1, timeout);
    }

    @Override
    public String[] SCCBA_MsfWrite(String arg0, String arg1, String arg2) {
        String[] aryRet = new String[2];
        aryRet[0] = "-120";
        aryRet[1] = "其它错误";
        return aryRet;
    }

    @Override
    public String[] SCCBA_ReadAPRPInfo(String arg0, String arg1) {
        if (arg1 == null || arg1.equals(""))
            return new String[]{"-120", "超时不可为空"};
        int timeout = Integer.parseInt(arg1) * 1000;
        Bitmap photo = null;
        String[] idinfo = new String[15];
        // reader.PlayVoice("请刷身份证");
        IDCardDev idmessage = IDCardDev.getInstance();
        try {
            int i = idmessage.Read(0, timeout);
            if (i == 0) {

                if (idmessage.getID_Type() != "I") {
                    idinfo[0] = "-1";
                    idinfo[1] = "居留证读取失败";
                    return idinfo;
                }

                idinfo[0] = "0";
                idinfo[1] = idmessage.getEnName();

                idinfo[2] = idmessage.getSex();

                idinfo[3] = idmessage.getIDNum();

                idinfo[4] = idmessage.getUNMARC();

                idinfo[5] = idmessage.getChName();

                idinfo[6] = idmessage.getDateStart();
                idinfo[7] = idmessage.getDateEnd();

                idinfo[8] = idmessage.getBirth();
                idinfo[9] = idmessage.getID_Ver();
                idinfo[10] = idmessage.getGrantDepart();
                idinfo[11] = idmessage.getID_Type();

                photo = idmessage.getPhoto();
                saveBitmap(arg0, photo);

            } else {
                idinfo[0] = String.valueOf(i);
                idinfo[1] = "身份证读取失败";
            }
        } catch (Exception e) {
        }
        return idinfo;
    }

    @Override
    public String[] SCCBA_ReadIDAndAPRPInfo(String arg0, String arg1) {
        if (arg1 == null || arg1.equals(""))
            return new String[]{"-120", "超时不可为空"};
        int timeout = Integer.parseInt(arg1) * 1000;
        // reader.PlayVoice("请刷身份证");
        Bitmap photo = null;
        String[] idinfo = new String[15];

        IDCardDev idmessage = IDCardDev.getInstance();
        try {
            int i = idmessage.Read(0, timeout);
            if (i == 0) {

                if (idmessage.getID_Type() == "I") {
                    idinfo[0] = "0";
                    idinfo[1] = idmessage.getEnName();

                    idinfo[2] = idmessage.getSex();

                    idinfo[3] = idmessage.getIDNum();

                    idinfo[4] = idmessage.getUNMARC();

                    idinfo[5] = idmessage.getChName();

                    idinfo[6] = idmessage.getDateStart();
                    idinfo[7] = idmessage.getDateEnd();

                    idinfo[8] = idmessage.getBirth();
                    idinfo[9] = idmessage.getID_Ver();
                    idinfo[10] = idmessage.getGrantDepart();
                    idinfo[11] = idmessage.getID_Type();
                } else {
                    idinfo[0] = "0";
                    idinfo[1] = idmessage.getName();

                    idinfo[2] = idmessage.getSex();

                    idinfo[3] = idmessage.getNation();

                    idinfo[4] = idmessage.getBirth();

                    idinfo[5] = idmessage.getAddr();

                    idinfo[6] = idmessage.getIDNum();
                    idinfo[7] = idmessage.getGrantDepart();

                    idinfo[8] = idmessage.getDateStart() + "-" + idmessage.getDateEnd();
                }


                photo = idmessage.getPhoto();
                saveBitmap(arg0, photo);

            } else {
                idinfo[0] = String.valueOf(i);
                idinfo[1] = "读取失败";
            }
        } catch (Exception e) {
        }
        return idinfo;
    }

    @Override
    public String[] SCCBA_getSignature(String arg0, String arg1) {
        // reader.PlayVoice("请签名");
        //ToolFun.Dalpey(200);
        Log.e("超时", arg1);
        int timeout = Integer.parseInt(arg1) * 1000;
        String[] aryRet = new String[2];
        SignatureDev sd = SignatureDev.getInstance();
        Log.e("超时2---------", String.format("timeout=%d", timeout));
        int sRet = sd.Start(timeout);
        if (sRet == 0) {
            Bitmap bm = sd.getmSignaImg();
            saveBitmap(arg0, bm);
            aryRet[0] = "0";
            aryRet[1] = "获取成功";
        } else {
            aryRet[0] = "-120";
            aryRet[1] = "其它错误";
        }
        return aryRet;
    }

    @Override
    public String[] SCCBA_cancel() {
        ReaderDev.getInstance().Cancel();
        MT3YCommMan.isCancel = true;
        return new String[]{"0", ""};

    }
}
