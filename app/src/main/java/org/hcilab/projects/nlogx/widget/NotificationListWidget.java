package org.hcilab.projects.nlogx.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.widget.RemoteViews;

import org.hcilab.projects.nlogx.R;
import org.hcilab.projects.nlogx.ui.BrowseActivity;

import org.hcilab.projects.nlogx.ui.MainActivity;


/**
 * Implementation of App Widget functionality.
 */
public class NotificationListWidget extends AppWidgetProvider {
    static RemoteViews views;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
         views = new RemoteViews(context.getPackageName(), R.layout.notification_list_widget);


        views.setTextViewText(R.id.appwidget_text, context.getString(R.string.app_name));//set app title on widget
        //TODO: set adapter to list
        views.setRemoteAdapter(R.id.widget_list_view,new Intent(context,ExampleWidgetService.class));


        //TODO: launch browser activity
        views.setPendingIntentTemplate(R.id.widget_list_view, PendingIntent.getActivity(context, appWidgetId,
                    new Intent(context,BrowseActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        views.setOnClickPendingIntent(R.id.view_all_text, PendingIntent.getActivity(context, appWidgetId, new Intent(context, BrowseActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));



        //TODO: launch main activity on title widget clicked
        views.setOnClickPendingIntent(R.id.appwidget_text, PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0));





        //TODO: Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_list_view);

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context,appWidgetManager,appWidgetIds);
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }
}

