package com.example.plastic_waste_management;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IPsetting extends Activity {
	EditText ed_ip;
	Button bt_ip;
	public static String ip;
	SharedPreferences sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ipsetting);
		
		sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		ed_ip = (EditText) findViewById(R.id.editText1);
		bt_ip = (Button) findViewById(R.id.button1);
		
		ed_ip.setText(sh.getString("ip","192.168."));
		
		bt_ip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				ip=ed_ip.getText().toString();
				if (ip.equals("")) {
					ed_ip.setError("Enter IP address");
					ed_ip.setFocusable(true);
					
				}else {
					Editor ed= sh.edit();
					ed.putString("ip",ip);
					ed.commit();
					startActivity(new Intent(getApplicationContext(),Login.class));
				}
			}
				
			
		});
		
		
	}
	

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit  :")
                .setMessage("Are you sure you want to exit..?")
                .setPositiveButton("Yes",new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        // TODO Auto-generated method stub
                        Intent i=new Intent(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                }).setNegativeButton("No",null).show();

    }

}
