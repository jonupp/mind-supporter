package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter<ToDosViewHolder> adapter;
    private Menu menu;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        this.menu = menu;
        this.menu.getItem(2).setChecked(true);
        return true;
    }

    private void openNewToDo(){
        Intent intent = new Intent(this, NewToDo.class);
        intent.putExtra("todo", new ToDo("", null, 0, 0, false, "../../res/drawable/image_placeholder.xml", ""));
        startActivity(intent);
    }

    public void onGroupItemClick(MenuItem item) {
        MenuItem show_pending = menu.getItem(0);
        MenuItem show_finished =  menu.getItem(1);
        MenuItem show_all =  menu.getItem(2);

        switch(item.getTitle().toString()){
            case "Show pending":
            if(item.isChecked()){
                return;
            }else{
                ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getPending());
                show_finished.setChecked(false);
                show_all.setChecked(false);
                item.setChecked(true);
            }
            break;
        case "Show finished":
            if(item.isChecked()){
                return;
            }else {
                ((ToDosAdapter) adapter).setAdapterDataBasis(ToDoStorage.getFinished());
                show_pending.setChecked(false);
                show_all.setChecked(false);
                item.setChecked(true);
            }
            break;
        case "Show all":
            if(item.isChecked()){
                return;
            }else {
                ((ToDosAdapter) adapter).setAdapterDataBasis(ToDoStorage.getToDoArrayList());
                show_pending.setChecked(false);
                show_finished.setChecked(false);
                item.setChecked(true);
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                0
        );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ToDosAdapter(ToDoStorage.getToDoArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.getDrawable();

        RecyclerView recyclerView = findViewById(R.id.to_do_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton newToDo = findViewById(R.id.floatingActionButton2);
        newToDo.setOnClickListener((v)-> openNewToDo());
    }
}