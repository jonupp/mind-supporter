package ch.ost.rj.mge.mind_supporter;

import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Console;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ToDosAdapter extends RecyclerView.Adapter<ToDosViewHolder> {
    private ArrayList<ToDo> toDoArrayList;
    private RecyclerView recyclerView;

    public ToDosAdapter(ArrayList<ToDo> input){
        this.toDoArrayList=input;
    }

    public void setAdapterDataBasis(ArrayList<ToDo> in){
        this.toDoArrayList = in;
        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ToDosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.to_do_list_item_layout,parent,false);

        //On click edit
        view.setOnClickListener(v -> {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            ToDo todo =  toDoArrayList.get(itemPosition);
            Intent editNoteIntent = new Intent(context, NewToDo.class);
            editNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            editNoteIntent.putExtra("todo", todo);
            editNoteIntent.putExtra("isNewFlag", false);
            toDoArrayList.remove(todo);
            context.startActivity(editNoteIntent);
        });

        view.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete todo?");
            builder.setMessage("Do you really want to delete the selected todo?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                ToDo todo =  toDoArrayList.get(itemPosition);
                try {
                    ToDoStorage.removeToDoFromArrayList(todo);
                    ToDosAdapter.this.notifyDataSetChanged();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        });

        TextView title = view.findViewById(R.id.title);
        ImageView image = view.findViewById(R.id.image);
        TextView finishDate = view.findViewById(R.id.finish_date);

        return new ToDosViewHolder(view, title, image, finishDate);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDosViewHolder holder, int position) {
        ToDo todo = this.toDoArrayList.get(position);

        if(todo.isFinished()){
            holder.constraintLayout.setBackgroundResource(R.color.light_green);
        }else{
            holder.constraintLayout.setBackgroundResource(R.color.light_red);
        }

        holder.title.setText(toDoArrayList.get(position).getTitle());

        if(!toDoArrayList.get(position).getImage().equals("../../res/drawable/image_placeholder.xml")){ //Don't load if URI points to default image (set in XML-Layout)
            holder.image.setImageURI(Uri.parse(toDoArrayList.get(position).getImage()));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        holder.finishDate.setText(toDoArrayList.get(position).getDueDateTime().format(formatter));
    }

    @Override
    public int getItemCount() {
        return this.toDoArrayList.size();
    }
}