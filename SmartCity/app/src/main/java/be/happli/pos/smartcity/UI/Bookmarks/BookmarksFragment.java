package be.happli.pos.smartcity.UI.Bookmarks;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.Utils.Constants;
import be.happli.pos.smartcity.databinding.FragmentBookmarksBinding;

public class BookmarksFragment extends Fragment {

    private FragmentBookmarksBinding binding;
    private BookmarksViewModel bookmarksViewModel;
    private RecyclerView rvPlaces;
    private RVPlacesAdapter rvPlacesAdapter;
    private RVPlacesAdapter.PlaceClickListener placeClickListener;
    private EditText editTextPlaceName;
    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bookmarksViewModel = new ViewModelProvider(getActivity()).get(BookmarksViewModel.class);
        binding = FragmentBookmarksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        if(token == null) {
            InformationDialog informationDialog = InformationDialog.getInstance();
            informationDialog.setInformation(R.string.not_connected_error, R.string.not_connected_message);
            informationDialog.show(getParentFragmentManager().beginTransaction(), null);
        } else {
            //Bookmarked Places
            rvPlaces = root.findViewById(R.id.rv_places);
            bookmarksViewModel.getBookmarkedPlaceDeleted().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
                @Override
                public void onChanged(List<Place> places) {
                    bookmarksViewModel.getBookmarkedPlacesFromWeb();
                }
            });

            setPlaceClickListener();
            rvPlacesAdapter = new RVPlacesAdapter(R.drawable.ic_baseline_delete_24, placeClickListener);
            rvPlaces.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            rvPlaces.setAdapter(rvPlacesAdapter);
            bookmarksViewModel.getBookmarkedPlaces().observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
                @Override
                public void onChanged(List<Place> places) {
                    rvPlacesAdapter.setPlaces(places);
                }
            });

            bookmarksViewModel.getError().observe(getViewLifecycleOwner(), error -> {
                if(error != null) {
                    InformationDialog informationDialog = InformationDialog.getInstance();
                    informationDialog.setInformation(R.string.error, error.getErrorMessage());
                    informationDialog.show(getParentFragmentManager().beginTransaction(), null);
                }
            });

            bookmarksViewModel.getBookmarkedPlacesFromWeb();

            //Place Search
            editTextPlaceName = root.findViewById(R.id.editTextPlaceName);
            setEditTextPlaceName();
        }

        return root;
    }

    public void setPlaceClickListener() {
        placeClickListener = new RVPlacesAdapter.PlaceClickListener() {
            @Override
            public void onImageButtonBookmarkClick(View v, int position) {
                List<Place> places = bookmarksViewModel.getBookmarkedPlaces().getValue();
                bookmarksViewModel.deleteBookmarkedPlaceFromWeb(places.get(position).getId());
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
                List<Place> placesFiltered = getPlacesFiltered(String.valueOf(editTextPlaceName.getText()), bookmarksViewModel.getBookmarkedPlaces().getValue());
                rvPlacesAdapter.setPlaces(placesFiltered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<Place> getPlacesFiltered(String placeName, List<Place> places) {

        List<Place> placesFiltered = new ArrayList<>();
        if(places != null) {
            for(Place place : places) {
                if(place.getPlaceName().toLowerCase().contains(placeName.toLowerCase()))
                    placesFiltered.add(place);
            }
        }
        return placesFiltered;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}