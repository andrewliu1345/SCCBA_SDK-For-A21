package com.example.appdemo;

import com.joesmate.sccba.SccbaReader;
import com.joestmate.a21.sccbasdk.R;
import com.siro.touch.lib.drivers.api.SccbaTouchDriversApi;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends Activity {

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private SccbaTouchDriversApi sccba_reader;
    private String[] fingerture;


    // 图像
    private static ImageView image = null;

    /**
     * 连接设备
     */
    static Button connect;


    /**
     * 读取IC卡
     */
    static Button readic;


    /**
     * 断开设备连接
     */

    static Button closedevice;
    /**
     * 获取密码
     */

    static Button getPIN;
    static Button getMag;
    static Button getFinger;
    static Button getID;
    static Button sign;
    static Button initPassWord;
    static Button newMainKey;
    static Button loadwork;
    static Button work;

    private MyClick click = null;


    private String str_in = "";

    /**
     * 状态信息显示
     */
    private static TextView showMessage = null;
    /**
     * 结果信息显示
     */
    private static TextView resultInfo = null;

    boolean isCancel = false;
    int str;

    String potopath = Environment.getExternalStorageDirectory() + "/photo.bmp";
    String signpoto = Environment.getExternalStorageDirectory() + "/photo.png";

    final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            String str_in;
            int st = 0;
            super.handleMessage(msg);
            //handler处理消息   

            if (msg.what == 7) {
                String path = (String) msg.obj;
                Bitmap bmFinger = BitmapFactory.decodeFile(path);
                ImageView imageViewFinger = (ImageView) findViewById(R.id.image);
                Matrix matrix = new Matrix();
                matrix.postRotate(180);
                Bitmap rotFinger = Bitmap.createBitmap(bmFinger, 0, 0, bmFinger.getWidth(), bmFinger.getHeight(), matrix, true);
                imageViewFinger.setImageBitmap(rotFinger);
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sccba_reader = new SccbaReader();


        initButton();

    }

    private void initButton() {

        loadwork = (Button) findViewById(R.id.loadwork);
        work = (Button) findViewById(R.id.jihuo);
        newMainKey = (Button) findViewById(R.id.newmainKey);
        initPassWord = (Button) findViewById(R.id.initPassWork);
        //sign = (Button) findViewById(R.id.sign);
        getID = (Button) findViewById(R.id.getID);
        getFinger = (Button) findViewById(R.id.getFinger);
        getPIN = (Button) findViewById(R.id.getpass);
        closedevice = (Button) findViewById(R.id.close);
        image = (ImageView) findViewById(R.id.image);
        showMessage = (TextView) findViewById(R.id.info);
        resultInfo = (TextView) findViewById(R.id.result);
        connect = (Button) findViewById(R.id.connect);
        readic = (Button) findViewById(R.id.getic);
        getMag = (Button) findViewById(R.id.getmag);

        click = new MyClick();

        loadwork.setOnClickListener(click);
        work.setOnClickListener(click);
        newMainKey.setOnClickListener(click);
        initPassWord.setOnClickListener(click);
        getID.setOnClickListener(click);
        getFinger.setOnClickListener(click);
        getMag.setOnClickListener(click);
//        connect.setOnClickListener(click);
        readic.setOnClickListener(click);
        getPIN.setOnClickListener(click);
        //sign.setOnClickListener(click);
        closedevice.setOnClickListener(click);


    }


    private void ClearInfo() {
        resultInfo.setText("");
        image.setImageBitmap(null);
    }

    private class MyClick implements OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                /**
                 *
                 * 签字
                 *
                 * **/

                case R.id.sign:

                    break;
                /***
                 *
                 * 关闭设备
                 *
                 * **/
                case R.id.close:

                    ClearInfo();
                    isCancel = false;
                    String[] e = sccba_reader.SCCBA_Disconnect();
                    resultInfo.setText(e[0] + "设备关闭成功");
                    Log.e("设备关闭状态", e[0]);
                    showMessage.setText("设备关闭成功");
                    break;


                /**
                 *
                 *
                 * *获取密码
                 *
                 * **/
                case R.id.getpass:
                    ClearInfo();

                    String[] pin = sccba_reader.SCCBA_ReadPin(0, 1, 6, "请输入密码", 1, "20");

                    resultInfo.setText("返回值:" + pin[0] + "\t返回数据:" + pin[1]);
                    showMessage.setText("连接成功");
                    break;
                /**
                 *
                 * *IC卡
                 *
                 *
                 * **/
                case R.id.getic:
                    ClearInfo();
                    String[] string = sccba_reader.SCCBA_GetICCInfo(0, "A000000333", "ABCDEFGHIJKL".toUpperCase(), "60");
                    if (string != null && string[0].equals("0")) {
                        resultInfo.setText("返回值:" + string[0] + "\t返回数据:" + string[1]);
                        showMessage.setText("获取成功");
                    } else {
                        showMessage.setText("获取失败");
                    }
//			Log.e("账号",string+"");

                    break;
                /**
                 *
                 * *磁条卡
                 *
                 *
                 * **/
                case R.id.getmag:
                    ClearInfo();
                    String[] magString = sccba_reader.SCCBA_MsfRead("23", "30");
                    resultInfo.setText("磁条卡返回值：" + magString[0] + "\n返回信息/二三磁道信息：" + magString[1] + "\n二磁道：" + magString[2] + "\n三磁道：" + magString[3]);

                    break;
                /**
                 *
                 * 指纹
                 *
                 *
                 * **/
                case R.id.getFinger:

                    ClearInfo();
                    String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/finger.png";
                    resultInfo.setText("指纹厂家定义：1.维尔指纹 2.天诚指纹 3.中正指纹\n");
                    String[] fingerture = sccba_reader.SCCBA_ReadFinger(sdcardPath, "1", "30");
                    Bitmap bm = BitmapFactory.decodeFile(sdcardPath);
                    image.setImageBitmap(bm);
                    ShowMsg("指纹特征码：\n" + fingerture[3] + "\n指纹特征码大小：" + fingerture[4]);
                    showMessage.setText("读取成功");
                    break;
/**
 *
 *  身份证
 *
 * **/
                case R.id.getID:
                    ClearInfo();
                    String[] fin = sccba_reader.SCCBA_ReadIDFullInfo(potopath, "30");
                    if (fin[0] == "0") {
                        ShowMsg("身份证信息：\n" + fin[1] + fin[2] + fin[3] + fin[4] + fin[5] + fin[6] + fin[7] + fin[8]);
                        Bitmap bmp = BitmapFactory.decodeFile(potopath);
                        image.setImageBitmap(bmp);
                    } else
                        ShowMsg(String.format("读取失败：%s,%s", fin[0], fin[1]));

                    showMessage.setText("读取成功");


                    break;
                /***
                 *
                 * 初始化密码键盘
                 *
                 * ***/
                case R.id.initPassWork:
                    int returnpin = sccba_reader.SCCBA_InitPinPad();
                    resultInfo.setText("初始化密码键盘:" + returnpin);
                    break;
                /**
                 * 更新主密钥
                 * */
                case R.id.newmainKey:
                    String zmk = "D25AAA6415EA1CAC4CE349208E0DDFE9";
                    String checkvalue = "F8D06870B9EA321E";
                    int i = sccba_reader.SCCBA_UpdateMKey(0,2, zmk, checkvalue);
                    resultInfo.setText("更新主密钥:" + i);
                    Log.e("更新主密钥", i + "");
                    break;
                /**
                 *
                 * 下载工作密钥
                 *
                 * **/
                case R.id.loadwork:
                    String workey = "5C1451B0C7C4EDBCB3DA8D46FC01291A ";
                    String checkvalu = "83549D395C44E3C3";
                    int ie = sccba_reader.SCCBA_DownLoadWKey(0, 0, 2, workey, checkvalu);
                    if (ie == 0) {
                        resultInfo.setText("下载工作密钥成功：" + ie);
                    }
                    Log.e("下载成功", ie + "");
                    break;
                /**
                 *
                 * 激活工作密钥
                 *
                 * */
                case R.id.jihuo:
                    returnpin = sccba_reader.SCCBA_ActiveWKey(0, 0);
                    resultInfo.setText("激活工作密钥：" + returnpin);

                    break;

            }
        }
    }

    private static void ShowMsg(String string) {
        resultInfo.setText(string);
    }

    public void Signa(View v) {
        String[] s = sccba_reader.SCCBA_getSignature(signpoto, "30");
        if (s[0] == "0") {
            resultInfo.setText(s[0] + s[1]);
            showMessage.setText("获取成功");
        } else {
            resultInfo.setText(s[0] + s[1]);
            showMessage.setText("获取失败");
        }
    }

    public void Connect(View v) {
        ClearInfo();
        showMessage.setText("正在连接设备,请稍后...");
        Set<BluetoothDevice> pairedDevices = this._bluetooth.getBondedDevices();
        if (pairedDevices.size() < 0)
            resultInfo.setText("请配对设备");
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName() != null && (device.getName().startsWith("Dual-SPP")
                    || device.getName().startsWith("joesmate"))) {
                String[] s = sccba_reader.SCCBA_Connect(device);
                if (s[0] == "0") {
                    resultInfo.setText(s[0] + s[1]);
                    showMessage.setText("连接成功");
                } else {
                    resultInfo.setText(s[0] + s[1]);
                    showMessage.setText("连接失败");
                    sccba_reader.SCCBA_Disconnect();
                }
            }
        }
    }


    String idnum = null;
    String startDate = null;
    String endDate = null;

    String name = null;
    String cardno = null;
    String track2 = null;
    String track3 = null;
    String f55 = null;


//   public String[] SCCBA_GetICCInfo(int ICType,String AIDList,String TagList,String strTimeout){
//	 
//	   String[] str=new String[18];
//	  
//	   int i;
//	   JSONObject json=new JSONObject();
//	  if(ICType==0){//接触式IC卡
//		 
//		  json=emv.readCardInfo(Integer.parseInt(strTimeout), 0);
//		 if(json!=null){
//			 try {
//					 cardno=json.getString("CardNO");
//					 track2=json.getString("Track2");
//					 track3=json.getString("Track3");
//					 f55=json.getString("Fileld55");		        
//		        str[0]="0";	
//		        
//		        StringBuffer buffer = new StringBuffer();
//		        for(int k = 0,j= TagList.length();k<j;k++){
//		        	//A 账号
//		        	if("A".equals(TagList.substring(k))){
//		        		
//		        		buffer.append("A").append("0").append(cardno.length()).append(cardno);
//		        	}else if("B".equals(TagList.substring(k))){// B 姓名
//		        		
//		        		buffer.append("B").append("0").append(name.length()).append(name);
//		        	}	else if("C".equals(TagList.substring(k))){// C 证件类型
//		        		
//		        		buffer.append("C").append("").append("");
//		        	}  else if("D".equals(TagList.substring(k))){// D 证件号码
//		        		
//		        		buffer.append("D").append("0").append(idnum.length()).append(idnum);      	
//		          }else if("E".equals(TagList.substring(k))){// E 余额
//		        		
//		        		buffer.append("E").append("0").append("").append("");      	
//		          }else if("F".equals(TagList.substring(k))){// F 余额上限··
//		        		
//		        		buffer.append("F").append("0").append("").append("");      	
//		          }else if("G".equals(TagList.substring(k))){// G 单笔交易限额
//		        		
//		        		buffer.append("G").append("0").append("").append("");      	
//		          }else if("H".equals(TagList.substring(k))){// H 应用货币类型
//		        		
//		        		buffer.append("H").append("0").append("").append("");      	
//		          }else if("I".equals(TagList.substring(k))){// I 失效日期
//		        		
//		        		buffer.append("I").append("0").append(endDate.length()).append(endDate);      	
//		          }else if("J".equals(TagList.substring(k))){// J 卡序列号
//		        		
//		        		buffer.append("J").append("0").append("02").append("01");      	
//		          }else if("K".equals(TagList.substring(k))){// K 二磁道信息
//		        		
//		        		buffer.append("K").append("0").append(track2.length()).append(track2);      	
//		          }else if("L".equals(TagList.substring(k))){// J 卡序列号
//		        		
//		        		buffer.append("L").append("0").append(track3.length()).append(track3);      	
//		          }
//		        	 Log.i("TagList获取数据", TagList+"---"+buffer.toString());
//				        str[1]=buffer.toString();
//						return str;
//		        }
//		       
//					
//				} catch (JSONException e) {
//					
//					e.printStackTrace();
//				}    
//		 }
//	  }
//	  if(ICType==1){//非接
//		   i= emv.readPbocTrackData(1, json); 
//	  }
//	  if(ICType==2){//自动			
//	 }	  
//	  return str;
//   }

    public String getData(String string, String str, String str1) {
        boolean data = string.contains(str);
        String returnData = null;
        if (data) {
            returnData = string.replace(str, "");
            int i2 = returnData.indexOf(str1);
            returnData = returnData.substring(0, i2);
        }
        return returnData;
    }

}