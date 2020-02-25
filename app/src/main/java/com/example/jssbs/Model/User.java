package com.example.jssbs.Model;

import com.google.firebase.database.Exclude;
import java.util.HashMap;
import java.util.Map;

public class User {

    String uid,name,studentID,phoneNumber,email,password;

    //mutator

    public void setID (String userID)
    {
        uid = userID;
    }
    public void setName(String userName)
    {
        name = userName;
    }
    public void setStudentID(String userStudentID)
    {
        studentID = userStudentID;
    }
    public void setPhoneNumber(String userPhoneNumber)
    {
        phoneNumber = userPhoneNumber;
    }
    public void setEmail(String userEmail)
    {
        email = userEmail;
    }
    public void setPassword(String userPassword)
    {
        password = userPassword;
    }

    public String getUid() {
        return uid;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
//normal constructor

    // default constructor
    public User(){

    }

    public User(String uID,String uName,String uStudentID,String uPhoneNumber,String uEmail,String uPassword)
    {
        uid = uID;
        name = uName;
        studentID = uStudentID;
        phoneNumber = uPhoneNumber;
        email = uEmail;
        password = uPassword;
    }

    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("uid",uid);
        result.put("name",name);
        result.put("student",studentID);
        result.put("phoneNumber",phoneNumber);
        result.put("email",email);
        result.put("password",password);

        return result;
    }
}
