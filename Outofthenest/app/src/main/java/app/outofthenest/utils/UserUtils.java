package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.outofthenest.models.FamilyMember;
import app.outofthenest.models.User;

/**
 * Class to mantain user data in shared preferences.
 */
public class UserUtils {
    private static final String PREF_NAME = Constants.APP_PAKEGE_NAME+".users_prefs";
    private static final String KEY_USER = "user";

    //save the user data
    public static void saveUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (user != null) {
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            editor.putString(KEY_USER, userJson);
        } else {
            editor.remove(KEY_USER);
        }

        editor.apply();
    }

    //get user data
    public static User getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);

        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }

        return null;
    }

    // check if user data exist
    public static boolean hasUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_USER);
    }

    // clear user data
    public static void clearUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }

    // add a family member to the user
    public static void addFamilyMember(Context context, FamilyMember member) {
        User user = getUser(context);
        if (user != null) {
            ArrayList<FamilyMember> members = user.getFamilyMembers();
            if (members == null) members = new ArrayList<>();
            members.add(member);
            user.setFamilyMembers(members);
            saveUser(context, user);
        }
    }

    //remove a family member
    public static void removeFamilyMember(Context context, int index) {
        User user = getUser(context);
        if (user != null) {
            ArrayList<FamilyMember> members = user.getFamilyMembers();
            if (members != null && index >= 0 && index < members.size()) {
                members.remove(index);
                user.setFamilyMembers(members);
                saveUser(context, user);
            }
        }
    }

    //get all family members
    public static ArrayList<FamilyMember> getFamilyMembers(Context context) {
        User user = getUser(context);
        if (user != null && user.getFamilyMembers() != null) {
            return user.getFamilyMembers();
        }
        return new ArrayList<>();
    }
}