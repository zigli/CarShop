package cf.tilgiz.parse.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cf.tilgiz.parse.R;
import cf.tilgiz.parse.dao.CarsContract;
import cf.tilgiz.parse.dao.CarsDBHelper;
import cf.tilgiz.parse.pojo.Car;

import static cf.tilgiz.parse.crud.DbQueries.getBasketCount;
import static cf.tilgiz.parse.crud.DbQueries.getItemCount;
import static cf.tilgiz.parse.crud.DbQueries.getItemFromDb;
import static cf.tilgiz.parse.crud.DbQueries.insertItems;

public class CarDetailsActivity extends AppCompatActivity {

    private Button buttonAddToBasket;
    private CarsDBHelper dbHelper;
    private SQLiteDatabase database;
    private Car car;
    private int basketItemCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basket_menu, menu);
        if (basketItemCount > 0) {
            menu.findItem(R.id.basket).setTitle("Корзина (" + basketItemCount + ")");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.basket:
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        setTitle("Информация по авто");

        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewYear = findViewById(R.id.textViewYear);
        TextView textViewMileAge = findViewById(R.id.textViewMileAge);
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        ImageView imageViewCar = findViewById(R.id.imageViewCar);
        buttonAddToBasket = findViewById(R.id.buttonAddToBasket);

        basketItemCount = getBasketCount(this);
        invalidateOptionsMenu();

        Intent intent = getIntent();
        if (intent.hasExtra("position")) {
            int id = intent.getIntExtra("id", -1);

            car = getItemFromDb(this, CarsContract.NoteEntry.TABLE_NAME_CARS, id);
            textViewName.setText(car.getName());
            textViewMileAge.setText(String.valueOf(car.getMileage()));
            textViewYear.setText(String.valueOf(car.getYear()));
            textViewPrice.setText(String.valueOf(car.getPrice()));
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImageBytes(), 0, car.getImageBytes().length);
            imageViewCar.setImageBitmap(bitmap);

            buttonAddToBasket.setOnClickListener(v -> {
                int rowId = addToBasket(car);
                if (rowId != 0) {
                    basketItemCount = getBasketCount(this);
                    invalidateOptionsMenu();
                } else {
                    Toast.makeText(this, "Уже в корзине!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed");
        Intent setIntent = new Intent(this,ListActivity.class);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private int addToBasket(Car car) {
        int rowID = 0;
        if (getItemCount(this, CarsContract.NoteEntry.TABLE_NAME_BASKET, car.getCar_id()) == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CarsContract.NoteEntry.COLUNM_CAR_ID, car.getCar_id());
            contentValues.put(CarsContract.NoteEntry.COLUNM_IMAGE_URL, car.getImageUrl());
            contentValues.put(CarsContract.NoteEntry.COLUNM_NAME, car.getName());
            contentValues.put(CarsContract.NoteEntry.COLUNM_YEAR, car.getYear());
            contentValues.put(CarsContract.NoteEntry.COLUNM_MILEAGE, car.getMileage());
            contentValues.put(CarsContract.NoteEntry.COLUNM_PRICE, car.getPrice());
            contentValues.put(CarsContract.NoteEntry.COLUNM_IMAGE_BLOB, car.getImageBytes());
            rowID = insertItems(this, CarsContract.NoteEntry.TABLE_NAME_BASKET, contentValues);
        }
        return rowID;
    }


}
