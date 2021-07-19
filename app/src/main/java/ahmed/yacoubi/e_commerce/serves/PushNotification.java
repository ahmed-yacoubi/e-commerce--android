package ahmed.yacoubi.e_commerce.serves;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PushNotification {

    final static private String keyServiceId = "AAAASDZZjv8:APA91bFAlZSR1d1L64_0FPy_hpJ6g_el76Mp87VYB7tM9b7eeVQwpQbQhRrvz0Z0eS4Lw9b93DVaitITY7f9mPVyRLfu7FcQl-tW2I79dc-op9fKWjOYx0xw0-nQcnMAolIgASe9NFTB";
                                                //

    public static void sendNotification(final String token, final String title, final String body) {
         new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d("eeeee", "run: "+token);
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setUseCaches(false);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key=" + keyServiceId);
                    conn.setRequestProperty("Content-Type", "application/json");

                    JSONObject json = new JSONObject();

                    json.put("to", token);

                    JSONObject info = new JSONObject();
                    info.put("title", title);   // Notification title
                    info.put("body", body); // Notification body

                    json.put("notification", info);

                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(json.toString());
                    wr.flush();
                    conn.getInputStream();

                } catch (Exception e) {
                    Log.d("Error", "" + e);
                }


            }
        }).start();

    }


}