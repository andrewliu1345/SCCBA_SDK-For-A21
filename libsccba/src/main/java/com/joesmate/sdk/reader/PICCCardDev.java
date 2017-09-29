/*    */
package com.joesmate.sdk.reader;
/*    */ 
/*    */

import com.joesmate.sdk.listener.ReturnListener;
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ import com.joesmate.sdk.util.ToolFun;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;

/*    */
/*    */ 
/*    */ public class PICCCardDev
/*    */ {
    /* 14 */   private static final PICCCardDev sInstance = new PICCCardDev();

    /*    */
/*    */ 
/*    */
//    private static void ensureThreadLocked()
///*    */ {
///* 19 */
//        if (sInstance == null) {
///* 20 */
//            sInstance = new PICCCardDev();
///*    */
//        }
///*    */
//    }

    /*    */
/*    */
    public static PICCCardDev getInstance() {
/* 25 */
        synchronized (PICCCardDev.class) {
/* 26 */
            // ensureThreadLocked();
/* 27 */
            return sInstance;
/*    */
        }
/*    */
    }

    /*    */
/*    */
    public void cardAPDU(ReturnListener listener, String sendStr)
/*    */ {
/* 33 */
        LogMg.i("PICCCardDev", this + " cardAPDU sendStr is " + sendStr);
/* 34 */
        if (sendStr.length() % 2 != 0)
/*    */ {
/* 36 */
            if (listener != null)
/* 37 */ listener.onError(-37, RetCode.GetErrMsg(-37));
/* 38 */
            LogMg.i("PICCCardDev", this + " CardAPDU's sendStr length's modulus by 2 is not 0. sendStr's length is " + sendStr.length());
/* 39 */
            return;
/*    */
        }
/*    */     
/* 42 */
        byte[] sendBytes = new byte[sendStr.length() / 2];
/* 43 */
        ToolFun.asc_hex(sendStr.getBytes(), sendBytes, sendBytes.length);
/* 44 */
        byte[] cmd = new byte[3 + sendBytes.length];
/* 45 */
        cmd[0] = 50;
        cmd[1] = 38;
        cmd[2] = -1;
/* 46 */
        System.arraycopy(sendBytes, 0, cmd, 3, sendBytes.length);
/* 47 */
        DataManagement hexstrDataMan = new HexstrDataMan(cmd);
/* 48 */
        int st = hexstrDataMan.execCmd();
/* 49 */
        LogMg.i("PICCCardDev", this + " cardAPDU return " + st);
/* 50 */
        if (st == 0)
/*    */ {
/* 52 */
            JSONObject jsonObj = hexstrDataMan.getResult();
/* 53 */
            if (listener != null) {
/*    */
                try
/*    */ {
/* 56 */
                    listener.onSuccess(jsonObj.getString("Value"));
/*    */
                }
/*    */ catch (JSONException e) {
/* 59 */
                    e.printStackTrace();
/* 60 */
                    LogMg.e("PICCCardDev", this + " cardAPDU JSONException:" + e.getMessage());
/*    */
                }
/*    */         
/*    */
            }
/*    */       
/*    */
        }
/* 66 */
        else if (listener != null) {
/* 67 */
            listener.onError(st, RetCode.GetErrMsg(st));
/*    */
        }
/*    */
    }

    /*    */
/*    */
    public int rfHalt()
/*    */ {
/* 73 */
        byte[] cmd = {50, 37};
/* 74 */
        CmdManagement cmdMan = new MT3YCmdMan(cmd);
/* 75 */
        int st = cmdMan.SendRecv();
/* 76 */
        LogMg.i("PICCCardDev", this + " rfHalt return " + st);
/* 77 */
        return st;
/*    */
    }
/*    */
}


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\PICCCardDev.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */