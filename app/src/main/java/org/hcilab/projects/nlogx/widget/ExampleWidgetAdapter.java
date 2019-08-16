package org.hcilab.projects.nlogx.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.hcilab.projects.nlogx.R;
import org.hcilab.projects.nlogx.misc.Const;
import org.hcilab.projects.nlogx.misc.DatabaseHelper;
import org.hcilab.projects.nlogx.ui.BrowseAdapter;
import org.hcilab.projects.nlogx.ui.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import static org.hcilab.projects.nlogx.ui.BrowseAdapter.iconCache;

public class  ExampleWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    List<BrowseAdapter.DataItem> mCollection =  new ArrayList<>();
    private BrowseAdapter.DataItem item;
    private Intent intent;
    private Cursor cursor;
    private final static String DATA_LIMIT = "15";


    ExampleWidgetAdapter(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        Toast.makeText(context,"Widget created.",Toast.LENGTH_SHORT).show();
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        if(cursor != null){
            loadData();

        }
    }

    @Override
    public void onDestroy() {
        //close dataSource
        mCollection.clear();
    }

    @Override
    public int getCount() {
        return mCollection != null ? mCollection.size() : 0;
    }


    @Override
    public RemoteViews getViewAt(int i) {
        item = mCollection.get(i);

        //TODO: reference list item to set data.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        views.setTextViewText(R.id.widget_id,""+item.getId());
        views.setTextViewText(R.id.widget_item_name,item.getAppName());//set app name
        views.setTextViewText(R.id.widget_item_preview,item.getPreview().length() == 0 ? item.getText() : item.getPreview());//set preview/text

        //TODO: set app icon
        if(iconCache.containsKey(item.getPackageName()) && iconCache.get(item.getPackageName()) != null) {
            Drawable d = iconCache.get(item.getPackageName());
            assert d != null;
            views.setImageViewBitmap(R.id.item_icon,getBitmap(d));
        } else {
            views.setImageViewResource(R.id.item_icon,R.mipmap.ic_launcher_round);
        }


        views.setOnClickFillInIntent(R.id.widget_item_root, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    //TODO: fetch data from data source...
    private void loadData(){
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            mCollection.clear();
            cursor = db.query(DatabaseHelper.PostedEntry.TABLE_NAME,
                    new String[] {
                            DatabaseHelper.PostedEntry._ID,
                            DatabaseHelper.PostedEntry.COLUMN_NAME_CONTENT
                    },
                    DatabaseHelper.PostedEntry._ID + " < ?",
                    new String[] {""+ Integer.MAX_VALUE},
                    null,
                    null,
                    DatabaseHelper.PostedEntry._ID + " DESC",
                    DATA_LIMIT);

            if(cursor != null && cursor.moveToFirst()) {
                for(int i = 0; i < cursor.getCount(); i++) {
                    BrowseAdapter.DataItem dataItem = new BrowseAdapter.DataItem(context, cursor.getLong(0), cursor.getString(1));
                    mCollection.add(dataItem);
                    cursor.moveToNext();
                }
                cursor.close();
            }

            db.close();
            databaseHelper.close();
        } catch (Exception e) {
            if(Const.DEBUG) e.printStackTrace();
        }


    }

    /**
     * converts drawable to bitmap using ARGB_8888
     * @param drawable : the drawable to draw into bitmap
     * @return bitmap
     */
    private Bitmap getBitmap(Drawable drawable){
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error here
            return null;
        }
    }
}