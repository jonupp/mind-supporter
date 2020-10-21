package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class NewToDo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    String title, timeUnit, note;
    int deadlineYear, deadlineMonth, deadlineDay, deadlineHour, deadlineMinute, expenditure, priority;
    private boolean status;
    Uri imageUri = Uri.parse("../../res/drawable/image_placeholder.xml");

    private void reaction(String note) {
        Toast.makeText(NewToDo.this, note, Toast.LENGTH_SHORT).show();
    }

    private LocalDateTime createDateTime() {
        String dateTime = deadlineYear + "-";
        if (deadlineMonth < 10) {
            dateTime += "0" + deadlineMonth + "-";
        } else {
            dateTime += deadlineMonth + "-";
        }
        if (deadlineDay < 10) {
            dateTime += "0" + deadlineDay + " ";
        } else {
            dateTime += deadlineDay + " ";
        }
        if (deadlineHour < 10) {
            dateTime += "0" + deadlineHour + ":";
        } else {
            dateTime += deadlineHour + ":";
        }
        if (deadlineDay < 10) {
            dateTime += "0" + deadlineMinute;
        } else {
            dateTime += deadlineMinute;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(dateTime, formatter);
    }

    private void showAllToDos() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void getImageFromLibrary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                imageUri = data.getData();
                getContentResolver().openInputStream(imageUri);
                ImageView imageView = findViewById(R.id.new_todo_image);
                imageView.setImageURI(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToDo() throws IOException {
        //get Title
        EditText et = findViewById(R.id.new_todo_edittext_title);
        title = et.getText().toString();
        if (title.isEmpty()) {
            reaction("Title is not defined");
            return;
        }
        if (deadlineYear == 0 || deadlineMonth == 0 || deadlineDay == 0) {
            reaction("Deadline Date is not defined");
            return;
        }
        //get Priority
        RatingBar ratingBar = findViewById(R.id.new_todo_ratingbar_priority);
        priority = ratingBar.getNumStars();
        //get Note
        EditText editTextNote = findViewById(R.id.new_todo_edittext_note);
        note = editTextNote.getText().toString();
        switch (timeUnit) {
            case "hours":
                expenditure *= 60;
                break;
            case "days":
                expenditure *= 60 * 24;
                break;
        }
        ToDoStorage.addToToDoArrayList(title, createDateTime(), expenditure, priority, status, imageUri, note);
        ToDoStorage.persist();
        showAllToDos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);


        FloatingActionButton abortBtn = findViewById(R.id.floating_action_button_abort);
        abortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllToDos();
            }
        });

        //get Image
        final FloatingActionButton pickPicture = findViewById(R.id.floating_action_button_pick_picture);
        pickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromLibrary();
            }
        });
        //save inputs as newToDo
        FloatingActionButton saveToDo = findViewById(R.id.floating_action_button_transact);
        saveToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveToDo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //get Deadline
        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final Button pickTimeButton = findViewById(R.id.new_todo_button_deadline_time);
        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        deadlineHour = hourOfDay;
                        deadlineMinute = minute;
                        pickTimeButton.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(context));
                timePickerDialog.show();
            }
        });
        final Button pickDateButton = findViewById(R.id.new_todo_button_deadline_date);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        deadlineYear = year;
                        deadlineMonth = month;
                        deadlineDay = dayOfMonth;
                        pickDateButton.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //get Expenditure
        NumberPicker numberPicker = findViewById(R.id.new_todo_numberpicker_time_expenditure);
        if (numberPicker != null) {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    expenditure = newVal;
                }
            });
        }
        Spinner timeSpinner = findViewById(R.id.new_todo_spinner_timeunits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeUnit = timeSpinner.getSelectedItem().toString();

        //get Status
        CheckBox checkBox = findViewById(R.id.new_todo_checkbox_status);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                status = isChecked;
            }
        });


    }
}