package com.litao.ttweather.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ttweaher.db";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号
    private static final String CREATE_USER_TABLE = "create table User("
            + "u_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + "u_name Varchar(11), "
            + "password Varchar(11), "
            + "photo_path Varchar(10))";//数据库里的表;
    private static final String CREATE_CITY_TABLE = "create table City("
            + "c_name Varchar(2) PRIMARY KEY UNIQUE,"+ "c_province Varchar(2)"+ ")";
//            "
//            + "c_lastest_refresh Varchar(19))";
    private static final String CREATE_USER_CITY_TABLE = "create table User_City("
            + "c_name Varchar(2) NOT NULL,"
            + "u_id INTEGER NOT NULL,"
            + "FOREIGN KEY (c_name) REFERENCES City(c_name)  on delete cascade,"
            + "FOREIGN KEY (u_id) REFERENCES User(u_id)  on delete cascade,"
            + "PRIMARY KEY(c_name,u_id) )";
    private static final String CREATE_WEATHER_FUTURE_TABLE = "create table Weather_future("
            + "Wf_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            +"day varchar(2),"
            + "weather varchar(4),"
            + "Temperature_range Varchar(6),"
            + "c_name Varchar(2) NOT NULL,"
            + "FOREIGN KEY (c_name) REFERENCES City(c_name) on delete cascade"
            + ")";
    private static final String CREATE_WEATHER_TODAY_TABLE = "create table Weather_today("
            + "weather varchar(4),"
            + "wind_name Varchar(3),"
            + "wind_level Varchar(2),"
            + "humidity Varchar(3),"
            + "Pm25 Int(2) ,"
            + "Air_quality Varchar(1),"
            + "temperature varchar(3),"
            + "c_name Varchar(2) NOT NULL,"
            + "FOREIGN KEY (c_name) REFERENCES City(c_name) on delete cascade,"
            + "PRIMARY KEY(c_name) )";
    private static final String CREATE_COMMUNITY_ITEM_TABLE = "create table Community_item("
            + "Ci_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + "content Varchar(10),"
            + "Publish_time Varchar(15),"
            + "u_id INTEGER NOT NULL,"
            + "FOREIGN KEY (u_id) REFERENCES User(u_id) on delete cascade"
            + ")";
    private static final String CREATE_COMMUNITY_ITEM_IMG_TABLE = "create table Community_item_img("
            + "Cii_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + "Img_path Varchar(50),"
            + "Ci_id INTEGER NOT NULL,"
            + "FOREIGN KEY (Ci_id) REFERENCES Community_item(Ci_id) on delete cascade"
            + ")";
    private static final String CREATE_COMMUNITY_ITEM_COMMENT_TABLE = "create table Community_item_comment("
            + "Cic_id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + "content Varchar(10),"
            + "Publish_time Varchar(15),"
            + "Ci_id INTEGER NOT NULL,"
            + "u_id INTEGER NOT NULL,"
            + "FOREIGN KEY (Ci_id) REFERENCES Community_item(Ci_id) on delete cascade,"
            + "FOREIGN KEY (u_id) REFERENCES User(u_id) on delete cascade"
            + " )";
    private static final String CREATE_COMMUNITY_ITEM_LAUD_TABLE = "create table Community_item_laud("
            + "Ci_id INTEGER NOT NULL,"
            + "u_id INTEGER NOT NULL,"
            + "FOREIGN KEY (Ci_id) REFERENCES Community_item(Ci_id) on delete cascade,"
            + "FOREIGN KEY (u_id) REFERENCES User(u_id) on delete cascade,"
            + "PRIMARY KEY(Ci_id,u_id))";
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CITY_TABLE);
        db.execSQL(CREATE_USER_CITY_TABLE);
        //db.execSQL(CREATE_WEATHER_FUTURE_TABLE);
       // db.execSQL(CREATE_WEATHER_TODAY_TABLE);
        db.execSQL(CREATE_COMMUNITY_ITEM_TABLE);
        db.execSQL(CREATE_COMMUNITY_ITEM_IMG_TABLE);
        db.execSQL(CREATE_COMMUNITY_ITEM_COMMENT_TABLE);
        db.execSQL(CREATE_COMMUNITY_ITEM_LAUD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
