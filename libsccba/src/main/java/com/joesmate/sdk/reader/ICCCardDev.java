/*     */ package com.joesmate.sdk.reader;
/*     */ 
/*     */ import com.joesmate.sdk.listener.ReturnListener;
/*     */ import com.joesmate.sdk.util.LogMg;
/*     */ import com.joesmate.sdk.util.RetCode;
/*     */ import com.joesmate.sdk.util.ToolFun;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ICCCardDev
/*     */ {
/*     */   
/*     */   private static ICCCardDev sInstance;
/*     */   
/*     */   private static void ensureThreadLocked()
/*     */   {
/*  24 */     if (sInstance == null) {
/*  25 */       sInstance = new ICCCardDev();
/*     */     }
/*     */   }
/*     */   
/*     */   public static ICCCardDev getInstance() {
/*  30 */     synchronized (ICCCardDev.class) {
/*  31 */       ensureThreadLocked();
/*  32 */       return sInstance;
/*     */     }
/*     */   }
/*     */   
/*     */   public void samSltReset(ReturnListener listener, int delay, int cardNo)
/*     */   {
/*  38 */     DataManagement samSltResetDataMan = new SamSltResetDataMan(delay, (byte)cardNo);
/*  39 */     int st = samSltResetDataMan.execCmd();
/*  40 */     LogMg.i("ICCCardDev", this + " samSltReset return " + st);
/*  41 */     if (st == 0)
/*     */     {
/*  43 */       JSONObject jsonObj = samSltResetDataMan.getResult();
/*  44 */       if (listener != null) {
/*     */         try
/*     */         {
/*  47 */           listener.onSuccess(jsonObj.getString("ATR"));
/*     */         }
/*     */         catch (JSONException e) {
/*  50 */           e.printStackTrace();
/*  51 */           LogMg.e("ICCCardDev", "samSltReset:" + e.getMessage());
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*  57 */     else if (listener != null) {
/*  58 */       listener.onError(st, RetCode.GetErrMsg(st));
/*     */     }
/*     */   }
/*     */   
/*     */   public void cardAPDU(ReturnListener listener, String sendStr, int cardType)
/*     */   {
/*  64 */     LogMg.i("ICCCardDev", this + " cardAPDU sendStr is " + sendStr);
/*  65 */     if (sendStr.length() % 2 != 0)
/*     */     {
/*  67 */       if (listener != null)
/*  68 */         listener.onError(-37, RetCode.GetErrMsg(-37));
/*  69 */       LogMg.i("ICCCardDev", this + " CardAPDU's sendStr length's modulus by 2 is not 0. sendStr's length is " + sendStr.length());
/*  70 */       return;
/*     */     }
/*     */     
/*  73 */     byte[] sendBytes = new byte[sendStr.length() / 2];
/*  74 */     ToolFun.asc_hex(sendStr.getBytes(), sendBytes, sendBytes.length);
/*  75 */     byte[] cmd = new byte[3 + sendBytes.length];
/*  76 */     cmd[0] = 50;cmd[1] = 38;cmd[2] = ((byte)cardType);
/*  77 */     System.arraycopy(sendBytes, 0, cmd, 3, sendBytes.length);
/*  78 */     DataManagement hexstrDataMan = new HexstrDataMan(cmd);
/*  79 */     int st = hexstrDataMan.execCmd();
/*  80 */     LogMg.i("ICCCardDev", this + " cardAPDU return " + st);
/*  81 */     if (st == 0)
/*     */     {
/*  83 */       JSONObject jsonObj = hexstrDataMan.getResult();
/*  84 */       if (listener != null) {
/*     */         try
/*     */         {
/*  87 */           listener.onSuccess(jsonObj.getString("Value"));
/*     */         }
/*     */         catch (JSONException e) {
/*  90 */           e.printStackTrace();
/*  91 */           LogMg.e("ICCCardDev", this + " cardAPDU JSONException:" + e.getMessage());
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*  97 */     else if (listener != null) {
/*  98 */       listener.onError(st, RetCode.GetErrMsg(st));
/*     */     }
/*     */   }
/*     */   
/*     */   public int samSltPowerDown(int cardType)
/*     */   {
/* 104 */     byte[] cmd = { 50, 35, (byte)cardType };
/* 105 */     CmdManagement cmdMan = new MT3YCmdMan(cmd);
/* 106 */     int st = cmdMan.SendRecv();
/* 107 */     LogMg.i("ICCCardDev", this + " samSltPowerDown return " + st);
/* 108 */     return st;
/*     */   }
/*     */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\ICCCardDev.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */