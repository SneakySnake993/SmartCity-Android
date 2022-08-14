package be.happli.pos.smartcity.services.Mappers;

import be.happli.pos.smartcity.Model.User;
import be.happli.pos.smartcity.Repositories.DTO.UserDTO;

public class UserMapper {
    private static UserMapper instance = null;

    private UserMapper() { }

    public static UserMapper getInstance() {
        if(instance == null) {
            instance = new UserMapper();
        }
        return instance;
    }

    public User mapToUser(UserDTO dto) {
        if(dto == null)
            return null;
        try {
            User user = new User(dto.getUsername(), dto.getPassword(), dto.getAdmin());
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public UserDTO mapToUserDto(User user) {
        if(user == null)
            return null;
        UserDTO userDto = new UserDTO(user.getUsername(), user.getPassword(), user.getAdmin());
        return userDto;
    }
}
