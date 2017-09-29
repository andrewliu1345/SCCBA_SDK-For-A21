/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;

/*    */ 
/*    */ public class StrDataMan extends DataManagement
/*    */ {

/*    */   
/*    */   public StrDataMan(byte[] cmd)
/*    */   {
/* 13 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public StrDataMan(CmdManagement cmd)
/*    */   {
/* 18 */     this.cmdMan = cmd;
/*    */   }
/*    */   
/*    */   protected int parseResult() throws org.json.JSONException
/*    */   {
/* 23 */     if (this.cmdMan != null)
/*    */     {
/* 25 */       LogMg.i("StrDataMan", this + " enter parseResult method.");
/* 26 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/* 27 */       this.data.put("Value", new String(recvBuffer));
/* 28 */       LogMg.i("StrDataMan", this + " strDataMan parseResult end.");
/* 29 */       return 0;
/*    */     }
/* 31 */     this.data.put("ErrCode", -73);
/* 32 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 33 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\StrDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */