/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ 
/*    */ public class NumDataMan extends DataManagement
/*    */ {
/*    */   
/*    */   public NumDataMan(byte[] cmd)
/*    */   {
/* 13 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public NumDataMan(CmdManagement cmd)
/*    */   {
/* 18 */     this.cmdMan = cmd;
/*    */   }
/*    */   
/*    */   protected int parseResult() throws org.json.JSONException
/*    */   {
/* 23 */     if (this.cmdMan != null)
/*    */     {
/* 25 */       LogMg.i("NumDataMan", this + " enter parseResult method.");
/* 26 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/*    */       
/* 28 */       this.data.put("Value", recvBuffer[0] < 0 ? recvBuffer[0] + 256 : recvBuffer[0]);
/* 29 */       LogMg.i("NumDataMan", this + " NumDataMan parseResult end.");
/* 30 */       return 0;
/*    */     }
/* 32 */     this.data.put("ErrCode", -73);
/* 33 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 34 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\NumDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */