package com.example.think.waterworksapp.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Created by Think on 2016/8/24.
 */
public class NfcUtils {

    private final String TAG = "NFC Demo";

    //三种ENF的消息模式
    public static final String NFC_CONNECTED_MESSAGE = "cn.wsn.parkingproject.nfc.connected";
    public static final String NFC_DISCONNECTED_MESSAGE = "cn.wsn.parkingproject.nfc.disconnected";
    public static final String NFC_REFRESH_MESSAGE = "cn.wsn.parkingproject.nfc.refresh";

    private NfcAdapter nfcAdapter;
    private NfcA nfcA;
    private Activity activity;
    private GetDeviceIdListener listener;
    private Handler handler = new Handler();

    public NfcUtils(Activity activity,GetDeviceIdListener listener){
        this.activity = activity;
        this.listener = listener;
        initNfcAdapter();
    }

    private boolean initNfcAdapter() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (nfcAdapter == null){
            ToastUtils.showToast(activity,"不支持NFC");
            activity.finish();
            return false;
        }
        if (!nfcAdapter.isEnabled()){
            ToastUtils.showToast(activity,"请先打开NFC");
            activity.finish();
            return false;
        }
        final PendingIntent m_pending_intent = PendingIntent
                .getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        //m_nfc_adapter接收到意图就指向刚刚的创建pendingIntent
        handler.post(new Runnable() {
            @Override
            public void run() {
                    nfcAdapter.enableForegroundDispatch(activity, m_pending_intent,
                        new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
                                new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)},
                        new String[][]{new String[]{Ndef.class.getName()}});
            }
        });
        return true;
    }

    public void processNfcConnectedIntent(Intent intent) {
            //从标签读取数据（Parcelable对象）
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msgs[] = null;
            int contentSize = 0;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                //标签可能存储了多个NdefMessage对象，一般情况下只有一个NdefMessage对象
                for (int i = 0; i < rawMsgs.length; i++) {
                    //转换成NdefMessage对象
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    //计算数据的总长度
                    contentSize += msgs[i].toByteArray().length;
                }
            }else {
                listener.getDeviceFail();
                return;
            }
            try {
                if (msgs != null) {
                    NdefRecord record = msgs[0].getRecords()[0];
                    //分析第1个NdefRecorder，并创建TextRecord对象
                    TextRecord textRecord = TextRecord.parse(msgs[0]
                            .getRecords()[0]);
                    //获取实际的数据占用的大小，并显示在窗口上
                    listener.getDeviceSuccess(textRecord.getText());
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                listener.getDeviceFail();
            }
    }





    public interface GetDeviceIdListener{
        void getDeviceSuccess(String deviceID);
        void getDeviceFail();
    }
}
