package com.joesmate.sdk.reader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.joesmate.idcreader.HandImage;
import com.joesmate.sdk.listener.ClipReturnListener;
import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("HandlerLeak")
public class IDCardDev {
    private static final IDCardDev sInstance = new IDCardDev();
    protected String name = null;
    protected String sex = null;
    protected String nation = null;
    protected String birth = null;
    protected String addr = null;
    protected String idNum = null;
    protected String grantDepart = null;
    protected String dateStart = null;
    protected String dateEnd = null;
    protected Bitmap photo = null;
    protected String finger = null;
    protected String addInfo = null;

    protected String idtype = null;
    protected String enname = null;
    protected String chname = null;
    protected String UNMARC = null;
    protected String idver = null;

//    ClipReturnListener m_ClipReturnListener;
//    ReturnListener m_ReturnListener;

//	private static void ensureThreadLocked() {
//		if (sInstance == null) {
//			sInstance = new IDCardDev();
//		}
//	}

    public static IDCardDev getInstance() {
        synchronized (IDCardDev.class) {
            //ensureThreadLocked();
            return sInstance;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getSex() {
        return this.sex;
    }

    public String getNation() {
        return this.nation;
    }

    public String getBirth() {
        return this.birth;
    }

    public String getAddr() {
        return this.addr;
    }

    public String getIDNum() {
        return this.idNum;
    }

    public String getGrantDepart() {
        return this.grantDepart;
    }

    public String getDateStart() {
        return this.dateStart;
    }

    public String getDateEnd() {
        return this.dateEnd;
    }

    public Bitmap getPhoto() {
        return this.photo;
    }

    public String getFinger() {
        return this.finger;
    }

    public String getAddInfo() {
        return this.addInfo;
    }

    public String getID_Type() {
        return idtype;
    }

    public String getEnName() {
        return enname;
    }

    public String getChName() {
        return chname;
    }

    public String getUNMARC() {
        return UNMARC;
    }

    public String getID_Ver() {
        return idver;
    }

    public void Read(final int readType, final ClipReturnListener listerner, final int timeout) {
        LogMg.i("IDCardDev", this + " Read readType=" + readType);

        synchronized (IDCardDataMan.class) {
            //m_ClipReturnListener = listerner;

            new Thread(new Runnable() {

                @Override
                public void run() {
                    ReaderDev.getInstance().devLCDControl(false, false, true, false, false);
                    DataManagement idCardDataMan = getDataManInstance(readType);
                    long time = System.currentTimeMillis();
                    int st = 0;
                    ReaderDev.getInstance().devBeep();
                    while (System.currentTimeMillis() - time < timeout * 1000) {
                        st = idCardDataMan.execCmd();
                        LogMg.i("IDCardDev", this + " Read IDCard return " + st);

                        if (st == 0) {

                            try {
                                // ReaderDev.getInstance().devBeep();
                                JSONObject jsonObj = idCardDataMan.getResult();
                                byte[] photodatainfo = Base64.decode(jsonObj.getString("PhotoWlt"), Base64.DEFAULT);
                                byte[] imageTmp = new byte[38862];

                                HandImage.DecWlt2Bmp(photodatainfo, imageTmp);
                                String imageBase = Base64.encodeToString(imageTmp, Base64.DEFAULT);
                                jsonObj.put("PhotoImage", imageBase);

//								Message message = new Message();
//								message.what = 0;
//								message.obj = jsonObj;
//								myHandler.sendMessage(message);
                                listerner.onSuccess(jsonObj);
                                ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                                return;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                LogMg.e("IDCardDev", this + " Read IDCard JSONException:" + e.getMessage());
                            }

                        }

                    }

                    JSONObject json = new JSONObject();
                    try {
                        json.put("ErrCode", st);
                        json.put("ErrMsg", RetCode.GetErrMsg(st));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    listerner.onError(json);
//					Message message = new Message();
//					message.what = 1;
//					message.obj = json;
//					myHandler.sendMessage(message);
                    ReaderDev.getInstance().devLCDControl(false, false, false, false, false);
                }

            }).start();
        }
    }

    public int Read(int readType, int timeout) {
        LogMg.i("IDCardDev", this + " Read readType=" + readType);
        DataManagement idCardDataMan = getDataManInstance(readType, timeout);
        int st = idCardDataMan.execCmd();
        LogMg.i("IDCardDev", this + " Read IDCard return " + st);
        if (st == 0) {
            JSONObject jsonObj = idCardDataMan.getResult();
            try {
                if ((readType == 0) || (readType == 1)) {

                    idtype = jsonObj.getString("idtype");
                    if (idtype == "I") {
                        this.enname = jsonObj.getString("EnName");
                        this.sex = jsonObj.getString("Sex");
                        this.idNum = jsonObj.getString("IDNUM");
                        UNMARC = jsonObj.getString("UNMARC");
                        this.chname = jsonObj.getString("chname");
                        this.dateStart = jsonObj.getString("DateStart");
                        this.dateEnd = jsonObj.getString("DateEnd");
                        this.birth = jsonObj.getString("Birth");
                        idver = jsonObj.getString("idver");
                        this.grantDepart = jsonObj.getString("GrantDepart");
                    } else {
                        this.name = jsonObj.getString("Name");
                        this.sex = jsonObj.getString("Sex");
                        this.nation = jsonObj.getString("Nation");
                        this.birth = jsonObj.getString("Birth");
                        this.addr = jsonObj.getString("Address");
                        this.idNum = jsonObj.getString("IDNUM");
                        this.grantDepart = jsonObj.getString("GrantDepart");
                        this.dateStart = jsonObj.getString("DateStart");
                        this.dateEnd = jsonObj.getString("DateEnd");
                    }


                    byte[] photodatainfo = Base64.decode(jsonObj.getString("PhotoWlt"), Base64.DEFAULT);
                    byte[] imageTmp = new byte[38862];

                    // for (int i = 0; i < 200000; i++) {
                    HandImage.DecWlt2Bmp(photodatainfo, imageTmp);

                    photo = BitmapFactory.decodeByteArray(imageTmp, 0, 38862);

                    /// * 121 */ String photoPath = jsonObj.getString("Photo");
                    /// * 122 */ File file = new File(photoPath);
                    /// * 123 */ if (file.exists()) {
                    /// * 124 */ this.photo =
                    /// BitmapFactory.decodeFile(photoPath);
                    /// * */ } else
                    /// * 126 */ LogMg.i("IDCardDev", this + "Read IDCard's
                    /// photo is not exist, photoPath=" + photoPath);
                }
                if (readType == 1) {
                    this.finger = jsonObj.getString("Finger");
                } else if (readType == 2) {
                    this.addInfo = jsonObj.getString("AddInfo");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogMg.e("IDCardDev", this + " Read IDCard JSONException:" + e.getMessage());
            }
        }

        return st;
    }

    protected DataManagement getDataManInstance(int readType) {
        return new IDCardDataMan(readType);
    }

    protected DataManagement getDataManInstance(int readType, int timeout) {
        return new IDCardDataMan(readType, timeout);
    }
//	final Handler myHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:// ClipReturnListener.onSuccess
//				if (m_ClipReturnListener != null) {
//					m_ClipReturnListener.onSuccess((JSONObject) msg.obj);
//				}
//				break;
//			case 1:// ClipReturnListener.onError
//				if (m_ClipReturnListener != null) {
//					m_ClipReturnListener.onError((JSONObject) msg.obj);
//				}
//				break;
//			case 2:// ReturnListener.onSuccess
//				if (m_ReturnListener != null) {
//					m_ReturnListener.onSuccess((String) msg.obj);
//				}
//				break;
//			case 3:// ReturnListener.onError
//				int err_code = (Integer) msg.obj;
//				if (m_ReturnListener != null) {
//					m_ReturnListener.onError(err_code, RetCode.GetErrMsg(err_code));
//				}
//				break;
//			default:
//				break;
//			}
//		}
//	};
}
