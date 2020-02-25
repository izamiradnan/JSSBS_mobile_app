package com.example.jssbs.Item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.example.jssbs.Meeting.SetMeetingActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivityBuyer extends AppCompatActivity {

    ImageView imageView;
    TextView name,description,price,stock;
    String itemName,itemDesc,itemPrice,itemStock,image,uItemID,sellerID;
    Button setMeetingBtn;
    private DatabaseReference mItemRef;
    EditText uItemPrice,uItemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buyer);

        mItemRef = FirebaseDatabase.getInstance().getReference("Item");

        imageView = findViewById(R.id.item_image);
        name = findViewById(R.id.item_name);
        price = findViewById(R.id.item_price);
        stock = findViewById(R.id.item_stock);
        description = findViewById(R.id.item_description);
        setMeetingBtn = findViewById(R.id.setMeetingBtn);


        itemName = getIntent().getStringExtra("Name");
        itemDesc = getIntent().getStringExtra("Description");
        itemPrice = getIntent().getStringExtra("Price");
        itemStock = getIntent().getStringExtra("Stock");

        image = getIntent().getStringExtra("Image");

        name.setText(itemName);
        description.setText(itemDesc);
        price.setText(itemPrice);
        stock.setText(itemStock);
        Picasso.get().load(image).into(imageView);

        setMeetingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Item item = dataSnapshot1.getValue(Item.class);
                            if(name.getText().toString().equals(item.getItemName())) {
                                uItemID = item.getItemID();
                                sellerID = item.getSellerID();

                                Intent intent = new Intent(DetailActivityBuyer.this, SetMeetingActivity.class);
                                intent.putExtra("Item_ID", uItemID);
                                intent.putExtra("Seller ID",sellerID);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                startActivity(new Intent(DetailActivityBuyer.this,SetMeetingActivity.class));

            }
        });
    }

}
