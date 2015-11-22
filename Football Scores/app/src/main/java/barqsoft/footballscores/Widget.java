package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.R;

/**
 * Created by Abdel on 10/11/2015.
 */
public class Widget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int appWidgetId:appWidgetIds){

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent start = new Intent(context, MainActivity.class);
            PendingIntent startPending = PendingIntent.getActivity(context, 0, start, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.scores_list, startPending);

            views.setRemoteAdapter(R.id.scores_list, intent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }

    }


}
