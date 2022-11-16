package com.example.appquanlysinhvien;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewLop extends AppCompatActivity {

    SQLiteDatabase database;
    ListView listclass;
    ArrayList<String> strings;
    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_lop);
        database = openOrCreateDatabase("quanlysinhvien.db",MODE_PRIVATE,null);
        if (!isTableExists(database,"sinhvien")){
            doCreateDB(database);
        }
        Cursor cursor = database.rawQuery("select DISTINCT lopsv from sinhvien", null);
        cursor.moveToFirst();
        strings  = new ArrayList<>();
        while(cursor.isAfterLast()==false)
        {
            strings.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        listclass= findViewById(R.id.listsinhvien);
        adapter = new ArrayAdapter(
                ListViewLop.this,
                android.R.layout.simple_list_item_1,
                strings);
        listclass.setAdapter(adapter);

        listclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListViewLop.this,ListViewSinhVien.class);
                intent.putExtra("class",strings.get(i));
                getResultClass.launch(intent);
            }
        });

    }

    public boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name " +
                "from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public void doCreateDB(SQLiteDatabase data){
        String init_table = "create table sinhvien (" +
                "masv varchar(50) primary key," +
                "tensv text," +
                "lopsv varchar(50)," +
                "dtoan float," +
                "dtin float," +
                "dtienganh float)";

        data.execSQL(init_table);

        addRowSinhVien("2050531200123","Lê Đức Duy","20T1",5,7,9);
        addRowSinhVien("2050531200157","Đinh Văn Huy","20T1",9,7,7);
        addRowSinhVien("2050531200131","Nguyễn Trí Đức","20T1",7,8,8);

        addRowSinhVien("2050531200101","Lê Anh Quốc","20T2",5,7,9);
        addRowSinhVien("2050531200102","Hồ Quang Phúc","20T2",7,7,5);
        addRowSinhVien("2050531200103","Trương Bảo Phúc","20T2",9,4,5);

        addRowSinhVien("2050531200104","Thư Trang","20T3",5,7,9);
        addRowSinhVien("2050531200105","Nguyễn Văn A","20T3",1,5,2);
        addRowSinhVien("2050531200106","Trần Văn B","20T3",5,6,7);
    }

    public void addRowSinhVien(String masv, String tensv, String lopsv, float dToan, float dTin, float dTiengAnh){
        ContentValues sv = new ContentValues();
        sv.put("masv",masv);
        sv.put("tensv",tensv);
        sv.put("lopsv",lopsv);
        sv.put("dtoan",dToan);
        sv.put("dtin",dTin);
        sv.put("dtienganh",dTiengAnh);
        if (database.insert("sinhvien",null,sv) == -1) {
            Toast.makeText(this, "Fail to insert record", Toast.LENGTH_SHORT).show();
        }
    }

    private ActivityResultLauncher<Intent> getResultClass =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){

                        Cursor cursor = database.rawQuery("select DISTINCT lopsv from sinhvien", null);
                        cursor.moveToFirst();
                        strings  = new ArrayList<>();
                        while(cursor.isAfterLast()==false)
                        {
                            strings.add(cursor.getString(0));
                            cursor.moveToNext();
                        }
                        cursor.close();
                        adapter = new ArrayAdapter(
                                ListViewLop.this,
                                android.R.layout.simple_list_item_1,
                                strings);
                        listclass.setAdapter(adapter);
                    }
                }
            }
    );
}