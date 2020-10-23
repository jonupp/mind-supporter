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
import android.widget.Scroller;
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

    private final ToDo currentToDo = new ToDo("", null, 0, 0, false, "../../res/drawable/image_placeholder.xml", "");

    String timeUnit;
    int deadlineYear, deadlineMonth, deadlineDay, deadlineHour = 10, deadlineMinute;
    Uri imageUri = Uri.parse("../../res/drawable/image_placeholder.xml");

    private void reactionToast(String note) {
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
                reactionToast("Something went wrong");
            }

        } else {
            reactionToast("You haven't picked an Image");
        }
    }

    private void setCurrentToDoInputs(){
        //set Title
        EditText etTitle = findViewById(R.id.new_todo_edittext_title);
        etTitle.setText(currentToDo.getTitle());
        //set deadlineDate

        /*
        if(currentToDo.getDueDateTime().isEqual(null)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String str = currentToDo.getDueDateTime().format(formatter);
            reactionToast(currentToDo.getDueDateTime().format(formatter));

            String dueDateTime = (String) currentToDo.getDueDateTime().toString();
            String[] date = dueDateTime.split(" ", 2);
            reactionToast(date[0] + "\n" + date[1]);
            Button dateBtn = findViewById(R.id.new_todo_button_deadline_date);
            dateBtn.setText(date[0]);
            Button timeBtn = findViewById(R.id.new_todo_button_deadline_time);
            timeBtn.setText(date[1]);
        }*/

        //set Duration
        NumberPicker np = findViewById(R.id.new_todo_numberpicker_time_expenditure);
        np.setValue(currentToDo.getDurationMinutes());
        np.setEnabled(true);
        //set Priority
        RatingBar ratingBar = findViewById(R.id.new_todo_ratingbar_priority);
        ratingBar.setNumStars(currentToDo.getPriority());
        //set Note
        EditText etNote = findViewById(R.id.new_todo_edittext_note);
        etNote.setText(currentToDo.getNote());
        //set Status
        CheckBox cb = findViewById(R.id.new_todo_checkbox_status);
        cb.setActivated(currentToDo.isFinished());
        //set Image
        ImageView image = findViewById(R.id.floating_action_button_pick_picture);
        image.setImageURI(Uri.parse(currentToDo.getImage()));
    }

    private void saveToDo() throws IOException {
        //get Title
        EditText et = findViewById(R.id.new_todo_edittext_title);
        currentToDo.setTitle(et.getText().toString());
        /*if (currentToDo.getTitle().isEmpty()) {
            reactionToast("Title is not defined");
            return;
        }

        if (deadlineYear == 0 || deadlineMonth == 0 || deadlineDay == 0) {
            reactionToast("Deadline Date is not defined");
            return;
        }*/
        //get Priority
        RatingBar ratingBar = findViewById(R.id.new_todo_ratingbar_priority);
        currentToDo.setPriority(ratingBar.getNumStars());
        //get Note
        EditText editTextNote = findViewById(R.id.new_todo_edittext_note);
        currentToDo.setNote(editTextNote.getText().toString());
        switch (timeUnit) {
            case "hours":
                currentToDo.setDurationMinutes(currentToDo.getDurationMinutes() * 60);
                break;
            case "days":
                currentToDo.setDurationMinutes(currentToDo.getDurationMinutes() * 60 * 24);
                break;
        }
        //get dueDateTime
        currentToDo.setDueDateTime(createDateTime());
        //save & return
        ToDoStorage.addToToDoArrayList(currentToDo);
        showAllToDos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        //for editing existing ToDoObj
        Bundle bundle = getIntent().getExtras();
        currentToDo.overrideWithOtherToDo((ToDo) bundle.getSerializable("todo"));
        imageUri = Uri.parse(currentToDo.getImage());
        setCurrentToDoInputs();


        FloatingActionButton abortBtn = findViewById(R.id.floating_action_button_abort);
        abortBtn.setOnClickListener(v -> showAllToDos());

        //get Image
        final FloatingActionButton pickPicture = findViewById(R.id.floating_action_button_pick_picture);
        pickPicture.setOnClickListener(v -> getImageFromLibrary());
        ImageView imageView = findViewById(R.id.new_todo_image);
        imageView.setImageURI(imageUri);

        //save inputs as newToDo
        FloatingActionButton saveToDo = findViewById(R.id.floating_action_button_transact);
        saveToDo.setOnClickListener(v -> {
            try {
                saveToDo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //get Deadline
        final Context context = this;
        Calendar calendar = Calendar.getInstance();
        final Button pickTimeButton = findViewById(R.id.new_todo_button_deadline_time);
        pickTimeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
                deadlineHour = hourOfDay;
                deadlineMinute = minute1;
                pickTimeButton.setText(hourOfDay + ":" + minute1);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(context));
            timePickerDialog.show();
        });
        final Button pickDateButton = findViewById(R.id.new_todo_button_deadline_date);
        pickDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
                deadlineYear = year1;
                deadlineMonth = ++month1;
                deadlineDay = dayOfMonth;
                pickDateButton.setText(dayOfMonth + "." + month1 + "." + year1);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        //get Expenditure
        NumberPicker numberPicker = findViewById(R.id.new_todo_numberpicker_time_expenditure);
        if (numberPicker != null) {
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(100);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> currentToDo.setDurationMinutes(newVal));
        }
        Spinner timeSpinner = findViewById(R.id.new_todo_spinner_timeunits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeUnit = timeSpinner.getSelectedItem().toString();

        //get Status
        CheckBox checkBox = findViewById(R.id.new_todo_checkbox_status);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> currentToDo.setFinished(isChecked));


    }
}