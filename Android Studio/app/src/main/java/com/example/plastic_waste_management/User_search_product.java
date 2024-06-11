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

public class User_search_product extends Activity implements JsonResponse, OnItemClickListener{
	ListView l1;
	String[] pro_id,stock_id,com_id,company_name,availability,product,description,amount,image,details,value;
	public static String cm_id,st_id,p_id,amounts;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_search_product);
		
		l1=(ListView)findViewById(R.id.lvproduct);
		l1.setOnItemClickListener(this);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_search_product.this;
		String q="/user_view_product";
		JR.execute(q);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_search_product, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		
		try{
			String status=jo.getString("status");
			if(status.equalsIgnoreCase("Success"))
			{
				JSONArray ja=(JSONArray)jo.getJSONArray("data");
				pro_id=new String[ja.length()];
				stock_id=new String[ja.length()];
				com_id=new String[ja.length()];
				image=new String[ja.length()];
				company_name= new String[ja.length()];
				product= new String[ja.length()];
				amount=new String[ja.length()];
				description=new String[ja.length()];
				availability= new String[ja.length()];
				details=new String[ja.length()];
				value=new String[ja.length()];
				
				
				
				for(int i=0;i<ja.length();i++)
				{	
					pro_id[i]=ja.getJSONObject(i).getString("pro_id");
					stock_id[i]=ja.getJSONObject(i).getString("stock_id");
					com_id[i]=ja.getJSONObject(i).getString("com_id");
					image[i]=ja.getJSONObject(i).getString("image");
					company_name[i]=ja.getJSONObject(i).getString("com_name");
					product[i]=ja.getJSONObject(i).getString("pro_name");
					amount[i]=ja.getJSONObject(i).getString("amount");
					description[i]=ja.getJSONObject(i).getString("description");
					availability[i]=ja.getJSONObject(i).getString("availability");



					value[i] = "company_name:" + company_name[i] + "\navailability: " + availability[i] + "\n product_name: " + product[i] + "\ndescription: " + description[i] + "\namount: " + amount[i] +"\nimage:" +image[i];


//					details[i]="product : "+product[i]+"\ndescription : "+description[i]+"\namount : "+amount[i]+"\nimage : "+image[i];
//					val="image : "+image[i]+"\ncompany_name : "+company_name[i]+"\nproduct : "+product[i]+"\namount : "+amount[i]+"\ndescription : "+description[i]+"\navailability : "+availability[i];
				}
				//driver_list.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.spin,ConfirmRide.name_s));
				l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.cust_text,details));
				Custimage a = new Custimage(this, company_name, availability, product, description, amount ,image);
				l1.setAdapter(a);
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

	@Override
	
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		 cm_id = com_id[arg2];
		 st_id  =stock_id[arg2];
		 p_id = pro_id[arg2];
		 amounts = amount[arg2];
		
//		final CharSequence[] items = {"View Player","Cancel"};
//		
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(User_view_team.this);
//		builder.setTitle("Select Option!");
//		builder.setItems(items, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int item) {
//				// TODO Auto-generated method stub
//				if (items[item].equals("View Player"))
//				{
					Intent il=new Intent(getApplicationContext(),User_purchase.class);
					startActivity(il);
//				}
//			}
//		});
//		builder.show();
		
	}

	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent b=new Intent(getApplicationContext(),User_home.class);
		startActivity(b);
	}

}
