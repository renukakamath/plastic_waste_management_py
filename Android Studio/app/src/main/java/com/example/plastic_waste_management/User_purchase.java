package com.example.plastic_waste_management;




import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class User_purchase extends Activity implements JsonResponse{
	ImageView img1;
	TextView t1,t2,t3,t4,t5,t6;
	EditText e1;
	Button b1;
	public static String t_price,amount,quantity;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_purchase);
		
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		t1=(TextView)findViewById(R.id.tvcompany);
		t2=(TextView)findViewById(R.id.tvproduct);
		t3=(TextView)findViewById(R.id.tvamount);
		t4=(TextView)findViewById(R.id.tvdescription);
		t5=(TextView)findViewById(R.id.tvtprice);
		t6=(TextView)findViewById(R.id.tvavqnty);
		
		e1=(EditText)findViewById(R.id.etquantity);
		
		img1=(ImageView)findViewById(R.id.imgv1);
		
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_purchase.this;
		String q="/user_view_product_details?pro_id="+User_search_product.p_id;
		JR.execute(q);
//		log_id="+sh.getString("logid","")+"&complany_id="+User_search_product.cm_id+"&stock_id="+User_search_product.st_id+"&
		
		b1=(Button)findViewById(R.id.btorder);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				quantity=e1.getText().toString();
				t_price=t5.getText().toString();
				
				
				JsonReq jr= new JsonReq();
				jr.json_response=(JsonResponse) User_purchase.this;
				String q="user_order_product/?log_id="+sh.getString("logid","")+"&company_id="+User_search_product.cm_id+"&stock_id="+User_search_product.st_id+"&t_price="+t_price+"&quantity="+quantity+"&amount="+User_search_product.amounts;
				q.replace("", "%20");
				jr.execute(q);
				
			}
		});
		e1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String count = e1.getText().toString().trim();
				
				if(count.length()> 0){
					if(Integer.parseInt(count)<=Integer.parseInt(t6.getText().toString()))
					{
						int r=Integer.parseInt(t3.getText().toString().trim());
						int q=Integer.parseInt(count);
						int tot = r*q;
						t5.setText(tot+"");
						b1.setEnabled(true);
						}
					else
					{
						e1.setError("Quantity Exceeded");
						b1.setEnabled(false);
					}
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_purchase, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		try{
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("user_view_product_details"))
			{
		
				try{
					String status=jo.getString("status");
					  if (status.equalsIgnoreCase("Success")) {
			                JSONArray ja = (JSONArray) jo.getJSONArray("data");
			                t1.setText(ja.getJSONObject(0).getString("com_name"));
			                t2.setText(ja.getJSONObject(0).getString("pro_name"));
			                t3.setText(ja.getJSONObject(0).getString("amount"));
			                t4.setText(ja.getJSONObject(0).getString("description"));
			                t6.setText(ja.getJSONObject(0).getString("availability"));


						  String pro_pic = ja.getJSONObject(0).getString("image");

						  String pth = "http://" + sh.getString("ip", "") + "/" + pro_pic;
						  pth = pth.replace("~", "");
//	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();

						  Log.d("-------------", pth);
						  Picasso.with(getApplicationContext())
								  .load(pth)
								  .placeholder(R.drawable.pla)
								  .error(R.drawable.pla).into(img1);


			               
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
				else if(method.equalsIgnoreCase("user_order_product"))
				{
					try
					{
						String status=jo.getString("status");
						Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
						if(status.equalsIgnoreCase("Success"))
						{
							Toast.makeText(getApplicationContext(), "Order Processing", Toast.LENGTH_LONG).show();
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
	}}

	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent b=new Intent(getApplicationContext(),User_search_product.class);
		startActivity(b);
	}

		 
	}


