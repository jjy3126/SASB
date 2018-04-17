package com.example.wonsucklee.myapplication;

import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wonsucklee on 2018. 2. 7..
 */

public class RegisterRequest extends StringRequest {

    final static private String URL= "http://121.187.77.1:25000/register.php";
    private Map<String, String> parameters;

    public RegisterRequest(Bitmap bitmap, String userID, String userPassword, String userName, String userMajor, Response.Listener<String>listener){
        super(Method.POST, URL, listener, null );
        parameters=new HashMap<>();
        String imageData= imageToString(bitmap);

        String user_name = null;
        String user_major = null;

        try {
            user_name = URLEncoder.encode(userName, "utf-8");
            user_major = URLEncoder.encode(userMajor, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        parameters.put("image",imageData);
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", user_name);
        parameters.put("userMajor", user_major);
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes= outputStream.toByteArray();

        String encodedImage= Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
