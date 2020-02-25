package com.example.jssbs.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jssbs.Item.DetailActivityBuyer;
import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapterBuyer extends RecyclerView.Adapter<ItemAdapterBuyer.MyViewHolder>{
    Context context;
    ArrayList<Item> item;

    public ItemAdapterBuyer(Context c, ArrayList<Item> i){
        context = c;
        item = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cardview,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.description.setText(item.get(i).getItemDescription());
        myViewHolder.name.setText(item.get(i).getItemName());
        myViewHolder.price.setText(item.get(i).getItemPrice());
        Picasso.get().load(item.get(i).getImage()).into(myViewHolder.image);

        myViewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivityBuyer.class);
                intent.putExtra("Name",item.get(i).getItemName());
                intent.putExtra("Image",item.get(i).getImage());
                intent.putExtra("Price",item.get(i).getItemPrice());
                intent.putExtra("Description",item.get(i).getItemDescription());
                intent.putExtra("Stock",item.get(i).getItemStock());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,description;
        ImageView image;
        CardView cvItem;
        public MyViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            description = (TextView) itemView.findViewById(R.id.product_description);
            image = (ImageView) itemView.findViewById(R.id.product_image);
            cvItem = (CardView) itemView.findViewById(R.id.cvItem);

        }
    }
}
