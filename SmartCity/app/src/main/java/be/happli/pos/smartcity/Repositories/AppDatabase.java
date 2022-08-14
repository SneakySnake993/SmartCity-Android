package be.happli.pos.smartcity.Repositories;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import be.happli.pos.smartcity.Model.User;

/**
 * Putting everything together : Declare an abstract class to hold all the DAOs (Database)
 *
 */

//@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // list here all the DAO
    // public abstract UserDAO userDao();
}