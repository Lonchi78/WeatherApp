package Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andre on 3.6.2018.
 */

public class Helper {
    static String stream = null;

    public Helper(){

    }

    //  Makes a request to API and get return result
    public static String getHTTTPData( String urlString ){
        try {
            URL mUrl = new URL( urlString );
            HttpURLConnection mHttpURLConnection = (HttpURLConnection)mUrl.openConnection();
            if( mHttpURLConnection.getResponseCode() == 200 ){
                BufferedReader r = new BufferedReader( new InputStreamReader( mHttpURLConnection.getInputStream() ));
                StringBuilder sb = new StringBuilder();
                String line;
                while( ( line = r.readLine() ) != null ){
                    sb.append( line );
                }
                stream = sb.toString();
                mHttpURLConnection.disconnect();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream;
    }
}
