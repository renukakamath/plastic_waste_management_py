package com.example.plastic_waste_management;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_view_my_orders extends Activity implements JsonResponse{
    EditText e1;
    ListView l1;
    Button b1;
    String feedback;
    SharedPreferences sh;
    String[] pro_name,quantity,total_amount,date,com_name,delivery_status,details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_my_orders);

        l1=(ListView)findViewById(R.id.lvfeed);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        JsonReq JR= new JsonReq();
        JR.json_response=(JsonResponse)User_view_my_orders.this;
        String q="/User_view_my_orders?logid="+sh.getString("logid", "");
        JR.execute(q);


    }


    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub

//		Toast.makeText(getApplicationContext(), "No,,,,,,,,,,,,,,,,", Toast.LENGTH_LONG).show();
        try{
            String method=jo.getString("method");
            if(method.equalsIgnoreCase("User_view_my_orders"))
            {

                try{
                    String status=jo.getString("status");
                    if(status.equalsIgnoreCase("success"))
                    {
                        JSONArray ja=(JSONArray)jo.getJSONArray("data");
                        pro_name=new String[ja.length()];
                        quantity= new String[ja.length()];
                        total_amount=new String[ja.length()];
                        com_name= new String[ja.length()];
                        date=new String[ja.length()];
                        delivery_status= new String[ja.length()];

                        details=new String[ja.length()];


                        for(int i=0;i<ja.length();i++)
                        {
                            pro_name[i]=ja.getJSONObject(i).getString("pro_name");
                            quantity[i]=ja.getJSONObject(i).getString("quantity");
                            total_amount[i]=ja.getJSONObject(i).getString("total_amount");
                            com_name[i]=ja.getJSONObject(i).getString("com_name");
                            date[i]=ja.getJSONObject(i).getString("date");
                            delivery_status[i]=ja.getJSONObject(i).getString("delivery_status");

                            details[i]="Peoduct Name : "+pro_name[i]+"\nQuantity : "+quantity[i]
                                    +"\nTotal : "+total_amount[i]+"\nCompany Name : "+com_name[i]
                                    +"\nDate : "+date[i]+"\nStatus : "+delivery_status[i];

                        }
                        //driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
                        l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_text,details));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
        }
//
    }

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),User_home.class);
        startActivity(b);
    }

}
