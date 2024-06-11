package com.example.plastic_waste_management;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class User_payment extends Activity implements JsonResponse {
	TextView t1,t2,t3;
	EditText e1,e2,e3,e4;
	Button b1;
	SharedPreferences sh;
	String t_price,del="40",e1name,e2account,e3cvv,e4exp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_payment);
		
		t1=(TextView)findViewById(R.id.tvtotal);
		t3=(TextView)findViewById(R.id.tvgrand);

		e1=(EditText)findViewById(R.id.etname);
		e2=(EditText)findViewById(R.id.etaccount);
		e3=(EditText)findViewById(R.id.etcvv);
		e4=(EditText)findViewById(R.id.etexp);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


		String tt=User_purchase.t_price;
		Float tp=new Float(tt);
		Float dt=new Float(del);
		Float gt=tp+dt;


		t1.setText(User_purchase.t_price);
		t3.setText(gt+"");

		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_payment.this;
		String q="/user_view_tot_amount?t_price ="+User_purchase.t_price+"&log_id="+sh.getString("logid","");
		JR.execute(q);
		
		b1=(Button)findViewById(R.id.btpayment);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				t_price=t3.getText().toString();

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
					jr.json_response = (JsonResponse) User_payment.this;
					String q = "user_payment/?log_id=" + sh.getString("logid", "") + "&t_price=" + t_price;
					q.replace("", "%20");
					jr.execute(q);
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_payment, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		try{
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("user_view_tot_amount"))
			{
		
				try{
					String status=jo.getString("status");
					if(status.equalsIgnoreCase("Success"))
					{
		//				JSONArray ja = (JSONArray) jo.getJSONArray("data");
		//                t1.setText(ja.getJSONObject(0).getString("total_amount"));
						Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

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
			else if(method.equalsIgnoreCase("user_payment"))
			{
				try
				{
					String status=jo.getString("status");
					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					if(status.equalsIgnoreCase("Success"))
					{
						Toast.makeText(getApplicationContext(), "Order Success", Toast.LENGTH_LONG).show();
						startActivity(new Intent(getApplicationContext(),User_search_product.class));
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
		Intent b=new Intent(getApplicationContext(),User_purchase.class);
		startActivity(b);
	}

}
