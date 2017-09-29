package com.joesmate.sdk.reader;

import com.joesmate.sdk.util.LogMg;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class DataManagement {
    protected CmdManagement cmdMan = null;
    protected JSONObject data = new JSONObject();


    public int execCmd() {
        synchronized (DataManagement.class) {
            if (this.cmdMan != null) {
                LogMg.i("DataManagement", this + " enter execCmd method.");
                int st = this.cmdMan.SendRecv();
                if (st != 0) {
                    LogMg.i("DataManagement", this + " CmdManagement return " + st);
                    return st;
                }
            }

            int pRes = -74;
            try {
                pRes = parseResult();

            } catch (JSONException e) {
                e.printStackTrace();
                LogMg.e("DataManagement", this + " parseResult " + e.getMessage());
            }
            LogMg.i("DataManagement", this + " parseResult return " + pRes);
            return pRes;
        }
    }

    protected abstract int parseResult() throws JSONException;

    public JSONObject getResult() {
        return this.data;
    }
}


