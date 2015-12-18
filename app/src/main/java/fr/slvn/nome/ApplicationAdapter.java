package fr.slvn.nome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApplicationAdapter extends BaseAdapter {

    private static final String TAG = "ApplicationAdapter";

    private Context context;
    private LayoutInflater inflater;
    private List<LauncherActivityInfo> applications;

    public ApplicationAdapter(Context context, List<LauncherActivityInfo> applications) {
        this.context = context;
        this.applications = applications;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @Override
    public Object getItem(int position) {
        return applications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return applications.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = (ImageView) inflater.inflate(R.layout.icon, parent, false);
        } else {
            imageView = (ImageView) convertView;
        }
        LauncherActivityInfo info = (LauncherActivityInfo) getItem(position);
        Picasso.with(context)
                .load(getIconRessourceUri(info))
                .placeholder(R.drawable.ic_placeholder)
                .into(imageView);
        return imageView;
    }

    private Uri getIconRessourceUri(LauncherActivityInfo info) {
        return getResourceUri(info.getApplicationInfo().packageName,
                              info.getApplicationInfo().icon);
    }

    private Uri getResourceUri(String packageName, int resId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
        builder.authority(packageName);
        builder.appendPath(Integer.toString(resId));
        return builder.build();
    }

    public static class DisplayNameComparator implements Comparator<LauncherActivityInfo> {

        public DisplayNameComparator() {
        }

        public final int compare(LauncherActivityInfo a, LauncherActivityInfo b) {
            CharSequence  sa = a.getLabel();
            if (sa == null) sa = a.getName();
            CharSequence  sb = b.getLabel();
            if (sb == null) sb = b.getName();
            return collator.compare(sa.toString(), sb.toString());
        }

        private final Collator collator = Collator.getInstance();
    }
}
