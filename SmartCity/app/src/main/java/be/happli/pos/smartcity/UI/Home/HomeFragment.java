package be.happli.pos.smartcity.UI.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.Model.User;
import be.happli.pos.smartcity.R;
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.UI.RVAdapter.RVCitiesAdapter;
import be.happli.pos.smartcity.UI.Login.AccountViewModel;
import be.happli.pos.smartcity.Utils.Constants;
import be.happli.pos.smartcity.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView rvCities;
    private RVCitiesAdapter rvCitiesAdapter;
    private RVCitiesAdapter.CityClickListener cityClickListener;
    private HomeViewModel homeViewModel;
    private AccountViewModel accountViewModel;
    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Constants.TOKEN, null);

        if(token != null) {
            accountViewModel.getUserFromToken(token);
        }

        accountViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    View header = navigationView.getHeaderView(0);
                    TextView tvNavHeader = header.findViewById(R.id.tv_nav_header);
                    tvNavHeader.setText(getText(R.string.welcome_nav_header_title) + " " + user.getUsername());
                }
            }
        });

        rvCities = root.findViewById(R.id.rv_cities);

        setCityClickListener();
        rvCitiesAdapter = new RVCitiesAdapter(cityClickListener);
        rvCities.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvCities.setAdapter(rvCitiesAdapter);
        homeViewModel.getAllLocations().observe(getViewLifecycleOwner(), rvCitiesAdapter::setCities);

        homeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        homeViewModel.getStatusCode().observe(getViewLifecycleOwner(), code -> {
            InformationDialog informationDialog = InformationDialog.getInstance();
            if(code == 404) {
                informationDialog.setInformation(R.string.error, R.string.elements_not_found);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            } else if(code == 500) {
                informationDialog.setInformation(R.string.error, R.string.error_servor);
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        homeViewModel.getAllLocationsFromWeb();

        return root;
    }

    public void setCityClickListener() {
        cityClickListener = new RVCitiesAdapter.CityClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("location_Id", homeViewModel.getAllLocations().getValue().get(position).getId());
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_activity, bundle);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}