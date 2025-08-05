package app.outofthenest.utils;

import app.outofthenest.R;

public class TagIconMap {

    public static final String TYPE_PLACE = "PLACE";

    public static Integer getTagIconMap(int position, String tagCategory) {
        int[] tagIcons = new int[0];

        if (TYPE_PLACE.equals(tagCategory)) {
            tagIcons = new int[]{
                    R.drawable.ic_tag_01,
                    R.drawable.ic_tag_02,
                    R.drawable.ic_tag_03,
                    R.drawable.ic_tag_04,
                    R.drawable.ic_tag_05,
                    R.drawable.ic_tag_06,
                    R.drawable.ic_tag_07,
                    R.drawable.ic_tag_08,
                    R.drawable.ic_tag_09,
                    R.drawable.ic_tag_10,
                    R.drawable.ic_tag_11,
                    R.drawable.ic_tag_12
            };
        }

        if (position >= 0 && position < tagIcons.length) {
            return tagIcons[position % tagIcons.length];
        }
        return -1;
    }
}