package com.umutsoysal.ajandam.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class Sqllite extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION =1;

    // Database Name
    private static final String DATABASE_NAME = "Ajandam";

    private static final String TABLE_NAME = "akademsiyen";
    private static String PERSON_JSON = "json";

    private static final String USER = "user";
    private static String USERNAME = "username";
    private static String PASSWORD = "password";

    private static final String TABLE_NAME_2 = "ogrenci";
    private static String OGRENCI_JSON = "json";

    private static final String ALARM = "alarm";
    private static String LESSON = "ders";
    private static String CLOCK = "saat";
    private static String CLASS = "sinif";
    private static String DAY = "gun";


    public Sqllite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + PERSON_JSON + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        String tablo = "CREATE TABLE " + USER + "( "
                + USERNAME + " TEXT ,"
                + PASSWORD + " TEXT" + ")";
        db.execSQL(tablo);


        String tablom = "CREATE TABLE " + TABLE_NAME_2 + "( "
                + OGRENCI_JSON + " TEXT" + ")";
        db.execSQL(tablom);


        String notify = "CREATE TABLE " + ALARM + "( "
                + LESSON + " TEXT ,"
                + CLOCK + " TEXT ,"
                + CLASS + " TEXT ,"
                + DAY + " TEXT" + ")";
        db.execSQL(notify);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void alarmEkle(String dersAdi,String dersSaati,String sinif,String dersGunu) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LESSON,dersAdi);
        values.put(CLOCK,dersSaati);
        values.put(CLASS,sinif);
        values.put(DAY,dersGunu);

        db.insert(ALARM, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public void userEkle(String username,String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME,username);
        values.put(PASSWORD,password);

        db.insert(USER, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public void ogrenciEkle(String json) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OGRENCI_JSON,json);

        db.insert(TABLE_NAME_2, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public void akademisyenEkle(String json) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PERSON_JSON,json);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public void AlarmSil(String dersAdi){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALARM, LESSON + " = ?",
                new String[] { dersAdi });
        db.close();
    }


    public ArrayList<HashMap<String, String>> alarmGET(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ALARM;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> islemlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                islemlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return islem liste
        return islemlist;
    }




    public ArrayList<HashMap<String, String>> getOgrenci(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_2;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> islemlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                islemlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return islem liste
        return islemlist;
    }


    public ArrayList<HashMap<String, String>> getUSERINFO(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + USER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> islemlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                islemlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return islem liste
        return islemlist;
    }



    public ArrayList<HashMap<String, String>> getAkademisyen(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> islemlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                islemlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return islem liste
        return islemlist;
    }

    public void resetOgrenci(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_2, null, null);
        db.close();
    }

    public void resetUSER(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER, null, null);
        db.close();
    }

    public void resetAkademisyen(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public void resetALARM(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ALARM, null, null);
        db.close();
    }

}
