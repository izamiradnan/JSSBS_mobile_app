package com.example.jssbs.Item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Model.Item;
import com.example.jssbs.R;
import com.example.jssbs.User.ViewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView name,description,price,stock;
    String itemName,itemDesc,itemPrice,itemStock,image,uItemID;
    Button deleteButton,updateButton;
    private DatabaseReference mItemRef;
    EditText uItemPrice,uItemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mItemRef = FirebaseDatabase.getInstance().getReference("Item");

        imageView = findViewById(R.id.item_image);
        name = findViewById(R.id.item_name);
        price = findViewById(R.id.item_price);
        stock = findViewById(R.id.item_stock);
        description = findViewById(R.id.item_description);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

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


       /* mItemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    Item item = userSnapshot.getValue(Item.class);
                    if(name.equals(item.getItemName())){

                        uItemID = item.getItemID(); //ambik item PK
                        Toast.makeText(DetailActivity.this, "Masuk", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mItemRef.addValueEventListener(new ValueEventListener() {
                    //startActivity(new Intent(DetailActivity.this, UpdateItemActivity .class));
                    @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                            Item item = dataSnapshot1.getValue(Item.class);
                            if(name.getText().toString().equals(item.getItemName()))
                                uItemID = item.getItemID();
                            Toast.makeText(DetailActivity.this, "MASUK", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                showUpdateDialog();

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemRef.addValueEventListener(new ValueEventListener() {
                    //startActivity(new Intent(DetailActivity.this, UpdateItemActivity .class));
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                            Item item = dataSnapshot2.getValue(Item.class);
                            if(name.getText().toString().equals(item.getItemName()))
                                uItemID = item.getItemID();
                            Toast.makeText(DetailActivity.this, "MASUK", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                showDeleteDialog();
            }
        });
    }

    private void showDeleteDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_item_dialog,null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteItemBtn);

        dialogBuilder.setTitle(name.getText().toString());

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference drItem = FirebaseDatabase.getInstance().getReference("Item").child(uItemID);
                drItem.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(DetailActivity.this, "Delete fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.dismiss();

            }
        });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_item,null);
        dialogBuilder.setView(dialogView);

        final Button updateItem = (Button) dialogView.findViewById(R.id.updateItemBtn);
        uItemPrice = dialogView.findViewById(R.id.editItemPrice);
        uItemQuantity = dialogView.findViewById(R.id.editItemQuantity);

        //dialogBuilder.setTitle("Update item ");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        updateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> result = new HashMap<>();

                String newPrice = uItemPrice.getText().toString();
                String newQuantity = uItemQuantity.getText().toString();

                result.put("itemPrice", newPrice);
                result.put("itemStock", newQuantity);

                mItemRef.child(uItemID).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(DetailActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.dismiss();

            }
        });
    }
}
