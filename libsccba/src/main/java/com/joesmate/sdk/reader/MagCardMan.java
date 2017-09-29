package com.joesmate.sdk.reader;

import com.joesmate.sdk.util.LogMg;
import com.joesmate.sdk.util.RetCode;

import org.json.JSONException;

public class MagCardMan extends DataManagement {


    public MagCardMan(int timeout) {

        byte[] cmd = {50, 96, (byte) timeout};
        this.cmdMan = new MT3YCmdMan(cmd);
        cmdMan.secTimeout = timeout * 1000;
    }

    public MagCardMan(byte[] cmd) {
        this.cmdMan = new MT3YCmdMan(cmd);
    }

    public MagCardMan(CmdManagement cmd) {
        this.cmdMan = cmd;
    }

    protected int parseResult()
            throws JSONException {
        if (this.cmdMan != null) {
            LogMg.i("MagCardMan", this + " enter parseResult method.");
            byte[] recvBuffer = this.cmdMan.getRecvData();
            this.data.put("Track1", "");
            this.data.put("Track2", "");
            this.data.put("Track3", "");

            if ((recvBuffer != null) && (recvBuffer.length > 2)) {
                if (((recvBuffer[0] != 245) && (recvBuffer[0] > 79)) ||
                        ((recvBuffer[1] != 245) && (recvBuffer[1] > 40)) || (
                        (recvBuffer[2] != 245) && (recvBuffer[2] > 170))) {
                    this.data.put("ErrCode", -60);
                    this.data.put("ErrMsg", RetCode.GetErrMsg(-60));
                    return -60;
                }
                LogMg.i("MagCardMan", this + " begin parse track data.");
                int track1Len = 0;
                int track2Len = 0;
                int track3Len = 0;
                if (recvBuffer[0] != 245) track1Len = recvBuffer[0];
                if (recvBuffer[1] != 245) track2Len = recvBuffer[1];
                if (recvBuffer[2] != 245) track3Len = recvBuffer[2];
                LogMg.i("MagCardMan", this + " track1Len=" + track1Len + ",track2Len=" + track2Len + ",track3Len=" + track3Len);
                if ((track1Len > 0) && (track1Len <= 80)) {
                    byte[] track1Data = new byte[track1Len];
                    System.arraycopy(recvBuffer, 3, track1Data, 0, track1Len);
                    this.data.put("Track1", new String(track1Data));
                }
                if ((track2Len > 0) && (track2Len <= 40)) {
                    byte[] track2Data = new byte[track2Len];
                    System.arraycopy(recvBuffer, 3 + track1Len, track2Data, 0, track2Len);
                    this.data.put("Track2", new String(track2Data));
                }
                if ((track3Len > 0) && (track3Len <= 170)) {
                    byte[] track3Data = new byte[track3Len];
                    System.arraycopy(recvBuffer, 3 + track1Len + track2Len, track3Data, 0, track3Len);
                    this.data.put("Track3", new String(track3Data));
                }

                LogMg.i("MagCardMan", this + " magnetic_read end");
                return 0;
            }
        }
        this.data.put("ErrCode", -73);
        this.data.put("ErrMsg", RetCode.GetErrMsg(-73));
        return -73;
    }
}
