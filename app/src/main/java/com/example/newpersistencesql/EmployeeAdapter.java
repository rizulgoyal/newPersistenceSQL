package com.example.newpersistencesql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.aware.PublishDiscoverySession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter {

    Context mContext;
    int layoutRes;
    List<Employee> employees;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(@NonNull Context context, int resource, List<Employee> employees, SQLiteDatabase mDatabase) {
        super( context, resource, employees);
        this.mContext = context;
        this.layoutRes = resource;
        this.employees = employees;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from( mContext );
        View v = inflater.inflate( layoutRes, null );
        TextView tvName = v.findViewById( R.id.emp_name );
        TextView tvID = v.findViewById( R.id.emp_id );
        TextView tvDept = v.findViewById( R.id.emp_dept );
        TextView tvDate = v.findViewById( R.id.emp_date );
        TextView tvSalary = v.findViewById( R.id.emp_salary );

        final Employee employee = employees.get( position );
        tvName.setText( employee.getName() );
        tvID.setText( String.valueOf(  employee.getId()) );
        tvDate.setText( employee.getJoiningdate() );
        tvDept.setText( employee.getDepartment() );
        tvSalary.setText( String.valueOf(  employee.getSalary()) );

        v.findViewById( R.id.button_edit ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateemployee(employee);
            }
        } );

        v.findViewById( R.id.button_delete ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee(employee);
            }
        } );

        return v;
    }

    private void updateemployee(final Employee employee) {

//        Intent myintent = new Intent(mContext, UpdateEmployeeActivity.class);
//      //  myintent.putExtra("empobject", employee);
//        //myintent.putExtra("empindex",position);
//        mContext.startActivity(myintent);
        AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
       // builder.setTitle( "Edit Employee" );
        LayoutInflater inflater = LayoutInflater.from( mContext );
        View v = inflater.inflate( R.layout.dialog_update_employee,null );
        builder.setView( v );
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText etName = v.findViewById( R.id.editTextName );
        final EditText etSalary = v.findViewById( R.id.editTextSalary );
        final Spinner spinnerDept = v.findViewById( R.id.spinnerDept );
        Button buttonEdit = v.findViewById( R.id.buttonUpdateEmployee );

        etName.setText( employee.getName() );
        etSalary.setText( String.valueOf( employee.getSalary() ) );
        buttonEdit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String salary = etSalary.getText().toString().trim();
                String dept = spinnerDept.getSelectedItem().toString();

                if(name.isEmpty())
                {
                    etName.setError( "NaME IS MANDATORY" );
                    etName.requestFocus();
                    return;
                }
                if(salary.isEmpty())
                {
                    etSalary.setError( "SALARY IS MANDATORY" );
                    etSalary.requestFocus();
                    return;
                }

                String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
                mDatabase.execSQL( sql, new String[]{name, dept, salary, String.valueOf( employee.getId() )} );
                Toast.makeText( mContext, "Employee Updated ", Toast.LENGTH_SHORT ).show();
                loadEmployees();
                alertDialog.dismiss();



            }
        } );



    }

    private void deleteEmployee(final Employee employee) {

        AlertDialog.Builder builder = new AlertDialog.Builder( mContext );
        builder.setTitle( "Are you sure" );
        builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String sql = "DELETE FROM employees WHERE id=?";
                mDatabase.execSQL( sql, new Integer[]{employee.getId()} );
                loadEmployees();
            }
        } );

        builder.setNegativeButton( "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        } );

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void loadEmployees() {


        String sql = "SELECT * FROM employees";
        Cursor cursor = mDatabase.rawQuery( sql, null );
        if (cursor.moveToFirst()) {
            employees.clear();
            do {
                employees.add( new Employee(
                        cursor.getInt( 0 ),
                        cursor.getString( 1 ),
                        cursor.getString( 2 ),
                        cursor.getString( 3 ),
                        cursor.getDouble( 4 )
                ) );

            } while (cursor.moveToNext());
            cursor.close();

           notifyDataSetChanged();


        }
    }
}


