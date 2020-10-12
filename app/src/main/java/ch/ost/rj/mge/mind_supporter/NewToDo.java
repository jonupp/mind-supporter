package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class NewToDo extends AppCompatActivity {

    private void abortNewToDo(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);

        Spinner spinner = (Spinner) findViewById(R.id.new_todo_spinner_timeunits);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        FloatingActionButton abortBtn = findViewById(R.id.floating_action_button_abort);
        abortBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                abortNewToDo();
            }
        });

        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final int day  = calendar.get(Calendar.DAY_OF_MONTH);
        final int month  = calendar.get(Calendar.MONTH);
        final int year  = calendar.get(Calendar.YEAR);
        final int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute  = calendar.get(Calendar.MINUTE);
        final Button pickTimeButton = (Button) findViewById(R.id.new_todo_Button_deadline_time);
        pickTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        pickTimeButton.setText(hourOfDay + ":" + minute);
                    }
                },hour, minute, android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }
        });
        final Button pickDateButton = (Button) findViewById(R.id.new_todo_Button_deadline_date);
        pickDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        pickDateButton.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });



    }
}