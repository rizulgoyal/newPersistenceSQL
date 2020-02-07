package com.example.newpersistencesql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DATABASE_NAME = "mydatabase";

    SQLiteDatabase mDatabase;

    EditText editTextName, editTextSalary;
    Spinner spinnerDepartment;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        editTextName = findViewById( R.id.editTextName );
        editTextSalary = findViewById( R.id.editTextSalary );
        spinnerDepartment = findViewById( R.id.spinnerDept );



        buttonAdd = findViewById( R.id.buttonAddEmployee );
        buttonAdd.setOnClickListener( this );
        findViewById( R.id.tvViewEmployee ).setOnClickListener( this );

        //database

mDatabase = openOrCreateDatabase( DATABASE_NAME, MODE_PRIVATE, null );
createTable();



    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS employees("+
                "id INTEGER NOT NULL CONSTRAINT employee_pk PRIMARY KEY AUTOINCREMENT, "+
                "name VARCHAR(200) NOT NULL, "+
                "department VARCHAR(200) NOT NULL, "+
                "joiningdate DATETIME NOT NULL, "+
                "salary DOUBLE NOT NULL);";
    mDatabase.execSQL( sql );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonAddEmployee:
                addEmployee();

                break;
            case R.id.tvViewEmployee:
                Intent myintent = new Intent(v.getContext(), EmployeeActivity.class);
                //myintent.putExtra("empobject", employee);
                //myintent.putExtra("empindex",position);
                v.getContext().startActivity(myintent);
                //start activity to another activity to see the list of employees
                break;
        }

    }

    private void addEmployee() {

        String name = editTextName.getText().toString().trim();
        String salary = editTextSalary.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();

        //calendar
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "YYY MM DD hh:mm:ss" );
        String joiningDate = simpleDateFormat.format( calendar.getTime() );

        if(name.isEmpty())
        {
            editTextName.setError( "NaME IS MANDATORY" );
            editTextName.requestFocus();
        }
        if(salary.isEmpty())
        {
            editTextSalary.setError( "SALARY IS MANDATORY" );
            editTextSalary.requestFocus();
        }
        String sql = "INSERT INTO employees(name, department, joiningdate, salary)"+
                "VALUES (?,?,?,?)";
        mDatabase.execSQL( sql , new String[]{name, department, joiningDate, salary});

        Toast.makeText( this, "Employee Added", Toast.LENGTH_SHORT ).show();



    }
}
