package fr.neamar.lolgamedata.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Support TLSv1.2 on Android 4 to 5.
 * Otherwise, the deviceis unable to connect to the server
 */

public class VolleyQueue {
    public static RequestQueue newRequestQueue(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            HttpStack stack = null;
            try {
                stack = new HurlStack(null, new TLSSocketFactory());
            } catch (KeyManagementException e) {
                e.printStackTrace();
                Log.e("WTF", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.e("WTF", "Could not create new stack for TLS v1.2");
                stack = new HurlStack();
            }
            return Volley.newRequestQueue(context, stack);
        } else {
            return Volley.newRequestQueue(context);
        }
    }
}
