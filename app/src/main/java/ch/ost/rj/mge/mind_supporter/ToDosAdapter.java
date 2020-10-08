package ch.ost.rj.mge.mind_supporter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ToDosAdapter extends RecyclerView.Adapter {
    private ArrayList<ToDo> toDoArrayList;

    public ToDosAdapter(ArrayList<ToDo> input){
        this.toDoArrayList=input;
    }

    public void setAdapterDataBasis(ArrayList<ToDo> in){
        this.toDoArrayList = in;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.to_do_list_item_layout,parent,false);

        TextView title = view.findViewById(R.id.title);
        TextView finishStatus = view.findViewById(R.id.finish_status);
        TextView finishDate = view.findViewById(R.id.finish_date);

        return new ToDosViewHolder(view,title,finishStatus, finishDate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ToDosViewHolder tmp = (ToDosViewHolder) holder;
        ToDo todo = this.toDoArrayList.get(position);
        tmp.title.setText(toDoArrayList.get(position).getTitle());
        tmp.finishedStatus.setText(Boolean.toString(toDoArrayList.get(position).isFinished()));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        tmp.finishDate.setText(toDoArrayList.get(position).getDueDateTime().format(formatter));
    }

    @Override
    public int getItemCount() {
        return this.toDoArrayList.size();
    }
}
