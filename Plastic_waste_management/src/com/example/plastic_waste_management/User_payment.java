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
	TextView t1;
	EditText e1,e2,e3;
	Button b1;
	SharedPreferences sh;
	String t_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_payment);
		
		t1=(TextView)findViewById(R.id.tvtotal);
		e1=(EditText)findViewById(R.id.etname);
		e2=(EditText)findViewById(R.id.etaccount);
		e3=(EditText)findViewById(R.id.etcvv);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		t1.setText(User_purchase.t_price);
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_payment.this;
		String q="user_view_tot_amount/?t_price ="+User_purchase.t_price+"&log_id="+sh.getString("logid","");
		JR.execute(q);
		
		b1=(Button)findViewById(R.id.btpayment);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				t_price=t1.getText().toString();
				
				JsonReq jr= new JsonReq();
				jr.json_response=(JsonResponse) User_payment.this;
				String q="user_payment/?log_id="+sh.getString("logid","")+"&t_price="+User_purchase.t_price;
				q.replace("", "%20");
				jr.execute(q);
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
						startActivity(new Intent(getApplicationContext(),User_payment.class));
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

}
