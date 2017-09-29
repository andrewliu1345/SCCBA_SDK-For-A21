package com.joesmate.sdk.util;

public class RetCode {
    public static final int OP_OK = 0;

    public static final int ERR_ICREADER = -1;

    public static final int ERR_ICCARD = -2;

    public static final int ERR_BUILDARQC = -3;

    public static final int ERR_ARPC = -4;

    public static final int ERR_USER_CANCEL = -5;

    public static final int ERR_CARD_APDU = -7;

    public static final int ERR_WRITE_TIMEOUT = -8;

    public static final int ERR_INTERRUPT = -9;

    public static final int ERR_READ = -10;

    public static final int ERR_WRITESCR = -11;

    public static final int ERR_WRITE = -12;

    public static final int ERR_CARDTYPE = -15;

    public static final int ERR_SOCK = -17;

    public static final int ERR_TIMEOUT = -19;

    public static final int ERR_GENERAL = -36;

    public static final int ERR_DATAFORMAT = -37;

    public static final int ERR_PINLEN = -46;

    public static final int ERR_VERIFYPIN = -47;
    public static final int ERR_MODIFYPIN = -48;
    public static final int ERR_AUTH = -50;
    public static final int ERR_PINLOCK = -49;
    public static final int ERR_RELOAD = -51;
    public static final int ERR_RELOAD_UNSAFE = -52;
    public static final int ERR_RELOAD_UNIF = -53;
    public static final int ERR_RELOAD_DATA = -54;
    public static final int ERR_RELOAD_UNSUP = -55;
    public static final int ERR_RELOAD_PAR = -56;
    public static final int ERR_RELOAD_UNFIND = -57;
    public static final int ERR_RELOAD_APPLOCK = -58;
    public static final int ERR_POWERON = -59;
    public static final int ERR_MAGNETIC_DATA = -60;
    public static final int ERR_IDCARDREAD = -70;
    public static final int ERR_DATAINDEX = -71;
    public static final int ERR_LCD_LNUM = -72;
    public static final int ERR_OBJNULL = -73;
    public static final int Init_Code = -74;
    public static final int ERR_FINDNOTHING = -75;
    public static final int ERR_NODATA = -76;
    public static final int ERR_KEYEMPTY = -80;
    public static final int ERR_INPUTEMPTY = -81;
    public static final int ERR_KEYLEN = -82;
    public static final int ERR_INPUTLEN = -83;
    public static final int ERR_STX = -97;
    public static final int ERR_ETX = -98;
    public static final int ERR_IDCARD = -99;
    public static final int ERR_LEN = -100;
    public static final int ERR_CRC = -101;
    public static final int ERR_DISCONN = -102;
    public static final int ERR_PARSEIDCARD = -200;
    public static final int ERR_CHALSEND = -201;
    public static final int ERR_SETBAUD = -202;
    public static final int ERR_GETIMGNUM = -203;
    public static final int ERR_AMOUNT = -204;
    public static final int ERR_SETAMOUNT = -205;
    public static final int ERR_SETTRANTIME = -206;
    public static final int ERR_GETF55 = -207;
    public static final int ERR_STBD = -208;
    public static final int ERR_UNSUPPORTED = -4097;
    public static final int ERR_PARSEFINGER = -209;
    public static final int ERR_OVERFLOW = -210;
    public static final int ERR_SAVEIMG = -211;
    public static final int ERR_GETDATA = -212;
    public static final int ERR_ASCTOHEX = -213;
    public static final String Key_Value = "Value";
    public static final String Key_ErrCode = "ErrCode";
    public static final String Key_ErrMsg = "ErrMsg";
    public static final String Key_SussCode = "SussCode";
    public static final String Key_SussMsg = "SussMsg";
    public static final String Key_ContactCard = "ContactCard";
    public static final String Key_ContactlessCard = "ContactlessCard";
    public static final String Key_IDCard = "IDCard";
    public static final String Key_ATR = "ATR";
    public static final String Key_ContactCardType = "ContactCardType";
    public static final String Key_ContactlessCardType = "ContactlessCardType";
    public static final String Key_ContactlessCardTypeAB = "ContactlessCardTypeA/B";
    public static final String Key_Snr = "Snr";
    public static final String Key_Track1 = "Track1";
    public static final String Key_Track2 = "Track2";
    public static final String Key_Track3 = "Track3";
    public static final String Key_Fileld55 = "Fileld55";
    public static final String Key_Mag = "Mag";
    public static final String Key_Name = "Name";
    public static final String Key_Sex = "Sex";
    public static final String Key_Nation = "Nation";
    public static final String Key_Birth = "Birth";
    public static final String Key_Address = "Address";
    public static final String Key_IDNUM = "IDNUM";
    public static final String Key_GrantDepart = "GrantDepart";
    public static final String Key_DateStart = "DateStart";
    public static final String Key_DateEnd = "DateEnd";
    public static final String Key_Photo = "Photo";
    public static final String Key_Photo_Wlt = "PhotoWlt";
    public static final String Key_Photo_Image = "PhotoImage";
    public static final String Key_Finger = "Finger";
    public static final String Key_AddInfo = "AddInfo";
    public static final String Key_CardNO = "CardNO";

    public static String GetErrMsg(int errCode) {
        switch (errCode) {
            case -2:
                return "磁条卡读取失败";
            case -17:
                return "通讯通道异常";
            case -19:
                return "通讯超时";
            case -73:
                return "对象为空";
            case -4097:
                return "不支持的类型";
            case -99:
                return "此卡可能为身份证卡";
            case -70:
                return "未检测到身份证";
            case -75:
                return "未寻到卡";
            case -37:
                return "数据格式有误";
            case -97:
                return "数据包头有误";
            case -98:
                return "数据包尾有误";
            case -100:
                return "数据长度有误";
            case -101:
                return "CRC校验有误";
            case -201:
                return "下发透传指令失败";
            case -76:
                return "没有数据";
            case -204:
                return "输入金额无效";
            case -205:
                return "设置验证金额失败";
            case -206:
                return "设置时间失败";
            case -207:
                return "获取域55失败";
            case -102:
                return "设备已断开";
            case -208:
                return "设置波特率失败";
            case -209:
                return "数据传输正常,但指纹数据解析错误";
            case -210:
                return "数据溢出";
            case -211:
                return "保存头像错误";
            case -213:
                return "Asc 转  hex 数据失败";
        }

        return "";
    }
}