package be.happli.pos.smartcity.services.Mappers;

import be.happli.pos.smartcity.Model.Activity;
import be.happli.pos.smartcity.Repositories.DTO.ActivityDTO;

public class ActivityMapper {
    private static ActivityMapper instance = null;

    public static ActivityMapper getInstance() {
        if (instance == null) {
            instance = new ActivityMapper();
        }
        return instance;
    }

    public Activity mapToActivity(ActivityDTO dto) {
        if(dto == null) {
            return null;
        }

        return new Activity(dto.getId(), dto.getName(), dto.getDescription(), dto.getCategoryId());
    }
}
