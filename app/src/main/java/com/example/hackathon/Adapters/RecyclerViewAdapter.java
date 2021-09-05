package com.example.hackathon.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hackathon.MainActivity;
import com.example.hackathon.ProductDetails;
import com.example.hackathon.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.Viewholder> {

    List<ProductDetails> productDetailsList;
    Context context;

    public RecyclerViewAdapter(List<ProductDetails> productDetailsList, Context context) {
        this.productDetailsList = productDetailsList;
        this.context = context;
    }

    public void setList(List<ProductDetails> productDetailsList){
        this.productDetailsList = productDetailsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.Viewholder holder, int position) {

        ProductDetails productDetails = productDetailsList.get(position);
        holder.productName.setText(productDetails.product_name);
        holder.productPrice.setText(String.valueOf(productDetails.product_price));
        holder.productSource.setText(productDetails.shopping_site);
        Glide.with(context).load(productDetails.product_image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(productDetailsList != null) {
            return productDetailsList.size();
        }
        return 0;
    }

    class Viewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView productName, productPrice, productSource;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_imageView);
            productName = itemView.findViewById(R.id.product_name_textView);
            productPrice = itemView.findViewById(R.id.product_price_textView);
            productSource = itemView.findViewById(R.id.product_source_textView);

        }
    }
}


