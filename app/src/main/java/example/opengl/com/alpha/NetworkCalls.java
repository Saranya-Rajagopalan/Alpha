package example.opengl.com.alpha;

import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by jprince on 17-Nov-16.
 */

public class NetworkCalls {
    public static void post(NetworkCallback callback, String URL){
        new Post(callback, URL).start();
    }

    public static void get(NetworkCallback callback, String URL){
        new Get(callback, URL).start();
    }
}


class Post extends Thread{

    private final String URL ;
    private final NetworkCallback callback;

    public Post(NetworkCallback callback, String URL){
        this.callback = callback;
        this.URL = URL;
    }

    @Override
    public void run() {
        super.run();

        String remoteUri = Uri.encode(URL, ":/");

        try {

            java.net.URL connectURL = new URL(remoteUri);

            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                Log.e("HTTP", "Invalid HTTP protocol '{}'");
            }
            InputStream in = conn.getInputStream();
            String encoding = conn.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            String body = IOUtils.toString(in, encoding);

            int serverResponseCode = conn.getResponseCode();
            boolean success = serverResponseCode == 200;

            conn.disconnect();

            if(success){
                callback.success(body);
            }else {
                Log.e("HTTP", "Failed");
                callback.failure();
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP", "MalformedURLException");
        } catch (IOException ioe) {
            Log.e("HTTP", "IOException");
        }
    }
}

class Get extends Thread {

    private final String URL;
    private final NetworkCallback callback;

    public Get(NetworkCallback callback, String URL) {
        this.callback = callback;
        this.URL = URL;
    }

    @Override
    public void run() {
        super.run();

        String remoteUri = Uri.encode(URL, ":/");

        try {

            java.net.URL connectURL = new URL(remoteUri);

            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                Log.e("HTTP", "Invalid HTTP protocol '{}'");
            }
            InputStream in = conn.getInputStream();
            String encoding = conn.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            String body = IOUtils.toString(in, encoding);

            int serverResponseCode = conn.getResponseCode();
            boolean success = serverResponseCode == 200;

            conn.disconnect();

            if (success) {
                callback.success(body);
                Log.e("HTTP", "Failed");
            } else {
                callback.failure();
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP", "MalformedURLException");
        } catch (IOException ioe) {
            Log.e("HTTP", "IOException");
        }

    }
}