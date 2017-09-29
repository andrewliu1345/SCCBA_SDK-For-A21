/*     */
package com.joesmate.sdk.reader;

/*     */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.joesmate.sdk.util.CmdCode;
import com.joesmate.sdk.util.LogMg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public abstract class CommManagement extends Thread {
    protected static CommManagement sInstance;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    protected BluetoothSocket socket = null;
    protected BluetoothDevice mLastDevice = null;
    protected InputStream mInputStream = null;
    protected OutputStream mOutputStream = null;
    protected volatile boolean mIsConnected = false;
    private volatile boolean mThreadPause = true;
    private volatile boolean mThreadExit = false;
    private static int NconnectType = 1;
    protected String mVersion = "";
    protected int ulTotalTimeOuts = 8000;
    protected boolean isBigData = false;

    public void setBigData(boolean isbigData) {
        this.isBigData = isbigData;
    }

    protected CommManagement() {
    }

    public void run() {
        LogMg.i("CommManagement", this + " runing...");

        while (true) {
            while (true) {
                this.delay(10L);
                if (this.mThreadExit) {
                    if (this.mIsConnected) {
                        this.bluetoothClose();
                    }

                    LogMg.i("CommManagement", this + " exited");
                    return;
                }

                LogMg.i("CommManagement",
                        this + "run  mThreadPause=" + this.mThreadPause + ",mIsConnected=" + this.mIsConnected);
                if (this.mThreadPause) {
                    if (this.mIsConnected) {
                        this.bluetoothClose();
                        LogMg.i("CommManagement", this + " paused...");
                    }

                    this.delay(1000L);
                } else {
                    LogMg.i("CommManagement", this + "run  mIsConnected=" + this.mIsConnected);
                    if (!this.mIsConnected) {
                        try {
                            if (this.deviceConnect(mLastDevice) != 0) {
                                this.delay(1500L);
                                continue;
                            }
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        }
                    }

                    this.delay(10000L);
                    this.heartBeat();
                }
            }
        }
    }

    private int heartBeat() {
        synchronized (CommManagement.class) {
            byte[] Writebuffer = new byte[]{(byte) 88};
            LogMg.d("CommManagement", "heartBeat");
            if (this.socket != null && this.socket.isConnected()) {
                try {
                    this.mOutputStream.write(Writebuffer, 0, 1);
                    this.mOutputStream.flush();
                } catch (IOException var4) {
                    this.bluetoothBroken();
                    var4.printStackTrace();
                    LogMg.e("CommManagement", "heartBeat Error:" + var4.getMessage());
                    return -17;
                }

                return 0;
            } else {
                this.bluetoothBroken();
                return -17;
            }
        }
    }

    protected int ReadVersion() {

        synchronized (CommManagement.class) {
            LogMg.i("CommManagement", this + " ReadVersion");
            byte[] cmd = new byte[]{(byte) 49, (byte) 17, (byte) 0};
            byte[] recv = new byte[CmdCode.MAX_SIZE_BUFFER];
            int[] recvLen = new int[1];
            LogMg.i("CommManagement", this + " ReadVersion SendRecv");
            int st = this.SendRecv(cmd, cmd.length, recv, recvLen);
            LogMg.i("CommManagement", this + " ReadVersion SendRecv return st=" + st);
            if (st == 0) {
                this.mVersion = new String(recv);
                LogMg.d("CommManagement", this + " ReadVersion=" + this.getVersion());
            } else {
                this.mVersion = "";
                LogMg.d("CommManagement", this + " ReadVersion, return=" + st);
            }

            return st;
        }
    }

    private void delay(long m) {
        try {
            Thread.sleep(m);
        } catch (Exception var4) {
            this.bluetoothClose();
        }

    }

    public int bluetoothClose() {

        synchronized (CommManagement.class) {
            LogMg.i("CommManagement", this + " bluetoothClose");
            this.mVersion = "";
            this.mIsConnected = false;
            if (this.mInputStream != null) {
                try {
                    this.mInputStream.close();
                } catch (IOException var5) {
                    LogMg.v("CommManagement", "mInputStream.close:" + var5.getMessage());
                }
            }

            if (this.mOutputStream != null) {
                try {
                    this.mOutputStream.close();
                } catch (IOException var4) {
                    LogMg.v("CommManagement", "mOutputStream.close:" + var4.getMessage());
                }
            }

            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException var3) {
                    LogMg.v("CommManagement", "socket.close:" + var3.getMessage());
                }
            }

            this.mInputStream = null;
            this.mOutputStream = null;
            this.socket = null;
            return 0;
        }
    }

    public int deviceConnect() throws IOException {

        synchronized (CommManagement.class) {
            LogMg.i("CommManagement", this + " deviceConnect, mIsConnected=" + this.mIsConnected);
            int st;
            if (this.mIsConnected) {
                st = this.ReadVersion();
                switch (st) {
                    case -19:
                        st = this.ReadVersion();
                        if (st == 0) {
                            return 0;
                        }
                        break;
                    case 0:
                        return 0;
                }
            }

            this.mThreadPause = true;
            LogMg.i("CommManagement", this + " deviceConnect, socketConn");
            st = this.socketConn();
            LogMg.i("CommManagement", this + " deviceConnect, socketConn st=" + st);
            if (st == 0) {
                this.delay(1000L);
                st = this.ReadVersion();
            }

            this.mThreadPause = false;
            this.mIsConnected = st == 0;
            return st;
        }
    }

    public int deviceConnect(BluetoothDevice device) {

        synchronized (CommManagement.class) {
            if (device != null) {
                LogMg.i("CommManagement",
                        this + " deviceConnect, mIsConnected=" + this.mIsConnected + " ,device=" + device.getAddress());
                if (this.mLastDevice != null && this.mLastDevice.getAddress().equals(device.getAddress())
                        && this.mIsConnected) {
                    int stReadVer = this.ReadVersion();
                    switch (stReadVer) {
                        case -19:
                            stReadVer = this.ReadVersion();
                            if (stReadVer == 0) {
                                return 0;
                            }
                            break;
                        case 0:
                            return 0;
                    }
                }

                if (this.mIsConnected) {
                    this.bluetoothClose();
                }

                this.mThreadPause = true;
                NconnectType = 1;
                LogMg.i("CommManagement", this + " deviceConnect, bluetoothConn");
                this.bluetoothConn(device);
                LogMg.i("CommManagement", this + " deviceConnect, bluetoothConn NconnectType=" + NconnectType);
                if (NconnectType == 0) {
                    this.mLastDevice = device;
                    this.delay(1000L);
                    //NconnectType = this.ReadVersion();
                }

                this.mThreadPause = false;
                this.mIsConnected = NconnectType == 0;
                return NconnectType;
            } else {
                return -74;
            }
        }
    }

    private int socketConn() {

        synchronized (CommManagement.class) {
            LogMg.i("CommManagement", this + "start socketConn");
            NconnectType = 1;
            Set<?> pairedDevices = this._bluetooth.getBondedDevices();
            if (this.mLastDevice != null && pairedDevices.contains(this.mLastDevice)) {
                LogMg.i("CommManagement", this + " socketConn, connect to mLastDevice");
                this.bluetoothConn(this.mLastDevice);
                if (NconnectType < 0) {
                    this.mLastDevice = null;
                }

                return NconnectType;
            } else {
                if (pairedDevices.size() > 0) {
                    Iterator<?> var4 = pairedDevices.iterator();

                    while (var4.hasNext()) {
                        BluetoothDevice device = (BluetoothDevice) var4.next();
                        if (device.getName() != null && (device.getName().startsWith("Dual-SPP")
                                || device.getName().startsWith("joesmate"))) {
                            LogMg.i("CommManagement", this + " socketConn, device=" + device.getAddress());
                            this.bluetoothConn(device);
                            if (NconnectType == 0) {
                                this.mLastDevice = device;
                                return NconnectType;
                            }
                        }
                    }
                }

                LogMg.i("CommManagement", this + "socketConn return NconnectType=" + NconnectType);
                return NconnectType;
            }
        }
    }

    private void bluetoothConn(BluetoothDevice device) {

        synchronized (CommManagement.class) {
            LogMg.i("CommManagement", this + "start bluetoothConn, NconnectType=" + NconnectType);

            try {

                NconnectType = this.open_device(device);
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            if (NconnectType == 0) {
                LogMg.v("CommManagement", "openDevice OK");
            } else {
                LogMg.v("CommManagement", "openDevice Fail");
            }

            LogMg.i("CommManagement", this + " bluetoothConn, return NconnectType=" + NconnectType);
        }
    }

    private int open_device(BluetoothDevice device) throws IOException {
        LogMg.i("CommManagement", this + " open_device start");

        label97:
        {
            try {
                if (device != null) {
                    this.socket = device
                            .createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    if (socket.isConnected())
                        socket.close();
                    this.socket.connect();

                }
                break label97;
            } catch (IOException var11) {
                var11.printStackTrace();
                this.socket = null;
            } finally {
                if (this.socket != null) {
                    LogMg.i("CommManagement", "Connect OK " + this.socket);

                    try {
                        this.mInputStream = this.socket.getInputStream();
                        this.mOutputStream = this.socket.getOutputStream();
                    } catch (IOException var10) {
                        LogMg.e("CommManagement", "open_device exceptioon: " + var10.getMessage());
                        return -2;
                    }
                }

            }

            return -1;
        }

        LogMg.i("CommManagement", this + " open_device return socket = " + this.socket);
        return this.socket != null ? 0 : -3;
    }

    public boolean isConnected() {
        return this.getIsConnected();
    }

    public boolean getIsConnected() {
        return this.mIsConnected;
    }

    /**
     * 发送指令
     **/
    public abstract int SendRecv(byte[] var1, int var2, byte[] var3, int[] var4);

    protected void bluetoothBroken() {
        this.bluetoothClose();
    }

    public int closeDevice() {
        LogMg.i("CommManagement", this + " closeDevice");
        this.mThreadPause = true;
        return this.bluetoothClose();
    }

    public void setCommTimeouts(int mSecTotal) {
        this.ulTotalTimeOuts = mSecTotal;
    }

    public String getVersion() {
        return this.mVersion;
    }

    protected void InputFulsh() throws Exception {

        int ix = this.mInputStream.available();
        if (ix > 0) {
            byte[] b = new byte[ix];
            this.mInputStream.read(b);
            LogMg.e("InputFulsh", "清空缓存：ix=%d,ix=%s", ix, b);
        }
    }
}