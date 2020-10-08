package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    public void onGroupItemClick(MenuItem item) {
    switch(item.getItemId()){
        case R.id.show_pending:
            ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getFinished());
            break;
        case R.id.show_finished:
            ((ToDosAdapter)adapter).setAdapterDataBasis(ToDoStorage.getPending());
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
        adapter = new ToDosAdapter(ToDoStorage.getToDoArrayList());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.getDrawable();

        RecyclerView recyclerView = findViewById(R.id.to_do_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}