package ch.ost.rj.mge.mind_supporter;

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
    }
}
