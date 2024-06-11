package com.example.plastic_waste_management;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class Waste_payment extends Activity implements JsonResponse {
    TextView t1,t2,t3;
    EditText e1,e2,e3,e4;
    Button b1;
    SharedPreferences sh;
    String t_price,del="20";
    String e1name,e2account,e3cvv,e4exp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waste_payment);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        t1=(TextView)findViewById(R.id.tvdel);
        t1.setText(sh.getString("tot_amount",""));

        e1=(EditText)findViewById(R.id.etname);
        e2=(EditText)findViewById(R.id.etaccount);
        e3=(EditText)findViewById(R.id.etcvv);
        e4=(EditText)findViewById(R.id.etexp);


        b1=(Button)findViewById(R.id.btpayment);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                e1name=e1.getText().toString();
                e2account=e2.getText().toString();
                e3cvv=e3.getText().toString();
                e4exp=e4.getText().toString();

                if(e1name.equalsIgnoreCase("")){
                    e1.setError("Enter Name");
                    e1.setFocusable(true);
                }
                else if(e2account.equalsIgnoreCase("") || e2account.length()!=16 ){
                    e2.setError("Enter Card Number");
                    e2.setFocusable(true);
                }
                else if(e3cvv.equalsIgnoreCase("") || e3cvv.length()!=3){
                    e3.setError("Enter CVV");
                    e3.setFocusable(true);
                }
                else if(e4exp.equalsIgnoreCase("")){
                    e4.setError("Enter Exp date");
                    e4.setFocusable(true);
                }
                else {


                    JsonReq jr = new JsonReq();
                    jr.json_response = (JsonResponse) Waste_payment.this;
                    String q = "/Waste_payment?log_id=" + sh.getString("logid", "") + "&wamount=" + sh.getString("tot_amount","");
                    q.replace("", "%20");
                    jr.execute(q);
                }
            }
        });


    }



    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub

        try{
            String method=jo.getString("method");
             if(method.equalsIgnoreCase("Waste_payment"))
            {
                try
                {
                    String status=jo.getString("status");
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    if(status.equalsIgnoreCase("Success"))
                    {
                        Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),User_collection_request.class));
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "Not Successfull", Toast.LENGTH_LONG).show();
                    }

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Hai"+e, Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
        }

    }


    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),User_collection_request.class);
        startActivity(b);
    }

}
