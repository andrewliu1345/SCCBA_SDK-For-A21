/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class FindCardDataMan extends DataManagement
/*    */ {
/* 11 */   private int findMode = 0;
/*    */   
/*    */   public FindCardDataMan(int mode) {
/* 14 */     this.findMode = mode;
/*    */   }
/*    */   
/*    */ 
/*    */   protected int parseResult()
/*    */     throws JSONException
/*    */   {
/* 21 */     LogMg.i("FindCardDataMan", this + " enter parseResult method.");
/* 22 */     switch (this.findMode)
/*    */     {
/*    */     case 0: 
/*    */     case 1: 
/*    */     case 8: 
/* 27 */       DataManagement magcardMan = new MagCardMan(0);
/* 28 */       int st = magcardMan.execCmd();
/* 29 */       if (st == 0)
/*    */       {
/* 31 */         JSONObject json = magcardMan.getResult();
/* 32 */         this.data.put("Mag", true);
/* 33 */         this.data.put("ContactCard", false);
/* 34 */         this.data.put("ContactlessCard", false);
/* 35 */         this.data.put("Track1", json.getString("Track1"));
/* 36 */         this.data.put("Track2", json.getString("Track2"));
/* 37 */         this.data.put("Track3", json.getString("Track3"));
/* 38 */         return 0;
/*    */       }
/*    */       break;
/*    */     }
/* 42 */     DataManagement contCardPowerMan = new ContCardPowerOnDataMan(this.findMode);
/* 43 */     int st = contCardPowerMan.execCmd();
/* 44 */     if (st == 0)
/*    */     {
/* 46 */       this.data.put("ContactCard", true);
/* 47 */       this.data.put("Mag", false);
/* 48 */       this.data.put("ContactlessCard", false);
/* 49 */       JSONObject json = contCardPowerMan.getResult();
/* 50 */       int contCardType = json.getInt("ContactCardType");
/* 51 */       LogMg.i("FindCardDataMan", this + " contact card type is " + contCardType);
/* 52 */       this.data.put("ContactCardType", contCardType);
/* 53 */       if (contCardType == 0)
/* 54 */         this.data.put("ATR", json.getString("ATR"));
/* 55 */       return 0;
/*    */     }
/* 57 */     DataManagement contlessCardOpenMan = new ContlessCardOpenDataMan(0);
/* 58 */     st = contlessCardOpenMan.execCmd();
/* 59 */     if (st == 0)
/*    */     {
/* 61 */       this.data.put("ContactlessCard", true);
/* 62 */       this.data.put("Mag", false);
/* 63 */       this.data.put("ContactCard", false);
/* 64 */       JSONObject jsonObj = contlessCardOpenMan.getResult();
/* 65 */       int contlessCardType = jsonObj.getInt("ContactlessCardType");
/* 66 */       this.data.put("ContactlessCardType", contlessCardType);
/* 67 */       this.data.put("ContactlessCardTypeA/B", jsonObj.getInt("ContactlessCardTypeA/B"));
/* 68 */       LogMg.i("FindCardDataMan", this + " contactless card type is " + contlessCardType);
/* 69 */       this.data.put("Snr", jsonObj.getString("Snr"));
/* 70 */       this.data.put("ATR", jsonObj.getString("ATR"));
/* 71 */       switch (this.findMode)
/*    */       {
/*    */       case 0: 
/*    */       case 3: 
/* 75 */         return 0;
/*    */       
/*    */       case 7: 
/* 78 */         if (contlessCardType == 1) {
/* 79 */           return 0;
/*    */         }
/*    */         break;
/*    */       case 5: case 8: 
/* 83 */         if (contlessCardType == 0)
/* 84 */           return 0;
/*    */         break;
/*    */       }
/*    */     }
/* 88 */     this.data.put("ErrCode", -75);
/* 89 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-75));
/* 90 */     return -75;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\FindCardDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */