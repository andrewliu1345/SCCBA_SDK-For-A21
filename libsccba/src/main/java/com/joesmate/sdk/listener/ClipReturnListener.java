package com.joesmate.sdk.listener;

import org.json.JSONObject;

public abstract interface ClipReturnListener
{
  public abstract void onSuccess(JSONObject paramJSONObject);
  
  public abstract void onError(JSONObject paramJSONObject);
}


/* Location:              F:\Work\A20\（X86）MT4B-00-RV9B37-FP-YM新版[V1.0.0.7]\DEMO\YMReader\libs\YMReader.jar!\com\example\listener\ClipReturnListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */