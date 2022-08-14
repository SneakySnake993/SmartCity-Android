package be.happli.pos.smartcity.UI.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Activity;
import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.R;
import be.happli.pos.smartcity.UI.ActivityFilters.ActivityFiltersViewModel;
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.UI.RVAdapter.RVActivitiesAdapter;
import be.happli.pos.smartcity.ViewModel.LocationViewModel;
import be.happli.pos.smartcity.databinding.FragmentActivityBinding;

public class ActivityFragment extends Fragment {

    private FragmentActivityBinding binding;
    private RecyclerView rvActivities;
    private RVActivitiesAdapter rvActivitiesAdapter;
    private RVActivitiesAdapter.ActivityClickListener activityClickListener;
    private ActivityViewModel activityViewModel;
    private LocationViewModel locationViewModel;
    private ActivityFiltersViewModel activityFiltersViewModel;
    private Spinner locationNameSpinner;
    private EditText editTextActivityName;
    private Button editFiltersBtn;
    private Button searchActivityBtn;
    private Integer[] categoriesId;
    private ViewGroup container;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationViewModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        activityViewModel = new ViewModelProvider(getActivity()).get(ActivityViewModel.class);
        activityFiltersViewModel = new ViewModelProvider(getActivity()).get(ActivityFiltersViewModel.class);

        binding = FragmentActivityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.container = container;

        //Location Spinner
        locationNameSpinner = root.findViewById(R.id.spinner_locationNames);
        locationViewModel.getAllLocations().observe(getViewLifecycleOwner(), this::setLocationNameSpinner);

        locationViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });
        sendLocationRequest();

        //ActivityNameTextField
        editTextActivityName = root.findViewById(R.id.editTextActivityName);
        setEditTextActivityName();

        //Filters
        editFiltersBtn = root.findViewById(R.id.editFiltersBtn);
        setAddFiltersBtn();

        //SearchBtn
        searchActivityBtn = root.findViewById(R.id.searchActivityBtn);
        setSearchActivityBtn();

        //Activity
        rvActivities = root.findViewById(R.id.rv_activities);
        setActivityClickListener();
        rvActivitiesAdapter = new RVActivitiesAdapter(activityClickListener);
        rvActivities.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvActivities.setAdapter(rvActivitiesAdapter);
        activityViewModel.getAllActivities().observe(getViewLifecycleOwner(), new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                rvActivitiesAdapter.setActivities(activities);
                List<Activity> activitiesFiltered = getActivitiesFiltered(String.valueOf(editTextActivityName.getText()), activityViewModel.getAllActivities().getValue());
                rvActivitiesAdapter.setActivities(activitiesFiltered);
            }
        });

        activityViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        //Activity Filters
        activityFiltersViewModel.getCategoriesIdSelected().observe(getViewLifecycleOwner(), new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] integers) {
                setActivityFilters(integers);
            }
        });

        activityFiltersViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void sendActivityRequest(Integer[] categoriesId) {
        Integer locationId = null;
        if (locationNameSpinner.getSelectedItemPosition() != 0)
            locationId = locationNameSpinner.getSelectedItemPosition();

        activityViewModel.getAllActivitiesFromWeb(locationId, categoriesId);
    }

    public void sendLocationRequest() {
        locationViewModel.getAllLocationsFromWeb();
    }

    public void setActivityClickListener() {
        activityClickListener = new RVActivitiesAdapter.ActivityClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("activity_id", activityViewModel.getAllActivities().getValue().get(position).getId());
                bundle.putInt("location_id", locationNameSpinner.getSelectedItemPosition());
                Navigation.findNavController(v).navigate(R.id.action_nav_activity_to_nav_place, bundle);
            }
        };
    }

    public void setLocationNameSpinner(List<Location> locations) {
        List<String> locationNames = new ArrayList<>();
        // First item will be used for hint
        locationNames.add(getString(R.string.city_spinner_hint));

        for (Location location : locations) {
            locationNames.add(StringUtils.capitalize(location.getCityName().toLowerCase().trim()));
        }

        setSpinnerAdapter(locationNames);

        Bundle bundle = getArguments();
        if(bundle != null) {
            Integer locationSelected = bundle.getInt("location_Id");

            if(locationSelected != null) {
                locationNameSpinner.setSelection(locationSelected);
            } else {
                Navigation.findNavController(container).navigate(R.id.action_nav_activity_to_nav_home);
            }
        } else {
            Navigation.findNavController(container).navigate(R.id.action_nav_activity_to_nav_home);
        }

        locationNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bundle.putInt("location_Id", locationNameSpinner.getSelectedItemPosition());
                sendActivityRequest(categoriesId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        //First request to get activities
        sendActivityRequest(categoriesId);
    }

    public void setSpinnerAdapter(List<String> locationNames) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, locationNames) {
            @Override
            public boolean isEnabled(int position) {
                return !(position == 0);
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                    tv.setTextColor(Color.GRAY);
                return view;
            }
        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationNameSpinner.setAdapter(spinnerAdapter);
    }

    public void setEditTextActivityName() {
        editTextActivityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Activity> activitiesFiltered = getActivitiesFiltered(String.valueOf(editTextActivityName.getText()), activityViewModel.getAllActivities().getValue());
                rvActivitiesAdapter.setActivities(activitiesFiltered);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public List<Activity> getActivitiesFiltered(String filter, List<Activity> activities) {
        List<Activity> activitiesFiltered = new ArrayList<>();
        for(Activity activity : activities) {
            if(activity.getName().toLowerCase().contains(filter.toLowerCase()))
                activitiesFiltered.add(activity);
        }
        return activitiesFiltered;
    }

    public void setAddFiltersBtn() {
        editFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_activity_to_nav_activity_filters);
            }
        });
    }

    public void setActivityFilters(Integer[] categoriesId) {
        this.categoriesId = categoriesId;
    }

    public void setSearchActivityBtn() {
        searchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActivityRequest(categoriesId);
            }
        });
    }

    // STUBS Activities
    public List<Activity> getStubActivities() {
        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(1, "Cinéma", "", 1));
        activities.add(new Activity(2, "Parc d'attractions", "", 1));
        activities.add(new Activity(3, "Musée", "", 1));
        activities.add(new Activity(1, "Cinéma", "", 1));
        activities.add(new Activity(2, "Parc d'attractions", "", 1));
        activities.add(new Activity(3, "Musée", "", 1));
        activities.add(new Activity(1, "Cinéma", "", 1));
        activities.add(new Activity(2, "Parc d'attractions", "", 1));
        activities.add(new Activity(3, "Musée", "", 1));
        activities.add(new Activity(1, "Cinéma", "", 1));
        activities.add(new Activity(2, "Parc d'attractions", "", 1));
        activities.add(new Activity(3, "Musée", "", 1));
        return activities;
    }
}