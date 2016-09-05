//package com.example.think.waterworksapp;
//
//import android.app.Activity;
//
//
//import android.app.Activity;
//import android.content.Intent;
//import android.nfc.NdefMessage;
//import android.nfc.NdefRecord;
//import android.nfc.NfcAdapter;
//import android.nfc.Tag;
//import android.nfc.tech.Ndef;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ShowNFCTagContentActivity extends Activity {
//    private TextView mTagContent;
//
//    private Tag      mDetectedTag;
//
//    private String   mTagText;
//
//    private void readAndShowData(Intent intent) {
//        mDetectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        Ndef ndef = Ndef.get(mDetectedTag);
//        mTagText = ndef.getType() + "\n最大数据容量：" + ndef.getMaxSize()
//                + " bytes\n\n";
//        readNFCTag();
//        mTagContent.setText(mTagText);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_nfctag_content);
//        mTagContent = (TextView) findViewById(R.id.textview_tag_content);
//        //获取Tag对象
//        mDetectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        //创建Ndef对象
//        Ndef ndef = Ndef.get(mDetectedTag);
//        //获取标签的类型和最大容量
//        mTagText = ndef.getType() + "\n最大数据容量：" + ndef.getMaxSize()
//                + " bytes\n\n";
//        //读取NFC标签的数据并解析
//        readNFCTag();
//        //将标签的相关信息显示在界面上
//        mTagContent.setText(mTagText);
//
//    }
//
//    private void readNFCTag() {
//        //判断是否为ACTION_NDEF_DISCOVERED
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            //从标签读取数据（Parcelable对象）
//            Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(
//                    NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//            NdefMessage msgs[] = null;
//            int contentSize = 0;
//            if (rawMsgs != null) {
//                msgs = new NdefMessage[rawMsgs.length];
//                //标签可能存储了多个NdefMessage对象，一般情况下只有一个NdefMessage对象
//                for (int i = 0; i < rawMsgs.length; i++) {
//                    //转换成NdefMessage对象
//                    msgs[i] = (NdefMessage) rawMsgs[i];
//                    //计算数据的总长度
//                    contentSize += msgs[i].toByteArray().length;
//
//                }
//            }
//            try {
//
//                if (msgs != null) {
//                    //程序中只考虑了1个NdefRecord对象，若是通用软件应该考虑所有的NdefRecord对象
//                    NdefRecord record = msgs[0].getRecords()[0];
//                    //分析第1个NdefRecorder，并创建TextRecord对象
//                    TextRecord textRecord = TextRecord.parse(msgs[0]
//                            .getRecords()[0]);
//                    //获取实际的数据占用的大小，并显示在窗口上
//                    mTagText += textRecord.getText() + "\n\n纯文本\n"
//                            + contentSize + " bytes";
//
//                }
//
//            } catch (Exception e) {
//                mTagContent.setText(e.getMessage());
//            }
//        }
//    }
//}