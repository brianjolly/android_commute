package com.brianjolly.commute.model;

/**
 * AndroidManifest.xml
 * SimpleFinchVideoActivity.java
 * video/FinchVideo.java
 * video/SimpleFinchVideoContentProvider.java
 *  * Simple Videos columns
 *   */

import android.provider.BaseColumns;
import android.net.Uri;

//public class FinchVideo {
public class CommuteTrips {

    //public static final class SimpleVideos implements BaseColumns {
    public static final class Trip implements BaseColumns {
        
        // This class cannot be instantiated
        //private SimpleVideos() {}
        private Trip() {}

        public static final String AUTHORITY = "com.brianjolly.commute.model.CommuteTrips";
        public static final String DEFAULT_SORT_ORDER = "DESC"; //TODO: check if this is a real sort order

        // uri references all trips 
        public static final Uri TRIPS_URI = Uri.parse("content://" + AUTHORITY + "/" + Trip.TRIP);

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = TRIPS_URI;

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of trips.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.commute.trip";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * trip.
         */
        public static final String CONTENT_TRIP_TYPE = "vnd.android.cursor.item/vnd.commute.trip";

        /**
         * The trip itself
         * <P>Type: TEXT</P>
         */
        public static final String TRIP = "trip";

        /**
         * The trip table name 
         */
        public static final String TRIP_TABLE_NAME = "trip_table";

        /**
         * Column name for the name of the trip 
         * <P>Type: TEXT</P>
         */
        public static final String NAME = "name";

        /**
         * Column name for the date of the trip. 
         * <P>Type: INT</P>
         */
        public static final String DATE = "date";

    }

    public static final class Location implements BaseColumns {
        // This class cannot be instantiated
        private Location() {}

        public static final String AUTHORITY = "com.brianjolly.commute.model.Location";
        public static final String DEFAULT_SORT_ORDER = "DESC"; //TODO: check if this is a real sort order

        // uri references all locations 
        public static final Uri LOCATIONS_URI = Uri.parse("content://" + AUTHORITY + "/" + Location.LOCATION);

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = LOCATIONS_URI;

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of trips.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.commute.location";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
         * location.
         */
        public static final String CONTENT_LOCATION_TYPE = "vnd.android.cursor.item/vnd.commute.location";

        /**
         * The location itself
         * <P>Type: TEXT</P>
         */
        public static final String LOCATION = "location";

        /**
         * The location table name 
         */
        public static final String LOCATION_TABLE_NAME = "location_table";

        /**
         * Column name for latitude
         * <P>Type: REAL</P>
         */
        public static final String LATITUDE = "latitude";

        /**
         * Column name for longitude
         * <P>Type: REAL</P>
         */
        public static final String LONGITUDE = "longitude";

        /**
         * Column name for altitude 
         * <P>Type: REAL</P>
         */
        public static final String ALTITUDE = "altitude";

        /**
         * Column name for time stamp 
         * <P>Type: INT</P>
         */
        public static final String TIME = "time";

    }

} 
