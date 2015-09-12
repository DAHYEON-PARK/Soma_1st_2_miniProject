package com.example.word.temp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.word.ListAdapter;
import com.example.word.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dahyeon on 2015. 9. 10..
 */
public class Temp {


    public class MainActivity extends ActionBarActivity implements View.OnClickListener {

        ListView listView;
        EditText editText;
        Button button;
        ListAdapter adapter;

        ArrayList<String> arrayList = new ArrayList<String>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editText = (EditText) findViewById(R.id.wordEdit);

            button = (Button) findViewById(R.id.wordBtn);
            button.setOnClickListener(this);

            listView = (ListView) findViewById(R.id.wordList);
            adapter = new ListAdapter(this, null);
            listView.setAdapter(adapter);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.wordBtn) {

                String word = editText.getText().toString();
                Log.d("####", word);

                if (word != null) {
                    new NetworkThread().execute(word);
                }
            }
        }

        public void addToArray(String word) {
            arrayList.add(word);
            //adapter.setData(arrayList);
            //adapter.notifyDataSetChanged();
        }

        private class NetworkThread extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String[] params) {

                String word = params[0];
                String response = getDataFromServer(word);

                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                addToArray(result);
            }

            public String getDataFromServer(String word) {

                String response = "default";
                String urlString = "http://dic.daum.net/search.do?q=" + word;
                Log.d("####", urlString);

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.connect();

                    // inputstream -> bytebufferinputstream

                    InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
                    isr.read();

                    ByteArrayInputStream bs;
                    httpURLConnection.getInputStream().read();

                    response = StreamToString(httpURLConnection.getInputStream());



//                Log.d("##*##", Html.fromHtml(response).toString());
//                JSONArray jsonArray = (JSONArray) new JSONTokener(response).nextValue();
//                Log.d("####", jsonArray.toString());

//                String noHTML = stripHtml(response);
//                JSONObject jsonArray = new JSONObject(noHTML);
//                Log.d("####", jsonArray.toString());

                    Log.d("####", response);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
                }

                return response;
            }

            public String stripHtml(String html)
            {
                return Html.fromHtml(html).toString();
            }

            private String StreamToString(InputStream is) throws IOException {
                String str = "";

                if (is != null) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));

                        while ((line = reader.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        reader.close();
                    } finally {
                        is.close();
                    }
                    str = sb.toString();
                }
                return str;
            }

            private void PullParserFromXML(String html){
                String tag;

                try{
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();

                    parser.setInput(new StringReader(html));
                    int eventType = parser.getEventType();

                    while(eventType != XmlPullParser.END_DOCUMENT){

                        switch (eventType){
                            case XmlPullParser.START_TAG :
                                tag = parser.getName();

                                if("<div class=\"result_fst eng_sch\">".equalsIgnoreCase(tag)){

                                }
                        }

                        eventType = parser.next();
                    }

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void file() throws FileNotFoundException {
                File f = new File("text");
                InputStream in = new FileInputStream(f);
// convert the inpustream to a byte array
                byte[] buf = null;
                try {
                    buf = new byte[in.available()];
                    while (in.read(buf) != -1) {
                    }
                } catch (Exception e) {
                    System.out.println("Got exception while is -> bytearr conversion: " + e);
                }
// now convert it to a bytearrayinputstream
                ByteArrayInputStream bin = new ByteArrayInputStream(buf);
            }
        }
    }

}
