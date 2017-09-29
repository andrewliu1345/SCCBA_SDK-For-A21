/*    */ package com.joesmate.sdk.reader;
/*    */ 
/*    */ import com.joesmate.sdk.util.LogMg;
/*    */ import com.joesmate.sdk.util.RetCode;
/*    */ 
/*    */ public class ContactTypeDataMan extends DataManagement
/*    */ {
/*    */   
/*    */   public ContactTypeDataMan()
/*    */   {
/* 13 */     byte[] cmd = { 50, 50 };
/* 14 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public ContactTypeDataMan(byte[] cmd)
/*    */   {
/* 19 */     this.cmdMan = new MT3YCmdMan(cmd);
/*    */   }
/*    */   
/*    */   public ContactTypeDataMan(CmdManagement cmd)
/*    */   {
/* 24 */     this.cmdMan = cmd;
/*    */   }
/*    */   
/*    */   protected int parseResult() throws org.json.JSONException
/*    */   {
/* 29 */     byte[][] gl_CardCode1 = { { -127, 21 }, { -120, 21 }, { -96, 21 }, { -94, 19 } };
/* 30 */     byte[][] gl_CardCode2 = { { -110, 35 }, { -127, 19 } };
/* 31 */     if (this.cmdMan != null)
/*    */     {
/* 33 */       LogMg.i("ContactTypeDataMan", this + " enter parseResult method.");
/* 34 */       byte[] recvBuffer = this.cmdMan.getRecvData();
/* 35 */       int len = recvBuffer.length;
/* 36 */       int i = 0;
/* 37 */       if (len > 2)
/*    */       {
/* 39 */         for (i = 0; i < 4; i++)
/*    */         {
/* 41 */           if ((recvBuffer[0] == gl_CardCode1[i][0]) && (recvBuffer[1] == gl_CardCode1[i][1])) {
/*    */             break;
/*    */           }
/*    */         }
/*    */         
/* 46 */         if (i < 4)
/*    */         {
/* 48 */           LogMg.i("ContactTypeDataMan", this + " 4442 card.");
/* 49 */           this.data.put("ContactCardType", 4);
/* 50 */           return 0;
/*    */         }
/*    */         
/* 53 */         for (i = 0; i < 2; i++)
/*    */         {
/* 55 */           if ((recvBuffer[0] == gl_CardCode2[i][0]) && (recvBuffer[1] == gl_CardCode2[i][1])) {
/*    */             break;
/*    */           }
/*    */         }
/*    */         
/* 60 */         if (i < 2)
/*    */         {
/* 62 */           LogMg.i("ContactTypeDataMan", this + " 4428 card.");
/* 63 */           this.data.put("ContactCardType", 3);
/* 64 */           return 0;
/*    */         }
/*    */         
/* 67 */         if (((recvBuffer[0] == 49) && (recvBuffer[1] == 58)) || ((recvBuffer[0] == 105) && (recvBuffer[1] == 53)) || ((recvBuffer[0] == 22) && (recvBuffer[1] == 4)))
/*    */         {
/* 69 */           this.data.put("ContactCardType", 8);
/* 70 */           return 0;
/*    */         }
/* 72 */         this.data.put("ErrCode", 61439);
/* 73 */         this.data.put("ErrMsg", RetCode.GetErrMsg(61439));
/* 74 */         return 61439;
/*    */       }
/*    */       
/*    */ 
/* 78 */       this.data.put("ContactCardType", recvBuffer[0]);
/* 79 */       return 0;
/*    */     }
/*    */     
/* 82 */     this.data.put("ErrCode", -73);
/* 83 */     this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
/* 84 */     return -73;
/*    */   }
/*    */ }


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\reader\ContactTypeDataMan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */