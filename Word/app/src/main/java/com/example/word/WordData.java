package com.example.word;

/**
 * Created by dahyeon on 2015. 9. 11..
 */
public class WordData {

    private String title;
    private String means;
    private int count=0;
    private long time;
    private boolean flag=false;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void updateCount(){
        this.count = this.count+1;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
//        return title + "/" + means + "/" + count + "/" +time + "/" + flag;
        return String.format("%s/%s/%d/%d/%b", title, means, count, time, flag);
    }
}
