/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ public class MT3YCmdMan extends CmdManagement
/*    */ {
/*    */   protected MT3YCmdMan(byte[] cmd)
/*    */   {
/*  7 */     this.sendBuffer = cmd;
/*  8 */     this.commMan = getCommInstance();
/*    */   }
/*    */   
/*    */   protected CommManagement getCommInstance()
/*    */   {
/* 13 */     return MT3YCommMan.getInstance();
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\MT3YCmdMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */