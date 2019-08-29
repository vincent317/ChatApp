package com.example.android.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ClosingService extends Service {
    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        ChatRoomActivity.clearConnection();
        MainActivity.clearConnection();
        SignupActivity.clearConnection();
        stopSelf();
    }
}
