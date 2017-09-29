/*    */
package com.joesmate.sdk.reader;
/*    */ 
/*    */

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.joesmate.sdk.util.CmdCode;
/*    */ import com.joesmate.sdk.util.LogMg;

/*    */
/*    */ public abstract class CmdManagement
/*    */ {
    /*  9 */   protected byte[] sendBuffer = null;
    /* 10 */   protected byte[] recvBuffer = null;
    /* 11 */   protected CommManagement commMan = null;
    /* 12 */   protected int secTimeout = 8000;
    /*    */
/* 14 */   protected boolean isBigData = false;

    /*    */
/*    */
    protected abstract CommManagement getCommInstance();

    /*    */
/* 18 */
    public void setBigData(boolean isbigData) {
        this.isBigData = isbigData;
    }

    /*    */
/*    */ 
/*    */
    public int SendRecv() {

        synchronized (CmdManagement.class) {

            if (this.commMan != null) {

                this.commMan.isBigData = this.isBigData;

                this.commMan.setCommTimeouts(this.secTimeout);

                LogMg.i("CmdManagement", this + " enter SendRecv method.");
                byte[] readdata;
                if (isBigData)
                    readdata = new byte[CmdCode.MAX_FINGER_SIZE];
                else
                    readdata = new byte[CmdCode.MAX_SIZE_BUFFER];

                int[] readLen = new int[1];

                if (!this.commMan.getIsConnected()) return -102;

                int st = this.commMan.SendRecv(this.sendBuffer, this.sendBuffer.length, readdata, readLen);


                if (st == 0) {

                    this.recvBuffer = new byte[readLen[0]];

                    System.arraycopy(readdata, 0, this.recvBuffer, 0, readLen[0]);


                } else if (st == -17) {

                    try {

                        Thread.sleep(4000L);

                    } catch (Exception localException) {
                    }


                    LogMg.i("CmdManagement", this + " SendRecv retry again.");

                    st = this.commMan.SendRecv(this.sendBuffer, this.sendBuffer.length, readdata, readLen);

                    if (st == 0) {

                        this.recvBuffer = new byte[readLen[0]];

                        System.arraycopy(readdata, 0, this.recvBuffer, 0, readLen[0]);

                    }

                }

                this.commMan.setCommTimeouts(8000);

                LogMg.i("CmdManagement", this + " SendRecv return " + st);

                return st;

            }

            LogMg.e("CmdManagement", this + " CommManagent object is null.");

            return -73;

        }

    }

    /*    */
/*    */
    public void setCommTimeouts(int mSecTotal)
/*    */ {
/* 65 */
        this.secTimeout = mSecTotal;
/*    */
    }

    /*    */
/*    */ 
/*    */
    public byte[] getRecvData()
/*    */ {
/* 71 */
        return this.recvBuffer;
/*    */
    }
/*    */
}


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\CmdManagement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */