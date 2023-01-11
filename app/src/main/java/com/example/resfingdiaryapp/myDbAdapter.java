package com.example.resfingdiaryapp;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class myDbAdapter {

    myDbHelper myhelper;
    public myDbAdapter(Context context)
    {
        myhelper = new myDbHelper(context);
        Log.w("this","created adapter in constructor");
    }

    public long insertData(String title,int fromPage, int toPage, String dateTime, String readerComment,String supportComment){
        Log.w("this","In insert data");
        SQLiteDatabase dbb = myhelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Title,title);
        contentValues.put(myDbHelper.FromPage,fromPage);
        contentValues.put(myDbHelper.ToPage,toPage);
        contentValues.put(myDbHelper.DateTime,dateTime);
        contentValues.put(myDbHelper.ReaderComment,readerComment);
        contentValues.put(myDbHelper.SupportComment,supportComment);
        long id = dbb.insert(myDbHelper.TABLE_NAME,null, contentValues);
        return id;
    }

    public String getData(){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.Title,myDbHelper.FromPage,myDbHelper.ToPage,myDbHelper.DateTime,myDbHelper.ReaderComment,myDbHelper.SupportComment};
       Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
         StringBuffer buffer = new StringBuffer();
         //Log.w("this",cursor.getString());
        while (cursor.moveToNext()){
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.UID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.Title));
            String fromPage = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.FromPage));
            String toPage = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.ToPage));
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DateTime));
            String readerComment = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.ReaderComment));
            String supportComment = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.SupportComment));
            buffer.append(cid+" "+title+" "+fromPage+" "+toPage+" "+dateTime+" "+readerComment+" "+supportComment+" \n");
        }
        return buffer.toString();
    }
    public String getDataByID(int id){
        SQLiteDatabase db = myhelper.getReadableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.Title,myDbHelper.FromPage,myDbHelper.ToPage,myDbHelper.DateTime,myDbHelper.ReaderComment,myDbHelper.SupportComment};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,myDbHelper.UID+"="+id,null,null,null,null);
        String data;
        cursor.moveToNext();
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(myDbHelper.UID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.Title));
            String fromPage = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.FromPage));
            String toPage = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.ToPage));
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.DateTime));
            String readerComment = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.ReaderComment));
            String supportComment = cursor.getString(cursor.getColumnIndexOrThrow(myDbHelper.SupportComment));
            data = cid+"<>"+title+"<>"+fromPage+"<>"+toPage+"<>"+dateTime+"<>"+readerComment+"<> "+supportComment;
        return data;
    }

    public int delete(String idNumber){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {idNumber};
        int count = db.delete(myDbHelper.TABLE_NAME,myDbHelper.UID+" = ?",whereArgs);
        return count;
    }
    public int update(String rowId,String title,int fromPage, int toPage, String dateTime, String readerComment,String supportComment){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs = {rowId};
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.Title,title);
        contentValues.put(myDbHelper.FromPage,fromPage);
        contentValues.put(myDbHelper.ToPage,toPage);
        contentValues.put(myDbHelper.DateTime,dateTime);
        contentValues.put(myDbHelper.ReaderComment,readerComment);
        contentValues.put(myDbHelper.SupportComment,supportComment);
        int count = db.update(myDbHelper.TABLE_NAME,contentValues,myDbHelper.UID+" = ?",whereArgs);
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "myDatabase";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_NAME = "diaryRecord";

        private static final String UID = "record_id";
        private static final String Title = "title" ;
        private static final String FromPage = "fromPage" ;
        private static final String ToPage = "toPage" ;
        private static final String DateTime = "timestamp" ;
        private static final String ReaderComment = "readerComment" ;
        private static final String SupportComment = "supportComment" ;


        private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME+
                "("
                +UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Title+" VARCHAR(255),"
                +FromPage+ " INTEGER,"
                +ToPage+ " INTEGER,"
                +DateTime + " VARCHAR(255),"
                +SupportComment+ " VARCHAR(255),"
                +ReaderComment+ " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        //Initialize - constructor
        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.w("this","done the super, created the initialize and new database");
            this.context=context;
            //context.deleteDatabase(DATABASE_NAME); /* For resetting Database */
        }




         public void onCreate(SQLiteDatabase db){
            Log.w("this","In the on create");
            try{
                Log.w("this","TABLE CREATED+++");
                db.execSQL(CREATE_TABLE);
            }
            catch(Exception e){
                Message.message(context,""+e);
                Log.w("this","TABLE NOT CREATED");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion){
            Log.w("this","iN THE ONUPGRADE");
            try{
                Message.message(context, "onUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }
            catch (Exception e){
                Message.message(context, ""+e);
            }
        }

    }
}
