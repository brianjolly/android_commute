package com.brianjolly.commute;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class TripsOpenHelper extends SQLiteOpenHelper {
  // following tutorial at
  // http://droidreign.com/2010/10/dev-tutorials-android-sqlite-database-basics/
  // and
  // http://www.screaming-penguin.com/node/7742

  private static final String DATABASE_NAME = "commute.db";
  private static final int DATABASE_VERSION = 1;
  private static final String TRIPS_TABLE = "Trips";
  private static final String COL_ID = "TripID";
  private static final String COL_TIME = "Time";
  private static final String COL_LATITUDE = "Latitude";
  private static final String COL_LONGITUDE = "Longitude";
  private static final String COL_ALTITUDE = "Altitude";
  private static final String COL_ACCURACY = "Accuracy";
  private static final String COL_SPEED = "Speed";

  private static final String VIEW_TRIPS = "ViewTrips";


  TripsOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  /* SQLite Datatypes
   *
   * NULL. The value is a NULL value.
   * INTEGER. The value is a signed integer, stored in 1,2,3,4,6,or 8 bytes depending on the magnitude of the value.
   * REAL. The value is a floating point value, stored as an 8-byte IEEE floating point number.
   * TEXT. The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
   * BLOB. The value is a blob of data, stored exactly as it was input.
   *
   */

  @Override
    public void onCreate(SQLiteDatabase db) {
      
       db.execSQL("CREATE TABLE "+TRIPS_TABLE+" 
                    ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ 
                       COL_TIME+" INTEGER, "+
                       COL_LATITUDE+" REAL, "+
                       COL_LONGITUDE+" REAL, "+
                       COL_ALTITUDE+" REAL, "+
                       COL_ACCURACY+" REAL, "+
                       COL_SPEED+" REAL "+
                    )");
      
       db.execSQL("CREATE VIEW "+VIEW_TRIPS+
                    " AS SELECT "+TRIPS_TABLE+"."+COL_ID+" AS _id,"+
                    "           "+TRIPS_TABLE+"."+COL_TIME+","+
                    "           "+TRIPS_TABLE+"."+COL_LATITUDE+","+
                    "           "+TRIPS_TABLE+"."+COL_LONGITUDE+","+
                    "           "+TRIPS_TABLE+"."+COL_ALTITUDE+","+
                    "           "+TRIPS_TABLE+"."+COL_ACCURACY+","+
                    "           "+TRIPS_TABLE+"."+COL_SPEED+","+
                    " FROM "+TRIPS_TABLE+")"
                  );

       //Inserts pre-defined departments
       InsertDepts(db);
    }

  @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+TRIPS_TABLE);
      db.execSQL("DROP VIEW IF EXISTS "+VIEW_TRIPS);
      onCreate(db);
    }

  /*
   * SQLiteDatabase db=this.getWritableDatabase();
   *   ContentValues cv=new ContentValues();
   *     cv.put(colDeptID, 1);
   *     cv.put(colDeptName, "Sales");
   *     db.insert(deptTable, colDeptID, cv);
   *
   *     cv.put(colDeptID, 2);
   *     cv.put(colDeptName, "IT");
   *     db.insert(deptTable, colDeptID, cv);
   *     db.close();
   */

  /* public int UpdateEmp(Employee emp)
   * {
   *    SQLiteDatabase db=this.getWritableDatabase();
   *      ContentValues cv=new ContentValues();
   *      cv.put(colName, emp.getName());
   *      cv.put(colAge, emp.getAge());
   *      cv.put(colDept, emp.getDept());
   *      return db.update(employeeTable, cv, colID+"=?", new String []{String.valueOf(emp.getID())});
   * }
   * the update method has the following parameters:
   *
   * String Table: the table to update a value in
   * ContentValues cv: the content values object that has the new values
   * String where clause: the WHERE clause to specify which record to update.
   * String[] args: the arguments of the WHERE clause.
   */

  /* public void DeleteEmp(Employee emp)
   * {
   *    SQLiteDatabase db=this.getWritableDatabase();
   *      db.delete(employeeTable,colID+"=?", new String [] {String.valueOf(emp.getID())});
   *      db.close();
   * }
   * the delete method has the same parameters as the update method.
   */

  /* to execute a raw query to retreive all departments
   * Cursor getAllDepts()
   * {
   *     SQLiteDatabase db=this.getReadableDatabase();
   *     Cursor cur=db.rawQuery("SELECT "+colDeptID+" as _id, "+colDeptName+" from "+deptTable,new String [] {});
   *
   *     return cur;
   * }
   * the rawQuery method has two parameters:
   *
   * String query: the select statement.
   * String[] selection args: the arguments if a WHERE clause is included in the select statement.
   * Notes:
   *
   * The result of a query is returned in Cursor object.
   * In a select statement if the primary key column (the id column) of the table has a name other than _id then you have to use an alias in the form SELECT [Column Name] as _id
   * cause the Cursor object always expects that the primary key column has the name _id or it will throw an exception .
   */

  /* another way to perform a query is to use a db.query method. 
   * A query to select all employees in a certain department from a view would be like this:
   *
   * public Cursor getEmpByDept(String Dept)
   * {
   *     SQLiteDatabase db=this.getReadableDatabase();
   *     String [] columns=new String[]{"_id",colName,colAge,colDeptName};
   *     Cursor c=db.query(viewEmps, columns, colDeptName+"=?", new String[]{Dept}, null, null, null);
   *     return c;
   * }
   * the db.query has the folowing parameters:
   *
   * String Table Name: the name of the table to run the query against.
   * String [ ] columns: the projection of the query i.e the columns to retrieve.
   * String WHERE clause: where clause, if none pass null.
   * String [ ] selection args: the parameters of the WHERE clause.
   * String Group by: a string specifying group by clause.
   * String Having: a string specifying HAVING clause.
   * String Order By by: a string Order By by clause.
   */

  /*Managing Cursors:
   *
   * Result sets of queries are returned in Cursor objects.
   * there are some common methdos that you will use with cursors:
   *
   * boolean moveToNext(): moves the cursor by one record in the result set, returns false if moved past the last row in the result set.
   * boolean moveToFirst(): moves the cursor to the first row in the result set, returns false if the result set is empty.
   * boolean moveToPosition(int position): moves the cursor to a certain row index within the boolean result set, returns false if the position is un-reachable
   * boolean moveToPrevious():moves the cursor to the preevious row in the result set, returns false if the cursor is past the first row.
   * boolean moveToLast():moves the cursor to the lase row in the result set, returns false if the result set is empty.
   * there are also some useful methods to check the position of a cursor:
   * boolean isAfterLast(), isBeforeFirst, isFirst,isLast and isNull(columnIndex).
   */

  /* also if you have a result set of only one row and you need to retreive values of certain columns, you can do it like this:
   *
   * public int GetDeptID(String Dept)
   * {
   *    SQLiteDatabase db=this.getReadableDatabase();
   *    Cursor c=db.query(deptTable, new String[]{colDeptID+" as _id",colDeptName},colDeptName+"=?", new String[]{Dept}, null, null, null);
   *    //Cursor c=db.rawQuery("SELECT "+colDeptID+" as _id FROM "+deptTable+" WHERE "+colDeptName+"=?", new String []{Dept});
   *    c.moveToFirst();
   *    return c.getInt(c.getColumnIndex("_id"));
   *
   * }
   * we have Cursor.getColumnIndex(String ColumnName) to get the index of a column.
   *
   * then to get the value of a certain column we have Cursor.getInt(int ColumnIndex) method.
   * also there are getShort,getString,getDouble, getBlob to return the value as a byte array.
   *
   * it's a good practice to close() the cursor after using it.
   */



}
