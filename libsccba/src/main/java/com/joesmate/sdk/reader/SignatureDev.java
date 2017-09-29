package com.joesmate.sdk.reader;

import android.graphics.Bitmap;

import com.joesmate.sdk.util.RetCode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andre on 2017/7/27 .
 */

public class SignatureDev {
    private SignatureDev() {
    }

    private static final SignatureDev sInstance = new SignatureDev();

    public static SignatureDev getInstance() {
        return sInstance;
    }

    private Bitmap mSignaImg = null;

    public Bitmap getmSignaImg() {
        return mSignaImg;
    }

    public int Start(int timeout) {
        final byte cmd[] = new byte[]{(byte) 0xc7, (byte) 0x01};
        SignatureDataMan sd = new SignatureDataMan(cmd, timeout);
        int iRet = sd.execCmd();
        if (iRet == 0) {
            JSONObject json = sd.getResult();
            try {
                mSignaImg = (Bitmap) json.get("SignaBMP");
            } catch (JSONException e) {
                return RetCode.ERR_DATAFORMAT;
            }

        }

        return iRet;
    }
}
