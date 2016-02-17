package com.euroitlabs.broadcastapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Broadcastapp";

    private static final int START_STICKY = 0;
    EditText et;
    Button bt;
    String encryptedMessage = "";
    String complete_encryptedMessage = "";
    String decryptedMessage = "";
    int M1 = 2, M2 = 4, M3 = 9, M4 = 5;
    int N1 = 4, N2 = 6;
    static WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText);
        bt = (Button) findViewById(R.id.button);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (source.charAt(i) < 32 || source.charAt(i) > 126) {
                        Toast.makeText(getApplicationContext(), "Character " + source.charAt(start) + " is not allowed in the message.", Toast.LENGTH_SHORT).show();
                        return "";
                    }
                }


                return null;
            }
        };

        et.setFilters(new InputFilter[]{filter});

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = "";
                String message = "";

                if (wifi.isWifiEnabled()) {
                    Log.i("wificheckthread", "Mainactivity inside wifi enabled message");
                    st = et.getText().toString();
                    message = messageToBeEncrypted(st);
                    Log.i("wificheckthread", "Mainactivity wifi enabled message = " + message);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    if (setHotspotName(encryptMessage(message), getApplicationContext()) == true) {
                        turnOnOffHotspot(getApplicationContext(), true);
                        et.setText("");
                    }
                } else {
                    Log.i("wificheckthread", "Mainactivity inside wifi disabled message");
//                    turnOnOffHotspot(getApplicationContext(), false);
//                    turnOnOffWifi(getApplicationContext(), true);
                    if (turnOnOffHotspot(getApplicationContext(), false) == false && turnOnOffWifi(getApplicationContext(), true) == true) {
                        //  if(wifi.isWifiEnabled()){
                        st = et.getText().toString();
                        message = messageToBeEncrypted(st);
                        Log.i("wificheckthread", "Mainactivity wifi disabled message = " + message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (setHotspotName(encryptMessage(message), getApplicationContext()) == true) {
                            turnOnOffHotspot(getApplicationContext(), true);
                            et.setText("");
                        }
                    }
                }


            }
        });


    }


    /**
     * Turn on or off Hotspot.
     *
     * @param context
     * @param isTurnToOn
     */
    public static boolean turnOnOffHotspot(Context context, boolean isTurnToOn) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiApControl apControl = WifiApControl.getApControl(wifiManager);
        if (apControl != null) {

            // TURN OFF YOUR WIFI BEFORE ENABLE HOTSPOT
            if (wifiManager.isWifiEnabled() && isTurnToOn)
                turnOnOffWifi(context, false);

            apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),
                    isTurnToOn);
        }
        return isTurnToOn;
    }

    /**
     * Turn On or Off wifi
     *
     * @param context
     * @param isTurnToOn
     */
    public static boolean turnOnOffWifi(Context context, boolean isTurnToOn) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        wifiManager.setWifiEnabled(isTurnToOn);
        return isTurnToOn;
    }

    public boolean setHotspotName(String newName, Context context) {


        try {
            //  newName = "";
            Log.i("wificheckthread", "Mainactivity newName = " + newName);
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

            wifiConfig.SSID = newName;

            Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    String messageToBeEncrypted(String msg) {
        if (msg.length() < 30) {
            if (msg.length() < 14) {
                for (int j = msg.length() + 1; j < 15; j++) {
                    msg += " ";
                }
                msg += "x";
                for (int k = 1; k <= 15; k++) {
                    msg += " ";
                }
            } else if (msg.length() > 14) {
                String msg_1 = msg.substring(0, 14);
                String msg_2 = msg.substring(14);
                msg = msg_1 + "x" + msg_2;
                for (int m = msg.length(); m < 30; m++) {
                    msg += " ";
                }
            } else if (msg.length() == 14) {
                msg += "x";
                for (int m = msg.length(); m < 30; m++) {
                    msg += " ";
                }
            }
        }
        return msg;
    }


    String encryptMessage(String hotspotName) {
        int index;
        encryptedMessage = "";
        //   char c ;
        for (index = 0; index < hotspotName.length(); index++) {
            //  Toast.makeText(getApplicationContext(), String.valueOf(hotspotName.length()), Toast.LENGTH_SHORT).show();
            char c = hotspotName.charAt(index);
            //  Toast.makeText(getApplicationContext(), String.valueOf(c), Toast.LENGTH_SHORT).show();
            switch (c) {
                case 'A':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_A = M1 + N1 + 1;
                        encryptedChar(char_A);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_A = M1 + N2 + 1;
                        encryptedChar(char_A);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_A = M2 + N1 + 1;
                        encryptedChar(char_A);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_A = M2 + N2 + 1;
                        encryptedChar(char_A);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_A = M3 + N1 + 1;
                        encryptedChar(char_A);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_A = M3 + N2 + 1;
                        encryptedChar(char_A);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_A = M4 + N1 + 1;
                        encryptedChar(char_A);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_A = M4 + N2 + 1;
                        encryptedChar(char_A);
                    }
                    break;
                case 'B':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_B = M1 + N1 + 2;
                        encryptedChar(char_B);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_B = M1 + N2 + 2;
                        encryptedChar(char_B);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_B = M2 + N1 + 2;
                        encryptedChar(char_B);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_B = M2 + N2 + 2;
                        encryptedChar(char_B);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_B = M3 + N1 + 2;
                        encryptedChar(char_B);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_B = M3 + N2 + 2;
                        encryptedChar(char_B);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_B = M4 + N1 + 2;
                        encryptedChar(char_B);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_B = M4 + N2 + 2;
                        encryptedChar(char_B);
                    }
                    break;
                case 'C':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_C = M1 + N1 + 3;
                        encryptedChar(char_C);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_C = M1 + N2 + 3;
                        encryptedChar(char_C);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_C = M2 + N1 + 3;
                        encryptedChar(char_C);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_C = M2 + N2 + 3;
                        encryptedChar(char_C);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_C = M3 + N1 + 3;
                        encryptedChar(char_C);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_C = M3 + N2 + 3;
                        encryptedChar(char_C);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_C = M4 + N1 + 3;
                        encryptedChar(char_C);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_C = M4 + N2 + 3;
                        encryptedChar(char_C);
                    }
                    break;
                case 'D':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_D = M1 + N1 + 4;
                        encryptedChar(char_D);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_D = M1 + N2 + 4;
                        encryptedChar(char_D);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_D = M2 + N1 + 4;
                        encryptedChar(char_D);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_D = M2 + N2 + 4;
                        encryptedChar(char_D);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_D = M3 + N1 + 4;
                        encryptedChar(char_D);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_D = M3 + N2 + 4;
                        encryptedChar(char_D);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_D = M4 + N1 + 4;
                        encryptedChar(char_D);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_D = M4 + N2 + 4;
                        encryptedChar(char_D);
                    }
                    break;
                case 'E':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_E = M1 + N1 + 5;
                        encryptedChar(char_E);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_E = M1 + N2 + 5;
                        encryptedChar(char_E);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_E = M2 + N1 + 5;
                        encryptedChar(char_E);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_E = M2 + N2 + 5;
                        encryptedChar(char_E);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_E = M3 + N1 + 5;
                        encryptedChar(char_E);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_E = M3 + N2 + 5;
                        encryptedChar(char_E);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_E = M4 + N1 + 5;
                        encryptedChar(char_E);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_E = M4 + N2 + 5;
                        encryptedChar(char_E);
                    }
                    break;

                case 'F':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_F = M1 + N1 + 6;
                        encryptedChar(char_F);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_F = M1 + N2 + 6;
                        encryptedChar(char_F);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_F = M2 + N1 + 6;
                        encryptedChar(char_F);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_F = M2 + N2 + 6;
                        encryptedChar(char_F);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_F = M3 + N1 + 6;
                        encryptedChar(char_F);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_F = M3 + N2 + 6;
                        encryptedChar(char_F);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_F = M4 + N1 + 6;
                        encryptedChar(char_F);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_F = M4 + N2 + 6;
                        encryptedChar(char_F);
                    }
                    break;
                case 'G':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_G = M1 + N1 + 7;
                        encryptedChar(char_G);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_G = M1 + N2 + 7;
                        encryptedChar(char_G);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_G = M2 + N1 + 7;
                        encryptedChar(char_G);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_G = M2 + N2 + 7;
                        encryptedChar(char_G);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_G = M3 + N1 + 7;
                        encryptedChar(char_G);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_G = M3 + N2 + 7;
                        encryptedChar(char_G);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_G = M4 + N1 + 7;
                        encryptedChar(char_G);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_G = M4 + N2 + 7;
                        encryptedChar(char_G);
                    }
                    break;
                case 'H':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_H = M1 + N1 + 8;
                        encryptedChar(char_H);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_H = M1 + N2 + 8;
                        encryptedChar(char_H);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_H = M2 + N1 + 8;
                        encryptedChar(char_H);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_H = M2 + N2 + 8;
                        encryptedChar(char_H);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_H = M3 + N1 + 8;
                        encryptedChar(char_H);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_H = M3 + N2 + 8;
                        encryptedChar(char_H);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_H = M4 + N1 + 8;
                        encryptedChar(char_H);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_H = M4 + N2 + 8;
                        encryptedChar(char_H);
                    }
                    break;
                case 'I':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_I = M1 + N1 + 9;
                        encryptedChar(char_I);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_I = M1 + N2 + 9;
                        encryptedChar(char_I);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_I = M2 + N1 + 9;
                        encryptedChar(char_I);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_I = M2 + N2 + 9;
                        encryptedChar(char_I);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_I = M3 + N1 + 9;
                        encryptedChar(char_I);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_I = M3 + N2 + 9;
                        encryptedChar(char_I);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_I = M4 + N1 + 9;
                        encryptedChar(char_I);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_I = M4 + N2 + 9;
                        encryptedChar(char_I);
                    }
                    break;
                case 'J':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_J = M1 + N1 + 10;
                        encryptedChar(char_J);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_J = M1 + N2 + 10;
                        encryptedChar(char_J);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_J = M2 + N1 + 10;
                        encryptedChar(char_J);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_J = M2 + N2 + 10;
                        encryptedChar(char_J);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_J = M3 + N1 + 10;
                        encryptedChar(char_J);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_J = M3 + N2 + 10;
                        encryptedChar(char_J);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_J = M4 + N1 + 10;
                        encryptedChar(char_J);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_J = M4 + N2 + 10;
                        encryptedChar(char_J);
                    }
                    break;
                case 'K':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_K = M1 + N1 + 11;
                        encryptedChar(char_K);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_K = M1 + N2 + 11;
                        encryptedChar(char_K);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_K = M2 + N1 + 11;
                        encryptedChar(char_K);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_K = M2 + N2 + 11;
                        encryptedChar(char_K);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_K = M3 + N1 + 11;
                        encryptedChar(char_K);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_K = M3 + N2 + 11;
                        encryptedChar(char_K);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_K = M4 + N1 + 11;
                        encryptedChar(char_K);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_K = M4 + N2 + 11;
                        encryptedChar(char_K);
                    }
                    break;
                case 'L':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_L = M1 + N1 + 12;
                        encryptedChar(char_L);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_L = M1 + N2 + 12;
                        encryptedChar(char_L);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_L = M2 + N1 + 12;
                        encryptedChar(char_L);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_L = M2 + N2 + 12;
                        encryptedChar(char_L);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_L = M3 + N1 + 12;
                        encryptedChar(char_L);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_L = M3 + N2 + 12;
                        encryptedChar(char_L);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_L = M4 + N1 + 12;
                        encryptedChar(char_L);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_L = M4 + N2 + 12;
                        encryptedChar(char_L);
                    }
                    break;
                case 'M':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_M = M1 + N1 + 13;
                        encryptedChar(char_M);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_M = M1 + N2 + 13;
                        encryptedChar(char_M);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_M = M2 + N1 + 13;
                        encryptedChar(char_M);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_M = M2 + N2 + 13;
                        encryptedChar(char_M);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_M = M3 + N1 + 13;
                        encryptedChar(char_M);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_M = M3 + N2 + 13;
                        encryptedChar(char_M);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_M = M4 + N1 + 13;
                        encryptedChar(char_M);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_M = M4 + N2 + 13;
                        encryptedChar(char_M);
                    }
                    break;
                case 'N':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_N = M1 + N1 + 14;
                        encryptedChar(char_N);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_N = M1 + N2 + 14;
                        encryptedChar(char_N);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_N = M2 + N1 + 14;
                        encryptedChar(char_N);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_N = M2 + N2 + 14;
                        encryptedChar(char_N);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_N = M3 + N1 + 14;
                        encryptedChar(char_N);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_N = M3 + N2 + 14;
                        encryptedChar(char_N);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_N = M4 + N1 + 14;
                        encryptedChar(char_N);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_N = M4 + N2 + 14;
                        encryptedChar(char_N);
                    }
                    break;
                case 'O':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_O = M1 + N1 + 4;
                        encryptedChar(char_O);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_O = M1 + N2 + 15;
                        encryptedChar(char_O);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_O = M2 + N1 + 15;
                        encryptedChar(char_O);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_O = M2 + N2 + 15;
                        encryptedChar(char_O);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_O = M3 + N1 + 15;
                        encryptedChar(char_O);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_O = M3 + N2 + 15;
                        encryptedChar(char_O);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_O = M4 + N1 + 15;
                        encryptedChar(char_O);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_O = M4 + N2 + 15;
                        encryptedChar(char_O);
                    }
                    break;
                case 'P':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_P = M1 + N1 + 16;
                        encryptedChar(char_P);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_P = M1 + N2 + 16;
                        encryptedChar(char_P);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_P = M2 + N1 + 16;
                        encryptedChar(char_P);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_P = M2 + N2 + 16;
                        encryptedChar(char_P);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_P = M3 + N1 + 16;
                        encryptedChar(char_P);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_P = M3 + N2 + 16;
                        encryptedChar(char_P);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_P = M4 + N1 + 16;
                        encryptedChar(char_P);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_P = M4 + N2 + 16;
                        encryptedChar(char_P);
                    }
                    break;
                case 'Q':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Q = M1 + N1 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Q = M1 + N2 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Q = M2 + N1 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Q = M2 + N2 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Q = M3 + N1 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Q = M3 + N2 + 17;
                        encryptedChar(char_Q);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Q = M4 + N1 + 4;
                        encryptedChar(char_Q);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Q = M4 + N2 + 17;
                        encryptedChar(char_Q);
                    }
                    break;
                case 'R':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_R = M1 + N1 + 18;
                        encryptedChar(char_R);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_R = M1 + N2 + 18;
                        encryptedChar(char_R);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_R = M2 + N1 + 18;
                        encryptedChar(char_R);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_R = M2 + N2 + 18;
                        encryptedChar(char_R);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_R = M3 + N1 + 18;
                        encryptedChar(char_R);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_R = M3 + N2 + 18;
                        encryptedChar(char_R);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_R = M4 + N1 + 18;
                        encryptedChar(char_R);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_R = M4 + N2 + 18;
                        encryptedChar(char_R);
                    }
                    break;
                case 'S':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_S = M1 + N1 + 19;
                        encryptedChar(char_S);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_S = M1 + N2 + 19;
                        encryptedChar(char_S);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_S = M2 + N1 + 19;
                        encryptedChar(char_S);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_S = M2 + N2 + 19;
                        encryptedChar(char_S);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_S = M3 + N1 + 19;
                        encryptedChar(char_S);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_S = M3 + N2 + 19;
                        encryptedChar(char_S);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_S = M4 + N1 + 19;
                        encryptedChar(char_S);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_S = M4 + N2 + 19;
                        encryptedChar(char_S);
                    }
                    break;
                case 'T':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_T = M1 + N1 + 20;
                        encryptedChar(char_T);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_T = M1 + N2 + 20;
                        encryptedChar(char_T);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_T = M2 + N1 + 20;
                        encryptedChar(char_T);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_T = M2 + N2 + 20;
                        encryptedChar(char_T);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_T = M3 + N1 + 20;
                        encryptedChar(char_T);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_T = M3 + N2 + 20;
                        encryptedChar(char_T);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_T = M4 + N1 + 20;
                        encryptedChar(char_T);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_T = M4 + N2 + 20;
                        encryptedChar(char_T);
                    }
                    break;
                case 'U':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_U = M1 + N1 + 21;
                        encryptedChar(char_U);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_U = M1 + N2 + 21;
                        encryptedChar(char_U);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_U = M2 + N1 + 21;
                        encryptedChar(char_U);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_U = M2 + N2 + 21;
                        encryptedChar(char_U);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_U = M3 + N1 + 21;
                        encryptedChar(char_U);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_U = M3 + N2 + 21;
                        encryptedChar(char_U);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_U = M4 + N1 + 21;
                        encryptedChar(char_U);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_U = M4 + N2 + 21;
                        encryptedChar(char_U);
                    }
                    break;
                case 'V':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_V = M1 + N1 + 22;
                        encryptedChar(char_V);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_V = M1 + N2 + 22;
                        encryptedChar(char_V);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_V = M2 + N1 + 22;
                        encryptedChar(char_V);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_V = M2 + N2 + 22;
                        encryptedChar(char_V);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_V = M3 + N1 + 22;
                        encryptedChar(char_V);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_V = M3 + N2 + 22;
                        encryptedChar(char_V);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_V = M4 + N1 + 22;
                        encryptedChar(char_V);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_V = M4 + N2 + 22;
                        encryptedChar(char_V);
                    }
                    break;
                case 'W':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_W = M1 + N1 + 23;
                        encryptedChar(char_W);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_W = M1 + N2 + 23;
                        encryptedChar(char_W);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_W = M2 + N1 + 23;
                        encryptedChar(char_W);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_W = M2 + N2 + 23;
                        encryptedChar(char_W);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_W = M3 + N1 + 23;
                        encryptedChar(char_W);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_W = M3 + N2 + 23;
                        encryptedChar(char_W);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_W = M4 + N1 + 23;
                        encryptedChar(char_W);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_W = M4 + N2 + 23;
                        encryptedChar(char_W);
                    }
                    break;
                case 'X':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_X = M1 + N1 + 24;
                        encryptedChar(char_X);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_X = M1 + N2 + 24;
                        encryptedChar(char_X);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_X = M2 + N1 + 24;
                        encryptedChar(char_X);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_X = M2 + N2 + 24;
                        encryptedChar(char_X);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_X = M3 + N1 + 24;
                        encryptedChar(char_X);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_X = M3 + N2 + 24;
                        encryptedChar(char_X);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_X = M4 + N1 + 24;
                        encryptedChar(char_X);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_X = M4 + N2 + 24;
                        encryptedChar(char_X);
                    }
                    break;
                case 'Y':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Y = M1 + N1 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Y = M1 + N2 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Y = M2 + N1 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Y = M2 + N2 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Y = M3 + N1 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Y = M3 + N2 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Y = M4 + N1 + 25;
                        encryptedChar(char_Y);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Y = M4 + N2 + 25;
                        encryptedChar(char_Y);
                    }
                    break;
                case 'Z':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_Z = M1 + N1 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_Z = M1 + N2 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_Z = M2 + N1 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_Z = M2 + N2 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_Z = M3 + N1 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_Z = M3 + N2 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_Z = M4 + N1 + 26;
                        encryptedChar(char_Z);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_Z = M4 + N2 + 26;
                        encryptedChar(char_Z);
                    }
                    break;
                case 'a':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_a = M1 + N1 + 27;
                        encryptedChar(char_a);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_a = M1 + N2 + 27;
                        encryptedChar(char_a);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_a = M2 + N1 + 27;
                        encryptedChar(char_a);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_a = M2 + N2 + 27;
                        encryptedChar(char_a);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_a = M3 + N1 + 27;
                        encryptedChar(char_a);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_a = M3 + N2 + 27;
                        encryptedChar(char_a);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_a = M4 + N1 + 27;
                        encryptedChar(char_a);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_a = M4 + N2 + 27;
                        encryptedChar(char_a);
                    }
                    break;
                case 'b':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_b = M1 + N1 + 28;
                        encryptedChar(char_b);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_b = M1 + N2 + 28;
                        encryptedChar(char_b);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_b = M2 + N1 + 28;
                        encryptedChar(char_b);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_b = M2 + N2 + 28;
                        encryptedChar(char_b);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_b = M3 + N1 + 28;
                        encryptedChar(char_b);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_b = M3 + N2 + 28;
                        encryptedChar(char_b);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_b = M4 + N1 + 28;
                        encryptedChar(char_b);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_b = M4 + N2 + 28;
                        encryptedChar(char_b);
                    }
                    break;
                case 'c':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_c = M1 + N1 + 29;
                        encryptedChar(char_c);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_c = M1 + N2 + 29;
                        encryptedChar(char_c);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_c = M2 + N1 + 29;
                        encryptedChar(char_c);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_c = M2 + N2 + 29;
                        encryptedChar(char_c);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_c = M3 + N1 + 29;
                        encryptedChar(char_c);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_c = M3 + N2 + 29;
                        encryptedChar(char_c);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_c = M4 + N1 + 29;
                        encryptedChar(char_c);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_c = M4 + N2 + 29;
                        encryptedChar(char_c);
                    }
                    break;
                case 'd':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_d = M1 + N1 + 30;
                        encryptedChar(char_d);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_d = M1 + N2 + 30;
                        encryptedChar(char_d);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_d = M2 + N1 + 30;
                        encryptedChar(char_d);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_d = M2 + N2 + 30;
                        encryptedChar(char_d);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_d = M3 + N1 + 30;
                        encryptedChar(char_d);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_d = M3 + N2 + 30;
                        encryptedChar(char_d);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_d = M4 + N1 + 30;
                        encryptedChar(char_d);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_d = M4 + N2 + 30;
                        encryptedChar(char_d);
                    }
                    break;
                case 'e':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_e = M1 + N1 + 31;
                        encryptedChar(char_e);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_e = M1 + N2 + 31;
                        encryptedChar(char_e);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_e = M2 + N1 + 31;
                        encryptedChar(char_e);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_e = M2 + N2 + 31;
                        encryptedChar(char_e);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_e = M3 + N1 + 31;
                        encryptedChar(char_e);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_e = M3 + N2 + 31;
                        encryptedChar(char_e);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_e = M4 + N1 + 31;
                        encryptedChar(char_e);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_e = M4 + N2 + 31;
                        encryptedChar(char_e);
                    }
                    break;
                case 'f':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_f = M1 + N1 + 32;
                        encryptedChar(char_f);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_f = M1 + N2 + 32;
                        encryptedChar(char_f);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_f = M2 + N1 + 32;
                        encryptedChar(char_f);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_f = M2 + N2 + 32;
                        encryptedChar(char_f);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_f = M3 + N1 + 32;
                        encryptedChar(char_f);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_f = M3 + N2 + 32;
                        encryptedChar(char_f);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_f = M4 + N1 + 32;
                        encryptedChar(char_f);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_f = M4 + N2 + 32;
                        encryptedChar(char_f);
                    }
                    break;
                case 'g':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_g = M1 + N1 + 33;
                        encryptedChar(char_g);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_g = M1 + N2 + 33;
                        encryptedChar(char_g);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_g = M2 + N1 + 33;
                        encryptedChar(char_g);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_g = M2 + N2 + 33;
                        encryptedChar(char_g);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_g = M3 + N1 + 33;
                        encryptedChar(char_g);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_g = M3 + N2 + 33;
                        encryptedChar(char_g);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_g = M4 + N1 + 33;
                        encryptedChar(char_g);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_g = M4 + N2 + 33;
                        encryptedChar(char_g);
                    }
                    break;
                case 'h':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_h = M1 + N1 + 34;
                        encryptedChar(char_h);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_h = M1 + N2 + 34;
                        encryptedChar(char_h);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_h = M2 + N1 + 34;
                        encryptedChar(char_h);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_h = M2 + N2 + 34;
                        encryptedChar(char_h);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_h = M3 + N1 + 34;
                        encryptedChar(char_h);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_h = M3 + N2 + 34;
                        encryptedChar(char_h);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_h = M4 + N1 + 34;
                        encryptedChar(char_h);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_h = M4 + N2 + 34;
                        encryptedChar(char_h);
                    }
                    break;
                case 'i':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_i = M1 + N1 + 35;
                        encryptedChar(char_i);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_i = M1 + N2 + 35;
                        encryptedChar(char_i);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_i = M2 + N1 + 35;
                        encryptedChar(char_i);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_i = M2 + N2 + 35;
                        encryptedChar(char_i);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_i = M3 + N1 + 35;
                        encryptedChar(char_i);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_i = M3 + N2 + 35;
                        encryptedChar(char_i);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_i = M4 + N1 + 35;
                        encryptedChar(char_i);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_i = M4 + N2 + 35;
                        encryptedChar(char_i);
                    }
                    break;
                case 'j':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_j = M1 + N1 + 36;
                        encryptedChar(char_j);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_j = M1 + N2 + 36;
                        encryptedChar(char_j);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_j = M2 + N1 + 36;
                        encryptedChar(char_j);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_j = M2 + N2 + 36;
                        encryptedChar(char_j);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_j = M3 + N1 + 36;
                        encryptedChar(char_j);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_j = M3 + N2 + 36;
                        encryptedChar(char_j);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_j = M4 + N1 + 36;
                        encryptedChar(char_j);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_j = M4 + N2 + 36;
                        encryptedChar(char_j);
                    }
                    break;
                case 'k':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_k = M1 + N1 + 37;
                        encryptedChar(char_k);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_k = M1 + N2 + 37;
                        encryptedChar(char_k);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_k = M2 + N1 + 37;
                        encryptedChar(char_k);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_k = M2 + N2 + 37;
                        encryptedChar(char_k);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_k = M3 + N1 + 37;
                        encryptedChar(char_k);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_k = M3 + N2 + 37;
                        encryptedChar(char_k);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_k = M4 + N1 + 37;
                        encryptedChar(char_k);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_k = M4 + N2 + 37;
                        encryptedChar(char_k);
                    }
                    break;
                case 'l':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_l = M1 + N1 + 38;
                        encryptedChar(char_l);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_l = M1 + N2 + 38;
                        encryptedChar(char_l);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_l = M2 + N1 + 38;
                        encryptedChar(char_l);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_l = M2 + N2 + 38;
                        encryptedChar(char_l);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_l = M3 + N1 + 38;
                        encryptedChar(char_l);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_l = M3 + N2 + 38;
                        encryptedChar(char_l);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_l = M4 + N1 + 38;
                        encryptedChar(char_l);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_l = M4 + N2 + 38;
                        encryptedChar(char_l);
                    }
                    break;
                case 'm':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_m = M1 + N1 + 39;
                        encryptedChar(char_m);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_m = M1 + N2 + 39;
                        encryptedChar(char_m);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_m = M2 + N1 + 39;
                        encryptedChar(char_m);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_m = M2 + N2 + 39;
                        encryptedChar(char_m);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_m = M3 + N1 + 39;
                        encryptedChar(char_m);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_m = M3 + N2 + 39;
                        encryptedChar(char_m);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_m = M4 + N1 + 39;
                        encryptedChar(char_m);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_m = M4 + N2 + 39;
                        encryptedChar(char_m);
                    }
                    break;
                case 'n':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_n = M1 + N1 + 40;
                        encryptedChar(char_n);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_n = M1 + N2 + 40;
                        encryptedChar(char_n);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_n = M2 + N1 + 40;
                        encryptedChar(char_n);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_n = M2 + N2 + 40;
                        encryptedChar(char_n);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_n = M3 + N1 + 40;
                        encryptedChar(char_n);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_n = M3 + N2 + 40;
                        encryptedChar(char_n);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_n = M4 + N1 + 40;
                        encryptedChar(char_n);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_n = M4 + N2 + 40;
                        encryptedChar(char_n);
                    }
                    break;
                case 'o':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_o = M1 + N1 + 41;
                        encryptedChar(char_o);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_o = M1 + N2 + 41;
                        encryptedChar(char_o);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_o = M2 + N1 + 41;
                        encryptedChar(char_o);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_o = M2 + N2 + 41;
                        encryptedChar(char_o);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_o = M3 + N1 + 41;
                        encryptedChar(char_o);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_o = M3 + N2 + 41;
                        encryptedChar(char_o);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_o = M4 + N1 + 41;
                        encryptedChar(char_o);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_o = M4 + N2 + 41;
                        encryptedChar(char_o);
                    }
                    break;
                case 'p':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_p = M1 + N1 + 42;
                        encryptedChar(char_p);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_p = M1 + N2 + 42;
                        encryptedChar(char_p);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_p = M2 + N1 + 42;
                        encryptedChar(char_p);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_p = M2 + N2 + 42;
                        encryptedChar(char_p);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_p = M3 + N1 + 42;
                        encryptedChar(char_p);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_p = M3 + N2 + 42;
                        encryptedChar(char_p);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_p = M4 + N1 + 42;
                        encryptedChar(char_p);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_p = M4 + N2 + 42;
                        encryptedChar(char_p);
                    }
                    break;
                case 'q':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_q = M1 + N1 + 43;
                        encryptedChar(char_q);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_q = M1 + N2 + 43;
                        encryptedChar(char_q);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_q = M2 + N1 + 43;
                        encryptedChar(char_q);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_q = M2 + N2 + 43;
                        encryptedChar(char_q);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_q = M3 + N1 + 43;
                        encryptedChar(char_q);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_q = M3 + N2 + 43;
                        encryptedChar(char_q);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_q = M4 + N1 + 43;
                        encryptedChar(char_q);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_q = M4 + N2 + 43;
                        encryptedChar(char_q);
                    }
                    break;
                case 'r':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_r = M1 + N1 + 44;
                        encryptedChar(char_r);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_r = M1 + N2 + 44;
                        encryptedChar(char_r);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_r = M2 + N1 + 44;
                        encryptedChar(char_r);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_r = M2 + N2 + 44;
                        encryptedChar(char_r);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_r = M3 + N1 + 44;
                        encryptedChar(char_r);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_r = M3 + N2 + 44;
                        encryptedChar(char_r);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_r = M4 + N1 + 44;
                        encryptedChar(char_r);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_r = M4 + N2 + 44;
                        encryptedChar(char_r);
                    }
                    break;
                case 's':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_s = M1 + N1 + 45;
                        encryptedChar(char_s);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_s = M1 + N2 + 45;
                        encryptedChar(char_s);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_s = M2 + N1 + 45;
                        encryptedChar(char_s);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_s = M2 + N2 + 45;
                        encryptedChar(char_s);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_s = M3 + N1 + 45;
                        encryptedChar(char_s);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_s = M3 + N2 + 45;
                        encryptedChar(char_s);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_s = M4 + N1 + 45;
                        encryptedChar(char_s);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_s = M4 + N2 + 45;
                        encryptedChar(char_s);
                    }
                    break;
                case 't':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_t = M1 + N1 + 46;
                        encryptedChar(char_t);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_t = M1 + N2 + 46;
                        encryptedChar(char_t);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_t = M2 + N1 + 46;
                        encryptedChar(char_t);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_t = M2 + N2 + 46;
                        encryptedChar(char_t);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_t = M3 + N1 + 46;
                        encryptedChar(char_t);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_t = M3 + N2 + 46;
                        encryptedChar(char_t);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_t = M4 + N1 + 46;
                        encryptedChar(char_t);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_t = M4 + N2 + 46;
                        encryptedChar(char_t);
                    }
                    break;
                case 'u':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_u = M1 + N1 + 47;
                        encryptedChar(char_u);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_u = M1 + N2 + 47;
                        encryptedChar(char_u);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_u = M2 + N1 + 47;
                        encryptedChar(char_u);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_u = M2 + N2 + 47;
                        encryptedChar(char_u);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_u = M3 + N1 + 47;
                        encryptedChar(char_u);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_u = M3 + N2 + 47;
                        encryptedChar(char_u);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_u = M4 + N1 + 47;
                        encryptedChar(char_u);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_u = M4 + N2 + 47;
                        encryptedChar(char_u);
                    }
                    break;
                case 'v':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_v = M1 + N1 + 48;
                        encryptedChar(char_v);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_v = M1 + N2 + 48;
                        encryptedChar(char_v);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_v = M2 + N1 + 48;
                        encryptedChar(char_v);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_v = M2 + N2 + 48;
                        encryptedChar(char_v);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_v = M3 + N1 + 48;
                        encryptedChar(char_v);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_v = M3 + N2 + 48;
                        encryptedChar(char_v);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_v = M4 + N1 + 48;
                        encryptedChar(char_v);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_v = M4 + N2 + 48;
                        encryptedChar(char_v);
                    }
                    break;
                case 'w':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_w = M1 + N1 + 49;
                        encryptedChar(char_w);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_w = M1 + N2 + 49;
                        encryptedChar(char_w);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_w = M2 + N1 + 49;
                        encryptedChar(char_w);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_w = M2 + N2 + 49;
                        encryptedChar(char_w);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_w = M3 + N1 + 49;
                        encryptedChar(char_w);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_w = M3 + N2 + 49;
                        encryptedChar(char_w);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_w = M4 + N1 + 49;
                        encryptedChar(char_w);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_w = M4 + N2 + 49;
                        encryptedChar(char_w);
                    }
                    break;
                case 'x':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_x = M1 + N1 + 50;
                        encryptedChar(char_x);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_x = M1 + N2 + 50;
                        encryptedChar(char_x);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_x = M2 + N1 + 50;
                        encryptedChar(char_x);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_x = M2 + N2 + 50;
                        encryptedChar(char_x);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_x = M3 + N1 + 50;
                        encryptedChar(char_x);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_x = M3 + N2 + 50;
                        encryptedChar(char_x);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_x = M4 + N1 + 50;
                        encryptedChar(char_x);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_x = M4 + N2 + 50;
                        encryptedChar(char_x);
                    }
                    break;
                case 'y':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_y = M1 + N1 + 51;
                        encryptedChar(char_y);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_y = M1 + N2 + 51;
                        encryptedChar(char_y);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_y = M2 + N1 + 51;
                        encryptedChar(char_y);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_y = M2 + N2 + 51;
                        encryptedChar(char_y);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_y = M3 + N1 + 51;
                        encryptedChar(char_y);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_y = M3 + N2 + 51;
                        encryptedChar(char_y);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_y = M4 + N1 + 51;
                        encryptedChar(char_y);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_y = M4 + N2 + 51;
                        encryptedChar(char_y);
                    }
                    break;
                case 'z':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_z = M1 + N1 + 52;
                        encryptedChar(char_z);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_z = M1 + N2 + 52;
                        encryptedChar(char_z);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_z = M2 + N1 + 52;
                        encryptedChar(char_z);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_z = M2 + N2 + 52;
                        encryptedChar(char_z);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_z = M3 + N1 + 52;
                        encryptedChar(char_z);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_z = M3 + N2 + 52;
                        encryptedChar(char_z);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_z = M4 + N1 + 52;
                        encryptedChar(char_z);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_z = M4 + N2 + 52;
                        encryptedChar(char_z);
                    }
                    break;
                case '0':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_0 = M1 + N1 + 53;
                        encryptedChar(char_0);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_0 = M1 + N2 + 53;
                        encryptedChar(char_0);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_0 = M2 + N1 + 53;
                        encryptedChar(char_0);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_0 = M2 + N2 + 53;
                        encryptedChar(char_0);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_0 = M3 + N1 + 53;
                        encryptedChar(char_0);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_0 = M3 + N2 + 53;
                        encryptedChar(char_0);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_0 = M4 + N1 + 53;
                        encryptedChar(char_0);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_0 = M4 + N2 + 53;
                        encryptedChar(char_0);
                    }
                    break;
                case '1':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_1 = M1 + N1 + 54;
                        encryptedChar(char_1);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_1 = M1 + N2 + 54;
                        encryptedChar(char_1);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_1 = M2 + N1 + 54;
                        encryptedChar(char_1);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_1 = M2 + N2 + 54;
                        encryptedChar(char_1);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_1 = M3 + N1 + 54;
                        encryptedChar(char_1);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_1 = M3 + N2 + 54;
                        encryptedChar(char_1);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_1 = M4 + N1 + 54;
                        encryptedChar(char_1);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_1 = M4 + N2 + 54;
                        encryptedChar(char_1);
                    }
                    break;
                case '2':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_2 = M1 + N1 + 55;
                        encryptedChar(char_2);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_2 = M1 + N2 + 55;
                        encryptedChar(char_2);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_2 = M2 + N1 + 55;
                        encryptedChar(char_2);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_2 = M2 + N2 + 55;
                        encryptedChar(char_2);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_2 = M3 + N1 + 55;
                        encryptedChar(char_2);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_2 = M3 + N2 + 55;
                        encryptedChar(char_2);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_2 = M4 + N1 + 55;
                        encryptedChar(char_2);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_2 = M4 + N2 + 55;
                        encryptedChar(char_2);
                    }
                    break;
                case '3':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_3 = M1 + N1 + 56;
                        encryptedChar(char_3);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_3 = M1 + N2 + 56;
                        encryptedChar(char_3);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_3 = M2 + N1 + 56;
                        encryptedChar(char_3);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_3 = M2 + N2 + 56;
                        encryptedChar(char_3);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_3 = M3 + N1 + 56;
                        encryptedChar(char_3);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_3 = M3 + N2 + 56;
                        encryptedChar(char_3);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_3 = M4 + N1 + 56;
                        encryptedChar(char_3);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_3 = M4 + N2 + 56;
                        encryptedChar(char_3);
                    }
                    break;
                case '4':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_4 = M1 + N1 + 57;
                        encryptedChar(char_4);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_4 = M1 + N2 + 57;
                        encryptedChar(char_4);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_4 = M2 + N1 + 57;
                        encryptedChar(char_4);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_4 = M2 + N2 + 57;
                        encryptedChar(char_4);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_4 = M3 + N1 + 57;
                        encryptedChar(char_4);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_4 = M3 + N2 + 57;
                        encryptedChar(char_4);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_4 = M4 + N1 + 57;
                        encryptedChar(char_4);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_4 = M4 + N2 + 57;
                        encryptedChar(char_4);
                    }
                    break;
                case '5':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_5 = M1 + N1 + 58;
                        encryptedChar(char_5);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_5 = M1 + N2 + 58;
                        encryptedChar(char_5);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_5 = M2 + N1 + 58;
                        encryptedChar(char_5);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_5 = M2 + N2 + 58;
                        encryptedChar(char_5);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_5 = M3 + N1 + 58;
                        encryptedChar(char_5);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_5 = M3 + N2 + 58;
                        encryptedChar(char_5);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_5 = M4 + N1 + 58;
                        encryptedChar(char_5);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_5 = M4 + N2 + 58;
                        encryptedChar(char_5);
                    }
                    break;
                case '6':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_6 = M1 + N1 + 59;
                        encryptedChar(char_6);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_6 = M1 + N2 + 59;
                        encryptedChar(char_6);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_6 = M2 + N1 + 59;
                        encryptedChar(char_6);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_6 = M2 + N2 + 59;
                        encryptedChar(char_6);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_6 = M3 + N1 + 59;
                        encryptedChar(char_6);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_6 = M3 + N2 + 59;
                        encryptedChar(char_6);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_6 = M4 + N1 + 59;
                        encryptedChar(char_6);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_6 = M4 + N2 + 59;
                        encryptedChar(char_6);
                    }
                    break;
                case '7':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_7 = M1 + N1 + 60;
                        encryptedChar(char_7);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_7 = M1 + N2 + 60;
                        encryptedChar(char_7);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_7 = M2 + N1 + 60;
                        encryptedChar(char_7);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_7 = M2 + N2 + 60;
                        encryptedChar(char_7);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_7 = M3 + N1 + 60;
                        encryptedChar(char_7);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_7 = M3 + N2 + 60;
                        encryptedChar(char_7);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_7 = M4 + N1 + 60;
                        encryptedChar(char_7);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_7 = M4 + N2 + 60;
                        encryptedChar(char_7);
                    }
                    break;
                case '8':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_8 = M1 + N1 + 61;
                        encryptedChar(char_8);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_8 = M1 + N2 + 61;
                        encryptedChar(char_8);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_8 = M2 + N1 + 61;
                        encryptedChar(char_8);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_8 = M2 + N2 + 61;
                        encryptedChar(char_8);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_8 = M3 + N1 + 61;
                        encryptedChar(char_8);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_8 = M3 + N2 + 61;
                        encryptedChar(char_8);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_8 = M4 + N1 + 61;
                        encryptedChar(char_8);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_8 = M4 + N2 + 61;
                        encryptedChar(char_8);
                    }
                    break;
                case '9':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_9 = M1 + N1 + 62;
                        encryptedChar(char_9);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_9 = M1 + N2 + 62;
                        encryptedChar(char_9);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_9 = M2 + N1 + 62;
                        encryptedChar(char_9);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_9 = M2 + N2 + 62;
                        encryptedChar(char_9);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_9 = M3 + N1 + 62;
                        encryptedChar(char_9);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_9 = M3 + N2 + 62;
                        encryptedChar(char_9);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_9 = M4 + N1 + 62;
                        encryptedChar(char_9);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_9 = M4 + N2 + 62;
                        encryptedChar(char_9);
                    }
                    break;
                case ',':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_comma = M1 + N1 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_comma = M1 + N2 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_comma = M2 + N1 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_comma = M2 + N2 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_comma = M3 + N1 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_comma = M3 + N2 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_comma = M4 + N1 + 63;
                        encryptedChar(char_comma);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_comma = M4 + N2 + 63;
                        encryptedChar(char_comma);
                    }
                    break;
                case '.':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_fullstop = M1 + N1 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_fullstop = M1 + N2 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_fullstop = M2 + N1 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_fullstop = M2 + N2 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_fullstop = M3 + N1 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_fullstop = M3 + N2 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_fullstop = M4 + N1 + 64;
                        encryptedChar(char_fullstop);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_fullstop = M4 + N2 + 64;
                        encryptedChar(char_fullstop);
                    }
                    break;
                case '?':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_qmark = M1 + N1 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_qmark = M1 + N2 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_qmark = M2 + N1 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_qmark = M2 + N2 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_qmark = M3 + N1 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_qmark = M3 + N2 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_qmark = M4 + N1 + 65;
                        encryptedChar(char_qmark);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_qmark = M4 + N2 + 65;
                        encryptedChar(char_qmark);
                    }
                    break;
                case '!':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_exmark = M1 + N1 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_exmark = M1 + N2 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_exmark = M2 + N1 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_exmark = M2 + N2 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_exmark = M3 + N1 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_exmark = M3 + N2 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_exmark = M4 + N1 + 66;
                        encryptedChar(char_exmark);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_exmark = M4 + N2 + 66;
                        encryptedChar(char_exmark);
                    }
                    break;
                case ';':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_semicolon = M1 + N1 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_semicolon = M1 + N2 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_semicolon = M2 + N1 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_semicolon = M2 + N2 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_semicolon = M3 + N1 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_semicolon = M3 + N2 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_semicolon = M4 + N1 + 67;
                        encryptedChar(char_semicolon);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_semicolon = M4 + N2 + 67;
                        encryptedChar(char_semicolon);
                    }
                    break;
                case ':':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_colon = M1 + N1 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_colon = M1 + N2 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_colon = M2 + N1 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_colon = M2 + N2 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_colon = M3 + N1 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_colon = M3 + N2 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_colon = M4 + N1 + 68;
                        encryptedChar(char_colon);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_colon = M4 + N2 + 68;
                        encryptedChar(char_colon);
                    }
                    break;
                case '/':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_frwdslash = M1 + N1 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_frwdslash = M1 + N2 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_frwdslash = M2 + N1 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_frwdslash = M2 + N2 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_frwdslash = M3 + N1 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_frwdslash = M3 + N2 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_frwdslash = M4 + N1 + 69;
                        encryptedChar(char_frwdslash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_frwdslash = M4 + N2 + 69;
                        encryptedChar(char_frwdslash);
                    }
                    break;
                case '\\':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_backslash = M1 + N1 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_backslash = M1 + N2 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_backslash = M2 + N1 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_backslash = M2 + N2 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_backslash = M3 + N1 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_backslash = M3 + N2 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_backslash = M4 + N1 + 70;
                        encryptedChar(char_backslash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_backslash = M4 + N2 + 70;
                        encryptedChar(char_backslash);
                    }
                    break;
                case '@':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_at = M1 + N1 + 71;
                        encryptedChar(char_at);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_at = M1 + N2 + 71;
                        encryptedChar(char_at);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_at = M2 + N1 + 71;
                        encryptedChar(char_at);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_at = M2 + N2 + 71;
                        encryptedChar(char_at);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_at = M3 + N1 + 71;
                        encryptedChar(char_at);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_at = M3 + N2 + 71;
                        encryptedChar(char_at);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_at = M4 + N1 + 71;
                        encryptedChar(char_at);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_at = M4 + N2 + 71;
                        encryptedChar(char_at);
                    }
                    break;
                case '#':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_hash = M1 + N1 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_hash = M1 + N2 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_hash = M2 + N1 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_hash = M2 + N2 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_hash = M3 + N1 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_hash = M3 + N2 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_hash = M4 + N1 + 72;
                        encryptedChar(char_hash);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_hash = M4 + N2 + 72;
                        encryptedChar(char_hash);
                    }
                    break;
                case '%':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_percent = M1 + N1 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_percent = M1 + N2 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_percent = M2 + N1 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_percent = M2 + N2 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_percent = M3 + N1 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_percent = M3 + N2 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_percent = M4 + N1 + 73;
                        encryptedChar(char_percent);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_percent = M4 + N2 + 73;
                        encryptedChar(char_percent);
                    }
                    break;
                case '&':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_ampersand = M1 + N1 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_ampersand = M1 + N2 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_ampersand = M2 + N1 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_ampersand = M2 + N2 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_ampersand = M3 + N1 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_ampersand = M3 + N2 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_ampersand = M4 + N1 + 74;
                        encryptedChar(char_ampersand);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_ampersand = M4 + N2 + 74;
                        encryptedChar(char_ampersand);
                    }
                    break;
                case '*':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_asterisk = M1 + N1 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_asterisk = M1 + N2 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_asterisk = M2 + N1 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_asterisk = M2 + N2 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_asterisk = M3 + N1 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_asterisk = M3 + N2 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_asterisk = M4 + N1 + 75;
                        encryptedChar(char_asterisk);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_asterisk = M4 + N2 + 75;
                        encryptedChar(char_asterisk);
                    }
                    break;
                case '(':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_openbracket = M1 + N1 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_openbracket = M1 + N2 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_openbracket = M2 + N1 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_openbracket = M2 + N2 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_openbracket = M3 + N1 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_openbracket = M3 + N2 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_openbracket = M4 + N1 + 76;
                        encryptedChar(char_openbracket);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_openbracket = M4 + N2 + 76;
                        encryptedChar(char_openbracket);
                    }
                    break;
                case ')':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_closebracket = M1 + N1 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_closebracket = M1 + N2 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_closebracket = M2 + N1 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_closebracket = M2 + N2 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_closebracket = M3 + N1 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_closebracket = M3 + N2 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_closebracket = M4 + N1 + 77;
                        encryptedChar(char_closebracket);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_closebracket = M4 + N2 + 77;
                        encryptedChar(char_closebracket);
                    }
                    break;
                case '-':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_hyphen = M1 + N1 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_hyphen = M1 + N2 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_hyphen = M2 + N1 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_hyphen = M2 + N2 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_hyphen = M3 + N1 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_hyphen = M3 + N2 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_hyphen = M4 + N1 + 78;
                        encryptedChar(char_hyphen);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_hyphen = M4 + N2 + 78;
                        encryptedChar(char_hyphen);
                    }
                    break;
                case '+':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_plus = M1 + N1 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_plus = M1 + N2 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_plus = M2 + N1 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_plus = M2 + N2 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_plus = M3 + N1 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_plus = M3 + N2 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_plus = M4 + N1 + 79;
                        encryptedChar(char_plus);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_plus = M4 + N2 + 79;
                        encryptedChar(char_plus);
                    }
                    break;
                case '=':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_equals = M1 + N1 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_equals = M1 + N2 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_equals = M2 + N1 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_equals = M2 + N2 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_equals = M3 + N1 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_equals = M3 + N2 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_equals = M4 + N1 + 80;
                        encryptedChar(char_equals);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_equals = M4 + N2 + 80;
                        encryptedChar(char_equals);
                    }
                    break;
                case '<':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_lessthan = M1 + N1 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_lessthan = M1 + N2 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_lessthan = M2 + N1 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_lessthan = M2 + N2 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_lessthan = M3 + N1 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_lessthan = M3 + N2 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_lessthan = M4 + N1 + 81;
                        encryptedChar(char_lessthan);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_lessthan = M4 + N2 + 81;
                        encryptedChar(char_lessthan);
                    }
                    break;
                case '>':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_greaterthan = M1 + N1 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_greaterthan = M1 + N2 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_greaterthan = M2 + N1 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_greaterthan = M2 + N2 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_greaterthan = M3 + N1 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_greaterthan = M3 + N2 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_greaterthan = M4 + N1 + 82;
                        encryptedChar(char_greaterthan);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_greaterthan = M4 + N2 + 82;
                        encryptedChar(char_greaterthan);
                    }
                    break;
                case '"':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_doublequote = M1 + N1 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_doublequote = M1 + N2 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_doublequote = M2 + N1 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_doublequote = M2 + N2 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_doublequote = M3 + N1 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_doublequote = M3 + N2 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_doublequote = M4 + N1 + 83;
                        encryptedChar(char_doublequote);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_doublequote = M4 + N2 + 83;
                        encryptedChar(char_doublequote);
                    }
                    break;
                case '\'':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_singlequote = M1 + N1 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_singlequote = M1 + N2 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_singlequote = M2 + N1 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_singlequote = M2 + N2 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_singlequote = M3 + N1 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_singlequote = M3 + N2 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_singlequote = M4 + N1 + 84;
                        encryptedChar(char_singlequote);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_singlequote = M4 + N2 + 84;
                        encryptedChar(char_singlequote);
                    }
                    break;
                case ' ':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_wspace = M1 + N1 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_wspace = M1 + N2 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_wspace = M2 + N1 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_wspace = M2 + N2 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_wspace = M3 + N1 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_wspace = M3 + N2 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_wspace = M4 + N1 + 85;
                        encryptedChar(char_wspace);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_wspace = M4 + N2 + 85;
                        encryptedChar(char_wspace);
                    }
                    break;
                case '[':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_squareopenbrac = M1 + N1 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_squareopenbrac = M1 + N2 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_squareopenbrac = M2 + N1 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_squareopenbrac = M2 + N2 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_squareopenbrac = M3 + N1 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_squareopenbrac = M3 + N2 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_squareopenbrac = M4 + N1 + 86;
                        encryptedChar(char_squareopenbrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_squareopenbrac = M4 + N2 + 86;
                        encryptedChar(char_squareopenbrac);
                    }
                    break;
                case ']':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_squareclosebrac = M1 + N1 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_squareclosebrac = M1 + N2 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_squareclosebrac = M2 + N1 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_squareclosebrac = M2 + N2 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_squareclosebrac = M3 + N1 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_squareclosebrac = M3 + N2 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_squareclosebrac = M4 + N1 + 87;
                        encryptedChar(char_squareclosebrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_squareclosebrac = M4 + N2 + 87;
                        encryptedChar(char_squareclosebrac);
                    }
                    break;
                case '{':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_opencurlybrac = M1 + N1 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_opencurlybrac = M1 + N2 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_opencurlybrac = M2 + N1 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_opencurlybrac = M2 + N2 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_opencurlybrac = M3 + N1 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_opencurlybrac = M3 + N2 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_opencurlybrac = M4 + N1 + 88;
                        encryptedChar(char_opencurlybrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_opencurlybrac = M4 + N2 + 88;
                        encryptedChar(char_opencurlybrac);
                    }
                    break;
                case '}':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_closecurlybrac = M1 + N1 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_closecurlybrac = M1 + N2 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_closecurlybrac = M2 + N1 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_closecurlybrac = M2 + N2 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_closecurlybrac = M3 + N1 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_closecurlybrac = M3 + N2 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_closecurlybrac = M4 + N1 + 89;
                        encryptedChar(char_closecurlybrac);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_closecurlybrac = M4 + N2 + 89;
                        encryptedChar(char_closecurlybrac);
                    }
                    break;
                case '_':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_underscore = M1 + N1 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_underscore = M1 + N2 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_underscore = M2 + N1 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_underscore = M2 + N2 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_underscore = M3 + N1 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_underscore = M3 + N2 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_underscore = M4 + N1 + 90;
                        encryptedChar(char_underscore);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_underscore = M4 + N2 + 90;
                        encryptedChar(char_underscore);
                    }
                    break;
                case '~':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_tilde = M1 + N1 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_tilde = M1 + N2 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_tilde = M2 + N1 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_tilde = M2 + N2 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_tilde = M3 + N1 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_tilde = M3 + N2 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_tilde = M4 + N1 + 91;
                        encryptedChar(char_tilde);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_tilde = M4 + N2 + 91;
                        encryptedChar(char_tilde);
                    }
                    break;
                case '|':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_verticalbar = M1 + N1 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_verticalbar = M1 + N2 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_verticalbar = M2 + N1 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_verticalbar = M2 + N2 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_verticalbar = M3 + N1 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_verticalbar = M3 + N2 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_verticalbar = M4 + N1 + 92;
                        encryptedChar(char_verticalbar);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_verticalbar = M4 + N2 + 92;
                        encryptedChar(char_verticalbar);
                    }
                    break;
                case '$':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_dollar = M1 + N1 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_dollar = M1 + N2 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_dollar = M2 + N1 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_dollar = M2 + N2 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_dollar = M3 + N1 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_dollar = M3 + N2 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_dollar = M4 + N1 + 93;
                        encryptedChar(char_dollar);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_dollar = M4 + N2 + 93;
                        encryptedChar(char_dollar);
                    }
                    break;
                case '`':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_backtick = M1 + N1 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_backtick = M1 + N2 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_backtick = M2 + N1 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_backtick = M2 + N2 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_backtick = M3 + N1 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_backtick = M3 + N2 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_backtick = M4 + N1 + 94;
                        encryptedChar(char_backtick);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_backtick = M4 + N2 + 94;
                        encryptedChar(char_backtick);
                    }
                    break;
                case '^':
                    if (index == 0 || index == 8 || index == 16 || index == 24) {
                        int char_empty = M1 + N1 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 1 || index == 9 || index == 17 || index == 25) {
                        int char_empty = M1 + N2 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 2 || index == 10 || index == 18 || index == 26) {
                        int char_empty = M2 + N1 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 3 || index == 11 || index == 19 || index == 27) {
                        int char_empty = M2 + N2 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 4 || index == 12 || index == 20 || index == 28) {
                        int char_empty = M3 + N1 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 5 || index == 13 || index == 21 || index == 29) {
                        int char_empty = M3 + N2 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 6 || index == 14 || index == 22) {
                        int char_empty = M4 + N1 + 95;
                        encryptedChar(char_empty);
                    } else if (index == 7 || index == 15 || index == 23) {
                        int char_empty = M4 + N2 + 95;
                        encryptedChar(char_empty);
                    }
                    break;
                default:
                    encryptedMessage += String.valueOf(c);
                    break;
            }
        }
//        String encrptedtext = "";
//
////            String str_1 = str.substring(0, 15);
////            String str_2 = str.substring(15, 30);
//        encrptedtext = str + "
        complete_encryptedMessage = "1" + "5" + encryptedMessage;
        Log.i("wificheckthread", "Mainactivity service encrypted = " + encryptedMessage);


        //  tv2.setText(complete_encryptedMessage);
        //   }
//        else{
//
//        }
        return complete_encryptedMessage;
    }

    void encryptedChar(int n) {

        switch (n) {
            case 1:
                encryptedMessage += "A";
                break;
            case 2:
                encryptedMessage += "B";
                break;
            case 3:
                encryptedMessage += "C";
                break;
            case 4:
                encryptedMessage += "D";
                break;
            case 5:
                encryptedMessage += "E";
                break;
            case 6:
                encryptedMessage += "F";
                break;
            case 7:
                encryptedMessage += "G";
                break;
            case 8:
                encryptedMessage += "H";
                break;
            case 9:
                encryptedMessage += "I";
                break;
            case 10:
                encryptedMessage += "J";
                break;
            case 11:
                encryptedMessage += "K";
                break;
            case 12:
                encryptedMessage += "L";
                break;
            case 13:
                encryptedMessage += "M";
                break;
            case 14:
                encryptedMessage += "N";
                break;
            case 15:
                encryptedMessage += "O";
                break;
            case 16:
                encryptedMessage += "P";
                break;
            case 17:
                encryptedMessage += "Q";
                break;
            case 18:
                encryptedMessage += "R";
                break;
            case 19:
                encryptedMessage += "S";
                break;
            case 20:
                encryptedMessage += "T";
                break;
            case 21:
                encryptedMessage += "U";
                break;
            case 22:
                encryptedMessage += "V";
                break;
            case 23:
                encryptedMessage += "W";
                break;
            case 24:
                encryptedMessage += "X";
                break;
            case 25:
                encryptedMessage += "Y";
                break;
            case 26:
                encryptedMessage += "Z";
                break;
            case 27:
                encryptedMessage += "a";
                break;
            case 28:
                encryptedMessage += "b";
                break;
            case 29:
                encryptedMessage += "c";
                break;
            case 30:
                encryptedMessage += "d";
                break;
            case 31:
                encryptedMessage += "e";
                break;
            case 32:
                encryptedMessage += "f";
                break;
            case 33:
                encryptedMessage += "g";
                break;
            case 34:
                encryptedMessage += "h";
                break;
            case 35:
                encryptedMessage += "i";
                break;
            case 36:
                encryptedMessage += "j";
                break;
            case 37:
                encryptedMessage += "k";
                break;
            case 38:
                encryptedMessage += "l";
                break;
            case 39:
                encryptedMessage += "m";
                break;
            case 40:
                encryptedMessage += "n";
                break;
            case 41:
                encryptedMessage += "o";
                break;
            case 42:
                encryptedMessage += "p";
                break;
            case 43:
                encryptedMessage += "q";
                break;
            case 44:
                encryptedMessage += "r";
                break;
            case 45:
                encryptedMessage += "s";
                break;
            case 46:
                encryptedMessage += "t";
                break;
            case 47:
                encryptedMessage += "u";
                break;
            case 48:
                encryptedMessage += "v";
                break;
            case 49:
                encryptedMessage += "w";
                break;
            case 50:
                encryptedMessage += "x";
                break;
            case 51:
                encryptedMessage += "y";
                break;
            case 52:
                encryptedMessage += "z";
                break;
            case 53:
                encryptedMessage += "0";
                break;
            case 54:
                encryptedMessage += "1";
                break;
            case 55:
                encryptedMessage += "2";
                break;
            case 56:
                encryptedMessage += "3";
                break;
            case 57:
                encryptedMessage += "4";
                break;
            case 58:
                encryptedMessage += "5";
                break;
            case 59:
                encryptedMessage += "6";
                break;
            case 60:
                encryptedMessage += "7";
                break;
            case 61:
                encryptedMessage += "8";
                break;
            case 62:
                encryptedMessage += "9";
                break;
            case 63:
                encryptedMessage += ",";
                break;
            case 64:
                encryptedMessage += ".";
                break;
            case 65:
                encryptedMessage += "?";
                break;
            case 66:
                encryptedMessage += "!";
                break;
            case 67:
                encryptedMessage += ";";
                break;
            case 68:
                encryptedMessage += ":";
                break;
            case 69:
                encryptedMessage += "/";
                break;
            case 70:
                encryptedMessage += "\\";
                break;
            case 71:
                encryptedMessage += "@";
                break;
            case 72:
                encryptedMessage += "#";
                break;
            case 73:
                encryptedMessage += "%";
                break;
            case 74:
                encryptedMessage += "&";
                break;
            case 75:
                encryptedMessage += "*";
                break;
            case 76:
                encryptedMessage += "(";
                break;
            case 77:
                encryptedMessage += ")";
                break;
            case 78:
                encryptedMessage += "-";
                break;
            case 79:
                encryptedMessage += "+";
                break;
            case 80:
                encryptedMessage += "=";
                break;
            case 81:
                encryptedMessage += "<";
                break;
            case 82:
                encryptedMessage += ">";
                break;
            case 83:
                encryptedMessage += "\"";
                break;
            case 84:
                encryptedMessage += "'";
                break;
            case 85:
                encryptedMessage += " ";
                break;
            case 86:
                encryptedMessage += "[";
                break;
            case 87:
                encryptedMessage += "]";
                break;
            case 88:
                encryptedMessage += "{";
                break;
            case 89:
                encryptedMessage += "}";
                break;
            case 90:
                encryptedMessage += "_";
                break;
            case 91:
                encryptedMessage += "~";
                break;
            case 92:
                encryptedMessage += "|";
                break;
            case 93:
                encryptedMessage += "$";
                break;
            case 94:
                encryptedMessage += "`";
                break;
            case 95:
                encryptedMessage += "^";
                break;
            default:
                int x = Math.abs(n) - 95;
                encryptedChar(x);
                break;
        }


    }


    @Override
    public void onBackPressed() {
        if (wifi.isWifiEnabled() == false) {
            turnOnOffHotspot(getApplicationContext(), false);
            turnOnOffWifi(getApplicationContext(), true);
        }
        finish();
    }
}


