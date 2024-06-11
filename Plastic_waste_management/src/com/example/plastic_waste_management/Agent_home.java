package com.example.plastic_waste_management;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Agent_home extends Activity {
	Button b1,b3,b4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_home);
		
		b1=(Button)findViewById(R.id.btroute);
	
		b3=(Button)findViewById(R.id.btdelivery);
		b4=(Button)findViewById(R.id.btlogout);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(getApplicationContext(),Agent_assigned_routes.class));
			}
		});
		
		
		b3.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						startActivity(new Intent(getApplicationContext(),Agent_product_delivery.class));
					}
				});
		
		b4.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						startActivity(new Intent(getApplicationContext(),Login.class));
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_home, menu);
		return true;
	}

}
