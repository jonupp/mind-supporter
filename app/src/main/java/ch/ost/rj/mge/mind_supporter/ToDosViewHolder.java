package ch.ost.rj.mge.mind_supporter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ToDosViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView finishedStatus;
    public TextView finishDate;

    public ToDosViewHolder(View parent, TextView title, TextView finishedStatus, TextView finishDate) {
        super(parent);
        this.title = title;
        this.finishDate = finishDate;
        this.finishedStatus = finishedStatus;
    }

}
