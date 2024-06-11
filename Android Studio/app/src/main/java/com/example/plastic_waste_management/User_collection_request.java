package com.example.plastic_waste_management;



import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class User_collection_request extends Activity implements OnItemSelectedListener,JsonResponse, OnItemClickListener {
	Spinner s1;
	EditText e1;
	Button b1;
	SharedPreferences sh;
	public static String rqid,quantity,w_amount,tot_amount;
	String[] route_name,route_id,date,request_status,request_id,details;
	String route,lati,longi;
	ListView l1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_collection_request);
		s1=(Spinner)findViewById(R.id.sproute);
		e1=(EditText)findViewById(R.id.etqnty);
//		e2=(EditText)findViewById(R.id.etlongi);
//		e3=(EditText)findViewById(R.id.etdate);
		b1=(Button)findViewById(R.id.btrequest);
		l1=(ListView)findViewById(R.id.lvcollect);
		l1.setOnItemClickListener(this);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		s1.setOnItemSelectedListener(this);
				
		
		JsonReq JR1= new JsonReq();
		JR1.json_response=(JsonResponse)User_collection_request.this;
		String q1="user_view_collection_report/?logid="+sh.getString("logid", "");
		JR1.execute(q1);
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_collection_request.this;
		String q="/user_view_routes";
		JR.execute(q);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 quantity = e1.getText().toString();
				 w_amount="5";
				  tot_amount= String.valueOf(Float.parseFloat(quantity)*Float.parseFloat(w_amount));
				SharedPreferences.Editor ed=sh.edit();
				ed.putString("tot_amount",tot_amount);
				ed.commit();

//				Toast.makeText(getApplicationContext(),tot_amount,Toast.LENGTH_LONG).show();


//				longi=e2.getText().toString();
//				date=e3.getText().toString();
				
				JsonReq jr= new JsonReq();
				jr.json_response=(JsonResponse) User_collection_request.this;
				String q="/user_collection_request?lati="+LocationService.lati+"&longi="+LocationService.logi+"&logid="+sh.getString("logid", "")+"&route_id="+route+"&quantity="+quantity;
				q.replace("", "%20");
				jr.execute(q);
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_collection_request, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		route=route_id[arg2];
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		try{
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("user_view_routes"))
			{
				try{
					String status=jo.getString("status");
					if(status.equalsIgnoreCase("Success"))
					{
						JSONArray ja=(JSONArray)jo.getJSONArray("data");
						route_name= new String[ja.length()];
						route_id=new String[ja.length()];
						
						
						
						for(int i=0;i<ja.length();i++)
						{
							route_name[i]=ja.getJSONObject(i).getString("route_name");
							route_id[i]=ja.getJSONObject(i).getString("route_id");
							
							
		//					details[i]="username : "+username[i]+"\nmessage : "+message[i]+"\nreply : "+reply[i]+"\ndate : "+date[i];
							
						}
						//driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
						s1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_text,route_name));
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
			else if(method.equalsIgnoreCase("user_collection_request"))
			{
				try
				{
					String status=jo.getString("status");
					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					if(status.equalsIgnoreCase("Success"))
					{
						Toast.makeText(getApplicationContext(), "Request Added", Toast.LENGTH_LONG).show();
						startActivity(new Intent(getApplicationContext(),Waste_payment.class));
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
	
			else if(method.equalsIgnoreCase("user_view_collection_report"))
			{
				try
				{
					String status=jo.getString("status");
					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					if(status.equalsIgnoreCase("Success"))
					{
						
						JSONArray ja=(JSONArray)jo.getJSONArray("data");
						request_id=new String[ja.length()];
						request_status= new String[ja.length()];
//						description=new String[ja.length()];
						date=new String[ja.length()];
						
						details=new String[ja.length()];
						
						
						
						for(int i=0;i<ja.length();i++)
						{	
							request_id[i]=ja.getJSONObject(i).getString("request_id");
							request_status[i]=ja.getJSONObject(i).getString("request_status");
//							description[i]=ja.getJSONObject(i).getString("route_des");
							date[i]=ja.getJSONObject(i).getString("date");
							
					
							details[i]="request_status : "+request_status[i]+"\ndate : "+date[i];
							
						}
						//driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
						l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_text,details));
//						Toast.makeText(getApplicationContext(), "Collected", Toast.LENGTH_LONG).show();
//						startActivity(new Intent(getApplicationContext(),User_collection_request.class));
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
	
			else if(method.equalsIgnoreCase("user_confirm_collect"))
			{
				try
				{
					String status=jo.getString("status");
//					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					if(status.equalsIgnoreCase("Success"))
					{
						Toast.makeText(getApplicationContext(), "Collected", Toast.LENGTH_LONG).show();
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		 rqid = request_id[arg2];
		
		final CharSequence[] items = {"Collected","Cancel"};
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(User_collection_request.this);
		builder.setTitle("Select Option!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				if (items[item].equals("Collected"))
				{
					JsonReq JR= new JsonReq();
					JR.json_response=(JsonResponse)User_collection_request.this;
					String q="user_confirm_collect/?rqid="+rqid;
					JR.execute(q);

				}
				else{
                    dialog.dismiss();
					
				}
			}
		});
		builder.show();
		
	}
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent b=new Intent(getApplicationContext(),User_home.class);
		startActivity(b);
	}


}
