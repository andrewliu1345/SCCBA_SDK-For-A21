/*     */
package com.joesmate.sdk.reader;
/*     */

import android.util.Base64;

/*     */ import com.joesmate.sdk.util.LogMg;
/*     */ import com.joesmate.sdk.util.RetCode;
/*     */ import com.joesmate.sdk.util.ToolFun;

/*     */ import java.io.IOException;
/*     */ import org.json.JSONException;

/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IDCardDataMan
/*     */ extends DataManagement
/*     */ {

    /*  21 */   private int readType = 0;

    /*     */
/*     */
    public IDCardDataMan(CmdManagement cmd)
/*     */ {
/*  25 */
        this.cmdMan = cmd;
/*  26 */
        this.cmdMan.setCommTimeouts(10000);
/*     */
    }

    /*     */
/*     */
    public void setReadType(int type) {
/*  30 */
        this.readType = type;
/*     */
    }

    /*     */
/*     */ 
/*     */
    public IDCardDataMan(int type)
/*     */ {
/*  36 */
        this.readType = type;
/*  37 */
        byte[] cmd = new byte[2];
/*  38 */
        switch (this.readType)
/*     */ {
/*     */
            case 0:
/*  41 */
                cmd[0] = 50;
/*  42 */
                cmd[1] = 80;
/*  43 */
                break;
/*     */
            case 1:
/*  45 */
                cmd[0] = -48;
/*  46 */
                cmd[1] = 7;
/*  47 */
                break;
/*     */
            case 2:
/*  49 */
                cmd[0] = -48;
/*  50 */
                cmd[1] = 5;
/*  51 */
                break;
/*     */
            default:
/*  53 */
                return;
/*     */
        }
/*  55 */
        this.cmdMan = new MT3YCmdMan(cmd);
/*  56 */
        this.cmdMan.setCommTimeouts(10000);
/*     */
    }

    public IDCardDataMan(int type, int timeout) {

        this.readType = type;

        byte[] cmd = new byte[2];

        switch (this.readType) {

            case 0:

                cmd[0] = 50;

                cmd[1] = 80;

                break;

            case 1:

                cmd[0] = -48;

                cmd[1] = 7;

                break;

            case 2:

                cmd[0] = -48;

                cmd[1] = 5;

                break;

            default:

                return;

        }

        this.cmdMan = new MT3YCmdMan(cmd);

        this.cmdMan.setCommTimeouts(timeout);

    }

    protected int parseResult()
/*     */     throws JSONException
/*     */ {
/*  62 */
        if (this.cmdMan != null)
/*     */ {
/*  64 */
            LogMg.i("IDCardDataMan", this + " enter parseResult method.");
/*  65 */
            LogMg.i("IDCardDataMan", "readType=" + this.readType);
/*  66 */
            byte[] recvBuffer = this.cmdMan.getRecvData();
/*  67 */
            byte SW1 = 0;
            byte SW2 = 0;
            byte SW3 = 0;
/*     */
            try
/*     */ {
/*  70 */
                if ((this.readType == 0) || (this.readType == 1))
/*     */ {
/*     */ 
/*  73 */
                    SW1 = recvBuffer[2];
/*  74 */
                    SW2 = recvBuffer[3];
/*  75 */
                    SW3 = recvBuffer[4];
/*  76 */
                    if ((SW1 == 0) && (SW2 == 0) && (SW3 == -112))
/*     */ {
/*  78 */
                        int nDLen = recvBuffer[5] * 256 + recvBuffer[6];
/*  79 */
                        int nPLen = recvBuffer[7] * 256 + recvBuffer[8];
/*  80 */
                        int nFLen = this.readType == 1 ? recvBuffer[9] * 256 + recvBuffer[10] : 0;
/*  81 */
                        int index = this.readType == 1 ? 11 : 9;
/*     */
                        byte[] idtype = new byte[2];
                        System.arraycopy(recvBuffer, index + 248, idtype, 0, 2);
                        String typestr = new String(idtype, "UTF-16LE");
                        this.data.put("idtype", typestr);

                        if (typestr == "I") {//居留证
                            byte[] enname = new byte[120];
                            System.arraycopy(recvBuffer, index, enname, 0, 120);
                            this.data.put("EnName", new String(enname, "UTF-16LE"));

                            byte[] sex = new byte[2];
                            System.arraycopy(recvBuffer, index + 120, sex, 0, 2);
                            this.data.put("Sex", getSexInfo(sex));

                            byte[] idnum = new byte[30];
                            System.arraycopy(recvBuffer, index + 122, idnum, 0, 30);
                            this.data.put("IDNUM", new String(idnum, "UTF-16LE"));


                            byte[] UNMARC = new byte[6];
                            System.arraycopy(recvBuffer, index + 152, UNMARC, 0, 6);
                            this.data.put("UNMARC", new String(UNMARC, "UTF-16LE"));

                            byte[] chname = new byte[30];
                            System.arraycopy(recvBuffer, index + 158, chname, 0, 30);
                            this.data.put("chname", new String(chname, "UTF-16LE"));

                            byte[] datestart = new byte[16];
                            System.arraycopy(recvBuffer, index + 188, datestart, 0, 16);
                            this.data.put("DateStart", new String(datestart, "UTF-16LE"));

                            byte[] dateend = new byte[16];
                            System.arraycopy(recvBuffer, index + 204, dateend, 0, 16);
                            this.data.put("DateEnd", new String(dateend, "UTF-16LE"));

                            byte[] birth = new byte[16];
                            System.arraycopy(recvBuffer, index + 220, birth, 0, 16);
                            this.data.put("Birth", new String(birth, "UTF-16LE"));

                            byte[] idver = new byte[4];
                            System.arraycopy(recvBuffer, index + 246, idver, 0, 4);
                            this.data.put("idver", new String(idver, "UTF-16LE"));

                            byte[] asigndepartment = new byte[8];
                            System.arraycopy(recvBuffer, index + 250, asigndepartment, 0, 8);
                            this.data.put("GrantDepart", new String(asigndepartment, "UTF-16LE"));


                        } else {//身份证
                          /*  83 */
                            byte[] name = new byte[30];
                          /*  84 */
                            System.arraycopy(recvBuffer, index, name, 0, 30);
                          /*  85 */
                            this.data.put("Name", new String(name, "UTF-16LE"));
                    	  /*  86 */
                            byte[] sex = new byte[2];
                    	  /*  87 */
                            System.arraycopy(recvBuffer, index + 30, sex, 0, 2);
                    	  /*  88 */
                            this.data.put("Sex", getSexInfo(sex));
                    	  /*  89 */
                            byte[] nation = new byte[4];
                    	  /*  90 */
                            System.arraycopy(recvBuffer, index + 32, nation, 0, 4);
                    	  /* 91 */
                            this.data.put("Nation", getNation(nation));
                            byte[] birth = new byte[16];
                            System.arraycopy(recvBuffer, index + 36, birth, 0, 16);
                            this.data.put("Birth", new String(birth, "UTF-16LE"));
                            byte[] address = new byte[70];
                            System.arraycopy(recvBuffer, index + 52, address, 0, 70);
                            this.data.put("Address", new String(address, "UTF-16LE"));
                            byte[] idnum = new byte[36];
                            System.arraycopy(recvBuffer, index + 122, idnum, 0, 36);
                            this.data.put("IDNUM", new String(idnum, "UTF-16LE"));
                            byte[] asigndepartment = new byte[30];
                            System.arraycopy(recvBuffer, index + 158, asigndepartment, 0, 30);
                            this.data.put("GrantDepart", new String(asigndepartment, "UTF-16LE"));
                            byte[] datestart = new byte[16];
                            System.arraycopy(recvBuffer, index + 188, datestart, 0, 16);
                            this.data.put("DateStart", new String(datestart, "UTF-16LE"));
                            byte[] dateend = new byte[16];
                            System.arraycopy(recvBuffer, index + 204, dateend, 0, 16);
                            this.data.put("DateEnd", new String(dateend, "UTF-16LE"));
                        }

						/* 110 */
                        byte[] photodatainfo = new byte[nPLen];
						/* 111 */
                        System.arraycopy(recvBuffer, index + nDLen, photodatainfo, 0, nPLen);
                        String photowlt = Base64.encodeToString(photodatainfo, Base64.DEFAULT);
                        this.data.put("PhotoWlt", photowlt);
                        /// * 112 */ String StrWltFilePath =
                        /// Environment.getExternalStorageDirectory() +
                        /// "/photo.wlt";
                        /// * 113 */ String StrBmpFilePath =
                        /// Environment.getExternalStorageDirectory() +
                        /// "/photo.bmp";
                        /// * */
                        /// * 115 */ File wltFile = new File(StrWltFilePath);
                        /// * 116 */ FileOutputStream fos = new
                        /// FileOutputStream(wltFile);
                        /// * 117 */ fos.write(photodatainfo, 0, nPLen);
                        /// * 118 */ fos.close();
                        /// * */
                        /// * 120 */ DecodeWlt dw = new DecodeWlt();
                        /// * 121 */ int result = dw.Wlt2Bmp(StrWltFilePath,
                        /// StrBmpFilePath);
                        /// * 122 */ if (result == 1)
                        /// * */ {
                        /// * 124 */ this.data.put("Photo", StrBmpFilePath);
                        /// * */ }
						/* 126 */
                        byte[] fingerdatainfo = new byte[nFLen];
						/* 127 */
                        System.arraycopy(recvBuffer, index + nDLen + nPLen, fingerdatainfo, 0, nFLen);
						/* 128 */
                        byte[] ascFinger = new byte[nFLen * 2];
						/* 129 */
                        ToolFun.hex_asc(fingerdatainfo, ascFinger, nFLen);
						/* 130 */
                        this.data.put("Finger", new String(ascFinger));
						/* 131 */
                        return 0;
						/*     */
                    }
					/*     */
					/*     */
					/* 135 */
                    LogMg.e("IDCardDataMan", this + " SW1=" + SW1 + ",SW2=" + SW2 + ",SW3=" + SW3);
					/* 136 */
                    return -70;
					/*     */
                }
				/*     */
				/* 139 */
                if (this.readType == 2)
				/*     */ {
					/* 141 */
                    SW1 = recvBuffer[0];
					/* 142 */
                    SW2 = recvBuffer[1];
					/* 143 */
                    SW3 = recvBuffer[2];
					/* 144 */
                    if ((SW1 == 0) && (SW2 == 0) && (SW3 == -112))
					/*     */ {
						/* 146 */
                        byte[] address = new byte[70];
						/* 147 */
                        System.arraycopy(recvBuffer, 3, address, 0, 70);
						/* 148 */
                        this.data.put("AddInfo", new String(address, "UTF-16LE"));
						/* 149 */
                        return 0;
						/*     */
                    }
					/*     */
					/*     */
					/* 153 */
                    LogMg.e("IDCardDataMan", this + " SW1=" + SW1 + ",SW2=" + SW2 + ",SW3=" + SW3);
					/* 154 */
                    return -70;
					/*     */
                }
				/*     */
				/*     */
            }
			/*     */ catch (IOException e)
			/*     */ {
				/* 160 */
                LogMg.e("IDCardDataMan", e.getMessage());
				/* 161 */
                return 65336;
				/*     */
            }
			/*     */
        }
		/* 164 */
        this.data.put("ErrCode", -73);
		/* 165 */
        this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
		/* 166 */
        return -73;
		/*     */
    }

    /*     */
	/*     */
    private String getSexInfo(byte[] bsex)
	/*     */ {
		/* 171 */
        String StrSexInfo = "";
		/* 172 */
        if (bsex[0] == 48)
		/*     */ {
			/* 174 */
            StrSexInfo = "未知";
			/*     */
        }
		/* 176 */
        else if (bsex[0] == 49)
		/*     */ {
			/* 178 */
            StrSexInfo = "男";
			/*     */
        }
		/* 180 */
        else if (bsex[0] == 50)
		/*     */ {
			/* 182 */
            StrSexInfo = "女";
			/*     */
        }
		/* 184 */
        else if (bsex[0] == 57)
		/*     */ {
			/* 186 */
            StrSexInfo = "未说明";
			/*     */
        }
		/*     */
        else
		/*     */ {
			/* 190 */
            StrSexInfo = " ";
			/*     */
        }
		/*     */
		/* 193 */
        return StrSexInfo;
		/*     */
    }

    /*     */
	/*     */
	/*     */
    public String getNation(byte[] bNationinfo)
	/*     */ {
		/* 199 */
        String StrNation = "";
		/* 200 */
        int nNationNo = 0;
		/*     */
		/*     */
		/* 203 */
        nNationNo = (bNationinfo[0] - 48) * 10 + bNationinfo[2] - 48;
		/* 204 */
        switch (nNationNo)
		/*     */ {
		/*     */
            case 1:
			/* 207 */
                StrNation = "汉";
			/* 208 */
                break;
		/*     */
            case 2:
			/* 210 */
                StrNation = "蒙古";
			/* 211 */
                break;
		/*     */
            case 3:
			/* 213 */
                StrNation = "回";
			/* 214 */
                break;
		/*     */
            case 4:
			/* 216 */
                StrNation = "藏";
			/* 217 */
                break;
		/*     */
            case 5:
			/* 219 */
                StrNation = "维吾尔";
			/* 220 */
                break;
		/*     */
            case 6:
			/* 222 */
                StrNation = "苗";
			/* 223 */
                break;
		/*     */
            case 7:
			/* 225 */
                StrNation = "彝";
			/* 226 */
                break;
		/*     */
            case 8:
			/* 228 */
                StrNation = "壮";
			/* 229 */
                break;
		/*     */
            case 9:
			/* 231 */
                StrNation = "布依";
			/* 232 */
                break;
		/*     */
            case 10:
			/* 234 */
                StrNation = "朝鲜";
			/* 235 */
                break;
		/*     */
            case 11:
			/* 237 */
                StrNation = "满";
			/* 238 */
                break;
		/*     */
            case 12:
			/* 240 */
                StrNation = "侗";
			/* 241 */
                break;
		/*     */
            case 13:
			/* 243 */
                StrNation = "瑶";
			/* 244 */
                break;
		/*     */
            case 14:
			/* 246 */
                StrNation = "白";
			/* 247 */
                break;
		/*     */
            case 15:
			/* 249 */
                StrNation = "土家";
			/* 250 */
                break;
		/*     */
            case 16:
			/* 252 */
                StrNation = "哈尼";
			/* 253 */
                break;
		/*     */
            case 17:
			/* 255 */
                StrNation = "哈萨克";
			/* 256 */
                break;
		/*     */
            case 18:
			/* 258 */
                StrNation = "傣";
			/* 259 */
                break;
		/*     */
            case 19:
			/* 261 */
                StrNation = "黎";
			/* 262 */
                break;
		/*     */
            case 20:
			/* 264 */
                StrNation = "傈僳";
			/* 265 */
                break;
		/*     */
            case 21:
			/* 267 */
                StrNation = "佤";
			/* 268 */
                break;
		/*     */
            case 22:
			/* 270 */
                StrNation = "畲";
			/* 271 */
                break;
		/*     */
            case 23:
			/* 273 */
                StrNation = "高山";
			/* 274 */
                break;
		/*     */
            case 24:
			/* 276 */
                StrNation = "拉祜";
			/* 277 */
                break;
		/*     */
            case 25:
			/* 279 */
                StrNation = "水";
			/* 280 */
                break;
		/*     */
            case 26:
			/* 282 */
                StrNation = "东乡";
			/* 283 */
                break;
		/*     */
            case 27:
			/* 285 */
                StrNation = "纳西";
			/* 286 */
                break;
		/*     */
            case 28:
			/* 288 */
                StrNation = "景颇";
			/* 289 */
                break;
		/*     */
            case 29:
			/* 291 */
                StrNation = "柯尔克孜";
			/* 292 */
                break;
		/*     */
            case 30:
			/* 294 */
                StrNation = "土";
			/* 295 */
                break;
		/*     */
            case 31:
			/* 297 */
                StrNation = "达斡尔";
			/* 298 */
                break;
		/*     */
            case 32:
			/* 300 */
                StrNation = "仫佬";
			/* 301 */
                break;
		/*     */
            case 33:
			/* 303 */
                StrNation = "羌";
			/* 304 */
                break;
		/*     */
            case 34:
			/* 306 */
                StrNation = "布朗";
			/* 307 */
                break;
		/*     */
            case 35:
			/* 309 */
                StrNation = "撒拉";
			/* 310 */
                break;
		/*     */
            case 36:
			/* 312 */
                StrNation = "毛南";
			/* 313 */
                break;
		/*     */
            case 37:
			/* 315 */
                StrNation = "仡佬";
			/* 316 */
                break;
		/*     */
            case 38:
			/* 318 */
                StrNation = "锡伯";
			/* 319 */
                break;
		/*     */
            case 39:
			/* 321 */
                StrNation = "阿昌";
			/* 322 */
                break;
		/*     */
            case 40:
			/* 324 */
                StrNation = "普米";
			/* 325 */
                break;
		/*     */
            case 41:
			/* 327 */
                StrNation = "塔吉克";
			/* 328 */
                break;
		/*     */
            case 42:
			/* 330 */
                StrNation = "怒";
			/* 331 */
                break;
		/*     */
            case 43:
			/* 333 */
                StrNation = "乌孜别克";
			/* 334 */
                break;
		/*     */
            case 44:
			/* 336 */
                StrNation = "俄罗斯";
			/* 337 */
                break;
		/*     */
            case 45:
			/* 339 */
                StrNation = "鄂温克";
			/* 340 */
                break;
		/*     */
            case 46:
			/* 342 */
                StrNation = "德昂";
			/* 343 */
                break;
		/*     */
            case 47:
			/* 345 */
                StrNation = "保安";
			/* 346 */
                break;
		/*     */
            case 48:
			/* 348 */
                StrNation = "裕固";
			/* 349 */
                break;
		/*     */
            case 49:
			/* 351 */
                StrNation = "京";
			/* 352 */
                break;
		/*     */
            case 50:
			/* 354 */
                StrNation = "塔塔尔";
			/* 355 */
                break;
		/*     */
            case 51:
			/* 357 */
                StrNation = "独龙";
			/* 358 */
                break;
		/*     */
            case 52:
			/* 360 */
                StrNation = "鄂伦春";
			/* 361 */
                break;
		/*     */
            case 53:
			/* 363 */
                StrNation = "赫哲";
			/* 364 */
                break;
		/*     */
            case 54:
			/* 366 */
                StrNation = "门巴";
			/* 367 */
                break;
		/*     */
            case 55:
			/* 369 */
                StrNation = "珞巴";
			/* 370 */
                break;
		/*     */
            case 56:
			/* 372 */
                StrNation = "基诺";
			/* 373 */
                break;
		/*     */
            case 57:
			/* 375 */
                StrNation = "其他";
			/* 376 */
                break;
		/*     */
            case 58:
			/* 378 */
                StrNation = "外国血统中国籍人士";
			/* 379 */
                break;
		/*     */
            default:
			/* 381 */
                StrNation = " ";
			/*     */
        }
		/*     */
		/* 384 */
        return StrNation;
		/*     */
    }
	/*     */
}
