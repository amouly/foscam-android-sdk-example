package com.foscam.ipc.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.foscam.ipc.R;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    static class Helper extends SQLiteOpenHelper {

        protected final static String TAG = "DatabaseHelper";
        private final static String DATABASE_NAME = "DEVICE_LIST";
        private final static int DATABASE_VERSION = 2;
        private Context context;


        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }


        public void onCreate(SQLiteDatabase db) {
            Log.d("moon", "=========create db tbl");
            try {
                String sql = context.getString(R.string.sql_create_device_list_tbl);
                db.execSQL(sql);
                Log.i(TAG, sql);
            } catch (Exception e) {
                // TODO: handle exception
                Log.d("moon", e.getMessage());
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            String sql = context.getString(R.string.drop_sql);
            db.execSQL(sql);
            this.onCreate(db);

        }

    }

    private Context context;
    protected SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        this.context = context;
        db = new Helper(context).getWritableDatabase();
    }


    public static IPCameraInfo query(Context context, int id) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getWritableDatabase();
            String whereClause = " _id = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            String[] columns = new String[]{
                    "devType", "devName", "ip", "streamType", "webPort", "mediaPort", "uid", "userName", "password"
            };

            Cursor cursor = db.query("tb_device_list", columns, whereClause, whereArgs, null, null, null);
            if (cursor.getCount() == 0) {
                throw new Exception("û���ҵ�ID" + id + "������");
            }
            cursor.moveToFirst();
            IPCameraInfo param = new IPCameraInfo();
            param.devType = cursor.getInt(0);
            param.devName = cursor.getString(1);
            param.ip = cursor.getString(2);
            param.streamType = cursor.getInt(3);
            param.webport = cursor.getInt(4);
            param.mediaport = cursor.getInt(5);
            param.uid = cursor.getString(6);
            param.userName = cursor.getString(7);
            param.password = cursor.getString(8);
            cursor.close();
            return param;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public static long testInsert(Context context) {

        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getWritableDatabase();
            ContentValues values = new ContentValues();
            //		values.put("_id", -1);
            values.put("ip", "192.168.1.103");
            values.put("username", "adnim");
            values.put("password", "");
            values.put("streamType", 0);
            values.put("webPort", 88);
            values.put("mediaPort", 888);
            long num = db.insert("tb_device_list", null, values);
            return num;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    public static long insert(Context context, String table, ContentValues values) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getWritableDatabase();
            long num = db.insert(table, null, values);
            return num;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public static long update(Context context, String table, ContentValues values, int id) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getWritableDatabase();
            String whereClause = " _id = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            long num = db.update(table, values, whereClause, whereArgs);
            return num;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


    public static void testDelete(Context context) {
        SQLiteDatabase db = null;
        try {
            Helper helper = new Helper(context);
            db = helper.getWritableDatabase();
            db.execSQL("delete from tb_device_list;");
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    public static void delete(Context context, int id) throws Exception {
        SQLiteDatabase db = null;
        Log.d("remote", "delete  id =" + id);
        try {
            Helper helper = new Helper(context);
            db = helper.getWritableDatabase();
            String table = "tb_device_list";
            String whereClause = " _id = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            Log.d("remote", "delete  whereClause=" + whereClause);
            Log.d("remote", "delete  whereArgs=" + whereArgs);
            db.delete(table, whereClause, whereArgs);
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public static void deleteAll(Context context) throws Exception {
        SQLiteDatabase db = null;
        try {
            Helper helper = new Helper(context);
            db = helper.getWritableDatabase();
            db.execSQL("DELETE FROM tb_device_list");
            Log.d("moon", "Delete all record in database");
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public static void drop(Context context) {
        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getWritableDatabase();
            String sql = context.getString(R.string.drop_sql);
            db.execSQL(sql);
        } finally {
            if (db != null) {
                db.close();
            }
        }

    }

    public static int getCount(Context context, String table) throws Exception {
        SQLiteDatabase db = null;
        int cnt = 0;
        try {
            db = new Helper(context).getReadableDatabase();
            Cursor cur = db.query(table, new String[]{"_id", "ip"}, null, null, null, null, null);
            cnt = cur.getCount();
            cur.close();
            return cnt;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Cursor loadAllName() throws Exception {
        try {
            Cursor cur = db.query("tb_device_list", new String[]{"_id", "devName", "ip", "uid"}, null, null, null, null, "_id DESC");
            return cur;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        }
    }

    public static Cursor QueryDevice(Context context, String ip, int webPort) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = new Helper(context).getReadableDatabase();
            String sql = "SELECT * FROM tb_device_list WHERE ip=" + "'" + ip + "'" + " AND " + "webPort=" + webPort;
            Log.d("moon", "Exec sql:" + sql);
            Cursor cur = db.rawQuery(sql, null);
            return cur;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        }
    }

    public static Cursor QueryDevice(Context context, String uid) throws Exception {
        try {
            SQLiteDatabase db = null;
            db = new Helper(context).getReadableDatabase();
            String sql = "SELECT * FROM tb_device_list WHERE uid=" + "'" + uid + "'";
            Cursor cur = db.rawQuery(sql, null);
            return cur;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        }
    }


    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }


    public static List<String> loadName(Context context) throws Exception {
        SQLiteDatabase db = null;
        List<String> rst = new ArrayList<String>();
        try {
            db = new Helper(context).getReadableDatabase();
            Cursor cur = db.query("tb_device_list", new String[]{"_id", "ip"}, null, null, null, null, null);
            cur.moveToFirst();
            for (int i = 0; i < cur.getCount(); i++) {
                String s = cur.getString(1);
                rst.add(s);
                cur.moveToNext();
            }
            cur.close();
            return rst;
        } catch (Exception e) {
            throw e;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Cursor query(int id) throws Exception {
        try {
            String whereClause = " _id = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            Cursor cur = db.query("tb_device_list", new String[]{"devType", "devName", "ip", "streamType", "webPort", "mediaPort", "uid", "userName", "password"}, whereClause, whereArgs, null, null, "_id DESC");
            return cur;
        } catch (Exception e) {
            Log.d("moon", e.getMessage());
            throw e;
        }
    }
}
