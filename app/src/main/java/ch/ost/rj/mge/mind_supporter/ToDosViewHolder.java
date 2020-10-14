package ch.ost.rj.mge.mind_supporter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ToDosViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public ImageView image;
    public TextView finishDate;
    public ConstraintLayout constraintLayout;

    public ToDosViewHolder(View parent, TextView title, ImageView image, TextView finishDate) {
        super(parent);
        this.title = title;
        this.finishDate = finishDate;
        this.image = image;
        this.constraintLayout = parent.findViewById(R.id.to_do_list_item_layout);
        constraintLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent editNoteIntent = new Intent(App.getContext(), NewToDo.class);
                editNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //TODO: Add Parameters (edit = true, ToDo object or identifier for ToDo object)
                App.getContext().startActivity(editNoteIntent);
            }
        });
    }

}
