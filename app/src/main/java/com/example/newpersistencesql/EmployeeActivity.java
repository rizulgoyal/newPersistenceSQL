package com.example.newpersistencesql;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmployeeActivity extends AppCompatActivity {

    SQLiteDatabase mDatabase;
    List<Employee> employeeList;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_employee );

        listView = findViewById(R.id.lvemployees );
        employeeList = new ArrayList<>(  );
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null );
        loadEmployees();
    }

    private void loadEmployees() {
        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery( sql, null );
        if(cursor.moveToFirst())
        {
            do{
                employeeList.add( new Employee(
                        cursor.getInt( 0 ),
                        cursor.getString( 1),
                        cursor.getString(  2),
                        cursor.getString( 3 ),
                        cursor.getDouble( 4 )
                ) );

            }while (cursor.moveToNext());
            cursor.close();

            //show items in a list view


        }
    }


}
