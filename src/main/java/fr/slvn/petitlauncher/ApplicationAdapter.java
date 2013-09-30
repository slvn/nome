package fr.slvn.petitlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ApplicationAdapter extends BaseAdapter {

    private Context context;
    private List<ResolveInfo> resolveInfos;

    public ApplicationAdapter(Context context) {
        this.context = context;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
    }

    @Override
    public int getCount() {
        return resolveInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return resolveInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return resolveInfos.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(96, 96));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        ResolveInfo info = (ResolveInfo) getItem(position);
        imageView.setImageDrawable(getIcon(info));
        return imageView;
    }

    public Drawable getIcon(ResolveInfo info) {
        Resources resources;
        try {
            resources = context.getPackageManager().getResourcesForApplication(info.activityInfo.applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            resources = null;
        }
        if (resources != null) {
            int iconId = info.getIconResource();
            if (iconId != 0) {
                return resources.getDrawable(iconId);
            }
        }
        return null;
    }
}
