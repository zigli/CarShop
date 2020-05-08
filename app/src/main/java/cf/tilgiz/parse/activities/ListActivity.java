package cf.tilgiz.parse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import cf.tilgiz.parse.R;
import cf.tilgiz.parse.adapters.CarsAdapter;
import cf.tilgiz.parse.dao.CarsContract;
import cf.tilgiz.parse.pojo.Car;
import cf.tilgiz.parse.utils.NetworkUtils;
import cf.tilgiz.parse.utils.Parser;

import static cf.tilgiz.parse.crud.DbQueries.getBasketCount;
import static cf.tilgiz.parse.crud.DbQueries.getAllItemsFromDb;

public class ListActivity extends AppCompatActivity {

    private static final String CONTENT_URL = "https://www.tts.ru/sprobegom/";
    ArrayList<Car> carList;
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
        setContentView(R.layout.activity_main);

        setTitle("Список Авто");

        RecyclerView recyclerViewCars = findViewById(R.id.recyclerViewCars);
        carList = new ArrayList<>();

        basketItemCount = getBasketCount(this);
        invalidateOptionsMenu();

        boolean fromLoginActivity = false;
        Intent intentLogin = getIntent();
        if (intentLogin.hasExtra("fromLoginActivity"))
            fromLoginActivity = intentLogin.getBooleanExtra("fromLoginActivity", false);

//        carList.clear();
        if(fromLoginActivity) {
            String output = NetworkUtils.loadContent(CONTENT_URL);
            if (!output.isEmpty()) {
                Parser parser = new Parser(getApplicationContext());
                parser.parseContent(output);
                Toast.makeText(ListActivity.this, "База обновилась!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListActivity.this, "Ошибка получения данных!", Toast.LENGTH_SHORT).show();
            }
        }

        carList = getAllItemsFromDb(this, CarsContract.NoteEntry.TABLE_NAME_CARS);

        CarsAdapter adapter = new CarsAdapter(carList);
        recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCars.setAdapter(adapter);

        adapter.setOnCarClickListener(position -> {
            Car car = carList.get(position);
            Intent intent = new Intent(getApplicationContext(), CarDetailsActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("id", car.getId());
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
    }
}
