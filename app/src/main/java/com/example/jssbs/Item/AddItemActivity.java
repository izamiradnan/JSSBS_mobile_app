package com.example.jssbs.Item;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jssbs.Main.MainActivity;
import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.example.jssbs.RegisterLogin.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddItemActivity extends AppCompatActivity {

    private EditText item_name, item_desc, item_stock, item_price;
    private String saveCurrentDate, saveCurrentTime,productRandomKey,downloadImageUrl;
    private String itemName,desc,price,stock,userID;
    private long item_id;
    private String Vname,Vstudentid, VphoneNumber, Vemail;
    private Button addBtn,uploadBtn;
    private ImageView imageView;
    private Uri filepath;
    private ProgressDialog loadingBar;


    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef,mUser;
    private StorageReference mItemRef;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logOut:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create instances
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference("Item");
        mUser = FirebaseDatabase.getInstance().getReference("User");
        mItemRef = FirebaseStorage.getInstance().getReference("Item");

        //link with xml
        item_name = findViewById(R.id.item_name);
        item_desc = findViewById(R.id.item_desc);
        item_stock = findViewById(R.id.stock);
        item_price = findViewById(R.id.item_price);

        loadingBar = new ProgressDialog(this);

        addBtn = findViewById(R.id.addBtn);
        uploadBtn = findViewById(R.id.uploadBtn);

        imageView = findViewById(R.id.imageView);

        //attach listener to the button

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&data.getData() != null){
            filepath = data.getData();
            imageView.setImageURI(filepath);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void add(){

        if(filepath != null){

            itemName = item_name.getText().toString().trim();
            desc = item_desc.getText().toString().trim();
            stock = item_stock.getText().toString().trim();
            price = item_price.getText().toString().trim();



            if(TextUtils.isEmpty(itemName)){
                Toast.makeText(this, "Please enter item name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(desc)){
                Toast.makeText(this, "Please enter item description", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(stock)){
                Toast.makeText(this, "Please enter item quantity", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(price)){
                Toast.makeText(this, "Please enter item price", Toast.LENGTH_SHORT).show();
                return;
            }

            StoreProductInformation();
        }
    }


    private void StoreProductInformation() {
        loadingBar.setTitle("Add Item");
        loadingBar.setMessage("Please wait while we adding the item");
        loadingBar.setCancelable(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                    item_id = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        productRandomKey = saveCurrentDate + " " + saveCurrentTime;

        final StorageReference path = mItemRef.child(filepath.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = path.putFile(filepath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AddItemActivity.this,"Error" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddItemActivity.this,"Image Uploaded" , Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = path.getDownloadUrl().toString();
                        return path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AddItemActivity.this, "Image saved to storage", Toast.LENGTH_SHORT).show();
                        }

                        mUser.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    User user = dataSnapshot.getValue(User.class);

                                    userID = user.getUid();
                                    Vname = user.getName();
                                    Vstudentid = user.getStudentID();
                                    VphoneNumber = user.getPhoneNumber();
                                    Vemail = user.getEmail();

                                    userID.toString().trim();

                                    SaveProductInfoToDatabase();

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }
        });
    }

    private void SaveProductInfoToDatabase() {
        final HashMap<String,Object> productMap = new HashMap<>();

        productMap.put("itemID",productRandomKey);
        productMap.put("itemName",itemName);
        productMap.put("itemDescription",desc);
        productMap.put("itemPrice",price);
        productMap.put("itemStock",stock);
        productMap.put("image",downloadImageUrl);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("sellerID",userID);

        mUserRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();
                            Toast.makeText(AddItemActivity.this,"Item added to database",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItemActivity.this, MainActivity.class));
                        }
                        else{
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddItemActivity.this,"Error :" + message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}