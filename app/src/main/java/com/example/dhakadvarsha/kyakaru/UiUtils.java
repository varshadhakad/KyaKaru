package com.example.dhakadvarsha.kyakaru;
import android.app.Activity;
import android.view.View;

public class UiUtils {
    public static <T extends View> T findView(View root, int id)      {
        return (T) root.findViewById(id); }

    public static <T extends View> T findView(Activity activity, int id)      {
        return (T) activity.getWindow().getDecorView().getRootView().findViewById(id); }

}
