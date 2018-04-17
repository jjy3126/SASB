package com.example.wonsucklee.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wonsucklee on 2018. 2. 7..
 */

public class ValidateRequest extends StringRequest{

    final static private String URL= "http://121.187.77.1:25000/UserValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String>listener){
        super(Method.POST, URL, listener, null );
        parameters=new HashMap<>();
        parameters.put("userID", userID);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
