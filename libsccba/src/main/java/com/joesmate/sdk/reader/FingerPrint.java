/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.CmdCode;
/*    */ 
/*    */ public class FingerPrint {
/*    */   private int iWidth;
/*    */   private int iHeight;
/*  8 */   private byte[] imageBufferData = new byte[CmdCode.MAX_SIZE_BUFFER * 11];
/*  9 */   private int[] imageBufferLength = new int[1];
/*    */   
/* 11 */   public int getWidth() { return this.iWidth; }
/*    */   
/*    */   public void setWidth(int iWidth) {
/* 14 */     this.iWidth = iWidth;
/*    */   }
/*    */   
/* 17 */   public int getHeight() { return this.iHeight; }
/*    */   
/*    */   public void setHeight(int iHeight) {
/* 20 */     this.iHeight = iHeight;
/*    */   }
/*    */   
/* 23 */   public byte[] getImageBufferData() { return this.imageBufferData; }
/*    */   
/*    */   public int[] getImageBufferLength() {
/* 26 */     return this.imageBufferLength;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\FingerPrint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */