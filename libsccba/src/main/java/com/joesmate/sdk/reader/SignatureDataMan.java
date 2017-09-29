package com.joesmate.sdk.reader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.joesmate.sdk.util.RetCode;

import org.json.JSONException;

/**
 * Created by andre on 2017/7/27 .
 */

public class SignatureDataMan extends DataManagement {

    public SignatureDataMan(byte[] cmd, int timeout) {
        //  byte[] cmd = new byte[]{0, 0};
//        cmd[0] = (byte) 0xc7;
//        switch (type) {
//            case 0:
//                cmd[1] = (byte) 0x01;
//                break;
//            case 1:
//                cmd[1] = (byte) 0x02;
//                break;
//            case 2:
//                cmd[1] = (byte) 0x03;
//                break;
//        }
        cmdMan = new MT3YCmdMan(cmd);
        cmdMan.setBigData(true);
        cmdMan.setCommTimeouts(timeout);
    }

    @Override
    protected int parseResult() throws JSONException {
        byte[] recvBuffer = this.cmdMan.getRecvData();
        if (recvBuffer != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(recvBuffer, 0, recvBuffer.length);
            this.data.put("SignaBMP", bitmap);
            return RetCode.OP_OK;
        } else {
            return RetCode.ERR_USER_CANCEL;
        }

//        String str = new String(recvBuffer);
//        String[] strs = str.split("|");
//        if (strs.length >= 3) {
//            int heigth = Integer.parseInt(strs[0]); //(recvBuffer[1] & 0xff) + ((recvBuffer[0] << 8) & 0xffff);
//            int weigth = Integer.parseInt(strs[1]);//(recvBuffer[3] & 0xff) + ((recvBuffer[2] << 8) & 0xffff);
//
//            String Datastr = strs[3].substring(1, strs.length - 1);
//            String[] points = Datastr.split(";");
//            Paint mPaint = new Paint();
//            mPaint.setColor(Color.BLACK);
//            Bitmap bitmap = Bitmap.createBitmap(weigth, heigth, Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            canvas.drawColor(Color.WHITE);
//            for (String point : points) {
//                if (point != null || point != "") {
//                    String s[] = point.split(",");
//                    float x = Float.parseFloat(s[0]);
//                    float y = Float.parseFloat(s[1]);
//                    float w = Float.parseFloat(s[2]);
//                    mPaint.setStrokeWidth(w);
//                    canvas.drawPoint(x, y, mPaint);
//                }
//            }
//            this.data.put("SignaBMP", bitmap);
//            return RetCode.OP_OK;
//        } else {
//            return RetCode.ERR_USER_CANCEL;
//        }
        // bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

    }

}
