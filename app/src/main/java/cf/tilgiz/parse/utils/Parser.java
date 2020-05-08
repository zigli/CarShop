package cf.tilgiz.parse.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cf.tilgiz.parse.dao.CarsContract;
import cf.tilgiz.parse.dao.CarsDBHelper;

import static cf.tilgiz.parse.crud.DbQueries.insertItems;
import static cf.tilgiz.parse.crud.DbQueries.removeAllItemsFromTable;

public class Parser {

    private Context context;

    public Parser(Context context) {
        this.context = context;
    }

    public void parseContent(String content) {

        // Clear table
        removeAllItemsFromTable(context,CarsContract.NoteEntry.TABLE_NAME_CARS);
//        database.delete(CarsContract.NoteEntry.TABLE_NAME_CARS, null, null);

        Pattern pattern = Pattern.compile("<div class=\"preview-card_auto-wrapper vers2\" id=\"(.*?)\">(.*?)<img class=\"img\" src=\"(.*?)\" alt=\"(.*?)<div class=\"descriprion-model\" title=\"(.*?)\">(.*?)Год выпуска: (.*?)</div>(.*?)Пробег: (.*?) км</div>(.*?)<span>(.*?)</span>");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int carId = Integer.parseInt(matcher.group(1)); // id
            String imageUrl = matcher.group(3); // url
            String name = matcher.group(5); // name
            int year = Integer.parseInt(matcher.group(7).replaceAll("\\s+", "")); //year
            int mileage = Integer.parseInt(matcher.group(9).replaceAll("\\s+", "")); //mileage
            int price = Integer.parseInt(matcher.group(11).replaceAll("\\s+", "")); //price

            ContentValues contentValues = new ContentValues();
            contentValues.put(CarsContract.NoteEntry.COLUNM_CAR_ID, carId);
            contentValues.put(CarsContract.NoteEntry.COLUNM_IMAGE_URL, imageUrl);
            contentValues.put(CarsContract.NoteEntry.COLUNM_NAME, name);
            contentValues.put(CarsContract.NoteEntry.COLUNM_YEAR, year);
            contentValues.put(CarsContract.NoteEntry.COLUNM_MILEAGE, mileage);
            contentValues.put(CarsContract.NoteEntry.COLUNM_PRICE, price);
            Bitmap bitmap = NetworkUtils.getPicture(imageUrl);
            contentValues.put(CarsContract.NoteEntry.COLUNM_IMAGE_BLOB, getBitmapAsByteArray(bitmap));

            // Fill up table
            insertItems(context,CarsContract.NoteEntry.TABLE_NAME_CARS,contentValues);
//            database.insert(CarsContract.NoteEntry.TABLE_NAME_CARS, null, contentValues);
        }

    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
