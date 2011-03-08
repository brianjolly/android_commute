package com.brianjolly.commute.model;

import java.util.HashMap;
import android.content.ContentProvider;
import android.content.UriMatcher;
import android.net.Uri;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.content.Context;
import com.brianjolly.commute.model.CommuteTrips;
import com.brianjolly.commute.model.CommuteTrips.Trip;
import com.brianjolly.commute.model.CommuteTrips.Location;

public class LocationContentProvider extends ContentProvider {

    private static final String TAG = "LocationContentProvider";
	
    private static final String DATABASE_NAME = "simple_video.db";
    private static final int DATABASE_VERSION = 1; 
    private static final String TRIP_TABLE_NAME = "trip";
    private DatabaseHelper mOpenHelper;

    private static UriMatcher sUriMatcher;
    private static HashMap sTripsProjectionMap;
    private static HashMap sLocationsProjectionMap;

    private static final int TRIPS = 1;
    private static final int TRIP_ID = 2;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(CommuteTrips.Trip.AUTHORITY, CommuteTrips.Trip.TRIP, TRIPS);
        sUriMatcher.addURI(CommuteTrips.Trip.AUTHORITY, CommuteTrips.Trip.TRIP + "/#", TRIP_ID);

		sTripsProjectionMap = new HashMap<String, String>();
		sTripsProjectionMap.put(Trip.NAME, Trip.NAME);
		sTripsProjectionMap.put(Trip.DATE, Trip.DATE);

		sLocationsProjectionMap = new HashMap<String, String>();
		sLocationsProjectionMap.put(Location.LATITUDE, Location.LATITUDE);
		sLocationsProjectionMap.put(Location.LONGITUDE, Location.LONGITUDE);
		sLocationsProjectionMap.put(Location.ALTITUDE, Location.ALTITUDE);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            createTable(sqLiteDatabase);
        }

        // create table method may also be called from onUpgrade
        private void createTable(SQLiteDatabase sqLiteDatabase) {
            String tripQs = "CREATE TABLE " + CommuteTrips.Trip.TRIP_TABLE_NAME + " (" +
                CommuteTrips.Trip._ID + " INTEGER PRIMARY KEY, " +
                CommuteTrips.Trip.NAME+ " TEXT, " +
                CommuteTrips.Trip.DATE+ " INTEGER);";
            sqLiteDatabase.execSQL(tripQs);
            // TODO: I'm not sure if this is the right way to run two sql statements
            String locQs = "CREATE TABLE " + CommuteTrips.Location.LOCATION_TABLE_NAME + " (" +
                CommuteTrips.Location._ID + " INTEGER PRIMARY KEY, " +
                CommuteTrips.Location.LATITUDE+ " REAL, " +
                CommuteTrips.Location.LONGITUDE+ " REAL, " +
                CommuteTrips.Location.ALTITUDE+ " REAL, " +
                CommuteTrips.Location.TIME+ " INTEGER);";
            sqLiteDatabase.execSQL(locQs);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
					newVersion + ", which will destroy all old data"); */
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
    }

	@Override public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case TRIPS:
                return CommuteTrips.Trip.CONTENT_TYPE;
                //return FinchVideo.SimpleVideos.CONTENT_TYPE;

            case TRIP_ID:
                return CommuteTrips.Trip.CONTENT_TRIP_TYPE;
                //return FinchVideo.SimpleVideos.CONTENT_VIDEO_TYPE;

            default:
                throw new IllegalArgumentException("Unknown trip type: " + uri);
        }
    }

    @Override public Cursor query(Uri uri, String[] projection, String where, 
            String[] whereArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TRIP_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case TRIPS:
				qb.setProjectionMap(sTripsProjectionMap);
                break;
            case TRIP_ID:
				qb.setProjectionMap(sTripsProjectionMap);
				qb.appendWhere(Trip._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("unsupported uri: " + uri);
        }
		
        // If no sort order is specified use the default
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = CommuteTrips.Trip.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }        

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, where, whereArgs, null, null, sortOrder);

		// Tell the cursor what uri to watch, so it knows when it's source data changes
		c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override public Uri insert(Uri uri, ContentValues initialValues) {

		// Validate the requested uri
		 if (sUriMatcher.match(uri) != TRIPS) {
			throw new IllegalArgumentException("Unknown Uri " + uri);
		 }
		 ContentValues values;
		 if (initialValues != null) {
			 values = new ContentValues(initialValues);
		 } else {
			 values = new ContentValues();
		 }

		 Long now = Long.valueOf(System.currentTimeMillis());

		 // Make sure that the fields are all set
		 if (values.containsKey(CommuteTrips.Trip.NAME) == false) {
			 values.put(CommuteTrips.Trip.NAME, "");
		 }
		 if (values.containsKey(CommuteTrips.Trip.DATE) == false) {
			 values.put(CommuteTrips.Trip.DATE, now);
		 }

		 SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		 long rowId = db.insert(TRIP_TABLE_NAME, Trip.TRIP, values);
		 if (rowId > 0) {
			 Uri tripUri = ContentUris.withAppendedId(CommuteTrips.Trip.CONTENT_URI, rowId);
			 getContext().getContentResolver().notifyChange(tripUri, null);
			 return tripUri;
		 }

		 throw new SQLiteException("Failed to insert row into " + uri);
	}

    @Override public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case TRIPS:
				count = db.update(TRIP_TABLE_NAME, values, where, whereArgs);
				break;
			case TRIP_ID:
				String tripId = uri.getPathSegments().get(1);
				count = db.update(TRIP_TABLE_NAME, values, Trip._ID + "=" + tripId
						+ (!TextUtils.isEmpty(where) ? "AND (" + where + ')': ""), whereArgs);
					break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
    }

    @Override public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case TRIPS:
				count = db.delete(TRIP_TABLE_NAME, where, whereArgs);
				break;
			case TRIP_ID:
				String tripId = uri.getPathSegments().get(1);
				count = db.delete(TRIP_TABLE_NAME, Trip._ID + "=" + tripId
						+ (!TextUtils.isEmpty(where) ? "AND (" + where + ')': ""), whereArgs);
					break;
			default: 
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
    }

}
