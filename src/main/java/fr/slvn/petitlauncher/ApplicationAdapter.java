package fr.slvn.petitlauncher;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ApplicationAdapter extends BaseAdapter {

    private static final String TAG = "ApplicationAdapter";

    private Context context;
    private LayoutInflater inflater;
    private List<ResolveInfo> resolveInfos;
    private int appIconsize;

    public ApplicationAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        appIconsize = (int) context.getResources().getDimension(R.dimen.app_icon_size);
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
            imageView = (ImageView) inflater.inflate(R.layout.icon, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }
        ResolveInfo info = (ResolveInfo) getItem(position);
        Picasso.with(context)
                .load(getIconRessourceUri(info))
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);
        return imageView;
    }

    private Uri getIconRessourceUri(ResolveInfo info) {
        return getResourceUri(info.activityInfo.applicationInfo.packageName,
                              info.getIconResource());
    }

    private Uri getResourceUri(String packageName, int resId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
        builder.authority(packageName);
        builder.appendPath(Integer.toString(resId));
        return builder.build();
    }
}
