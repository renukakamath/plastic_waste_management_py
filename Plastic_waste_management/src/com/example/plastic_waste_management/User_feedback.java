package com.example.plastic_waste_management;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class User_feedback extends Activity implements JsonResponse{
	EditText e1;
	ListView l1;
	Button b1;
	String feedback;
	SharedPreferences sh;
	String[] feedbacks,date,details;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_feedback);
		
		e1=(EditText)findViewById(R.id.etfeed);
		l1=(ListView)findViewById(R.id.lvfeed);
		b1=(Button)findViewById(R.id.btfeed);
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)User_feedback.this;
		String q="view_feedback/?logid="+sh.getString("logid", "");
		JR.execute(q);
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				feedback=e1.getText().toString();
				
				
				JsonReq jr= new JsonReq();
				jr.json_response=(JsonResponse) User_feedback.this;
				String q="send_feedback/?Feedback="+feedback+"&log_id="+sh.getString("logid","");
				q.replace("", "%20");
				jr.execute(q);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_feedback, menu);
		return true;
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		try{
			String method=jo.getString("method");
			if(method.equalsIgnoreCase("view_feedback"))
			{
		
		try{
			String status=jo.getString("status");
			if(status.equalsIgnoreCase("success"))
			{
				JSONArray ja=(JSONArray)jo.getJSONArray("data");
				feedbacks=new String[ja.length()];
				date= new String[ja.length()];
				
				details=new String[ja.length()];
				
				
				
				for(int i=0;i<ja.length();i++)
				{
					feedbacks[i]=ja.getJSONObject(i).getString("feedback");
					date[i]=ja.getJSONObject(i).getString("date");
					
					details[i]="feedbacks : "+feedbacks[i]+"\nDate : "+date[i];
					
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
}
	catch(Exception e){
		e.printStackTrace();
		Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
	}
		
	}

}
