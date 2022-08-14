package be.happli.pos.smartcity.UI.ActivityFilters;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.ListView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.happli.pos.smartcity.Model.Category;
import be.happli.pos.smartcity.Model.Location;
import be.happli.pos.smartcity.R;
import be.happli.pos.smartcity.UI.InformationDialog;
import be.happli.pos.smartcity.databinding.FragmentActivityFiltersBinding;

public class ActivityFiltersFragment extends Fragment {

    private FragmentActivityFiltersBinding binding;
    private ListView activityFiltersListView;
    private Button confirmFiltersBtn;
    private ActivityFiltersViewModel activityFiltersViewModel;
    private Integer[] categoriesId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityFiltersViewModel = new ViewModelProvider(getActivity()).get(ActivityFiltersViewModel.class);
        binding = FragmentActivityFiltersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        activityFiltersListView = root.findViewById(R.id.activity_filters_list_View);
        confirmFiltersBtn = root.findViewById(R.id.confirm_filters_btn);

        activityFiltersViewModel.getCategoriesIdSelected().observe(getViewLifecycleOwner(), new Observer<Integer[]>() {
            @Override
            public void onChanged(Integer[] integers) {
                setActivityFilters(integers);
            }
        });

        activityFiltersViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                setActivityFiltersListView(categories);
            }
        });

        activityFiltersViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if(error != null) {
                InformationDialog informationDialog = InformationDialog.getInstance();
                informationDialog.setInformation(R.string.error, error.getErrorMessage());
                informationDialog.show(getParentFragmentManager().beginTransaction(), null);
            }
        });

        activityFiltersViewModel.getAllCategoriesFromWeb();

        setConfirmFiltersBtn();

        return root;
    }

    public void setActivityFiltersListView(List<Category> categoryList) {
        List<String> filtersNames = new ArrayList<>();
        for (Category filter : categoryList) {
            filtersNames.add(StringUtils.capitalize(filter.getTitle().toLowerCase().trim()));
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, filtersNames);
        activityFiltersListView.setAdapter(listAdapter);
        setSelectedFilters();
    }

    public void setSelectedFilters() {
        if(categoriesId != null) {
            for(Integer categoryId : categoriesId) {
                activityFiltersListView.setItemChecked(categoryId-1, true);
            }
        }
    }

    public void setConfirmFiltersBtn() {
        confirmFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putIntegerArrayList("categoriesId", getIdFromSelectedCategories());
//                Navigation.findNavController(view).navigate(R.id.action_nav_activity_filters_to_nav_activity, bundle);
                activityFiltersViewModel.setCategoriesIdSelected(getIdFromSelectedCategories().toArray(new Integer[0]));
                Navigation.findNavController(view).popBackStack();

            }
        });
    }

    public List<Integer> getIdFromSelectedCategories() {
        List<Integer> categoriesId = new ArrayList<>();
        for(int i = 0; i < activityFiltersListView.getCount(); i++) {
            if(activityFiltersListView.isItemChecked(i)) {
                categoriesId.add(i+1);
            }
        }
        return categoriesId;
    }

    public void setActivityFilters(Integer[] categoriesId) {
        this.categoriesId = categoriesId;
    }

    public List<Category> getStubCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Entertainment"));
        categories.add(new Category(2, "Arts"));
        categories.add(new Category(3, "Food"));
        categories.add(new Category(4, "Entertainment"));
        categories.add(new Category(5, "Arts"));
        categories.add(new Category(6, "Food"));
        categories.add(new Category(7, "Entertainment"));
        categories.add(new Category(8, "Arts"));
        categories.add(new Category(9, "Food"));
        return categories;
    }
}