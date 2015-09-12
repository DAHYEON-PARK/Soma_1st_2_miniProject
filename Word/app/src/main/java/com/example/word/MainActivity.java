package com.example.word;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    ListView listView;
    EditText editText;
    Button wordBtn, upBtn, downBtn, countBtn, recentBtn;
    ListAdapter adapter;

    FileInputStream fis;
    FileOutputStream fos;
    String path;

    // default는 10개임.
    ArrayList<WordData> arrayList = new ArrayList<WordData>(10000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.wordEdit);

        wordBtn = (Button) findViewById(R.id.wordBtn);
        upBtn = (Button) findViewById(R.id.upSort);
        downBtn = (Button) findViewById(R.id.downSort);
        countBtn = (Button) findViewById(R.id.countSort);
        recentBtn = (Button) findViewById(R.id.recentSort);
        wordBtn.setOnClickListener(this);
        upBtn.setOnClickListener(this);
        downBtn.setOnClickListener(this);
        countBtn.setOnClickListener(this);
        recentBtn.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.wordList);
        adapter = new ListAdapter(this, arrayList);
        listView.setAdapter(adapter);

        try {
            path = Environment.getExternalStorageDirectory().getPath();
            fis = new FileInputStream(path+"/word.txt");
            readFromFile(fis);
           // adapter.setData(arrayList);
            fis.close();
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromFile(FileInputStream fis){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while((line=br.readLine())!=null){

                String[] split = line.split("/");
                int size = split.length;
                WordData wordData = new WordData();

                switch (size){
                    case 5: wordData.setFlag(Boolean.parseBoolean(split[4]));
                    case 4: wordData.setTime(Long.parseLong(split[3]));
                    case 3: wordData.setCount(Integer.parseInt(split[2]));
                    case 2: wordData.setMeans(split[1]);
                    case 1: wordData.setTitle(split[0]);
                }
                arrayList.add(wordData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            if(fis != null)
//                fis.close();
//            if(fos != null)
//                fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public int checkExist(String word){
        boolean check = false;
        int index = 0;
        for(; index<arrayList.size(); index++){
            String wordTitle = arrayList.get(index).getTitle();
            // \n때문에 equalIgnoreCase로 하면 error가 남.
            if(wordTitle.equalsIgnoreCase(word)) {
                Log.d("####","title: "+wordTitle);
                check = true;
                break;
            }
        }

        if(check) return index;
        else return -1;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.wordBtn :
                String word = editText.getText().toString();
                if (word != null) {
                    Log.d("####",word);
                    int index = checkExist(word);
                    Log.d("####",""+index);

                    // exist and had been found
                    if(index != -1 && arrayList.get(index).getFlag()) {
                        Log.d("####","exist");
                        arrayList.get(index).setTime(System.currentTimeMillis());
                        arrayList.get(index).updateCount();
                        adapter.setData(arrayList);
                        adapter.notifyDataSetChanged();
                    } else {    // word do not exist || exist and do not have means
                        if(index != -1){
                            Log.d("####","exist & not mean");
                            getData(true, word, index);
                        }else {
                            Log.d("####","not exist");
                            getData(false, word, -1);
                        }
                    }
                }
                break;
            case R.id.upSort :
                Collections.sort(arrayList, new upCompare());
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.downSort :
                Collections.sort(arrayList, new downCompare());
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.countSort :
                Collections.sort(arrayList, new countCompare());
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();
                break;
            case R.id.recentSort :
                Collections.sort(arrayList, new recentCompare());
                adapter.setData(arrayList);
                adapter.notifyDataSetChanged();
                break;
            default: break;
        }
    }

    public void getData(boolean exist, String word, int index){
        new NetworkThread().execute(word, String .valueOf(exist), String.valueOf(index));
    }

    public void addToList(WordData wordData, String check, String num) {

        Boolean flag = Boolean.valueOf(check);
        int index = Integer.parseInt(num);

        WordData data = arrayList.get(index);

        data.setMeans(wordData.getMeans());
        data.setFlag(wordData.getFlag());
        data.setTime(wordData.getTime());
        data.setCount(wordData.getCount());
//        adapter.setData(arrayList);

        adapter.notifyDataSetChanged();

        saveFile();

//        String str = wordData.getTitle()+"/"+wordData.getMeans()+"/"+wordData.getCount()+"/"+wordData.getTime()+"/"+wordData.getFlag()+"/";
//        String str = wordData.toString();
//        try {
//            if(flag){
//                arrayList.get(index);
//            }else{
//                fos = openFileOutput(path+"/word.txt", Context.MODE_APPEND);
//                fos.write(str.getBytes());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public boolean saveFile()
    {
        File dirPath = getExternalFilesDir(null);
        String szFileName = "word.txt";
        File file = new File(dirPath, szFileName);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            for(WordData word : arrayList) {
                baos.write(word.toString().getBytes());
            }
            baos.close();

            FileOutputStream fos = new FileOutputStream(szFileName);
            fos.write(baos.toByteArray());
            fos.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private class NetworkThread extends AsyncTask<String, Void, WordData> {

        String check;
        String index;

        @Override
        protected WordData doInBackground(String[] params) {

            String word = params[0];
            this.check = params[1];
            this.index = params[2];
            String result = getDataFromServer(word);

            WordData wd = new WordData();
            wd.setTitle(word);
            wd.setMeans(result);
            wd.setCount(1);
            wd.setFlag(true);
            wd.setTime(System.currentTimeMillis());

            return wd;
        }

        @Override
        protected void onPostExecute(WordData wd) {
            addToList(wd, check, index);
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

    private class upCompare implements Comparator<WordData> {
        @Override
        public int compare(WordData lhs, WordData rhs) {
            if(lhs.getTitle().compareToIgnoreCase(rhs.getTitle()) > 0){
                return -1;
            }else{
                if(lhs.getTitle().compareToIgnoreCase(rhs.getTitle()) < 0 ) // lhs < rhs
                    return 1;
                else
                    return 0;
            }
        }
    }

    private class downCompare implements Comparator<WordData> {
        @Override
        public int compare(WordData lhs, WordData rhs) {
            if(lhs.getTitle().compareToIgnoreCase(rhs.getTitle()) < 0)
                return -1;
            else{
                if(lhs.getTitle().compareToIgnoreCase(rhs.getTitle()) > 0 ) // lhs < rhs
                    return 1;
                else
                    return 0;
            }
        }
    }

    private class recentCompare implements Comparator<WordData> {
        @Override
        public int compare(WordData lhs, WordData rhs) {
            return lhs.getTime() > rhs.getTime() ? -1
                    : lhs.getTime() < rhs.getTime() ? 1 : 0 ;
        }
    }

    private class countCompare implements Comparator<WordData> {
        @Override
        public int compare(WordData lhs, WordData rhs) {
            return lhs.getCount() > rhs.getCount() ? -1
                    : lhs.getCount() < rhs.getCount() ? 1 : 0 ;
        }
    }
}
