package app.outofthenest.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.outofthenest.models.FamilyMember;

public class FamilyMemberUtils {
    private static final String PREF_NAME = "out_of_the_nest_prefs";
    private static final String KEY_MEMBERS = "family_members";

    public static void saveFamilyMembers(Context context, ArrayList<FamilyMember> members) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(members);
        editor.putString(KEY_MEMBERS, json);
        editor.apply();
    }

    public static ArrayList<FamilyMember> getFamilyMembers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEMBERS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<FamilyMember>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void addFamilyMember(Context context, FamilyMember member) {
        ArrayList<FamilyMember> members = getFamilyMembers(context);
        members.add(member);
        saveFamilyMembers(context, members);
    }

    public static void removeFamilyMember(Context context, int index) {
        ArrayList<FamilyMember> members = getFamilyMembers(context);
        if (index >= 0 && index < members.size()) {
            members.remove(index);
            saveFamilyMembers(context, members);
        }
    }
}