package com.euroitlabs.broadcastapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class MyService extends Service {

    private static final String TAG = "HelloService";
    WifiManager mainWifi;
    private boolean isRunning = false;
    MainActivity mainactivity;
    BroadcastReceiver receiver;
    String decryptedMessage = "";
    int M1 = 2, M2 = 4, M3 = 9, M4 = 5;
    int N1 = 4, N2 = 6;
    String wifiname = "";
    private WifiReceiver receiverWifi;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        final boolean running = true;
     //   Toast.makeText(this, " Service Started", Toast.LENGTH_SHORT).show();
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();

        Runnable r = new Runnable() {

            @Override
            public void run() {
                while (running) {
                    if (mainWifi.isWifiEnabled() == true) {
                        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        mainWifi.startScan();
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        return START_STICKY;

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Notify(String broadcast_message, int number) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this, NotificationView.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Message from " + "Satvir")
                .setContentText(broadcast_message).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(number, noti);
    }

    String decryptMessage(String encryptedHotspotName) {
        int index;
        decryptedMessage = "";
        //   char c ;
        Log.d(TAG, "Service hotspot to decrypt: " + encryptedHotspotName);
        for (index = 0; index < encryptedHotspotName.length(); index++) {
            // Toast.makeText(getApplicationContext(), encryptedHotspotName, Toast.LENGTH_SHORT).show();
            char d = encryptedHotspotName.charAt(index);
            //    Toast.makeText(getApplicationContext(), String.valueOf(d), Toast.LENGTH_SHORT).show();
            switch (d) {
                case 'A':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_A = 1 - (M1 + N1);
                        decryptedChar(char_A);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_A = 1 - (M1 + N2);
                        decryptedChar(char_A);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_A = 1 - (M2 + N1);
                        decryptedChar(char_A);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_A = 1 - (M2 + N2);
                        decryptedChar(char_A);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_A = 1 - (M3 + N1);
                        decryptedChar(char_A);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_A = 1 - (M3 + N2);
                        decryptedChar(char_A);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_A = 1 - (M4 + N1);
                        decryptedChar(char_A);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_A = 1 - (M4 + N2);
                        decryptedChar(char_A);
                    }
                    break;
                case 'B':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_B = 2 - (M1 + N1);
                        decryptedChar(char_B);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_B = 2 - (M1 + N2);
                        decryptedChar(char_B);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_B = 2 - (M2 + N1);
                        decryptedChar(char_B);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_B = 2 - (M2 + N2);
                        decryptedChar(char_B);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_B = 2 - (M3 + N1);
                        decryptedChar(char_B);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_B = 2 - (M3 + N2);
                        decryptedChar(char_B);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_B = 2 - (M4 + N1);
                        decryptedChar(char_B);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_B = 2 - (M4 + N2);
                        decryptedChar(char_B);
                    }
                    break;
                case 'C':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_C = 3 - (M1 + N1);
                        decryptedChar(char_C);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_C = 3 - (M1 + N2);
                        decryptedChar(char_C);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_C = 3 - (M2 + N1);
                        decryptedChar(char_C);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_C = 3 - (M2 + N2);
                        decryptedChar(char_C);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_C = 3 - (M3 + N1);
                        decryptedChar(char_C);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_C = 3 - (M3 + N2);
                        decryptedChar(char_C);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_C = 3 - (M4 + N1);
                        decryptedChar(char_C);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_C = 3 - (M4 + N2);
                        decryptedChar(char_C);
                    }
                    break;
                case 'D':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_D = 4 - (M1 + N1);
                        decryptedChar(char_D);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_D = 4 - (M1 + N2);
                        decryptedChar(char_D);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_D = 4 - (M2 + N1);
                        decryptedChar(char_D);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_D = 4 - (M2 + N2);
                        decryptedChar(char_D);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_D = 4 - (M3 + N1);
                        decryptedChar(char_D);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_D = 4 - (M3 + N2);
                        decryptedChar(char_D);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_D = 4 - (M4 + N1);
                        decryptedChar(char_D);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_D = 4 - (M4 + N2);
                        decryptedChar(char_D);
                    }
                    break;
                case 'E':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_E = 5 - (M1 + N1);
                        decryptedChar(char_E);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_E = 5 - (M1 + N2);
                        decryptedChar(char_E);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_E = 5 - (M2 + N1);
                        decryptedChar(char_E);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_E = 5 - (M2 + N2);
                        decryptedChar(char_E);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_E = 5 - (M3 + N1);
                        decryptedChar(char_E);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_E = 5 - (M3 + N2);
                        decryptedChar(char_E);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_E = 5 - (M4 + N1);
                        decryptedChar(char_E);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_E = 5 - (M4 + N2);
                        decryptedChar(char_E);
                    }
                    break;
                case 'F':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_F = 6 - (M1 + N1);
                        decryptedChar(char_F);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_F = 6 - (M1 + N2);
                        decryptedChar(char_F);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_F = 6 - (M2 + N1);
                        decryptedChar(char_F);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_F = 6 - (M2 + N2);
                        decryptedChar(char_F);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_F = 6 - (M3 + N1);
                        decryptedChar(char_F);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_F = 6 - (M3 + N2);
                        decryptedChar(char_F);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_F = 6 - (M4 + N1);
                        decryptedChar(char_F);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_F = 6 - (M4 + N2);
                        decryptedChar(char_F);
                    }
                    break;
                case 'G':
                    //    Toast.makeText(getApplicationContext(), "Inside case G", Toast.LENGTH_SHORT).show();
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        //       Toast.makeText(getApplicationContext(), "Inside index 0", Toast.LENGTH_SHORT).show();
                        //     Toast.makeText(getApplicationContext(), String.valueOf(M1) + String.valueOf(N1), Toast.LENGTH_SHORT).show();
                        int char_G = 7 - (M1 + N1);
                        //   Toast.makeText(getApplicationContext(), String.valueOf(char_G), Toast.LENGTH_SHORT).show();
                        decryptedChar(char_G);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_G = 7 - (M1 + N2);
                        decryptedChar(char_G);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_G = 7 - (M2 + N1);
                        decryptedChar(char_G);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_G = 7 - (M2 + N2);
                        decryptedChar(char_G);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_G = 7 - (M3 + N1);
                        decryptedChar(char_G);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_G = 7 - (M3 + N2);
                        decryptedChar(char_G);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_G = 7 - (M4 + N1);
                        decryptedChar(char_G);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_G = 7 - (M4 + N2);
                        decryptedChar(char_G);
                    }
                    break;
                case 'H':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_H = 8 - (M1 + N1);
                        decryptedChar(char_H);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_H = 8 - (M1 + N2);
                        decryptedChar(char_H);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_H = 8 - (M2 + N1);
                        decryptedChar(char_H);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_H = 8 - (M2 + N2);
                        decryptedChar(char_H);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_H = 8 - (M3 + N1);
                        decryptedChar(char_H);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_H = 8 - (M3 + N2);
                        decryptedChar(char_H);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_H = 8 - (M4 + N1);
                        decryptedChar(char_H);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_H = 8 - (M4 + N2);
                        decryptedChar(char_H);
                    }
                    break;
                case 'I':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_I = 9 - (M1 + N1);
                        decryptedChar(char_I);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_I = 9 - (M1 + N2);
                        decryptedChar(char_I);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_I = 9 - (M2 + N1);
                        decryptedChar(char_I);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_I = 9 - (M2 + N2);
                        decryptedChar(char_I);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_I = 9 - (M3 + N1);
                        decryptedChar(char_I);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_I = 9 - (M3 + N2);
                        decryptedChar(char_I);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_I = 9 - (M4 + N1);
                        decryptedChar(char_I);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_I = 9 - (M4 + N2);
                        decryptedChar(char_I);
                    }
                    break;
                case 'J':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_J = 10 - (M1 + N1);
                        decryptedChar(char_J);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_J = 10 - (M1 + N2);
                        decryptedChar(char_J);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_J = 10 - (M2 + N1);
                        decryptedChar(char_J);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_J = 10 - (M2 + N2);
                        decryptedChar(char_J);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_J = 10 - (M3 + N1);
                        decryptedChar(char_J);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_J = 10 - (M3 + N2);
                        decryptedChar(char_J);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_J = 10 - (M4 + N1);
                        decryptedChar(char_J);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_J = 10 - (M4 + N2);
                        decryptedChar(char_J);
                    }
                    break;
                case 'K':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_K = 11 - (M1 + N1);
                        decryptedChar(char_K);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_K = 11 - (M1 + N2);
                        decryptedChar(char_K);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_K = 11 - (M2 + N1);
                        decryptedChar(char_K);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_K = 11 - (M2 + N2);
                        decryptedChar(char_K);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_K = 11 - (M3 + N1);
                        decryptedChar(char_K);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_K = 11 - (M3 + N2);
                        decryptedChar(char_K);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_K = 11 - (M4 + N1);
                        decryptedChar(char_K);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_K = 11 - (M4 + N2);
                        decryptedChar(char_K);
                    }
                    break;
                case 'L':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_L = 12 - (M1 + N1);
                        decryptedChar(char_L);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_L = 12 - (M1 + N2);
                        decryptedChar(char_L);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_L = 12 - (M2 + N1);
                        decryptedChar(char_L);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_L = 12 - (M2 + N2);
                        decryptedChar(char_L);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_L = 12 - (M3 + N1);
                        decryptedChar(char_L);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_L = 12 - (M3 + N2);
                        decryptedChar(char_L);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_L = 12 - (M4 + N1);
                        decryptedChar(char_L);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_L = 12 - (M4 + N2);
                        decryptedChar(char_L);
                    }
                    break;
                case 'M':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_M = 13 - (M1 + N1);
                        decryptedChar(char_M);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_M = 13 - (M1 + N2);
                        decryptedChar(char_M);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_M = 13 - (M2 + N1);
                        decryptedChar(char_M);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_M = 13 - (M2 + N2);
                        decryptedChar(char_M);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_M = 13 - (M3 + N1);
                        decryptedChar(char_M);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_M = 13 - (M3 + N2);
                        decryptedChar(char_M);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_M = 13 - (M4 + N1);
                        decryptedChar(char_M);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_M = 13 - (M4 + N2);
                        decryptedChar(char_M);
                    }
                    break;
                case 'N':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_N = 14 - (M1 + N1);
                        decryptedChar(char_N);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_N = 14 - (M1 + N2);
                        decryptedChar(char_N);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_N = 14 - (M2 + N1);
                        decryptedChar(char_N);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_N = 14 - (M2 + N2);
                        decryptedChar(char_N);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_N = 14 - (M3 + N1);
                        decryptedChar(char_N);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_N = 14 - (M3 + N2);
                        decryptedChar(char_N);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_N = 14 - (M4 + N1);
                        decryptedChar(char_N);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_N = 14 - (M4 + N2);
                        decryptedChar(char_N);
                    }
                    break;
                case 'O':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_O = 15 - (M1 + N1);
                        decryptedChar(char_O);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_O = 15 - (M1 + N2);
                        decryptedChar(char_O);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_O = 15 - (M2 + N1);
                        decryptedChar(char_O);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_O = 15 - (M2 + N2);
                        decryptedChar(char_O);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_O = 15 - (M3 + N1);
                        decryptedChar(char_O);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_O = 15 - (M3 + N2);
                        decryptedChar(char_O);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_O = 15 - (M4 + N1);
                        decryptedChar(char_O);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_O = 15 - (M4 + N2);
                        decryptedChar(char_O);
                    }
                    break;
                case 'P':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_P = 16 - (M1 + N1);
                        decryptedChar(char_P);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_P = 16 - (M1 + N2);
                        decryptedChar(char_P);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_P = 16 - (M2 + N1);
                        decryptedChar(char_P);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_P = 16 - (M2 + N2);
                        decryptedChar(char_P);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_P = 16 - (M3 + N1);
                        decryptedChar(char_P);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_P = 16 - (M3 + N2);
                        decryptedChar(char_P);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_P = 16 - (M4 + N1);
                        decryptedChar(char_P);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_P = 16 - (M4 + N2);
                        decryptedChar(char_P);
                    }
                    break;
                case 'Q':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Q = 17 - (M1 + N1);
                        decryptedChar(char_Q);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Q = 17 - (M1 + N2);
                        decryptedChar(char_Q);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Q = 17 - (M2 + N1);
                        decryptedChar(char_Q);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Q = 17 - (M2 + N2);
                        decryptedChar(char_Q);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Q = 17 - (M3 + N1);
                        decryptedChar(char_Q);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Q = 17 - (M3 + N2);
                        decryptedChar(char_Q);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Q = 17 - (M4 + N1);
                        decryptedChar(char_Q);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Q = 17 - (M4 + N2);
                        decryptedChar(char_Q);
                    }
                    break;
                case 'R':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_R = 18 - (M1 + N1);
                        decryptedChar(char_R);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_R = 18 - (M1 + N2);
                        decryptedChar(char_R);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_R = 18 - (M2 + N1);
                        decryptedChar(char_R);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_R = 18 - (M2 + N2);
                        decryptedChar(char_R);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_R = 18 - (M3 + N1);
                        decryptedChar(char_R);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_R = 18 - (M3 + N2);
                        decryptedChar(char_R);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_R = 18 - (M4 + N1);
                        decryptedChar(char_R);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_R = 18 - (M4 + N2);
                        decryptedChar(char_R);
                    }
                    break;
                case 'S':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_S = 19 - (M1 + N1);
                        decryptedChar(char_S);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_S = 19 - (M1 + N2);
                        decryptedChar(char_S);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_S = 19 - (M2 + N1);
                        decryptedChar(char_S);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_S = 19 - (M2 + N2);
                        decryptedChar(char_S);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_S = 19 - (M3 + N1);
                        decryptedChar(char_S);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_S = 19 - (M3 + N2);
                        decryptedChar(char_S);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_S = 19 - (M4 + N1);
                        decryptedChar(char_S);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_S = 19 - (M4 + N2);
                        decryptedChar(char_S);
                    }
                    break;
                case 'T':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_T = 20 - (M1 + N1);
                        decryptedChar(char_T);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_T = 20 - (M1 + N2);
                        decryptedChar(char_T);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_T = 20 - (M2 + N1);
                        decryptedChar(char_T);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_T = 20 - (M2 + N2);
                        decryptedChar(char_T);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_T = 20 - (M3 + N1);
                        decryptedChar(char_T);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_T = 20 - (M3 + N2);
                        decryptedChar(char_T);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_T = 20 - (M4 + N1);
                        decryptedChar(char_T);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_T = 20 - (M4 + N2);
                        decryptedChar(char_T);
                    }
                    break;
                case 'U':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_U = 21 - (M1 + N1);
                        decryptedChar(char_U);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_U = 21 - (M1 + N2);
                        decryptedChar(char_U);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_U = 21 - (M2 + N1);
                        decryptedChar(char_U);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_U = 21 - (M2 + N2);
                        decryptedChar(char_U);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_U = 21 - (M3 + N1);
                        decryptedChar(char_U);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_U = 21 - (M3 + N2);
                        decryptedChar(char_U);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_U = 21 - (M4 + N1);
                        decryptedChar(char_U);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_U = 21 - (M4 + N2);
                        decryptedChar(char_U);
                    }
                    break;
                case 'V':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_V = 22 - (M1 + N1);
                        decryptedChar(char_V);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_V = 22 - (M1 + N2);
                        decryptedChar(char_V);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_V = 22 - (M2 + N1);
                        decryptedChar(char_V);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_V = 22 - (M2 + N2);
                        decryptedChar(char_V);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_V = 22 - (M3 + N1);
                        decryptedChar(char_V);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_V = 22 - (M3 + N2);
                        decryptedChar(char_V);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_V = 22 - (M4 + N1);
                        decryptedChar(char_V);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_V = 22 - (M4 + N2);
                        decryptedChar(char_V);
                    }
                    break;
                case 'W':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_W = 23 - (M1 + N1);
                        decryptedChar(char_W);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_W = 23 - (M1 + N2);
                        decryptedChar(char_W);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_W = 23 - (M2 + N1);
                        decryptedChar(char_W);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_W = 23 - (M2 + N2);
                        decryptedChar(char_W);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_W = 23 - (M3 + N1);
                        decryptedChar(char_W);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_W = 23 - (M3 + N2);
                        decryptedChar(char_W);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_W = 23 - (M4 + N1);
                        decryptedChar(char_W);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_W = 23 - (M4 + N2);
                        decryptedChar(char_W);
                    }
                    break;
                case 'X':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_X = 24 - (M1 + N1);
                        decryptedChar(char_X);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_X = 24 - (M1 + N2);
                        decryptedChar(char_X);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_X = 24 - (M2 + N1);
                        decryptedChar(char_X);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_X = 24 - (M2 + N2);
                        decryptedChar(char_X);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_X = 24 - (M3 + N1);
                        decryptedChar(char_X);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_X = 24 - (M3 + N2);
                        decryptedChar(char_X);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_X = 24 - (M4 + N1);
                        decryptedChar(char_X);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_X = 24 - (M4 + N2);
                        decryptedChar(char_X);
                    }
                    break;
                case 'Y':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Y = 25 - (M1 + N1);
                        decryptedChar(char_Y);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Y = 25 - (M1 + N2);
                        decryptedChar(char_Y);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Y = 25 - (M2 + N1);
                        decryptedChar(char_Y);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Y = 25 - (M2 + N2);
                        decryptedChar(char_Y);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Y = 25 - (M3 + N1);
                        decryptedChar(char_Y);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Y = 25 - (M3 + N2);
                        decryptedChar(char_Y);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Y = 25 - (M4 + N1);
                        decryptedChar(char_Y);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Y = 25 - (M4 + N2);
                        decryptedChar(char_Y);
                    }
                    break;
                case 'Z':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Z = 26 - (M1 + N1);
                        decryptedChar(char_Z);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Z = 26 - (M1 + N2);
                        decryptedChar(char_Z);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Z = 26 - (M2 + N1);
                        decryptedChar(char_Z);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Z = 26 - (M2 + N2);
                        decryptedChar(char_Z);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Z = 26 - (M3 + N1);
                        decryptedChar(char_Z);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Z = 26 - (M3 + N2);
                        decryptedChar(char_Z);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Z = 26 - (M4 + N1);
                        decryptedChar(char_Z);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Z = 26 - (M4 + N2);
                        decryptedChar(char_Z);
                    }
                    break;
                case 'a':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_a = 27 - (M1 + N1);
                        decryptedChar(char_a);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_a = 27 - (M1 + N2);
                        decryptedChar(char_a);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_a = 27 - (M2 + N1);
                        decryptedChar(char_a);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_a = 27 - (M2 + N2);
                        decryptedChar(char_a);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_a = 27 - (M3 + N1);
                        decryptedChar(char_a);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_a = 27 - (M3 + N2);
                        decryptedChar(char_a);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_a = 27 - (M4 + N1);
                        decryptedChar(char_a);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_a = 27 - (M4 + N2);
                        decryptedChar(char_a);
                    }
                    break;
                case 'b':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_b = 28 - (M1 + N1);
                        decryptedChar(char_b);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_b = 28 - (M1 + N2);
                        decryptedChar(char_b);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_b = 28 - (M2 + N1);
                        decryptedChar(char_b);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_b = 28 - (M2 + N2);
                        decryptedChar(char_b);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_b = 28 - (M3 + N1);
                        decryptedChar(char_b);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_b = 28 - (M3 + N2);
                        decryptedChar(char_b);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_b = 28 - (M4 + N1);
                        decryptedChar(char_b);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_b = 28 - (M4 + N2);
                        decryptedChar(char_b);
                    }
                    break;
                case 'c':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_c = 29 - (M1 + N1);
                        decryptedChar(char_c);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_c = 29 - (M1 + N2);
                        decryptedChar(char_c);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_c = 29 - (M2 + N1);
                        decryptedChar(char_c);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_c = 29 - (M2 + N2);
                        decryptedChar(char_c);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_c = 29 - (M3 + N1);
                        decryptedChar(char_c);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_c = 29 - (M3 + N2);
                        decryptedChar(char_c);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_c = 29 - (M4 + N1);
                        decryptedChar(char_c);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_c = 29 - (M4 + N2);
                        decryptedChar(char_c);
                    }
                    break;
                case 'd':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_d = 30 - (M1 + N1);
                        decryptedChar(char_d);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_d = 30 - (M1 + N2);
                        decryptedChar(char_d);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_d = 30 - (M2 + N1);
                        decryptedChar(char_d);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_d = 30 - (M2 + N2);
                        decryptedChar(char_d);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_d = 30 - (M3 + N1);
                        decryptedChar(char_d);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_d = 30 - (M3 + N2);
                        decryptedChar(char_d);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_d = 30 - (M4 + N1);
                        decryptedChar(char_d);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_d = 30 - (M4 + N2);
                        decryptedChar(char_d);
                    }
                    break;
                case 'e':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_e = 31 - (M1 + N1);
                        decryptedChar(char_e);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_e = 31 - (M1 + N2);
                        decryptedChar(char_e);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_e = 31 - (M2 + N1);
                        decryptedChar(char_e);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_e = 31 - (M2 + N2);
                        decryptedChar(char_e);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_e = 31 - (M3 + N1);
                        decryptedChar(char_e);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_e = 31 - (M3 + N2);
                        decryptedChar(char_e);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_e = 31 - (M4 + N1);
                        decryptedChar(char_e);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_e = 31 - (M4 + N2);
                        decryptedChar(char_e);
                    }
                    break;
                case 'f':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_f = 32 - (M1 + N1);
                        decryptedChar(char_f);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_f = 32 - (M1 + N2);
                        decryptedChar(char_f);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_f = 32 - (M2 + N1);
                        decryptedChar(char_f);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_f = 32 - (M2 + N2);
                        decryptedChar(char_f);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_f = 32 - (M3 + N1);
                        decryptedChar(char_f);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_f = 32 - (M3 + N2);
                        decryptedChar(char_f);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_f = 32 - (M4 + N1);
                        decryptedChar(char_f);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_f = 32 - (M4 + N2);
                        decryptedChar(char_f);
                    }
                    break;
                case 'g':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_g = 33 - (M1 + N1);
                        decryptedChar(char_g);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_g = 33 - (M1 + N2);
                        decryptedChar(char_g);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_g = 33 - (M2 + N1);
                        decryptedChar(char_g);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_g = 33 - (M2 + N2);
                        decryptedChar(char_g);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_g = 33 - (M3 + N1);
                        decryptedChar(char_g);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_g = 33 - (M3 + N2);
                        decryptedChar(char_g);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_g = 33 - (M4 + N1);
                        decryptedChar(char_g);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_g = 33 - (M4 + N2);
                        decryptedChar(char_g);
                    }
                    break;
                case 'h':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_h = 34 - (M1 + N1);
                        decryptedChar(char_h);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_h = 34 - (M1 + N2);
                        decryptedChar(char_h);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_h = 34 - (M2 + N1);
                        decryptedChar(char_h);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_h = 34 - (M2 + N2);
                        decryptedChar(char_h);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_h = 34 - (M3 + N1);
                        decryptedChar(char_h);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_h = 34 - (M3 + N2);
                        decryptedChar(char_h);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_h = 34 - (M4 + N1);
                        decryptedChar(char_h);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_h = 34 - (M4 + N2);
                        decryptedChar(char_h);
                    }
                    break;
                case 'i':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_i = 35 - (M1 + N1);
                        decryptedChar(char_i);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_i = 35 - (M1 + N2);
                        decryptedChar(char_i);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_i = 35 - (M2 + N1);
                        decryptedChar(char_i);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_i = 35 - (M2 + N2);
                        decryptedChar(char_i);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_i = 35 - (M3 + N1);
                        decryptedChar(char_i);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_i = 35 - (M3 + N2);
                        decryptedChar(char_i);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_i = 35 - (M4 + N1);
                        decryptedChar(char_i);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_i = 35 - (M4 + N2);
                        decryptedChar(char_i);
                    }
                    break;
                case 'j':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_j = 36 - (M1 + N1);
                        decryptedChar(char_j);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_j = 36 - (M1 + N2);
                        decryptedChar(char_j);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_j = 36 - (M2 + N1);
                        decryptedChar(char_j);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_j = 36 - (M2 + N2);
                        decryptedChar(char_j);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_j = 36 - (M3 + N1);
                        decryptedChar(char_j);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_j = 36 - (M3 + N2);
                        decryptedChar(char_j);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_j = 36 - (M4 + N1);
                        decryptedChar(char_j);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_j = 36 - (M4 + N2);
                        decryptedChar(char_j);
                    }
                    break;
                case 'k':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_k = 37 - (M1 + N1);
                        decryptedChar(char_k);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_k = 37 - (M1 + N2);
                        decryptedChar(char_k);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_k = 37 - (M2 + N1);
                        decryptedChar(char_k);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_k = 37 - (M2 + N2);
                        decryptedChar(char_k);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_k = 37 - (M3 + N1);
                        decryptedChar(char_k);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_k = 37 - (M3 + N2);
                        decryptedChar(char_k);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_k = 37 - (M4 + N1);
                        decryptedChar(char_k);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_k = 37 - (M4 + N2);
                        decryptedChar(char_k);
                    }
                    break;
                case 'l':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_l = 38 - (M1 + N1);
                        decryptedChar(char_l);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_l = 38 - (M1 + N2);
                        decryptedChar(char_l);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_l = 38 - (M2 + N1);
                        decryptedChar(char_l);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_l = 38 - (M2 + N2);
                        decryptedChar(char_l);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_l = 38 - (M3 + N1);
                        decryptedChar(char_l);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_l = 38 - (M3 + N2);
                        decryptedChar(char_l);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_l = 38 - (M4 + N1);
                        decryptedChar(char_l);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_l = 38 - (M4 + N2);
                        decryptedChar(char_l);
                    }
                    break;
                case 'm':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_m = 39 - (M1 + N1);
                        decryptedChar(char_m);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_m = 39 - (M1 + N2);
                        decryptedChar(char_m);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_m = 39 - (M2 + N1);
                        decryptedChar(char_m);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_m = 39 - (M2 + N2);
                        decryptedChar(char_m);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_m = 39 - (M3 + N1);
                        decryptedChar(char_m);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_m = 39 - (M3 + N2);
                        decryptedChar(char_m);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_m = 39 - (M4 + N1);
                        decryptedChar(char_m);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_m = 39 - (M4 + N2);
                        decryptedChar(char_m);
                    }
                    break;
                case 'n':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_n = 40 - (M1 + N1);
                        decryptedChar(char_n);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_n = 40 - (M1 + N2);
                        decryptedChar(char_n);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_n = 40 - (M2 + N1);
                        decryptedChar(char_n);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_n = 40 - (M2 + N2);
                        decryptedChar(char_n);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_n = 40 - (M3 + N1);
                        decryptedChar(char_n);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_n = 40 - (M3 + N2);
                        decryptedChar(char_n);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_n = 40 - (M4 + N1);
                        decryptedChar(char_n);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_n = 40 - (M4 + N2);
                        decryptedChar(char_n);
                    }
                    break;
                case 'o':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_o = 41 - (M1 + N1);
                        decryptedChar(char_o);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_o = 41 - (M1 + N2);
                        decryptedChar(char_o);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_o = 41 - (M2 + N1);
                        decryptedChar(char_o);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_o = 41 - (M2 + N2);
                        decryptedChar(char_o);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_o = 41 - (M3 + N1);
                        decryptedChar(char_o);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_o = 41 - (M3 + N2);
                        decryptedChar(char_o);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_o = 41 - (M4 + N1);
                        decryptedChar(char_o);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_o = 41 - (M4 + N2);
                        decryptedChar(char_o);
                    }
                    break;
                case 'p':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_p = 42 - (M1 + N1);
                        decryptedChar(char_p);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_p = 42 - (M1 + N2);
                        decryptedChar(char_p);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_p = 42 - (M2 + N1);
                        decryptedChar(char_p);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_p = 42 - (M2 + N2);
                        decryptedChar(char_p);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_p = 42 - (M3 + N1);
                        decryptedChar(char_p);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_p = 42 - (M3 + N2);
                        decryptedChar(char_p);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_p = 42 - (M4 + N1);
                        decryptedChar(char_p);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_p = 42 - (M4 + N2);
                        decryptedChar(char_p);
                    }
                    break;
                case 'q':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_q = 43 - (M1 + N1);
                        decryptedChar(char_q);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_q = 43 - (M1 + N2);
                        decryptedChar(char_q);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_q = 43 - (M2 + N1);
                        decryptedChar(char_q);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_q = 43 - (M2 + N2);
                        decryptedChar(char_q);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_q = 43 - (M3 + N1);
                        decryptedChar(char_q);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_q = 43 - (M3 + N2);
                        decryptedChar(char_q);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_q = 43 - (M4 + N1);
                        decryptedChar(char_q);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_q = 43 - (M4 + N2);
                        decryptedChar(char_q);
                    }
                    break;
                case 'r':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_r = 44 - (M1 + N1);
                        decryptedChar(char_r);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_r = 44 - (M1 + N2);
                        decryptedChar(char_r);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_r = 44 - (M2 + N1);
                        decryptedChar(char_r);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_r = 44 - (M2 + N2);
                        decryptedChar(char_r);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_r = 44 - (M3 + N1);
                        decryptedChar(char_r);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_r = 44 - (M3 + N2);
                        decryptedChar(char_r);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_r = 44 - (M4 + N1);
                        decryptedChar(char_r);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_r = 44 - (M4 + N2);
                        decryptedChar(char_r);
                    }
                    break;
                case 's':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_s = 45 - (M1 + N1);
                        decryptedChar(char_s);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_s = 45 - (M1 + N2);
                        decryptedChar(char_s);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_s = 45 - (M2 + N1);
                        decryptedChar(char_s);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_s = 45 - (M2 + N2);
                        decryptedChar(char_s);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_s = 45 - (M3 + N1);
                        decryptedChar(char_s);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_s = 45 - (M3 + N2);
                        decryptedChar(char_s);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_s = 45 - (M4 + N1);
                        decryptedChar(char_s);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_s = 45 - (M4 + N2);
                        decryptedChar(char_s);
                    }
                    break;
                case 't':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_t = 46 - (M1 + N1);
                        decryptedChar(char_t);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_t = 46 - (M1 + N2);
                        decryptedChar(char_t);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_t = 46 - (M2 + N1);
                        decryptedChar(char_t);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_t = 46 - (M2 + N2);
                        decryptedChar(char_t);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_t = 46 - (M3 + N1);
                        decryptedChar(char_t);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_t = 46 - (M3 + N2);
                        decryptedChar(char_t);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_t = 46 - (M4 + N1);
                        decryptedChar(char_t);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_t = 46 - (M4 + N2);
                        decryptedChar(char_t);
                    }
                    break;
                case 'u':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_u = 47 - (M1 + N1);
                        decryptedChar(char_u);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_u = 47 - (M1 + N2);
                        decryptedChar(char_u);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_u = 47 - (M2 + N1);
                        decryptedChar(char_u);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_u = 47 - (M2 + N2);
                        decryptedChar(char_u);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_u = 47 - (M3 + N1);
                        decryptedChar(char_u);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_u = 47 - (M3 + N2);
                        decryptedChar(char_u);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_u = 47 - (M4 + N1);
                        decryptedChar(char_u);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_u = 47 - (M4 + N2);
                        decryptedChar(char_u);
                    }
                    break;
                case 'v':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_v = 48 - (M1 + N1);
                        decryptedChar(char_v);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_v = 48 - (M1 + N2);
                        decryptedChar(char_v);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_v = 48 - (M2 + N1);
                        decryptedChar(char_v);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_v = 48 - (M2 + N2);
                        decryptedChar(char_v);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_v = 48 - (M3 + N1);
                        decryptedChar(char_v);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_v = 48 - (M3 + N2);
                        decryptedChar(char_v);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_v = 48 - (M4 + N1);
                        decryptedChar(char_v);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_v = 48 - (M4 + N2);
                        decryptedChar(char_v);
                    }
                    break;
                case 'w':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_w = 49 - (M1 + N1);
                        decryptedChar(char_w);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_w = 49 - (M1 + N2);
                        decryptedChar(char_w);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_w = 49 - (M2 + N1);
                        decryptedChar(char_w);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_w = 49 - (M2 + N2);
                        decryptedChar(char_w);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_w = 49 - (M3 + N1);
                        decryptedChar(char_w);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_w = 49 - (M3 + N2);
                        decryptedChar(char_w);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_w = 49 - (M4 + N1);
                        decryptedChar(char_w);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_w = 49 - (M4 + N2);
                        decryptedChar(char_w);
                    }
                    break;
                case 'x':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_x = 50 - (M1 + N1);
                        decryptedChar(char_x);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_x = 50 - (M1 + N2);
                        decryptedChar(char_x);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_x = 50 - (M2 + N1);
                        decryptedChar(char_x);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_x = 50 - (M2 + N2);
                        decryptedChar(char_x);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_x = 50 - (M3 + N1);
                        decryptedChar(char_x);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_x = 50 - (M3 + N2);
                        decryptedChar(char_x);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_x = 50 - (M4 + N1);
                        decryptedChar(char_x);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_x = 50 - (M4 + N2);
                        decryptedChar(char_x);
                    }
                    break;
                case 'y':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_y = 51 - (M1 + N1);
                        decryptedChar(char_y);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_y = 51 - (M1 + N2);
                        decryptedChar(char_y);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_y = 51 - (M2 + N1);
                        decryptedChar(char_y);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_y = 51 - (M2 + N2);
                        decryptedChar(char_y);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_y = 51 - (M3 + N1);
                        decryptedChar(char_y);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_y = 51 - (M3 + N2);
                        decryptedChar(char_y);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_y = 51 - (M4 + N1);
                        decryptedChar(char_y);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_y = 51 - (M4 + N2);
                        decryptedChar(char_y);
                    }
                    break;
                case 'z':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_z = 52 - (M1 + N1);
                        decryptedChar(char_z);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_z = 52 - (M1 + N2);
                        decryptedChar(char_z);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_z = 52 - (M2 + N1);
                        decryptedChar(char_z);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_z = 52 - (M2 + N2);
                        decryptedChar(char_z);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_z = 52 - (M3 + N1);
                        decryptedChar(char_z);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_z = 52 - (M3 + N2);
                        decryptedChar(char_z);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_z = 52 - (M4 + N1);
                        decryptedChar(char_z);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_z = 52 - (M4 + N2);
                        decryptedChar(char_z);
                    }
                    break;
                case '0':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_0 = 53 - (M1 + N1);
                        decryptedChar(char_0);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_0 = 53 - (M1 + N2);
                        decryptedChar(char_0);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_0 = 53 - (M2 + N1);
                        decryptedChar(char_0);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_0 = 53 - (M2 + N2);
                        decryptedChar(char_0);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_0 = 53 - (M3 + N1);
                        decryptedChar(char_0);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_0 = 53 - (M3 + N2);
                        decryptedChar(char_0);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_0 = 53 - (M4 + N1);
                        decryptedChar(char_0);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_0 = 53 - (M4 + N2);
                        decryptedChar(char_0);
                    }
                    break;
                case '1':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_1 = 54 - (M1 + N1);
                        decryptedChar(char_1);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_1 = 54 - (M1 + N2);
                        decryptedChar(char_1);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_1 = 54 - (M2 + N1);
                        decryptedChar(char_1);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_1 = 54 - (M2 + N2);
                        decryptedChar(char_1);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_1 = 54 - (M3 + N1);
                        decryptedChar(char_1);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_1 = 54 - (M3 + N2);
                        decryptedChar(char_1);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_1 = 54 - (M4 + N1);
                        decryptedChar(char_1);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_1 = 54 - (M4 + N2);
                        decryptedChar(char_1);
                    }
                    break;
                case '2':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_2 = 55 - (M1 + N1);
                        decryptedChar(char_2);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_2 = 55 - (M1 + N2);
                        decryptedChar(char_2);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_2 = 55 - (M2 + N1);
                        decryptedChar(char_2);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_2 = 55 - (M2 + N2);
                        decryptedChar(char_2);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_2 = 55 - (M3 + N1);
                        decryptedChar(char_2);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_2 = 55 - (M3 + N2);
                        decryptedChar(char_2);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_2 = 55 - (M4 + N1);
                        decryptedChar(char_2);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_2 = 55 - (M4 + N2);
                        decryptedChar(char_2);
                    }
                    break;
                case '3':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_3 = 56 - (M1 + N1);
                        decryptedChar(char_3);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_3 = 56 - (M1 + N2);
                        decryptedChar(char_3);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_3 = 56 - (M2 + N1);
                        decryptedChar(char_3);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_3 = 56 - (M2 + N2);
                        decryptedChar(char_3);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_3 = 56 - (M3 + N1);
                        decryptedChar(char_3);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_3 = 56 - (M3 + N2);
                        decryptedChar(char_3);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_3 = 56 - (M4 + N1);
                        decryptedChar(char_3);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_3 = 56 - (M4 + N2);
                        decryptedChar(char_3);
                    }
                    break;
                case '4':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_4 = 57 - (M1 + N1);
                        decryptedChar(char_4);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_4 = 57 - (M1 + N2);
                        decryptedChar(char_4);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_4 = 57 - (M2 + N1);
                        decryptedChar(char_4);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_4 = 57 - (M2 + N2);
                        decryptedChar(char_4);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_4 = 57 - (M3 + N1);
                        decryptedChar(char_4);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_4 = 57 - (M3 + N2);
                        decryptedChar(char_4);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_4 = 57 - (M4 + N1);
                        decryptedChar(char_4);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_4 = 57 - (M4 + N2);
                        decryptedChar(char_4);
                    }
                    break;
                case '5':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_5 = 58 - (M1 + N1);
                        decryptedChar(char_5);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_5 = 58 - (M1 + N2);
                        decryptedChar(char_5);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_5 = 58 - (M2 + N1);
                        decryptedChar(char_5);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_5 = 58 - (M2 + N2);
                        decryptedChar(char_5);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_5 = 58 - (M3 + N1);
                        decryptedChar(char_5);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_5 = 58 - (M3 + N2);
                        decryptedChar(char_5);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_5 = 58 - (M4 + N1);
                        decryptedChar(char_5);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_5 = 58 - (M4 + N2);
                        decryptedChar(char_5);
                    }
                    break;
                case '6':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_6 = 59 - (M1 + N1);
                        decryptedChar(char_6);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_6 = 59 - (M1 + N2);
                        decryptedChar(char_6);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_6 = 59 - (M2 + N1);
                        decryptedChar(char_6);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_6 = 59 - (M2 + N2);
                        decryptedChar(char_6);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_6 = 59 - (M3 + N1);
                        decryptedChar(char_6);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_6 = 59 - (M3 + N2);
                        decryptedChar(char_6);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_6 = 59 - (M4 + N1);
                        decryptedChar(char_6);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_6 = 59 - (M4 + N2);
                        decryptedChar(char_6);
                    }
                    break;
                case '7':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_7 = 60 - (M1 + N1);
                        decryptedChar(char_7);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_7 = 60 - (M1 + N2);
                        decryptedChar(char_7);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_7 = 60 - (M2 + N1);
                        decryptedChar(char_7);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_7 = 60 - (M2 + N2);
                        decryptedChar(char_7);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_7 = 60 - (M3 + N1);
                        decryptedChar(char_7);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_7 = 60 - (M3 + N2);
                        decryptedChar(char_7);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_7 = 60 - (M4 + N1);
                        decryptedChar(char_7);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_7 = 60 - (M4 + N2);
                        decryptedChar(char_7);
                    }
                    break;
                case '8':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_8 = 61 - (M1 + N1);
                        decryptedChar(char_8);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_8 = 61 - (M1 + N2);
                        decryptedChar(char_8);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_8 = 61 - (M2 + N1);
                        decryptedChar(char_8);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_8 = 61 - (M2 + N2);
                        decryptedChar(char_8);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_8 = 61 - (M3 + N1);
                        decryptedChar(char_8);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_8 = 61 - (M3 + N2);
                        decryptedChar(char_8);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_8 = 61 - (M4 + N1);
                        decryptedChar(char_8);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_8 = 61 - (M4 + N2);
                        decryptedChar(char_8);
                    }
                    break;
                case '9':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_9 = 62 - (M1 + N1);
                        decryptedChar(char_9);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_9 = 62 - (M1 + N2);
                        decryptedChar(char_9);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_9 = 62 - (M2 + N1);
                        decryptedChar(char_9);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_9 = 62 - (M2 + N2);
                        decryptedChar(char_9);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_9 = 62 - (M3 + N1);
                        decryptedChar(char_9);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_9 = 62 - (M3 + N2);
                        decryptedChar(char_9);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_9 = 62 - (M4 + N1);
                        decryptedChar(char_9);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_9 = 62 - (M4 + N2);
                        decryptedChar(char_9);
                    }
                    break;
                case ',':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_comma = 63 - (M1 + N1);
                        decryptedChar(char_comma);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_comma = 63 - (M1 + N2);
                        decryptedChar(char_comma);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_comma = 63 - (M2 + N1);
                        decryptedChar(char_comma);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_comma = 63 - (M2 + N2);
                        decryptedChar(char_comma);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_comma = 63 - (M3 + N1);
                        decryptedChar(char_comma);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_comma = 63 - (M3 + N2);
                        decryptedChar(char_comma);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_comma = 63 - (M4 + N1);
                        decryptedChar(char_comma);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_comma = 63 - (M4 + N2);
                        decryptedChar(char_comma);
                    }
                    break;
                case '.':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_fullstop = 64 - (M1 + N1);
                        decryptedChar(char_fullstop);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_fullstop = 64 - (M1 + N2);
                        decryptedChar(char_fullstop);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_fullstop = 64 - (M2 + N1);
                        decryptedChar(char_fullstop);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_fullstop = 64 - (M2 + N2);
                        decryptedChar(char_fullstop);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_fullstop = 64 - (M3 + N1);
                        decryptedChar(char_fullstop);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_fullstop = 64 - (M3 + N2);
                        decryptedChar(char_fullstop);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_fullstop = 64 - (M4 + N1);
                        decryptedChar(char_fullstop);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_fullstop = 64 - (M4 + N2);
                        decryptedChar(char_fullstop);
                    }
                    break;
                case '?':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_qmark = 65 - (M1 + N1);
                        decryptedChar(char_qmark);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_qmark = 65 - (M1 + N2);
                        decryptedChar(char_qmark);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_qmark = 65 - (M2 + N1);
                        decryptedChar(char_qmark);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_qmark = 65 - (M2 + N2);
                        decryptedChar(char_qmark);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_qmark = 65 - (M3 + N1);
                        decryptedChar(char_qmark);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_qmark = 65 - (M3 + N2);
                        decryptedChar(char_qmark);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_qmark = 65 - (M4 + N1);
                        decryptedChar(char_qmark);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_qmark = 65 - (M4 + N2);
                        decryptedChar(char_qmark);
                    }
                    break;
                case '!':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_exmark = 66 - (M1 + N1);
                        decryptedChar(char_exmark);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_exmark = 66 - (M1 + N2);
                        decryptedChar(char_exmark);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_exmark = 66 - (M2 + N1);
                        decryptedChar(char_exmark);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_exmark = 66 - (M2 + N2);
                        decryptedChar(char_exmark);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_exmark = 66 - (M3 + N1);
                        decryptedChar(char_exmark);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_exmark = 66 - (M3 + N2);
                        decryptedChar(char_exmark);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_exmark = 66 - (M4 + N1);
                        decryptedChar(char_exmark);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_exmark = 66 - (M4 + N2);
                        decryptedChar(char_exmark);
                    }
                    break;
                case ';':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_semicolon = 67 - (M1 + N1);
                        decryptedChar(char_semicolon);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_semicolon = 67 - (M1 + N2);
                        decryptedChar(char_semicolon);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_semicolon = 67 - (M2 + N1);
                        decryptedChar(char_semicolon);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_semicolon = 67 - (M2 + N2);
                        decryptedChar(char_semicolon);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_semicolon = 67 - (M3 + N1);
                        decryptedChar(char_semicolon);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_semicolon = 67 - (M3 + N2);
                        decryptedChar(char_semicolon);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_semicolon = 67 - (M4 + N1);
                        decryptedChar(char_semicolon);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_semicolon = 67 - (M4 + N2);
                        decryptedChar(char_semicolon);
                    }
                    break;
                case ':':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_colon = 68 - (M1 + N1);
                        decryptedChar(char_colon);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_colon = 68 - (M1 + N2);
                        decryptedChar(char_colon);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_colon = 68 - (M2 + N1);
                        decryptedChar(char_colon);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_colon = 68 - (M2 + N2);
                        decryptedChar(char_colon);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_colon = 68 - (M3 + N1);
                        decryptedChar(char_colon);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_colon = 68 - (M3 + N2);
                        decryptedChar(char_colon);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_colon = 68 - (M4 + N1);
                        decryptedChar(char_colon);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_colon = 68 - (M4 + N2);
                        decryptedChar(char_colon);
                    }
                    break;
                case '/':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_frwdslash = 69 - (M1 + N1);
                        decryptedChar(char_frwdslash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_frwdslash = 69 - (M1 + N2);
                        decryptedChar(char_frwdslash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_frwdslash = 69 - (M2 + N1);
                        decryptedChar(char_frwdslash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_frwdslash = 69 - (M2 + N2);
                        decryptedChar(char_frwdslash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_frwdslash = 69 - (M3 + N1);
                        decryptedChar(char_frwdslash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_frwdslash = 69 - (M3 + N2);
                        decryptedChar(char_frwdslash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_frwdslash = 69 - (M4 + N1);
                        decryptedChar(char_frwdslash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_frwdslash = 69 - (M4 + N2);
                        decryptedChar(char_frwdslash);
                    }
                    break;
                case '\\':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_backslash = 70 - (M1 + N1);
                        decryptedChar(char_backslash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_backslash = 70 - (M1 + N2);
                        decryptedChar(char_backslash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_backslash = 70 - (M2 + N1);
                        decryptedChar(char_backslash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_backslash = 70 - (M2 + N2);
                        decryptedChar(char_backslash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_backslash = 70 - (M3 + N1);
                        decryptedChar(char_backslash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_backslash = 70 - (M3 + N2);
                        decryptedChar(char_backslash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_backslash = 70 - (M4 + N1);
                        decryptedChar(char_backslash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_backslash = 70 - (M4 + N2);
                        decryptedChar(char_backslash);
                    }
                    break;
                case '@':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_at = 71 - (M1 + N1);
                        decryptedChar(char_at);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_at = 71 - (M1 + N2);
                        decryptedChar(char_at);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_at = 71 - (M2 + N1);
                        decryptedChar(char_at);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_at = 71 - (M2 + N2);
                        decryptedChar(char_at);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_at = 71 - (M3 + N1);
                        decryptedChar(char_at);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_at = 71 - (M3 + N2);
                        decryptedChar(char_at);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_at = 71 - (M4 + N1);
                        decryptedChar(char_at);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_at = 71 - (M4 + N2);
                        decryptedChar(char_at);
                    }
                    break;
                case '#':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_hash = 72 - (M1 + N1);
                        decryptedChar(char_hash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_hash = 72 - (M1 + N2);
                        decryptedChar(char_hash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_hash = 72 - (M2 + N1);
                        decryptedChar(char_hash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_hash = 72 - (M2 + N2);
                        decryptedChar(char_hash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_hash = 72 - (M3 + N1);
                        decryptedChar(char_hash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_hash = 72 - (M3 + N2);
                        decryptedChar(char_hash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_hash = 72 - (M4 + N1);
                        decryptedChar(char_hash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_hash = 72 - (M4 + N2);
                        decryptedChar(char_hash);
                    }
                    break;
                case '%':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_percent = 73 - (M1 + N1);
                        decryptedChar(char_percent);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_percent = 73 - (M1 + N2);
                        decryptedChar(char_percent);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_percent = 73 - (M2 + N1);
                        decryptedChar(char_percent);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_percent = 73 - (M2 + N2);
                        decryptedChar(char_percent);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_percent = 73 - (M3 + N1);
                        decryptedChar(char_percent);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_percent = 73 - (M3 + N2);
                        decryptedChar(char_percent);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_percent = 73 - (M4 + N1);
                        decryptedChar(char_percent);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_percent = 73 - (M4 + N2);
                        decryptedChar(char_percent);
                    }
                    break;
                case '&':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_ampersand = 74 - (M1 + N1);
                        decryptedChar(char_ampersand);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_ampersand = 74 - (M1 + N2);
                        decryptedChar(char_ampersand);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_ampersand = 74 - (M2 + N1);
                        decryptedChar(char_ampersand);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_ampersand = 74 - (M2 + N2);
                        decryptedChar(char_ampersand);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_ampersand = 74 - (M3 + N1);
                        decryptedChar(char_ampersand);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_ampersand = 74 - (M3 + N2);
                        decryptedChar(char_ampersand);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_ampersand = 74 - (M4 + N1);
                        decryptedChar(char_ampersand);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_ampersand = 74 - (M4 + N2);
                        decryptedChar(char_ampersand);
                    }
                    break;
                case '*':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_asterisk = 75 - (M1 + N1);
                        decryptedChar(char_asterisk);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_asterisk = 75 - (M1 + N2);
                        decryptedChar(char_asterisk);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_asterisk = 75 - (M2 + N1);
                        decryptedChar(char_asterisk);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_asterisk = 75 - (M2 + N2);
                        decryptedChar(char_asterisk);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_asterisk = 75 - (M3 + N1);
                        decryptedChar(char_asterisk);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_asterisk = 75 - (M3 + N2);
                        decryptedChar(char_asterisk);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_asterisk = 75 - (M4 + N1);
                        decryptedChar(char_asterisk);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_asterisk = 75 - (M4 + N2);
                        decryptedChar(char_asterisk);
                    }
                    break;
                case '(':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_openbracket = 76 - (M1 + N1);
                        decryptedChar(char_openbracket);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_openbracket = 76 - (M1 + N2);
                        decryptedChar(char_openbracket);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_openbracket = 76 - (M2 + N1);
                        decryptedChar(char_openbracket);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_openbracket = 76 - (M2 + N2);
                        decryptedChar(char_openbracket);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_openbracket = 76 - (M3 + N1);
                        decryptedChar(char_openbracket);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_openbracket = 76 - (M3 + N2);
                        decryptedChar(char_openbracket);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_openbracket = 76 - (M4 + N1);
                        decryptedChar(char_openbracket);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_openbracket = 76 - (M4 + N2);
                        decryptedChar(char_openbracket);
                    }
                    break;
                case ')':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_closebracket = 77 - (M1 + N1);
                        decryptedChar(char_closebracket);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_closebracket = 77 - (M1 + N2);
                        decryptedChar(char_closebracket);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_closebracket = 77 - (M2 + N1);
                        decryptedChar(char_closebracket);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_closebracket = 77 - (M2 + N2);
                        decryptedChar(char_closebracket);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_closebracket = 77 - (M3 + N1);
                        decryptedChar(char_closebracket);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_closebracket = 77 - (M3 + N2);
                        decryptedChar(char_closebracket);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_closebracket = 77 - (M4 + N1);
                        decryptedChar(char_closebracket);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_closebracket = 77 - (M4 + N2);
                        decryptedChar(char_closebracket);
                    }
                    break;
                case '-':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_hyphen = 78 - (M1 + N1);
                        decryptedChar(char_hyphen);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_hyphen = 78 - (M1 + N2);
                        decryptedChar(char_hyphen);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_hyphen = 78 - (M2 + N1);
                        decryptedChar(char_hyphen);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_hyphen = 78 - (M2 + N2);
                        decryptedChar(char_hyphen);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_hyphen = 78 - (M3 + N1);
                        decryptedChar(char_hyphen);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_hyphen = 78 - (M3 + N2);
                        decryptedChar(char_hyphen);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_hyphen = 78 - (M4 + N1);
                        decryptedChar(char_hyphen);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_hyphen = 78 - (M4 + N2);
                        decryptedChar(char_hyphen);
                    }
                    break;
                case '+':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_plus = 79 - (M1 + N1);
                        decryptedChar(char_plus);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_plus = 79 - (M1 + N2);
                        decryptedChar(char_plus);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_plus = 79 - (M2 + N1);
                        decryptedChar(char_plus);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_plus = 79 - (M2 + N2);
                        decryptedChar(char_plus);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_plus = 79 - (M3 + N1);
                        decryptedChar(char_plus);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_plus = 79 - (M3 + N2);
                        decryptedChar(char_plus);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_plus = 79 - (M4 + N1);
                        decryptedChar(char_plus);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_plus = 79 - (M4 + N2);
                        decryptedChar(char_plus);
                    }
                    break;
                case '=':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_equals = 80 - (M1 + N1);
                        decryptedChar(char_equals);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_equals = 80 - (M1 + N2);
                        decryptedChar(char_equals);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_equals = 80 - (M2 + N1);
                        decryptedChar(char_equals);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_equals = 80 - (M2 + N2);
                        decryptedChar(char_equals);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_equals = 80 - (M3 + N1);
                        decryptedChar(char_equals);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_equals = 80 - (M3 + N2);
                        decryptedChar(char_equals);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_equals = 80 - (M4 + N1);
                        decryptedChar(char_equals);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_equals = 80 - (M4 + N2);
                        decryptedChar(char_equals);
                    }
                    break;
                case '<':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_lessthan = 81 - (M1 + N1);
                        decryptedChar(char_lessthan);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_lessthan = 81 - (M1 + N2);
                        decryptedChar(char_lessthan);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_lessthan = 81 - (M2 + N1);
                        decryptedChar(char_lessthan);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_lessthan = 81 - (M2 + N2);
                        decryptedChar(char_lessthan);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_lessthan = 81 - (M3 + N1);
                        decryptedChar(char_lessthan);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_lessthan = 81 - (M3 + N2);
                        decryptedChar(char_lessthan);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_lessthan = 81 - (M4 + N1);
                        decryptedChar(char_lessthan);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_lessthan = 81 - (M4 + N2);
                        decryptedChar(char_lessthan);
                    }
                    break;
                case '>':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_greaterthan = 82 - (M1 + N1);
                        decryptedChar(char_greaterthan);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_greaterthan = 82 - (M1 + N2);
                        decryptedChar(char_greaterthan);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_greaterthan = 82 - (M2 + N1);
                        decryptedChar(char_greaterthan);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_greaterthan = 82 - (M2 + N2);
                        decryptedChar(char_greaterthan);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_greaterthan = 82 - (M3 + N1);
                        decryptedChar(char_greaterthan);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_greaterthan = 82 - (M3 + N2);
                        decryptedChar(char_greaterthan);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_greaterthan = 82 - (M4 + N1);
                        decryptedChar(char_greaterthan);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_greaterthan = 82 - (M4 + N2);
                        decryptedChar(char_greaterthan);
                    }
                    break;
                case '"':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_doublequote = 83 - (M1 + N1);
                        decryptedChar(char_doublequote);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_doublequote = 83 - (M1 + N2);
                        decryptedChar(char_doublequote);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_doublequote = 83 - (M2 + N1);
                        decryptedChar(char_doublequote);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_doublequote = 83 - (M2 + N2);
                        decryptedChar(char_doublequote);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_doublequote = 83 - (M3 + N1);
                        decryptedChar(char_doublequote);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_doublequote = 83 - (M3 + N2);
                        decryptedChar(char_doublequote);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_doublequote = 83 - (M4 + N1);
                        decryptedChar(char_doublequote);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_doublequote = 83 - (M4 + N2);
                        decryptedChar(char_doublequote);
                    }
                    break;
                case '\'':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_singlequote = 84 - (M1 + N1);
                        decryptedChar(char_singlequote);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_singlequote = 84 - (M1 + N2);
                        decryptedChar(char_singlequote);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_singlequote = 84 - (M2 + N1);
                        decryptedChar(char_singlequote);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_singlequote = 84 - (M2 + N2);
                        decryptedChar(char_singlequote);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_singlequote = 84 - (M3 + N1);
                        decryptedChar(char_singlequote);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_singlequote = 84 - (M3 + N2);
                        decryptedChar(char_singlequote);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_singlequote = 84 - (M4 + N1);
                        decryptedChar(char_singlequote);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_singlequote = 84 - (M4 + N2);
                        decryptedChar(char_singlequote);
                    }
                    break;
                case ' ':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_wspace = 85 - (M1 + N1);
                        decryptedChar(char_wspace);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_wspace = 85 - (M1 + N2);
                        decryptedChar(char_wspace);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_wspace = 85 - (M2 + N1);
                        decryptedChar(char_wspace);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_wspace = 85 - (M2 + N2);
                        decryptedChar(char_wspace);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_wspace = 85 - (M3 + N1);
                        decryptedChar(char_wspace);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_wspace = 85 - (M3 + N2);
                        decryptedChar(char_wspace);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_wspace = 85 - (M4 + N1);
                        decryptedChar(char_wspace);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_wspace = 85 - (M4 + N2);
                        decryptedChar(char_wspace);
                    }
                    break;
                case '[':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_squareopenbrac = 86 - (M1 + N1);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_squareopenbrac = 86 - (M1 + N2);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_squareopenbrac = 86 - (M2 + N1);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_squareopenbrac = 86 - (M2 + N2);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_squareopenbrac = 86 - (M3 + N1);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_squareopenbrac = 86 - (M3 + N2);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_squareopenbrac = 86 - (M4 + N1);
                        decryptedChar(char_squareopenbrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_squareopenbrac = 86 - (M4 + N2);
                        decryptedChar(char_squareopenbrac);
                    }
                    break;
                case ']':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_squareclosebrac = 87 - (M1 + N1);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_squareclosebrac = 87 - (M1 + N2);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_squareclosebrac = 87 - (M2 + N1);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_squareclosebrac = 87 - (M2 + N2);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_squareclosebrac = 87 - (M3 + N1);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_squareclosebrac = 87 - (M3 + N2);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_squareclosebrac = 87 - (M4 + N1);
                        decryptedChar(char_squareclosebrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_squareclosebrac = 87 - (M4 + N2);
                        decryptedChar(char_squareclosebrac);
                    }
                    break;
                case '{':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_opencurlybrac = 88 - (M1 + N1);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_opencurlybrac = 88 - (M1 + N2);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_opencurlybrac = 88 - (M2 + N1);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_opencurlybrac = 88 - (M2 + N2);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_opencurlybrac = 88 - (M3 + N1);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_opencurlybrac = 88 - (M3 + N2);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_opencurlybrac = 88 - (M4 + N1);
                        decryptedChar(char_opencurlybrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_opencurlybrac = 88 - (M4 + N2);
                        decryptedChar(char_opencurlybrac);
                    }
                    break;
                case '}':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_closecurlybrac = 89 - (M1 + N1);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_closecurlybrac = 89 - (M1 + N2);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_closecurlybrac = 89 - (M2 + N1);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_closecurlybrac = 89 - (M2 + N2);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_closecurlybrac = 89 - (M3 + N1);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_closecurlybrac = 89 - (M3 + N2);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_closecurlybrac = 89 - (M4 + N1);
                        decryptedChar(char_closecurlybrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_closecurlybrac = 89 - (M4 + N2);
                        decryptedChar(char_closecurlybrac);
                    }
                    break;
                case '_':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_underscore = 90 - (M1 + N1);
                        decryptedChar(char_underscore);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_underscore = 90 - (M1 + N2);
                        decryptedChar(char_underscore);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_underscore = 90 - (M2 + N1);
                        decryptedChar(char_underscore);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_underscore = 90 - (M2 + N2);
                        decryptedChar(char_underscore);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_underscore = 90 - (M3 + N1);
                        decryptedChar(char_underscore);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_underscore = 90 - (M3 + N2);
                        decryptedChar(char_underscore);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_underscore = 90 - (M4 + N1);
                        decryptedChar(char_underscore);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_underscore = 90 - (M4 + N2);
                        decryptedChar(char_underscore);
                    }
                    break;
                case '~':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_tilde = 91 - (M1 + N1);
                        decryptedChar(char_tilde);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_tilde = 91 - (M1 + N2);
                        decryptedChar(char_tilde);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_tilde = 91 - (M2 + N1);
                        decryptedChar(char_tilde);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_tilde = 91 - (M2 + N2);
                        decryptedChar(char_tilde);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_tilde = 91 - (M3 + N1);
                        decryptedChar(char_tilde);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_tilde = 91 - (M3 + N2);
                        decryptedChar(char_tilde);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_tilde = 91 - (M4 + N1);
                        decryptedChar(char_tilde);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_tilde = 91 - (M4 + N2);
                        decryptedChar(char_tilde);
                    }
                    break;
                case '|':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_verticalbar = 92 - (M1 + N1);
                        decryptedChar(char_verticalbar);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_verticalbar = 92 - (M1 + N2);
                        decryptedChar(char_verticalbar);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_verticalbar = 92 - (M2 + N1);
                        decryptedChar(char_verticalbar);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_verticalbar = 92 - (M2 + N2);
                        decryptedChar(char_verticalbar);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_verticalbar = 92 - (M3 + N1);
                        decryptedChar(char_verticalbar);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_verticalbar = 92 - (M3 + N2);
                        decryptedChar(char_verticalbar);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_verticalbar = 92 - (M4 + N1);
                        decryptedChar(char_verticalbar);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_verticalbar = 92 - (M4 + N2);
                        decryptedChar(char_verticalbar);
                    }
                    break;
                case '$':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_dollar = 93 - (M1 + N1);
                        decryptedChar(char_dollar);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_dollar = 93 - (M1 + N2);
                        decryptedChar(char_dollar);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_dollar = 93 - (M2 + N1);
                        decryptedChar(char_dollar);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_dollar = 93 - (M2 + N2);
                        decryptedChar(char_dollar);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_dollar = 93 - (M3 + N1);
                        decryptedChar(char_dollar);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_dollar = 93 - (M3 + N2);
                        decryptedChar(char_dollar);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_dollar = 93 - (M4 + N1);
                        decryptedChar(char_dollar);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_dollar = 93 - (M4 + N2);
                        decryptedChar(char_dollar);
                    }
                    break;
                case '`':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_backtick = 94 - (M1 + N1);
                        decryptedChar(char_backtick);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_backtick = 94 - (M1 + N2);
                        decryptedChar(char_backtick);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_backtick = 94 - (M2 + N1);
                        decryptedChar(char_backtick);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_backtick = 94 - (M2 + N2);
                        decryptedChar(char_backtick);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_backtick = 94 - (M3 + N1);
                        decryptedChar(char_backtick);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_backtick = 94 - (M3 + N2);
                        decryptedChar(char_backtick);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_backtick = 94 - (M4 + N1);
                        decryptedChar(char_backtick);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_backtick = 94 - (M4 + N2);
                        decryptedChar(char_backtick);
                    }
                    break;
                case '^':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_empty = 95 - (M1 + N1);
                        decryptedChar(char_empty);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_empty = 95 - (M1 + N2);
                        decryptedChar(char_empty);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_empty = 95 - (M2 + N1);
                        decryptedChar(char_empty);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_empty = 95 - (M2 + N2);
                        decryptedChar(char_empty);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_empty = 95 - (M3 + N1);
                        decryptedChar(char_empty);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_empty = 95 - (M3 + N2);
                        decryptedChar(char_empty);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_empty = 95 - (M4 + N1);
                        decryptedChar(char_empty);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_empty = 95 - (M4 + N2);
                        decryptedChar(char_empty);
                    }
                    break;
                default:
                    decryptedMessage += String.valueOf(d);
                    break;
            }
        }

        // Notify(messageafterdecryption(decryptedMessage));
        String complete_decryptedMessage = messageafterdecryption(decryptedMessage);
        Log.d(TAG, "Service decrypted message: " + messageafterdecryption(decryptedMessage));


//        String encrptedtext = "";
//
////            String str_1 = str.substring(0, 15);
////            String str_2 = str.substring(15, 30);
//        encrptedtext = str + "             x";
        //  String complete_decryptedmessage = "";
        //  tv3.setText(messageafterdecryption(decryptedMessage));
        //   Toast.makeText(getApplicationContext(), messageafterdecryption(decryptedMessage), Toast.LENGTH_SHORT).show();
        //   }
//        else{
//
//        }
        return complete_decryptedMessage;
    }

    void decryptedChar(int n) {
        //  Toast.makeText(getApplicationContext(), String.valueOf(n), Toast.LENGTH_SHORT).show();
        switch (n) {
            case 1:
                decryptedMessage += "A";
                break;
            case 2:
                decryptedMessage += "B";
                break;
            case 3:
                decryptedMessage += "C";
                break;
            case 4:
                decryptedMessage += "D";
                break;
            case 5:
                decryptedMessage += "E";
                break;
            case 6:
                decryptedMessage += "F";
                break;
            case 7:
                decryptedMessage += "G";
                break;
            case 8:
                decryptedMessage += "H";
                break;
            case 9:
                decryptedMessage += "I";
                break;
            case 10:
                decryptedMessage += "J";
                break;
            case 11:
                decryptedMessage += "K";
                break;
            case 12:
                decryptedMessage += "L";
                break;
            case 13:
                decryptedMessage += "M";
                break;
            case 14:
                decryptedMessage += "N";
                break;
            case 15:
                decryptedMessage += "O";
                break;
            case 16:
                decryptedMessage += "P";
                break;
            case 17:
                decryptedMessage += "Q";
                break;
            case 18:
                decryptedMessage += "R";
                break;
            case 19:
                decryptedMessage += "S";
                break;
            case 20:
                decryptedMessage += "T";
                break;
            case 21:
                decryptedMessage += "U";
                break;
            case 22:
                decryptedMessage += "V";
                break;
            case 23:
                decryptedMessage += "W";
                break;
            case 24:
                decryptedMessage += "X";
                break;
            case 25:
                decryptedMessage += "Y";
                break;
            case 26:
                decryptedMessage += "Z";
                break;
            case 27:
                decryptedMessage += "a";
                break;
            case 28:
                decryptedMessage += "b";
                break;
            case 29:
                decryptedMessage += "c";
                break;
            case 30:
                decryptedMessage += "d";
                break;
            case 31:
                decryptedMessage += "e";
                break;
            case 32:
                decryptedMessage += "f";
                break;
            case 33:
                decryptedMessage += "g";
                break;
            case 34:
                decryptedMessage += "h";
                break;
            case 35:
                decryptedMessage += "i";
                break;
            case 36:
                decryptedMessage += "j";
                break;
            case 37:
                decryptedMessage += "k";
                break;
            case 38:
                decryptedMessage += "l";
                break;
            case 39:
                decryptedMessage += "m";
                break;
            case 40:
                decryptedMessage += "n";
                break;
            case 41:
                decryptedMessage += "o";
                break;
            case 42:
                decryptedMessage += "p";
                break;
            case 43:
                decryptedMessage += "q";
                break;
            case 44:
                decryptedMessage += "r";
                break;
            case 45:
                decryptedMessage += "s";
                break;
            case 46:
                decryptedMessage += "t";
                break;
            case 47:
                decryptedMessage += "u";
                break;
            case 48:
                decryptedMessage += "v";
                break;
            case 49:
                decryptedMessage += "w";
                break;
            case 50:
                decryptedMessage += "x";
                break;
            case 51:
                decryptedMessage += "y";
                break;
            case 52:
                decryptedMessage += "z";
                break;
            case 53:
                decryptedMessage += "0";
                break;
            case 54:
                decryptedMessage += "1";
                break;
            case 55:
                decryptedMessage += "2";
                break;
            case 56:
                decryptedMessage += "3";
                break;
            case 57:
                decryptedMessage += "4";
                break;
            case 58:
                decryptedMessage += "5";
                break;
            case 59:
                decryptedMessage += "6";
                break;
            case 60:
                decryptedMessage += "7";
                break;
            case 61:
                decryptedMessage += "8";
                break;
            case 62:
                decryptedMessage += "9";
                break;
            case 63:
                decryptedMessage += ",";
                break;
            case 64:
                decryptedMessage += ".";
                break;
            case 65:
                decryptedMessage += "?";
                break;
            case 66:
                decryptedMessage += "!";
                break;
            case 67:
                decryptedMessage += ";";
                break;
            case 68:
                decryptedMessage += ":";
                break;
            case 69:
                decryptedMessage += "/";
                break;
            case 70:
                decryptedMessage += "\\";
                break;
            case 71:
                decryptedMessage += "@";
                break;
            case 72:
                decryptedMessage += "#";
                break;
            case 73:
                decryptedMessage += "%";
                break;
            case 74:
                decryptedMessage += "&";
                break;
            case 75:
                decryptedMessage += "*";
                break;
            case 76:
                decryptedMessage += "(";
                break;
            case 77:
                decryptedMessage += ")";
                break;
            case 78:
                decryptedMessage += "-";
                break;
            case 79:
                decryptedMessage += "+";
                break;
            case 80:
                decryptedMessage += "=";
                break;
            case 81:
                decryptedMessage += "<";
                break;
            case 82:
                decryptedMessage += ">";
                break;
            case 83:
                decryptedMessage += "\"";
                break;
            case 84:
                decryptedMessage += "'";
                break;
            case 85:
                decryptedMessage += " ";
                break;
            case 86:
                decryptedMessage += "[";
                break;
            case 87:
                decryptedMessage += "]";
                break;
            case 88:
                decryptedMessage += "{";
                break;
            case 89:
                decryptedMessage += "}";
                break;
            case 90:
                decryptedMessage += "_";
                break;
            case 91:
                decryptedMessage += "~";
                break;
            case 92:
                decryptedMessage += "|";
                break;
            case 93:
                decryptedMessage += "$";
                break;
            case 94:
                decryptedMessage += "`";
                break;
            case 95:
                decryptedMessage += "^";
                break;
            default:
                int x = 95 - Math.abs(n);
                decryptedChar(x);
                break;
        }


    }

    String messageafterdecryption(String msg) {
        String message = "";
        //   for (int i = 0; i < msg.length(); i++) {
        String msg_1 = msg.substring(0, 14);
        String msg_2 = msg.substring(15);
        message = msg_1 + msg_2;
    //    message = msg_text.substring(0, msg_text.indexOf(' '));

        //   }
//        Toast.makeText(getApplicationContext(), "Display message =  " + message, Toast.LENGTH_SHORT).show();
        return message;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;

        Log.i(TAG, "Service onDestroy");
    }

    class WifiReceiver extends BroadcastReceiver {

        private List<ScanResult> wifiList;

        public void onReceive(Context c, Intent intent) {

            wifiList = mainWifi.getScanResults();

            for (int i = 0; i < wifiList.size(); i++) {
                Log.d(TAG, "Scanned wifi list is = " + wifiList.get(i).SSID);
                wifiname = wifiList.get(i).SSID;
                Log.i("wificheckthread", "Service running = " + wifiname);
                if (wifiname.charAt(0) == '0' || wifiname.charAt(0) == '1' || wifiname.charAt(0) == '2' || wifiname.charAt(0) == '3' || wifiname.charAt(0) == '4' || wifiname.charAt(0) == '5'
                        || wifiname.charAt(0) == '6' || wifiname.charAt(0) == '7' || wifiname.charAt(0) == '8' || wifiname.charAt(0) == '9') {
                    if (wifiname.charAt(1) == '0' || wifiname.charAt(1) == '1' || wifiname.charAt(1) == '2' || wifiname.charAt(1) == '3' || wifiname.charAt(1) == '4' || wifiname.charAt(1) == '5'
                            || wifiname.charAt(1) == '6' || wifiname.charAt(1) == '7' || wifiname.charAt(1) == '8' || wifiname.charAt(1) == '9') {
                        if (wifiname.charAt(16) == '6') {
                            String encrypt_msg = "";
                            encrypt_msg = wifiname.substring(2);
                            Log.d(TAG, "Service encrypted message: " + encrypt_msg);
                            //   mainactivity.decryptMessage(encrypt_msg);
                            String notify_message = "";
                            notify_message = decryptMessage(encrypt_msg);
                            Notify(notify_message, 0);

                        }
                    }
                } else {
                    // do nothing
                }

            }

        }

    }
}