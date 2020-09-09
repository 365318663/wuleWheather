package com.litao.ttweather.entity;

import com.litao.ttweather.alximageloader.SelectPhotoAdapter;

import java.util.ArrayList;

public class CommunityItem {
    private String header_path;
    private String name;
    private String content;
    public boolean isLaud() {
        return isLaud;
    }
    public void setLaud(boolean laud) {
        isLaud = laud;
    }
    private boolean isLaud;
    private int laud_num;
    private int Comment_Num;
    private long id;
    private String pubulish_time;

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    private boolean canDelete;

    public String getPubulish_time() {
        return pubulish_time;
    }

    public void setPubulish_time(String pubulish_time) {
        this.pubulish_time = pubulish_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHeader_path() {
        return header_path;
    }

    public void setHeader_path(String header_path) {
        this.header_path = header_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLaud_num() {
        return laud_num;
    }

    public void setLaud_num(int laud_num) {
        this.laud_num = laud_num;
    }

    public ArrayList<SelectPhotoAdapter.SelectPhotoEntity> getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photo_path) {
        this.photo_path = photo_path;
    }

    private ArrayList<SelectPhotoAdapter.SelectPhotoEntity> photo_path;


    public int getComment_Num() {
        return Comment_Num;
    }

    public void setComment_Num(int comment_Num) {
        Comment_Num = comment_Num;
    }
}
