package cf.tilgiz.parse.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cf.tilgiz.parse.R;
import cf.tilgiz.parse.utils.ApiRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewMessages;
    private EditText editTextUserName;
    private EditText editTextPassword;
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private static final String API_SERVER = "http://tilgiz.cf";
    private static final int TOKEN_TIMEOUT = 3600;
    private long now;

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(API_SERVER)
            .build();

    private ApiRequest req = retrofit.create(ApiRequest.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewMessages = findViewById(R.id.textViewResult);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long saved_token_timeout = preferences.getLong("token_timeout", 0);

        if ((System.currentTimeMillis() / 1000) < saved_token_timeout) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivity(intent);
        }

        button.setOnClickListener(v -> {
            String username = editTextUserName.getText().toString();
            String password = editTextPassword.getText().toString();

            if (username.isEmpty()) {
                textViewMessages.setText(R.string.hint_name);
            } else if (password.isEmpty()) {
                textViewMessages.setText(R.string.hint_pass);
            } else {

                Call<Object> call = req.requestPost(username, password);
                //Show loading animation
                progressBar.setVisibility(View.VISIBLE);
                textViewMessages.setText("");

                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        // Hide loading animation
                        progressBar.setVisibility(View.INVISIBLE);

                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                String token = jsonObject.getString("token");
                                if (token.equals("0")) {
                                    LoginActivity.this.actionOnAuthError();
                                } else {
                                    now = System.currentTimeMillis() / 1000;
                                    preferences.edit().putString("token", token).apply();
                                    preferences.edit().putString("username", username).apply();
                                    preferences.edit().putLong("token_timeout", now + TOKEN_TIMEOUT).apply();

                                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                    intent.putExtra("fromLoginActivity", true);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                LoginActivity.this.actionOnAuthError();
                                e.printStackTrace();
                            }
                        } else {
                            textViewMessages.setText(R.string.request_error);
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        // Hide loading animation
                        progressBar.setVisibility(View.INVISIBLE);

                        textViewMessages.setText(R.string.network_error);
                    }
                });
            }
        });
    }

    private void actionOnAuthError() {
        editTextUserName.setText("");
        editTextPassword.setText("");
        textViewMessages.setText(R.string.auth_error);
    }

}
