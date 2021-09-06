package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.hackathon.Adapters.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity: ";
    JSONPlaceHolderAPI jsonPlaceHolderAPI;
    List<ProductDetails> productDetailsList;
    RecyclerViewAdapter adapter;
    private AlertDialog loadingDialog;
    private RecyclerView recyclerView;
    private Button findButton;
    private EditText searchEditText;
    private LottieAnimationView mainAnimation;
    private LottieAnimationView notFoundAnimation;
    private TextView textMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        findButton = findViewById(R.id.findButton);
        searchEditText = findViewById(R.id.search_editText);
        mainAnimation = findViewById(R.id.mainAnimation);
        notFoundAnimation = findViewById(R.id.notFoundAnimation);
        textMain = findViewById(R.id.textMain);

        // custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null);
        loadingDialog = new AlertDialog.Builder(MainActivity.this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(loggingInterceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://0991-117-199-196-7.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);

        productDetailsList = new ArrayList<>();

        adapter = new RecyclerViewAdapter(productDetailsList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        findButton.setOnClickListener(view -> {
            String product = searchEditText.getText().toString();
            if(product.isEmpty()){
                Toast.makeText(MainActivity.this, "Product name cannot be empty!!", Toast.LENGTH_SHORT).show();
            } else {
                search(product);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findButton.getWindowToken(), 0);
                mainAnimation.setVisibility(View.INVISIBLE);
                textMain.setVisibility(View.INVISIBLE);
                loadingDialog.show();
            }
        });
    }

    public void search(String product){

        Call<List<ProductDetails>> call = jsonPlaceHolderAPI.getDetails(product);
        call.enqueue(new Callback<List<ProductDetails>>() {
            @Override
            public void onResponse(Call<List<ProductDetails>> call, Response<List<ProductDetails>> response) {
                productDetailsList = response.body();
                Log.d(TAG, "onResponse: successful");
                Log.d(TAG, "onResponse: " + String.valueOf(productDetailsList));
                adapter.setList(productDetailsList);
                adapter.notifyDataSetChanged();
                if(productDetailsList == null || productDetailsList.isEmpty()){
                    notFoundAnimation.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                } else {
                    notFoundAnimation.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                loadingDialog.hide();
            }

            @Override
            public void onFailure(Call<List<ProductDetails>> call, Throwable t) {

            }
        });
//        List<ProductDetails> productDetailsList = new ArrayList<>();
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(productDetailsList, getApplicationContext());
//        binding.recyclerView.setAdapter(adapter);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://d85a-117-205-80-98.ngrok.io/?q=samsung%20phones";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                response -> {
//                    try {
//                        JSONArray array = new JSONArray(response);
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object = array.getJSONObject(i);
//                            String name = object.getString("product_name");
//                            String site = object.getString("shopping_site");
//                            int price = object.getInt("product_price");
//                            String image = object.getString("product_image");
//                            Log.d(TAG, "search: " + name + site + price + image);
//                            ProductDetails productDetails = new ProductDetails(image, name, price, site);
//                            productDetailsList.add(productDetails);
//                            Log.i("data_details", name+" "+site+" "+price);
//                        }
//                        adapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> {
//            Toast.makeText(this, "Error : "+error, Toast.LENGTH_SHORT).show();
//
//        });
//
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                500000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestQueue.add(stringRequest);

    }
}