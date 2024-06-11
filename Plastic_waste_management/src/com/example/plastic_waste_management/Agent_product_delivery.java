package com.example.plastic_waste_management;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Agent_product_delivery extends Activity implements JsonResponse, OnItemClickListener {
	ListView l1;
	SharedPreferences sh;
	public static String pm_ids;
	String[] user_name,phone,email,house,place,pincode,com_name,pro_name,tot_amount,details,pm_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_product_delivery);
		
		l1=(ListView)findViewById(R.id.lvpro);
		l1.setOnItemClickListener(this);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)Agent_product_delivery.this;
		String q="agent_delivery_product/?logid="+sh.getString("logid", "");
		JR.execute(q);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_product_delivery, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		try{
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("agent_delivery_product"))
			{
		try{
			String status=jo.getString("status");
			if(status.equalsIgnoreCase("Success"))
			{
				JSONArray ja=(JSONArray)jo.getJSONArray("data");
				pm_id=new String[ja.length()];
				user_name=new String[ja.length()];
				phone=new String[ja.length()];
				email=new String[ja.length()];
				house=new String[ja.length()];
				place= new String[ja.length()];
				pincode= new String[ja.length()];
				com_name=new String[ja.length()];
				pro_name=new String[ja.length()];
				tot_amount= new String[ja.length()];
				details=new String[ja.length()];
				
				
				
				for(int i=0;i<ja.length();i++)
				{	
					pm_id[i]=ja.getJSONObject(i).getString("pm_id");
					user_name[i]=ja.getJSONObject(i).getString("user_name");
					phone[i]=ja.getJSONObject(i).getString("user_phone");
					email[i]=ja.getJSONObject(i).getString("user_email");
					house[i]=ja.getJSONObject(i).getString("hname");
					place[i]=ja.getJSONObject(i).getString("place");
					pincode[i]=ja.getJSONObject(i).getString("pincode");
					com_name[i]=ja.getJSONObject(i).getString("com_name");
					pro_name[i]=ja.getJSONObject(i).getString("pro_name");
					tot_amount[i]=ja.getJSONObject(i).getString("total_amount");
					
			
//					details[i]="product : "+product[i]+"\ndescription : "+description[i]+"\namount : "+amount[i]+"\nimage : "+image[i];
					details[i]="user_name : "+user_name[i]+"\nphone : "+phone[i]+"\nemail : "+email[i]+"\nhouse : "+house[i]+"\nplace : "+place[i]+"\npincode : "+pincode[i]+"\ncom_name : "+com_name[i]+"\npro_name : "+pro_name[i];
				}
				//driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
				l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,details));
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
		else if(method.equalsIgnoreCase("agent_confirm_delivery"))
		{
			try
			{
				String status=jo.getString("status");
//				Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
				if(status.equalsIgnoreCase("Success"))
				{
					Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
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
		 pm_ids = pm_id[arg2];
			
			final CharSequence[] items = {"Successfully Delivered","Cancel"};
			
			
			AlertDialog.Builder builder = new AlertDialog.Builder(Agent_product_delivery.this);
			builder.setTitle("Select Option!");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int item) {
					// TODO Auto-generated method stub
					if (items[item].equals("Successfully Delivered"))
					{
						JsonReq JR= new JsonReq();
						JR.json_response=(JsonResponse)Agent_product_delivery.this;
						String q="agent_confirm_delivery/?pm_ids="+pm_ids;
						JR.execute(q);

					}
					else{
	                    dialog.dismiss();
						
					}
				}
			});
			builder.show();
			
		}
	}


