package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    private void openNewToDo(){
        Intent intent = new Intent(this, NewToDo.class);
        startActivity(intent);
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
    switch(item.getItemId()){
        case R.id.show_pending:
            if(item.isChecked()){
                return;
            }
            //ToDoStorage.moveFinishedToFinishedArrayList();
            adapter.notifyDataSetChanged();
            item.setChecked(true);
            break;
        case R.id.show_finished:
            if(item.isChecked()){
                return;
            }
            //ToDoStorage.movePendingToPendingArrayList();
            adapter.notifyDataSetChanged();
            break;
        case R.id.show_all:
            break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ToDosAdapter(ToDoStorage.getToDoArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.getDrawable();

        RecyclerView recyclerView = findViewById(R.id.to_do_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton newToDo = findViewById(R.id.floatingActionButton2);
        newToDo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openNewToDo();
            }
        });
    }
}