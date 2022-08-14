package be.happli.pos.smartcity.UI.Place;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Place;
import be.happli.pos.smartcity.R;
import be.happli.pos.smartcity.UI.RVAdapter.RVPlacesAdapter;
import be.happli.pos.smartcity.UI.Bookmarks.BookmarksViewModel;
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.Utils.Constants;
import be.happli.pos.smartcity.databinding.FragmentPlaceBinding;

public class PlaceFragment extends Fragment {

    private PlaceViewModel placeViewModel;
    private BookmarksViewModel bookmarksViewModel;
    private FragmentPlaceBinding binding;
    private RecyclerView rvPlaces;
    private RVPlacesAdapter rvPlacesAdapter;
    private RVPlacesAdapter.PlaceClickListener placeClickListener;
    private EditText editTextPlaceName;
    private String token;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        placeViewModel = new ViewModelProvider(getActivity()).get(PlaceViewModel.class);
        bookmarksViewModel = new ViewModelProvider(getActivity()).get(BookmarksViewModel.class);
        binding = FragmentPlaceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.container = container;

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        //Places
        rvPlaces = root.findViewById(R.id.rv_places);
        setPlaceClickListener();
        rvPlacesAdapter = new RVPlacesAdapter(R.drawable.ic_baseline_bookmark_border_24, placeClickListener);
        rvPlaces.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvPlaces.setAdapter(rvPlacesAdapter);

        placeViewModel.getPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                if(token == null) {
                    rvPlacesAdapter.setPlaces(places);
                } else {
                    bookmarksViewModel.getBookmarkedPlacesFromWeb();
                }
            }
        });
        placeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null) {
            Integer activity_id = bundle.getInt("activity_id");
            Integer location_id = bundle.getInt("location_id");
            placeViewModel.getPlacesFromWeb(location_id, activity_id);
        }

        bookmarksViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        bookmarksViewModel.getBookmarkedPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> bookmarkedPlaces) {
                //Check if some places are bookmarked
                List<Place> places = placeViewModel.getPlaces().getValue();
                if(places != null) {
                    for(Place place : places){
                        for (Place bookmarkedPlace : bookmarkedPlaces) {
                            if(place.getId() == bookmarkedPlace.getId()) {
                                places.get(places.indexOf(place)).setBookmarked(true);
                            }
                        }
                    }
                    rvPlacesAdapter.setPlaces(places);
                }
            }
        });

        bookmarksViewModel.getBookmarkedPlaceDeleted().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                Bundle bundle = getArguments();
                if(bundle != null) {
                    Integer activity_id = bundle.getInt("activity_id");
                    Integer location_id = bundle.getInt("location_id");
                    placeViewModel.getPlacesFromWeb(location_id, activity_id);
                }
            }
        });

        bookmarksViewModel.getBookmarkedPlaceAddedd().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> places) {
                Bundle bundle = getArguments();
                if(bundle != null) {
                    Integer activity_id = bundle.getInt("activity_id");
                    Integer location_id = bundle.getInt("location_id");
                    placeViewModel.getPlacesFromWeb(location_id, activity_id);
                }
            }
        });

        //Place Search
        editTextPlaceName = root.findViewById(R.id.editTextPlaceName);
        setEditTextPlaceName();

        return root;
    }

    public void setPlaceClickListener() {
        placeClickListener = new RVPlacesAdapter.PlaceClickListener() {
            @Override
            public void onImageButtonBookmarkClick(View v, int position) {
                if(token == null) {
                    InformationDialog informationDialog = InformationDialog.getInstance();
                    informationDialog.setInformation(R.string.not_connected_error, R.string.not_connected_message);
                    informationDialog.show(getParentFragmentManager().beginTransaction(), null);
                }
                Place place = placeViewModel.getPlaces().getValue().get(position);
                if(place.getBookmarked()) {
                    //Delete from bookmark
                    bookmarksViewModel.deleteBookmarkedPlaceFromWeb(place.getId());
                } else {
                    //Add to bookmark
                    bookmarksViewModel.addBookmarkedPlaceFromWeb(place.getId());
                }
            }
        };
    }

    public void setEditTextPlaceName() {
        editTextPlaceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Place> placesFiltered = getPlacesFiltered(String.valueOf(editTextPlaceName.getText()), placeViewModel.getPlaces().getValue());
                rvPlacesAdapter.setPlaces(placesFiltered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<Place> getPlacesFiltered(String placeName, List<Place> places) {
        List<Place> placesFiltered = new ArrayList<>();
        for(Place place : places) {
            if(place.getPlaceName().toLowerCase().contains(placeName.toLowerCase()))
                placesFiltered.add(place);
        }
        return placesFiltered;
    }

    //STUB Test
    public List<Place> getStubPlaces() {
        List<Place> places = new ArrayList<>();
        places.add(new Place(1, "Acinapolis1", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis2", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis3", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis4", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis5", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis6", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis7", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis8", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        places.add(new Place(1, "Acinapolis9", "cinema", "...Description...", "Rue de la gare fleurie 16", 5100, "Jambes"));
        return places;
    }
}