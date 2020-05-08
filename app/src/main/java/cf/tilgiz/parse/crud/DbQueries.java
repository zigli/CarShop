package cf.tilgiz.parse.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cf.tilgiz.parse.dao.CarsContract;
import cf.tilgiz.parse.dao.CarsDBHelper;
import cf.tilgiz.parse.pojo.Car;

public class DbQueries {

    private static CarsDBHelper dbHelper;
    private static SQLiteDatabase database;

    public static int getBasketCount(Context context) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getWritableDatabase();

        Cursor cursor = null;
        try {
            String selectQuery = "SELECT  count(*) FROM " + CarsContract.NoteEntry.TABLE_NAME_BASKET;
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
    }

    public static ArrayList<Car> getAllItemsFromDb(Context context, String table_name) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();
        ArrayList<Car> arrayList = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = database.query(table_name, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry._ID));
                int carId = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_CAR_ID));
                String imageUrl = cursor.getString(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_IMAGE_URL));
                String name = cursor.getString(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_NAME));
                int year = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_YEAR));
                int mileage = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_MILEAGE));
                int price = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_PRICE));
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_IMAGE_BLOB));
                Car car = new Car(id, carId, imageUrl, name, year, mileage, price, imageByte);
                arrayList.add(car);
            }
            return arrayList;
        }finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
    }

    public static Car getItemFromDb(Context context, String table_name, int id) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();
        Car car = null;

        String where = CarsContract.NoteEntry._ID + " = ?";
        String[] whereArg = new String[]{Integer.toString(id)};
        Cursor cursor = null;
        try {
            cursor = database.query(table_name, null, where, whereArg, null, null, null);
            while (cursor.moveToNext()) {
                int carId = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_CAR_ID));
                String imageUrl = cursor.getString(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_IMAGE_URL));
                String name = cursor.getString(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_NAME));
                int year = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_YEAR));
                int mileage = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_MILEAGE));
                int price = cursor.getInt(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_PRICE));
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndex(CarsContract.NoteEntry.COLUNM_IMAGE_BLOB));
                car = new Car(id, carId, imageUrl, name, year, mileage, price, imageByte);
            }
            return car;
        }finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
    }

    public static int removeItemFromTable(Context context, ArrayList<Car> carList, String tableName, int position) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();

        int id = carList.get(position).getId();
        String where = CarsContract.NoteEntry._ID + " = ?";
        String[] whereArg = new String[]{Integer.toString(id)};
        int rowId = database.delete(tableName, where, whereArg);
        if (database != null) {
            database.close();
        }
        return rowId;
    }

    public static int removeAllItemsFromTable(Context context, String tableName) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();

        int rowId = database.delete(tableName, null, null);
        if (database != null) {
            database.close();
        }
        return rowId;
    }

    public static int insertItems(Context context, String tableName, ContentValues contentValues) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();

        int rowId = (int) database.insert(tableName, null, contentValues);
        if (database != null) {
            database.close();
        }
        return rowId;
    }

    public static int getItemCount(Context context, String tableName, int carId) {
        dbHelper = new CarsDBHelper(context);
        database = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            String where = CarsContract.NoteEntry.COLUNM_CAR_ID + " = ?";
            String[] whereArg = new String[]{Integer.toString(carId)};
            cursor = database.query(tableName, null, where, whereArg, null, null, null);
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                database.close();
            }
        }
    }
}
