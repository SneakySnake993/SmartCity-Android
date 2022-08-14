package be.happli.pos.smartcity.UI.RVAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import be.happli.pos.smartcity.Model.Activity;
import be.happli.pos.smartcity.R;

public class RVActivitiesAdapter extends RecyclerView.Adapter<RVActivitiesAdapter.ActivityViewHolder> {

    private List<Activity> activities;
    private ActivityClickListener listener;

    public RVActivitiesAdapter(ActivityClickListener listener) {
        this.listener = listener;
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView activityName;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.tv_activity_item_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view, getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public RVActivitiesAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RVActivitiesAdapter.ActivityViewHolder holder, int position) {
        holder.activityName.setText(StringUtils.capitalize(activities.get(position).getName().toLowerCase().trim()));
    }

    @Override
    public int getItemCount() {
        return activities == null ? 0 : activities.size();
    }

    public interface ActivityClickListener {
        void onClick(View v, int position);
    }

    public void setActivities(List<Activity> activities) {
        if(activities != null) {
            this.activities = activities;
            notifyDataSetChanged();
        }
    }
}
