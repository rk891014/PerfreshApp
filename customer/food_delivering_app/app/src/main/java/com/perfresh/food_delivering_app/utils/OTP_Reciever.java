package com.perfresh.food_delivering_app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class OTP_Reciever extends BroadcastReceiver {
    private static EditText editText;

    public void setEditText(EditText editText){
        OTP_Reciever.editText=editText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages= Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage sms : messages){
            String message=sms.getMessageBody();
            Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            Log.e("otp",message);
            editText.setText(message);
        }
    }
}
