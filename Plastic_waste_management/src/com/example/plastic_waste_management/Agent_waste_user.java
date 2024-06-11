package com.example.plastic_waste_management;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Agent_waste_user extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_waste_user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_waste_user, menu);
		return true;
	}

}
