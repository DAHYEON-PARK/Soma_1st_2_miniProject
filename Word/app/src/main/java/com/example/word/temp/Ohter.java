package com.example.word.temp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dahyeon on 2015. 9. 11..
 */
public class Ohter {

    // read는 read I/O를 줄이는 것이 관건.
    FileInputStream fis;
    InputStream is;
    BufferedReader br;
    StringBuilder sb;

    // 자동으로 json으로 만듦.
    // json이면 자료구조가 필요 없어짐. - search에서도
    JSONObject jsonObject = new JSONObject(sb.toString());


    ///
    File f;
    FileInputStream fs;

    ByteArrayOutputStream baos;  // 얘도 필요 없을지도... 바로 byte로 넣기.
    // file 사이즈를 모르면 가져와서 넣어줌.

    int nRead = FileInputStream.read(byte, offset, size); // size를 알면 3개??
    //파라미터가 1개면 image buffer라는 이야기.

    //os마다 읽어오는 사이즈가 다를 수 있음, 혹은 병목현상 생길 때 nRead를 다르게 return 시킬 수 있음.

    //파일 데이터의 길이가 가변적이라면 ByteArrayOutputStream 으로 읽어오는게 나음.
    //또한, network에서 읽어올 때는 chunked data때문에(알 수 없는 크기 - header크기 잘못하면) ByteArrayOutputStream 으로 읽기기


    ByteArrayInpuStream bais = new ByteArrayInputStream(byte[]);
    InputStreamReader isr;
    BufferedReader = new BufferedReader(isr);
    String line;
    while(line = br.readLine()){
        //arraList에 넣기
    }


    // 보조 스트림 사용할 때 flush()해주면 자동으로 써짐. 따라서 꼭 사용하는게 좋음
    // in/out은 꼭 close()해주기.
}
