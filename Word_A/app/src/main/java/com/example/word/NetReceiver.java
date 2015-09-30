package com.example.word;

import android.media.MediaActionSound;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;

import com.example.word.WordData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by dahyeon on 2015. 9. 15..
 */
public class NetReceiver extends AsyncTask<String, Void, String> {

    String check;
    String index;

    OnReceive recvObj;

    public NetReceiver(OnReceive recvObj) {
        this.recvObj = recvObj;
    }

//    public static byte[] getReceive(String url) {
//        return null;
//    }

    @Override
    protected String doInBackground(String[] params) {

        String word = params[0];
//        this.check = params[1];
//        this.index = params[2];
        String result = getDataFromServer(word);

//        WordData wd = new WordData();
//        wd.setTitle(word);
//        wd.setMeans(result);
//        wd.setCount(1);
//        wd.setFlag(true);
//        wd.setTime(System.currentTimeMillis());

        return result;
    }

    @Override
    protected void onPostExecute(String wd) {

        recvObj.onReceive(wd);

//        activity.addToList(wd, check, index);
    }

    public String getDataFromServer(String word) {

        String result = "default";
        String urlString = "http://dic.daum.net/search.do?q=" + word;

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            result = getDataFromInputStream(httpURLConnection.getInputStream());
            Spanned means = Html.fromHtml(result);
            result = means.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public String getDataFromInputStream(InputStream is) throws IOException {

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuffer response = new StringBuffer();
        String line;

        while((line = rd.readLine()) != null) {
            if(line.contains("<div class=\"txt_means_KUEK\">")){
                while(true){
                    response.append(line);
                    line = rd.readLine();
                    if(line.contains("</div>")) break;
                }
                break;
            }
        }
        rd.close();
        return response.toString();
    }

}
