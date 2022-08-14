package be.happli.pos.smartcity.UI.RVAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.R;

public class RVPlacesAdapter extends RecyclerView.Adapter<RVPlacesAdapter.PlaceViewHolder>{

    private List<Place> places;
    private int imageButtonResource;
    private PlaceClickListener placeClickListener;

    public RVPlacesAdapter(int imageButtonResource,PlaceClickListener placeClickListener) {
        this.imageButtonResource = imageButtonResource;
        this.placeClickListener = placeClickListener;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView tv_placeItemName;
        ImageButton imageButton_Bookmarks;
        TextView tv_placeItemActivityName;
        TextView tv_placeItemDescription;
        TextView tv_placeItemAddress;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_placeItemName = itemView.findViewById(R.id.tv_placeItemName);
            imageButton_Bookmarks = itemView.findViewById(R.id.imageButton_toggle_Bookmarked);
            tv_placeItemActivityName = itemView.findViewById(R.id.tv_placeItemActivityName);
            tv_placeItemDescription = itemView.findViewById(R.id.tv_placeItemDescription);
            tv_placeItemAddress = itemView.findViewById(R.id.tv_placeItemAddress);

            imageButton_Bookmarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeClickListener.onImageButtonBookmarkClick(view, getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.imageButton_Bookmarks.setImageResource(imageButtonResource);
        holder.tv_placeItemName.setText(StringUtils.capitalize(places.get(position).getPlaceName().toLowerCase().trim()));
        holder.tv_placeItemActivityName.setText(StringUtils.capitalize(places.get(position).getActivityName().toLowerCase().trim()));
        holder.tv_placeItemDescription.setText(places.get(position).getDescription());
        holder.tv_placeItemAddress.setText(places.get(position).getAddress() + ",\n" + places.get(position).getPostalCode() + ", " + places.get(position).getCityName());

        if(places.get(position).getBookmarked()) {
            holder.imageButton_Bookmarks.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
    }

    @Override
    public int getItemCount() {
        return places == null ? 0 : places.size();
    }

    public void setPlaces(List<Place> places) {
        if(places != null) {
            this.places = places;
            notifyDataSetChanged();
        }
    }

    public interface PlaceClickListener {
        void onImageButtonBookmarkClick(View v, int position);
    }

}
