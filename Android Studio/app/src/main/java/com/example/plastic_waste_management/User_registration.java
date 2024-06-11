package com.example.plastic_waste_management;


import org.json.JSONObject;



import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class User_registration extends Activity implements JsonResponse {
	EditText e1,e2,e3,e4,e5,e6,e7,e8,e9;
	
	String fname,lname,phone,email,hname,place,pincode,uname,pass;
	Button b1;
	String[] sid,sname;
	SharedPreferences sh;
	public static String sids;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_registration);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		e1=(EditText)findViewById(R.id.etfirst);
		e2=(EditText)findViewById(R.id.etlast);
		e3=(EditText)findViewById(R.id.etphone);
		e4=(EditText)findViewById(R.id.etemail);
		e5=(EditText)findViewById(R.id.ethname);
		e6=(EditText)findViewById(R.id.etplace);
		e7=(EditText)findViewById(R.id.etpincode);
		e8=(EditText)findViewById(R.id.etuname);
		e9=(EditText)findViewById(R.id.etpass);
		
		
//		
//		JsonReq jr= new JsonReq();
//		jr.json_response=(JsonResponse) User_registration.this;
//		String q="viewschools/";
//		q.replace("", "%20");
//		jr.execute(q);
		
		b1=(Button)findViewById(R.id.btreg);
		
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				fname=e1.getText().toString();
				lname=e2.getText().toString();
				phone=e3.getText().toString();
				email=e4.getText().toString();
				hname=e5.getText().toString();
				place=e6.getText().toString();
				pincode=e7.getText().toString();
				uname=e8.getText().toString();
				pass=e9.getText().toString();

				if(fname.equalsIgnoreCase("")){
					e1.setError("Enter First Name");
					e1.setFocusable(true);
				}
				else if(lname.equalsIgnoreCase("")){
					e2.setError("Enter Last Name");
					e2.setFocusable(true);
				}
				else if(phone.equalsIgnoreCase("") || phone.length()!=10){
					e3.setError("Enter Phone Number");
					e3.setFocusable(true);
				}
				else if(email.equalsIgnoreCase("")){
					e4.setError("Enter Email");
					e4.setFocusable(true);
				}
				else if(hname.equalsIgnoreCase("")){
					e5.setError("Enter House Name");
					e5.setFocusable(true);
				}
				else if(place.equalsIgnoreCase("")){
					e6.setError("Enter place");
					e6.setFocusable(true);
				}
				else if(pincode.equalsIgnoreCase("") || pincode.length()!=6){
					e7.setError("Enter pincode");
					e7.setFocusable(true);
				}
				else if(uname.equalsIgnoreCase("") ){
					e8.setError("Enter Username");
					e8.setFocusable(true);
				}
				else if(pass.equalsIgnoreCase("") ){
					e9.setError("Enter Password");
					e9.setFocusable(true);
				}
				else {


					JsonReq jr = new JsonReq();
					jr.json_response = (JsonResponse) User_registration.this;
					String q = "/register?firstname=" + fname + "&lastname=" + lname + "&phone=" + phone + "&email=" + email + "&hname=" + hname + "&place=" + place + "&pincode=" + pincode + "&uname=" + uname + "&pass=" + pass;
					q.replace("", "%20");
					jr.execute(q);
				}
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_registration, menu);
		return true;
	}



	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		try{
			String method=jo.getString("method");
			
				if(method.equalsIgnoreCase("register"))
				{
				
					try
					{
						String status=jo.getString("status");
						Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
						if(status.equalsIgnoreCase("success"))
						{
							Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
							startActivity(new Intent(getApplicationContext(),Login.class));
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
		Intent b=new Intent(getApplicationContext(), Login.class);
		startActivity(b);
	}

}
