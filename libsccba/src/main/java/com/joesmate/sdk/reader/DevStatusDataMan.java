/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;

/*    */ 
/*    */ public class DevStatusDataMan extends DataManagement
/*    */ {
/*    */   
/*    */   public DevStatusDataMan(byte[] cmd)
/*    */   {
/* 13 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public DevStatusDataMan(CmdManagement cmd)
/*    */   {
/* 18 */     this.cmdMan = cmd;
/*    */   }
/*    */   
/*    */   protected int parseResult() throws org.json.JSONException
/*    */   {
/* 23 */     if (this.cmdMan != null)
/*    */     {
/* 25 */       LogMg.i("DevStatusDataMan", this + " enter parseResult method.");
/* 26 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/*    */       
/* 28 */       byte devicestatus = recvBuffer[0];
/* 29 */       this.data.put("ContactCard", (devicestatus & 0x1) == 1 ? 1 : 0);
/* 30 */       this.data.put("ContactlessCard", (devicestatus & 0x2) == 2 ? 1 : 0);
/* 31 */       this.data.put("IDCard", (devicestatus & 0x4) == 4 ? 1 : 0);
/*    */       
/* 33 */       LogMg.i("DevStatusDataMan", this + " DevStatusDataMan parseResult end.");
/* 34 */       return 0;
/*    */     }
/* 36 */     this.data.put("ErrCode", -73);
/* 37 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 38 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\DevStatusDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */