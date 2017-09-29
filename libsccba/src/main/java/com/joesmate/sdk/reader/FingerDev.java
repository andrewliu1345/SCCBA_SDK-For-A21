/*    */ package com.joesmate.sdk.reader;

import org.json.JSONObject;

import com.joesmate.sdk.listener.ClipReturnListener;
/*    */
/*    */ import com.joesmate.sdk.listener.ReturnListener;
import com.joesmate.sdk.util.RetCode;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/*    */
/*    */ @SuppressLint("HandlerLeak")
public abstract class FingerDev {
	/*    */ private static final String TAG = "FingerDev";
	/*    */


	/*    */
	/*    */ public abstract void regFingerPrint(final ReturnListener paramReturnListener, final int paramInt);

	public abstract String getFactory();

	/*    */
	/* 14 */ protected byte[] recvBuffer = null;
	/* 15 */ protected boolean isBigData = false;
	protected ClipReturnListener m_ClipReturnListener;
	protected ReturnListener m_ReturnListener;

	/*    */
	/*    */ public abstract void sampFingerPrint(final ReturnListener paramReturnListener, final int paramInt);

	/*    */
	/*    */ public abstract void imgFingerPrint(final ReturnListener paramReturnListener, final int paramInt);

	/*    */
	/*    */ protected int fingerChannel(byte[] data, byte mode)
	/*    */ {
		/* 23 */ byte[] sendData = new byte[data.length + 3];
		/* 24 */ sendData[0] = -58;
		sendData[1] = 2;
		sendData[2] = mode;
		 System.arraycopy(data, 0, sendData, 3, data.length);
		 CmdManagement cmdMan = new MT3YCmdMan(sendData);
		 int st = cmdMan.SendRecv();
		 if (st == 0)/* 29 */ this.recvBuffer = cmdMan.getRecvData();
		 return st;
		 }







	 public int fingerSetBaudRate(int indxBaudRate)
	 {
		 byte[] sendData = { -58, 1, (byte) indxBaudRate };
		 CmdManagement cmdMan = new MT3YCmdMan(sendData);
		 int st = cmdMan.SendRecv();
		 return st;
		 }


	 public void setBigData(boolean isbigData)
	 {
		 this.isBigData = isbigData;
		 }

//	protected final Handler myHandler = new Handler() {
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
	/*    */ }

/*
 * Location:
 * F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader
 * .jar!\com\example\reader\FingerDev.class Java compiler version: 6 (50.0)
 * JD-Core Version: 0.7.1
 */