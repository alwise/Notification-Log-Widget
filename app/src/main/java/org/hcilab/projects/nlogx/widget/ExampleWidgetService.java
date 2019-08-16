package org.hcilab.projects.nlogx.widget;


import android.content.Intent;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.android.gms.common.api.internal.RemoteCall;


public class ExampleWidgetService extends RemoteViewsService  {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ExampleWidgetAdapter(getApplicationContext(),intent);
    }


}
