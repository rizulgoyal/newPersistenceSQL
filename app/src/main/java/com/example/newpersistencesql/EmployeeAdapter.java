package com.example.newpersistencesql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.aware.PublishDiscoverySession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EmployeeAdapter extends ArrayAdapter {

    Context mContext;
    int layoutRes;
    List<Employee> employees;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(@NonNull Context context, int resource, List<Employee> employees, SQLiteDatabase mDatabase) {
        super( context, resource );
        this.mContext = mContext;
        this.layoutRes = layoutRes;
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
        tvID.setText( employee.getId() );
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

    private void updateemployee(Employee employee) {

        Intent myintent = new Intent(mContext, UpdateEmployeeActivity.class);
      //  myintent.putExtra("empobject", employee);
        //myintent.putExtra("empindex",position);
        mContext.startActivity(myintent);

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

    }
}


