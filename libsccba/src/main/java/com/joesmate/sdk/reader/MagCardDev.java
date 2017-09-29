package com.joesmate.sdk.reader;


import com.joesmate.sdk.listener.ClipReturnListener;
import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;

import org.json.JSONException;
import org.json.JSONObject;


public class MagCardDev {
    private static final MagCardDev sInstance = new MagCardDev();


    public static MagCardDev getInstance() {
        synchronized (MagCardDev.class) {

            return sInstance;
        }
    }


    public int magReadStart() {
        byte[] cmd = {50, 98};
        CmdManagement cmdMan = new MT3YCmdMan(cmd);
        int st = cmdMan.SendRecv();
        return st;
    }


    public void magRead(final ClipReturnListener listener, final int timeout) {
        synchronized (MagCardDev.class) {
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    int st = magReadStart();
                    if (st != 0) {
                        if (listener != null) {
                            JSONObject json = new JSONObject();
                            try {
                                json.put("ErrCode", st);
                                json.put("ErrMsg", RetCode.GetErrMsg(st));
                            }
                            /*     */ catch (JSONException e) {
								/* 60 */
                                e.printStackTrace();
								/*     */
                            }
							/*     */
							/* 63 */
                            listener.onError(json);
							/*     */
                        }

                        return;
                    }
					/* 39 */
                    JSONObject obj = new JSONObject();
					/* 40 */
                    long time = System.currentTimeMillis();
					/* 41 */
					/* 42 */
                    while (System.currentTimeMillis() - time < timeout * 1000)
					/*     */ {
						/* 44 */
                        st = magnet(obj, 0);
						/* 45 */
                        if (st == 0)
						/*     */ {
							/* 47 */
                            if (listener != null)/* 48 */
                                listener.onSuccess(obj);
							/* 49 */
                            return;
							/*     */
                        }
						/*     */
                    }
					/* 52 */
                    if (listener != null)
					/*     */ {
						/* 54 */
                        JSONObject json = new JSONObject();
						/*     */
                        try {
							/* 56 */
                            json.put("ErrCode", st);
							/* 57 */
                            json.put("ErrMsg", RetCode.GetErrMsg(st));
							/*     */
                        }
						/*     */ catch (JSONException e) {
							/* 60 */
                            e.printStackTrace();
							/*     */
                        }
						/*     */
						/* 63 */
                        listener.onError(json);
						/*     */
                    }

                }
            })).start();

			/*     */
        }
    }

    /*     */
	/*     */
    private int magnet(JSONObject obj, int timeout) {
		/* 68 */
        byte[] cmd = {50, 96, (byte) timeout};
		/* 69 */
        DataManagement magMan = new MagCardMan(cmd);
		/* 70 */
        int st = magMan.execCmd();
		/* 71 */
        LogMg.i("MagCardDev", this + " magRead return " + st);
		/*     */
		/* 73 */
        JSONObject json = magMan.getResult();
		/* 74 */
        if (st == 0)
		/*     */ {
			/*     */
            try {
				/* 77 */
                obj.put("Track1", json.getString("Track1"));
				/* 78 */
                obj.put("Track2", json.getString("Track2"));
				/* 79 */
                obj.put("Track3", json.getString("Track3"));
				/*     */
            }
			/*     */ catch (JSONException e) {
				/* 82 */
                e.printStackTrace();
				/*     */
            }
			/*     */
			/* 85 */
            return st;
			/*     */
        }
		/*     */
		/*     */
        try
		/*     */ {
			/* 90 */
            if (obj != null)
			/*     */ {
				/* 92 */
                obj.put(RetCode.Key_ErrCode, st);
				/* 93 */
                obj.put(RetCode.Key_ErrMsg, RetCode.GetErrMsg(st));
				/*     */
            }
			/*     */
        }
		/*     */ catch (JSONException e) {
			/* 97 */
            e.printStackTrace();
			/*     */
        }
		/*     */
		/* 100 */
        return st;
		/*     */
    }
	/*     */
}