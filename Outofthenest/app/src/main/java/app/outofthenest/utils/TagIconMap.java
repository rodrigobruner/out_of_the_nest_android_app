package app.outofthenest.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import app.outofthenest.R;

public class TagIconMap {
    public static Integer getTagIconMap(int position, String tagCategory) {

        int[] tagIcons = new int[0];

        if (tagCategory.equals("PLACE")) {
            tagIcons = new int[]{
                    R.drawable.ic_tag_01,
                    R.drawable.ic_tag_02,
                    R.drawable.ic_tag_03,
                    R.drawable.ic_tag_04,
                    R.drawable.ic_tag_05
            };
        }

//        if (tagCategory.equals("EVENT")) {
//            tagIcons = new int[]{
//                    R.drawable.ic_tag_06,
//                    R.drawable.ic_tag_07,
//                    R.drawable.ic_tag_08,
//                    R.drawable.ic_tag_09,
//                    R.drawable.ic_tag_10
//            };
//        }

        if(tagIcons.length > 0) {
            return tagIcons[position % tagIcons.length];
        }
        return null;

    }
}
