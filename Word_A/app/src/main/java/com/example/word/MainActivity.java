package com.example.word;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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

    public void getData(final boolean exist, final String word, final int index){
        new NetReceiver(new OnReceive() {
            @Override
            public int onReceive(String data) {

                WordData wd = new WordData();
                wd.setTitle(word);
                wd.setMeans(data);
                wd.setCount(1);
                wd.setFlag(true);
                wd.setTime(System.currentTimeMillis());

                addToList(wd, String.valueOf(exist), String.valueOf(index));

                return 0;
            }
        }).execute(word, String .valueOf(exist), String.valueOf(index));
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
