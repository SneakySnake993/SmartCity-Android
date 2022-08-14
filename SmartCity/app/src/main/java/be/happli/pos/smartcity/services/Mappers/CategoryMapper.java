package be.happli.pos.smartcity.services.Mappers;

import be.happli.pos.smartcity.Model.Activity;
import be.happli.pos.smartcity.Model.Category;
import be.happli.pos.smartcity.Repositories.DTO.ActivityDTO;
import be.happli.pos.smartcity.Repositories.DTO.CategoryDTO;

public class CategoryMapper {
    private static CategoryMapper instance = null;

    public static CategoryMapper getInstance() {
        if (instance == null) {
            instance = new CategoryMapper();
        }
        return instance;
    }

    public Category mapToCategory(CategoryDTO dto) {
        if(dto == null) {
            return null;
        }

        return new Category(dto.getId(), dto.getTitle());
    }
}
