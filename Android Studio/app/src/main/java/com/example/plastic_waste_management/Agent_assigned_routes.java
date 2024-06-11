package com.example.plastic_waste_management;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Agent_assigned_routes extends Activity implements JsonResponse, OnItemClickListener{
	ListView l1;
	String[] route_name,description,date,request_id,details;
	public static String rqid;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_assigned_routes);
		
		l1=(ListView)findViewById(R.id.lvroutes); 
		l1.setOnItemClickListener(this);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)Agent_assigned_routes.this;
		String q="/view_routes?logid="+sh.getString("logid", "");
		JR.execute(q);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_assigned_routes, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		
		try{
			String method=jo.getString("method");
			if(method.equals("view_routes")){
			
			
			String status=jo.getString("status");
			if(status.equalsIgnoreCase("Success"))
			{
				JSONArray ja=(JSONArray)jo.getJSONArray("data");
				request_id=new String[ja.length()];
				route_name= new String[ja.length()];
				description=new String[ja.length()];
				date=new String[ja.length()];
				
				details=new String[ja.length()];
				
				
				
				for(int i=0;i<ja.length();i++)
				{	
					request_id[i]=ja.getJSONObject(i).getString("request_id");
					route_name[i]=ja.getJSONObject(i).getString("route_name");
					description[i]=ja.getJSONObject(i).getString("route_des");
					date[i]=ja.getJSONObject(i).getString("date");
					
			
					details[i]="route_name : "+route_name[i]+"\ndescription : "+description[i]+"\ndate : "+date[i];
					
				}
				//driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
				l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.cust_text,details));
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
			}
			}
			else if(method.equals("agent_confirm_request")){
				String status=jo.getString("status");
				if(status.equalsIgnoreCase("Success"))
				{
					
					
					Toast.makeText(getApplicationContext(), "Collected", Toast.LENGTH_LONG).show();
					
				}
				else{
					Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
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
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(Agent_assigned_routes.this);
		builder.setTitle("Select Option!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				if (items[item].equals("Collected"))
				{
					JsonReq JR= new JsonReq();
					JR.json_response=(JsonResponse)Agent_assigned_routes.this;
					String q="agent_confirm_request/?rqid="+rqid;
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
		Intent b=new Intent(getApplicationContext(),Agent_home.class);
		startActivity(b);
	}

}
