package com.mygdx.data.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.Array;

public class DatabaseTest {

    Database dbHandler;

    public static final String TABLE = "goods";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATA = "data";

    private static final String DATABASE_NAME = "good.db";
    private static final int DATABASE_VERSION = 1;

    private Array<String> columnsNames;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists "
            + TABLE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_DATA
            + " text not null);";

    public DatabaseTest() {
        Gdx.app.log("DatabaseTest", "creation started");
        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME,
                DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        Gdx.app.log("DatabaseTest", "created successfully");

        columnsNames = getColumns();
    }

    public void insertData (String s, String a) {
        try {
            dbHandler
                    .execSQL("INSERT INTO " + TABLE +" ('" + COLUMN_NAME + "' , '" + COLUMN_DATA + "' " + ") VALUES ('" + s +"' , '" + a + "' )");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }

    public void closeDB(){
        try {
            dbHandler.closeDatabase();
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        dbHandler = null;
        Gdx.app.log("DatabaseTest", "dispose");
    }

    public Array<Array> getData(String s) {
        Array<Array> ans = new Array<>();

        DatabaseCursor cursor = null;

        try {
            cursor = dbHandler.rawQuery("SELECT * FROM " + TABLE + " WHERE name LIKE '%" + s + "%'");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            Array<String> array = new Array<>();
            for (int i = 0; i < columnsNames.size; i ++) {
                array.add(cursor.getString(i));
            }
            ans.add(array);
        }


        return ans;
    }

    public Array<String> getColumns() {
        Array<String> ans = new Array<>();

        DatabaseCursor cursor = null;

        try {
            cursor = dbHandler.rawQuery("PRAGMA table_info(goods);");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        while (cursor.next()) {
            System.out.println(String.valueOf(cursor.getString(1)));
            ans.add(String.valueOf(cursor.getString(1)));
        }


        return ans;
    }
}

