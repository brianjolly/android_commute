package com.brianjolly.commute.model;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.net.Uri;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

//public class SimpleFinchVideoContentProvider extends ContentProvider {
public class LocationContentProvider extends ContentProvider {
    private static final String DATABASE_NAME = "simple_video.db";
    private static final int DATABASE_VERSION = 2; 
    private static final String VIDEO_TABLE_NAME = "video";
    private DatabaseHelper mOpenHelper;

    private static UriMatcher sUriMatcher;

    //private static final int VIDEOS = 1;
    //private static final int VIDEO_ID = 2;
    private static final int TRIPS = 1;
    private static final int TRIP_ID = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //sUriMatcher.addURI(AUTHORITY, FinchVideo.SimpleVideos.VIDEO, VIDEOS);
        sUriMatcher.addURI(CommuteTrips.Trips.AUTHORITY, CommuteTrips.Trips.TRIP, TRIPS);
        // use of the hash character indicates matching of an id
        sUriMatcher.addURI(CommuteTrips.Trips.AUTHORITY, CommuteTrips.Trips.TRIP + "/#", TRIP_ID);
    }

    @Override public boolean onCreate() { }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createTable(sqLiteDatabase);
        }

        // create table method may also be called from onUpgrade
        private void createTable(SQLiteDatabase sqLiteDatabase) {
            String tripQs = "CREATE TABLE " + CommuteTrips.Trips.TRIP_TABLE_NAME + " (" +
                CommuteTrips.Trips._ID + " INTEGER PRIMARY KEY, " +
                CommuteTrips.Trips.NAME+ " TEXT, " +
                CommuteTrips.Trips.DATE+ " INTEGER);";
            sqLiteDatabase.execSQL(tripQs);
            // TODO: I'm not sure if this is the right way to run two sql statements
            String locQs = "CREATE TABLE " + CommuteTrips.Trips.LOCATION_TABLE_NAME + " (" +
                CommuteTrips.Locations._ID + " INTEGER PRIMARY KEY, " +
                CommuteTrips.Locations.LATITUDE+ " REAL, " +
                CommuteTrips.Locations.LONGITUDE+ " REAL, " +
                CommuteTrips.Locations.ALTITITUDE+ " REAL, " +
                CommuteTrips.Locations.TIME+ " INTEGER);";
            sqLiteDatabase.execSQL(locQs);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            //case VIDEOS:
            case TRIPS:
                return CommuteTrips.Trips.CONTENT_TYPE;
                //return FinchVideo.SimpleVideos.CONTENT_TYPE;

            case TRIP_ID:
                return CommuteTrips.Trips.CONTENT_TRIP_TYPE;
                //return FinchVideo.SimpleVideos.CONTENT_VIDEO_TYPE;

            default:
                throw new IllegalArgumentException("Unknown trip type: " + uri);
        }
    }

    @Override public Cursor query(Uri uri, String[] projection, String where, 
            String[] whereArgs, String sortOrder) {
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = CommuteTrips.Trips.DEFAULT_SORT_ORDER;
            //orderBy = FinchVideo.SimpleVideos.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }        

        int match = sUriMatcher.match(uri);

        Cursor c;

        switch (match) {
           // case VIDEOS:
            case TRIPS:
                // query the database for all videos
                //c = mDb.query(CommuteTrips.Trips.TRIP_TABLE_NAME, projection,
                c = mOpenHelper.query(CommuteTrips.Trips.TRIP_TABLE_NAME, projection,
                        where, whereArgs,
                        null, null, sortOrder);

                // the call to notify the uri after deletion is explicit
                c.setNotificationUri(mContentResolver,
                        CommuteTrips.Trips.CONTENT_URI);
                break;
            case TRIP_ID:
                // query the database for a specific video
                long tripID = ContentUris.parseId(uri);
                //c = mDb.query(CommuteTrips.Trips.TRIP_TABLE_NAME, projection,
                c = mOpenHelper.query(CommuteTrips.Trips.TRIP_TABLE_NAME, projection,
                        CommuteTrips.Trips._ID + " = " + videoID +
                        (!TextUtils.isEmpty(where) ?
                         " AND (" + where + ')' : ""),
                        whereArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }

        return c;
    }

    @Override public Uri insert(Uri uri, ContentValues values) {
        // Validate the requested uri
        int m = sUriMatcher.match(uri);
        if (m != TRIPS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        int match = sUriMatcher.match(uri);
        Uri insertUri = null;
        switch (match) {
            case TRIPS:
                // insert the values into a new database row
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                long rowId = db.insert(CommuteTrips.Trips.TRIP_TABLE_NAME,
                        CommuteTrips.Trips.TRIP, values);
                if (rowId > 0) {
                    insertUri =
                        ContentUris.withAppendedId(
                                CommuteTrips.Trips.CONTENT_URI, rowId);
                    // the call to notify the uri after deletion is explicit
                    mContentResolver.notifyChange(insertUri, null);
                }
                break;
            case TRIP_ID:
                break;
            default:
                throw new IllegalArgumentException("unknown trip: " +
                        uri);
        }

        return insertUri;
    }

    @Override public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        mContentResolver.notifyChange(uri, null);

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int affected;
        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                affected = db.update(TRIP_TABLE_NAME, values, where, whereArgs);
                break;

            case TRIP_ID:
                String videoId = uri.getPathSegments().get(1);
                affected = db.update(CommuteTrips.Trips.TRIP_TABLE_NAME, values,
                        CommuteTrips.Trips._ID + "=" + videoId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // the call to notify the uri after deletion is explicit
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override public int delete(Uri uri, String where, String[] whereArgs) {
        int match = sUriMatcher.match(uri);
        int affected;

        switch (match) {
            case TRIPS:
                //affected = mDb.delete(CommuteTrips.Trips.TRIP_TABLE_NAME,
                affected = mOpenHelper.delete(CommuteTrips.Trips.TRIP_TABLE_NAME,
                        (!TextUtils.isEmpty(where) ?
                         " AND (" + where + ')' : ""),
                        whereArgs);
                break;
            case TRIP_ID:
                long videoId = ContentUris.parseId(uri);
                //affected = mDb.delete(CommuteTrips.Trips.TRIP_TABLE_NAME,
                affected = mOpenHelper.delete(CommuteTrips.Trips.TRIP_TABLE_NAME,
                        FinchVideo.SimpleVideos._ID + "=" + videoId
                        + (!TextUtils.isEmpty(where) ?
                            " AND (" + where + ')' : ""),
                        whereArgs);
                // the call to notify the uri after deletion is explicit
                mContentResolver.notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("unknown trip: " +
                        uri);
        }
        return affected;
    }

}
