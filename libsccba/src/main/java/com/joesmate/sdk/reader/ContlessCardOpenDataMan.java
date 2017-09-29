/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ import com.joesmate.sdk.util.ToolFun;
/*    */ import org.json.JSONException;
/*    */ 
/*    */ public class ContlessCardOpenDataMan
/*    */   extends DataManagement
/*    */ {

/*    */   
/*    */   public ContlessCardOpenDataMan(int delayTime)
/*    */   {
/* 16 */     byte[] cmd = { 50, 40, (byte)(delayTime / 256), (byte)(delayTime % 256) };
/* 17 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   protected int parseResult() throws JSONException
/*    */   {
/* 22 */     if (this.cmdMan != null)
/*    */     {
/* 24 */       LogMg.i("ContlessCardOpenDataMan", this + " enter parseResult method.");
/* 25 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/* 26 */       this.data.put("ContactlessCardType", recvBuffer[0]);
/* 27 */       this.data.put("ContactlessCardTypeA/B", recvBuffer[1]);
/* 28 */       int snrLen = recvBuffer[2];
/* 29 */       byte[] snrData = new byte[snrLen];
/* 30 */       boolean isIDCard = true;
/* 31 */       for (int i = 0; i < snrLen; i++)
/*    */       {
/* 33 */         snrData[i] = recvBuffer[(3 + i)];
/* 34 */         if (snrData[i] != 0) isIDCard = false;
/*    */       }
/* 36 */       byte[] ascSnr = new byte[2 * snrLen];
/* 37 */       ToolFun.hex_asc(snrData, ascSnr, snrLen);
/* 38 */       this.data.put("Snr", new String(ascSnr));
/* 39 */       if (isIDCard)
/*    */       {
/* 41 */         LogMg.i("ContlessCardOpenDataMan", this + " is IDCard.");
/* 42 */         this.data.put("ErrCode", -99);
/* 43 */         this.data.put("ErrMsg", RetCode.GetErrMsg(-99));
/* 44 */         return -99;
/*    */       }
/* 46 */       int atrLen = recvBuffer[(3 + snrLen)];
/* 47 */       byte[] atrAsc = new byte[atrLen * 2];
/* 48 */       ToolFun.hex_asc(recvBuffer, 4 + snrLen, atrAsc, atrLen);
/* 49 */       this.data.put("ATR", new String(atrAsc));
/* 50 */       return 0;
/*    */     }
/* 52 */     this.data.put("ErrCode", -73);
/* 53 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 54 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\ContlessCardOpenDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */