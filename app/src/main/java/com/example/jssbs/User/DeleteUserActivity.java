package com.example.jssbs.User;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jssbs.Item.ViewItemActivity;
import com.example.jssbs.Model.Item;
import com.example.jssbs.Model.User;
import com.example.jssbs.R;
import com.example.jssbs.ViewHolder.UserList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;

    private ListView listViewUser;
    private List<User> userList;

    @Override
    protected void onStart() {
        super.onStart();
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    User user = userSnapshot.getValue(User.class);

                    userList.add(user);
                }

                UserList adapter = new UserList(DeleteUserActivity.this, userList);
                listViewUser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDeleteDialog(final String userID, String userName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        dialogBuilder.setView(dialogView);

        final Button buttonDelete = (Button) dialogView.findViewById(R.id.deleteUserBtn);

        dialogBuilder.setTitle("Delete user "+userName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Item item = new Item();
                //String sellerID = item.getSellerID();
                //if(sellerID.equals(userID)){
                    //sellerID=item.getItemID();
                    deleteUser(userID);//,sellerID);
                    alertDialog.dismiss();

            }
        });
    }

    private void deleteUser(String userID) {
        DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("User").child(userID);
        //DatabaseReference drItem = FirebaseDatabase.getInstance().getReference("Item").child(sellerID);

        drUser.removeValue();
        //drItem.removeValue();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference("User");

        listViewUser = findViewById(R.id.listViewUser);

        userList = new ArrayList<>();

        listViewUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);

                showDeleteDialog(user.getUid(),user.getName());
                return false;
            }

        });

    }
}
