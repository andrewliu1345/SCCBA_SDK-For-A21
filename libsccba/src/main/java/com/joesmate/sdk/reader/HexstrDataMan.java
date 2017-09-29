/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ import com.joesmate.sdk.util.ToolFun;

/*    */ 
/*    */ public class HexstrDataMan extends DataManagement
/*    */ {

/*    */   
/*    */   public HexstrDataMan(byte[] cmd)
/*    */   {
/* 14 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public HexstrDataMan(CmdManagement cmd)
/*    */   {
/* 19 */     this.cmdMan = cmd;
/*    */   }
/*    */   
/*    */   protected int parseResult() throws org.json.JSONException
/*    */   {
/* 24 */     if (this.cmdMan != null)
/*    */     {
/* 26 */       LogMg.i("HexstrDataMan", this + " enter parseResult method.");
/* 27 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/* 28 */       byte[] ascData = new byte[recvBuffer.length * 2];
/* 29 */       ToolFun.hex_asc(recvBuffer, ascData, recvBuffer.length);
/* 30 */       this.data.put("Value", new String(ascData));
/* 31 */       LogMg.i("HexstrDataMan", this + " HexstrDataMan parseResult end.");
/* 32 */       return 0;
/*    */     }
/* 34 */     this.data.put("ErrCode", -73);
/* 35 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 36 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\HexstrDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */