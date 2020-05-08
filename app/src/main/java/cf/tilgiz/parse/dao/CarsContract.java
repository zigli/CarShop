package cf.tilgiz.parse.dao;

import android.provider.BaseColumns;

public class CarsContract {
    public static final class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME_CARS = "cars";
        public static final String TABLE_NAME_BASKET = "basket";

        public static final String COLUNM_CAR_ID = "car_id";
        public static final String COLUNM_IMAGE_URL = "image_url";
        public static final String COLUNM_NAME = "name";
        public static final String COLUNM_YEAR = "year";
        public static final String COLUNM_MILEAGE = "mileage";
        public static final String COLUNM_PRICE = "price";
        public static final String COLUNM_IMAGE_BLOB = "image_bitmap";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";
        public static final String TYPE_BLOB = "BLOB";

        public static final String CREATE_COMMAND_CARS = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CARS + "(" +
                _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                COLUNM_CAR_ID + " " + TYPE_INTEGER + ", " +
                COLUNM_IMAGE_URL + " " + TYPE_TEXT + ", " +
                COLUNM_NAME + " " + TYPE_TEXT + ", " +
                COLUNM_YEAR + " " + TYPE_INTEGER + ", " +
                COLUNM_MILEAGE + " " + TYPE_INTEGER + ", " +
                COLUNM_PRICE + " " + TYPE_INTEGER + ", " +
                COLUNM_IMAGE_BLOB + " " + TYPE_BLOB +
                ")";

        public static final String CREATE_COMMAND_BASKET = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BASKET + "(" +
                _ID + " " + TYPE_INTEGER + " PRIMARY KEY, " +
                COLUNM_CAR_ID + " " + TYPE_INTEGER + ", " +
                COLUNM_IMAGE_URL + " " + TYPE_TEXT + ", " +
                COLUNM_NAME + " " + TYPE_TEXT + ", " +
                COLUNM_YEAR + " " + TYPE_INTEGER + ", " +
                COLUNM_MILEAGE + " " + TYPE_INTEGER + ", " +
                COLUNM_PRICE + " " + TYPE_INTEGER + ", " +
                COLUNM_IMAGE_BLOB + " " + TYPE_BLOB +
                ")";
        public static final String DROP_COMMAND_CARS = "DROP TABLE IF EXISTS " + TABLE_NAME_CARS;
        public static final String DROP_COMMAND_BASKET = "DROP TABLE IF EXISTS " + TABLE_NAME_BASKET;


    }
}
