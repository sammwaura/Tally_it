package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Objects;

/**
 * Created by Gachukia on 8/20/2015.
 */
public class CheckNetwork {
    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static Boolean isOnline() {

        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    static boolean internet = false;
    public static boolean isInternetAvailable(Context context) {
        if (context != null) {
            NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                    Objects.requireNonNull(context.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();

            if (info == null) {
                Log.d(TAG, "no internet connection");
                internet = false;
            }

            if(info != null)
            {
                if (info.isConnected()) {
                    if(isOnline())
                    {
                        Log.d(TAG, " internet connection available...");
                        internet = true;
                    }
                    else
                    {
                        Log.d(TAG, "no internet connection");
                        internet = false;
                    }
                }
            }
            else
            {
                Log.d(TAG, "no internet connection");
                internet = false;
            }


        }

        if(!internet)
        {
            if(context != null)
            {
                Intent intent = new Intent(context, No_Internet.class);
                context.startActivity(intent);
            }

        }


        return internet;
    }

    }


