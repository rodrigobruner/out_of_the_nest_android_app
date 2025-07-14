package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import app.outofthenest.models.FamilyMember;
import app.outofthenest.models.User;

public class UserUtils {
    private static final String PREF_NAME = "out_of_the_nest_prefs";
    private static final String KEY_USER = "user";

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

    public static User getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_USER, null);

        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }

        return null;
    }

    public static boolean hasUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(KEY_USER);
    }

    public static void clearUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_USER);
        editor.apply();
    }

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

    public static ArrayList<FamilyMember> getFamilyMembers(Context context) {
        User user = getUser(context);
        if (user != null && user.getFamilyMembers() != null) {
            return user.getFamilyMembers();
        }
        return new ArrayList<>();
    }
}