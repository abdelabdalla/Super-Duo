package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Abdel on 22/11/2015.
 */
public class WidgetService extends RemoteViewsService {

    private static final String[] columns = {
            DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
    };

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CollectionFactory();
    }

    class CollectionFactory implements
            RemoteViewsService.RemoteViewsFactory {

        private Cursor data = null;

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }
            Uri todaysScoresUri = DatabaseContract.scores_table.buildScoreWithDate();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            data = getContentResolver().query(todaysScoresUri,
                    columns,
                    null,
                    new String[]{dateFormatter.format(c.getTime())},
                    DatabaseContract.scores_table.DATE_COL + " ASC");

        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }
            RemoteViews remoteViews = new RemoteViews(getPackageName(),
                    R.layout.widget_list_item);

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("EXTRA_ITEM", position);
            remoteViews.setOnClickFillInIntent(R.id.scores_list, fillInIntent);

            String homeTeam = data.getString(1);
            Integer homeGoals = data.getInt(2);
            String awayTeam = data.getString(3);
            Integer awayGoals = data.getInt(4);
            remoteViews.setTextViewText(R.id.home, homeTeam);


            if (homeGoals > -1 && awayGoals > -1) {
                remoteViews.setTextViewText(R.id.score, homeGoals.toString() + " - " + awayGoals.toString());
            } else if (homeGoals <= -1) {
                remoteViews.setTextViewText(R.id.score, "no score");
            }
            remoteViews.setTextViewText(R.id.away, awayTeam);


            return remoteViews;
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
            if (data.moveToPosition(i)){
                return data.getLong(0);
            }
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
