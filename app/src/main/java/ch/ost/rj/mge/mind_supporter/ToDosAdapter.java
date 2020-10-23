package ch.ost.rj.mge.mind_supporter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ToDosAdapter extends RecyclerView.Adapter {
    private ArrayList<ToDo> toDoArrayList;
    private RecyclerView recyclerView;
    private Context mainActivityContext;

    public ToDosAdapter(ArrayList<ToDo> input, Context context){
        this.toDoArrayList=input;
        this.mainActivityContext = context;
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                ToDo todo =  toDoArrayList.get(itemPosition);
                Intent editNoteIntent = new Intent(App.getContext(), NewToDo.class);
                editNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                editNoteIntent.putExtra("todo", todo);
                App.getContext().startActivity(editNoteIntent);
            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete todo?");
                builder.setMessage("Do you really want to delete the selected todo?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        ToDo todo =  toDoArrayList.get(itemPosition);
                        try {
                            ToDoStorage.removeToDoFromArrayList(todo);
                            ToDosAdapter.this.notifyDataSetChanged();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

        TextView title = view.findViewById(R.id.title);
        ImageView image = view.findViewById(R.id.image);
        TextView finishDate = view.findViewById(R.id.finish_date);

        return new ToDosViewHolder(view, title, image, finishDate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ToDosViewHolder tmp = (ToDosViewHolder) holder;
        ToDo todo = this.toDoArrayList.get(position);

        if(todo.isFinished()){
            ((ToDosViewHolder) holder).constraintLayout.setBackgroundResource(R.color.light_green);
        }else{
            ((ToDosViewHolder) holder).constraintLayout.setBackgroundResource(R.color.light_red);
        }

        tmp.title.setText(toDoArrayList.get(position).getTitle());

        if(!toDoArrayList.get(position).getImage().equals("../../res/drawable/image_placeholder.xml")){ //Don't load if URI points to default image (set in XML-Layout)
            final InputStream imageStream;
            try {
                //imageStream = App.getContext().getContentResolver().openInputStream(Uri.parse(toDoArrayList.get(position).getImage()));
                //tmp.image.setImageBitmap(BitmapFactory.decodeStream(imageStream));
                tmp.image.setImageBitmap(MediaStore.Images.Media.getBitmap(App.getContext().getContentResolver(), Uri.parse(toDoArrayList.get(position).getImage())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        tmp.finishDate.setText(toDoArrayList.get(position).getDueDateTime().format(formatter));
    }

    @Override
    public int getItemCount() {
        return this.toDoArrayList.size();
    }
}