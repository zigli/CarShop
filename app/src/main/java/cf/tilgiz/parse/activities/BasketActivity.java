package cf.tilgiz.parse.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cf.tilgiz.parse.R;
import cf.tilgiz.parse.adapters.CarsAdapter;
import cf.tilgiz.parse.dao.CarsContract;
import cf.tilgiz.parse.pojo.Car;
import cf.tilgiz.parse.utils.ApiRequest;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cf.tilgiz.parse.crud.DbQueries.getAllItemsFromDb;
import static cf.tilgiz.parse.crud.DbQueries.removeAllItemsFromTable;
import static cf.tilgiz.parse.crud.DbQueries.removeItemFromTable;

public class BasketActivity extends AppCompatActivity {

    private ArrayList<Car> carList;
    private CarsAdapter adapter;
    private Button buttonOrder;

    private ProgressBar progressBarOrder;
    private TextView textViewResultOrder;
    private SharedPreferences preferences;
    private static final String API_SERVER = "http://tilgiz.cf";
    private String username;
    private String saved_token;

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_SERVER)
            .build();

    private ApiRequest req = retrofit.create(ApiRequest.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        setTitle(getResources().getString(R.string.basket_text));

        RecyclerView recyclerViewCars = findViewById(R.id.recyclerViewBasket);
        buttonOrder = findViewById(R.id.buttonOrder);
        progressBarOrder = findViewById(R.id.progressBarOrder);
        textViewResultOrder = findViewById(R.id.textViewResultOrder);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        saved_token = preferences.getString("token", "");
        username = preferences.getString("username", "");

        carList = new ArrayList<>();

        carList.addAll(getAllItemsFromDb(this, CarsContract.NoteEntry.TABLE_NAME_BASKET));


        adapter = new CarsAdapter(carList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewCars.setLayoutManager(layoutManager);
        recyclerViewCars.setAdapter(adapter);

//        SnapHelper startSnapHelper = new StartSnapHelper();
//        SnapHelper startSnapHelper = new LinearSnapHelper();
//        startSnapHelper.attachToRecyclerView(recyclerViewCars);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewCars);

        if (carList.size() != 0) {
            buttonOrder.setOnClickListener(v -> {
                sendOrder();
            });
        } else {
            buttonOrder.setEnabled(false);
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

    private String makeOrderJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("token", saved_token);
        JSONArray jsonBasket = new JSONArray();
        for (int i = 0; i < carList.size(); i++) {
            Gson gson = new GsonBuilder().create();
            jsonBasket.put(new JSONObject(gson.toJson(carList.get(i))));
        }
        json.put("basket", jsonBasket);
        return json.toString();
    }

    private void sendOrder() {
        String postJsonString = null;
        try {
            postJsonString = makeOrderJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), postJsonString);
        Call<Object> call = req.requestPostOrder(body);
        //Show loading animation
        progressBarOrder.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // Hide loading animation
                progressBarOrder.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String api_response = jsonObject.getString("response");
                        if (api_response.equals("0")) {
                            textViewResultOrder.setTextColor(Color.GREEN);
                            textViewResultOrder.setText("Заказ оформлен!");
                            clearBasket();
                        } else {
                            textViewResultOrder.setText("Ошибка оформления!");
                        }
                    } catch (JSONException e) {
                        BasketActivity.this.actionOnAuthError();
                        e.printStackTrace();
                    }
                } else {
                    textViewResultOrder.setText(R.string.request_error);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // Hide loading animation
                progressBarOrder.setVisibility(View.INVISIBLE);
                textViewResultOrder.setText(R.string.network_error);
            }
        });
    }

    private void clearBasket() {
        removeAllItemsFromTable(this, CarsContract.NoteEntry.TABLE_NAME_BASKET);
        carList.clear();
        buttonOrder.setEnabled(false);
        adapter.notifyDataSetChanged();
    }

    private void actionOnAuthError() {
        textViewResultOrder.setText(R.string.auth_error);
    }

    private void remove(int position) {
        removeItemFromTable(this, carList, CarsContract.NoteEntry.TABLE_NAME_BASKET, position);
        carList.clear();
        carList.addAll(getAllItemsFromDb(this, CarsContract.NoteEntry.TABLE_NAME_BASKET));
        adapter.notifyDataSetChanged();
        if (carList.size() == 0) {
            buttonOrder.setEnabled(false);
        }
    }
}
