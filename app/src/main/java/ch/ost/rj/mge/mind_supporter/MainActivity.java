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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);
        return true;
    }

    private void openNewToDo(){
        Intent intent = new Intent(this, NewToDo.class);
        startActivity(intent);
    }

    public void onGroupItemClick(MenuItem item) {
    switch(item.getItemId()){
        case R.id.show_pending:
            ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getPending());
            break;
        case R.id.show_finished:
            ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getFinished());
            break;
        case R.id.show_all:
            ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getToDoArrayList());
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ToDosAdapter(ToDoStorage.getToDoArrayList(), this);
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