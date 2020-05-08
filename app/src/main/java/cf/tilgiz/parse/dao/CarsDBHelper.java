package cf.tilgiz.parse.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarsDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME= "cars.db";
    private static final int DB_VERSION = 8;

    public CarsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CarsContract.NoteEntry.CREATE_COMMAND_CARS);
        db.execSQL(CarsContract.NoteEntry.CREATE_COMMAND_BASKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CarsContract.NoteEntry.DROP_COMMAND_CARS);
        db.execSQL(CarsContract.NoteEntry.DROP_COMMAND_BASKET);
        onCreate(db);
    }
}
