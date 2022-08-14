package be.happli.pos.smartcity.UI.ActivityFilters;

import android.app.Application;
import android.widget.CursorTreeAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import be.happli.pos.smartcity.Model.Category;
import be.happli.pos.smartcity.Model.Category;
import be.happli.pos.smartcity.Model.NetworkError;
import be.happli.pos.smartcity.Repositories.ApiService.CategoryService;
import be.happli.pos.smartcity.Repositories.ApiService.CategoryService;
import be.happli.pos.smartcity.Repositories.DTO.CategoryDTO;
import be.happli.pos.smartcity.Repositories.RetrofitConfigurationService;
import be.happli.pos.smartcity.Utils.errors.NoConnectivityException;
import be.happli.pos.smartcity.services.Mappers.CategoryMapper;
import be.happli.pos.smartcity.services.Mappers.CategoryMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFiltersViewModel extends AndroidViewModel {

    private MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    private LiveData<List<Category>> categories = _categories;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<Integer[]> _categoriesIdSelected = new MutableLiveData<>();
    private LiveData<Integer[]> categoriesIdSelected = _categoriesIdSelected;

    private CategoryService categoryService;
    private CategoryMapper categoryMapper;

    public ActivityFiltersViewModel(@NonNull Application application) {
        super(application);
        this.categoryService = RetrofitConfigurationService.getInstance(getApplication()).categoryService();
        this.categoryMapper = CategoryMapper.getInstance();
    }

    public void getAllCategoriesFromWeb() {
        categoryService.getAllCategories().enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if(response.isSuccessful()) {
                    List<Category> categories = new ArrayList<>();
                    for(CategoryDTO categoryDTO : response.body()) {
                        categories.add(categoryMapper.mapToCategory(categoryDTO));
                    }
                    _categories.setValue(categories);
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return categories;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }


    public void setCategoriesIdSelected(Integer[] categoriesIdSelected) {
        _categoriesIdSelected.setValue(categoriesIdSelected);
    }
    public LiveData<Integer[]> getCategoriesIdSelected() {return categoriesIdSelected;}

}