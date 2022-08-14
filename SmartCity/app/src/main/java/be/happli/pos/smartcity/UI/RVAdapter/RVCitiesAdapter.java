package be.happli.pos.smartcity.UI.RVAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.R;

public class RVCitiesAdapter extends RecyclerView.Adapter<RVCitiesAdapter.CityViewHolder> {

    private List<Location> locations;
    private CityClickListener listener;

    public RVCitiesAdapter(CityClickListener listener) {
        this.listener = listener;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.tv_cityName);

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
    public RVCitiesAdapter.CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RVCitiesAdapter.CityViewHolder holder, int position) {
        holder.cityName.setText(StringUtils.capitalize(locations.get(position).getCityName().toLowerCase().trim()));
    }

    @Override
    public int getItemCount() {
        return locations == null ? 0 : locations.size();
    }

    public interface CityClickListener {
        void onClick(View v, int position);
    }

    public void setCities(List<Location> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }
}