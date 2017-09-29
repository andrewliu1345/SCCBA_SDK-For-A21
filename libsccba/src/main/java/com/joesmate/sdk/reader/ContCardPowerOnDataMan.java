/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class ContCardPowerOnDataMan
/*    */   extends DataManagement
/*    */ {
/* 11 */   private int findMode = 0;
/*    */   
/*    */   public ContCardPowerOnDataMan(int mode) {
/* 14 */     this.findMode = mode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected int parseResult()
/*    */     throws JSONException
/*    */   {
/* 23 */     switch (this.findMode)
/*    */     {
/*    */     case 0: 
/*    */     case 2: 
/*    */     case 4: 
/*    */     case 8: 
/* 29 */       LogMg.i("ContCardPowerOnDataMan", this + " enter parseResult method, samSltResetMan.");
/* 30 */       DataManagement samSltResetMan = new SamSltResetDataMan(0, (byte)0);
/* 31 */       int st = samSltResetMan.execCmd();
/* 32 */       if (st == 0)
/*    */       {
/* 34 */         JSONObject json = samSltResetMan.getResult();
/* 35 */         this.data.put("ContactCardType", 0);
/* 36 */         this.data.put("ATR", json.getString("ATR"));
/* 37 */         return 0;
/*    */       }
/*    */       break;
/*    */     }
/* 41 */     switch (this.findMode)
/*    */     {
/*    */     case 0: 
/*    */     case 2: 
/*    */     case 6: 
/* 46 */       LogMg.i("ContCardPowerOnDataMan", this + " enter parseResult method, ContactTypeDataMan.");
/* 47 */       DataManagement contactTypeMan = new ContactTypeDataMan();
/* 48 */       int st = contactTypeMan.execCmd();
/* 49 */       if (st == 0)
/*    */       {
/* 51 */         JSONObject json = contactTypeMan.getResult();
/* 52 */         this.data.put("ContactCardType", json.getInt("ContactCardType"));
/* 53 */         return 0;
/*    */       }
/*    */       break; }
/* 56 */     LogMg.e("ContCardPowerOnDataMan", this + " findMode is unsupported, findMode=" + this.findMode);
/* 57 */     return 61439;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\ContCardPowerOnDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */