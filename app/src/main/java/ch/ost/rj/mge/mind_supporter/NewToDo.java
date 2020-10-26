package ch.ost.rj.mge.mind_supporter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class NewToDo extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    String timeUnit;
    int deadlineYear, deadlineMonth, deadlineDay, deadlineHour = 10, deadlineMinute;
    Uri imageUri = Uri.parse("../../res/drawable/image_placeholder.xml");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_to_do);
        setSpinner();
        //for editing existing ToDoObj
        ToDo currentToDo;
        Bundle bundle = getIntent().getExtras();
        final boolean isNew = (boolean) bundle.getBoolean("isNewFlag");
        if(!isNew){
            currentToDo = (ToDo) bundle.getSerializable("todo");
            deadlineYear = currentToDo.getDueDateTime().getYear();
            deadlineMonth = currentToDo.getDueDateTime().getMonthValue();
            deadlineDay = currentToDo.getDueDateTime().getDayOfMonth();
            deadlineHour = currentToDo.getDueDateTime().getHour();
            deadlineMinute = currentToDo.getDueDateTime().getMinute();
            getExpenditure(currentToDo);
            setCurrentToDoInputs(currentToDo);
        }else {
            currentToDo = new ToDo("", createDefaultDateTime(), 0, 0, false, "../../res/drawable/image_placeholder.xml", "");
            getExpenditure(currentToDo);
        }
        imageUri = Uri.parse(currentToDo.getImage());

        FloatingActionButton abortBtn = findViewById(R.id.floating_action_button_abort);
        abortBtn.setOnClickListener(v -> {
            if (!isNew){
                try {
                    saveToDo(currentToDo, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                showAllToDos();
            }
        });

        //get Image
        final FloatingActionButton pickPicture = findViewById(R.id.floating_action_button_pick_picture);
        pickPicture.setOnClickListener(v -> getImageFromLibrary());
        ImageView imageView = findViewById(R.id.new_todo_image);
        imageView.setImageURI(imageUri);

        //save inputs as newToDo
        FloatingActionButton saveToDo = findViewById(R.id.floating_action_button_transact);
        saveToDo.setOnClickListener(v -> {
            try {
                saveToDo(currentToDo, isNew);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        getDeadline();
        getStatus(currentToDo);
    }

    private void reactionToast(String note) {
        Toast.makeText(NewToDo.this, note, Toast.LENGTH_SHORT).show();
    }

    private LocalDateTime createDefaultDateTime() {
        String str = "9999-12-31 23:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, formatter);
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
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,"image/*");
        intent.setAction(Intent. ACTION_OPEN_DOCUMENT );
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            reactionToast("You haven't picked an Image");
            return;
        }

        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            if (uri != null) {
                final int takeFlags = data.getFlags()&(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                this.getContentResolver().takePersistableUriPermission(uri, takeFlags);
                ImageView imageView = findViewById(R.id.new_todo_image);
                imageUri = data.getData();
                imageView.setImageURI(uri);
            }else{
                reactionToast("Something went wrong");
            }
        }
    }

    private void setCurrentToDoInputs(ToDo currentToDo){
        //set Title
        EditText etTitle = findViewById(R.id.new_todo_edittext_title);
        etTitle.setText(currentToDo.getTitle());
        //set deadlineDate
        Button dateBtn = findViewById(R.id.new_todo_button_deadline_date);
        dateBtn.setText(deadlineDay+"."+deadlineMonth+"."+deadlineYear);
        Button timeBtn = findViewById(R.id.new_todo_button_deadline_time);
        timeBtn.setText(deadlineHour+":"+deadlineMinute);
        //set Duration
        NumberPicker np = findViewById(R.id.new_todo_numberpicker_time_expenditure);
        int durationMinutes = currentToDo.getDurationMinutes();
        np.setMaxValue(durationMinutes*3);
        Spinner spinner = findViewById(R.id.new_todo_spinner_timeunits);
        if(durationMinutes%(24*60) == 0){
            spinner.setSelection(2);
            np.setValue(durationMinutes/24/60);
            currentToDo.setDurationMinutes(durationMinutes/24/60);
        }else if (durationMinutes%60 == 0){
            spinner.setSelection(1);
            np.setValue(durationMinutes/60);
            currentToDo.setDurationMinutes(durationMinutes/60);
        }else {
            spinner.setSelection(0);
            np.setValue(durationMinutes);
        }
        //set Priority
        RatingBar ratingBar = findViewById(R.id.new_todo_ratingbar_priority);
        ratingBar.setRating(currentToDo.getPriority());
        //set Note
        EditText etNote = findViewById(R.id.new_todo_edittext_note);
        etNote.setText(currentToDo.getNote());
        //set Status
        CheckBox cb = findViewById(R.id.new_todo_checkbox_status);
        cb.setChecked(currentToDo.isFinished());
        //set Image
        ImageView image = findViewById(R.id.new_todo_image);
        image.setImageURI(Uri.parse(currentToDo.getImage()));
    }

    private void saveToDo(ToDo currentToDo, boolean isNew) throws IOException {
        //get Title
        EditText et = findViewById(R.id.new_todo_edittext_title);
        currentToDo.setTitle(et.getText().toString());
        if (currentToDo.getTitle().isEmpty()) {
            reactionToast("Title is not defined");
            return;
        }

        if (deadlineYear == 0 || deadlineMonth == 0 || deadlineDay == 0) {
            reactionToast("Deadline Date is not defined");
            return;
        }
        //get Priority
        RatingBar ratingBar = findViewById(R.id.new_todo_ratingbar_priority);
        currentToDo.setPriority((int) ratingBar.getRating());
        //get Note
        EditText editTextNote = findViewById(R.id.new_todo_edittext_note);
        currentToDo.setNote(editTextNote.getText().toString());
        Spinner timeSpinner = findViewById(R.id.new_todo_spinner_timeunits);
        timeUnit = timeSpinner.getSelectedItem().toString();
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
        //save image
        currentToDo.setImage(imageUri.toString());
        //save & return
        ToDoStorage.addToToDoArrayList(currentToDo);
        showAllToDos();
    }

    private void getDeadline() {
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
    }

    private void getExpenditure(ToDo currentToDo) {
        NumberPicker numberPicker = findViewById(R.id.new_todo_numberpicker_time_expenditure);
        if (numberPicker != null) {
            numberPicker.setMinValue(0);
            if (currentToDo.getDurationMinutes() != 0) {
                numberPicker.setMaxValue(currentToDo.getDurationMinutes());
            }else numberPicker.setMaxValue(180);
            numberPicker.setWrapSelectorWheel(true);
            numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> currentToDo.setDurationMinutes(newVal));
        }
    }

    private void setSpinner() {
        Spinner timeSpinner = findViewById(R.id.new_todo_spinner_timeunits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeUnit = timeSpinner.getSelectedItem().toString();
    }

    private void getStatus(ToDo currentToDo) {
        CheckBox checkBox = findViewById(R.id.new_todo_checkbox_status);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> currentToDo.setFinished(isChecked));
    }
}