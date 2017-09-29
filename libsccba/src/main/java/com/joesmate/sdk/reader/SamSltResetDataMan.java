/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ import com.joesmate.sdk.util.ToolFun;
/*    */ import org.json.JSONException;

/*    */ 
/*    */ public class SamSltResetDataMan
/*    */   extends DataManagement
/*    */ {

/*    */   
/*    */   public SamSltResetDataMan(int delayTime, byte cardNo)
/*    */   {
/* 16 */     byte[] cmd = { 50, 34, (byte)(delayTime / 256), (byte)(delayTime % 256), cardNo };
/* 17 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   protected int parseResult() throws JSONException
/*    */   {
/* 22 */     if (this.cmdMan != null)
/*    */     {
/* 24 */       LogMg.i("SamSltResetDataMan", this + " enter parseResult method.");
/* 25 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/* 26 */       byte[] atrData = new byte[(recvBuffer.length - 1) * 2];
/* 27 */       ToolFun.hex_asc(recvBuffer, 1, atrData, recvBuffer.length - 1);
/* 28 */       this.data.put("ATR", new String(atrData));
/*    */       
/* 30 */       LogMg.i("SamSltResetDataMan", this + " SamSltResetDataMan parseResult end.");
/* 31 */       return 0;
/*    */     }
/* 33 */     this.data.put("ErrCode", -73);
/* 34 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 35 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\SamSltResetDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */