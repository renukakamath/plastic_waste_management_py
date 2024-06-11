package com.example.plastic_waste_management;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class LocationService extends Service implements JsonResponse {
	 private LocationManager locationManager;
	    private Boolean locationChanged;
	    String[] valss,vals;
	    private Handler handler = new Handler();
	    public static Location curLocation;
	    public static boolean isService = true;
	    private File root;
	    private ArrayList<String> fileList = new ArrayList<String>();
	    String[] blocknumber,erase,erases;
	    String val;
	    
	    public static String lati="9.9786543",logi="76.7804",place="Kochi";
	    String ip="";
	    String[] zone;
	    String pc="";
	   
	    String imei="";
	    String encodedImage = null;

	    TelephonyManager telemanager;
	    SharedPreferences sh;
	    
	    String namespace="urn:demo";    //website open for every website there were same namespace
		   
		String method="location";   
		String soap=namespace+method;
		
		String method1="files";
		String soap1=namespace+method1;
		
		String method3="getbackup";
		String soap3=namespace+method3;
		
		String method4="updatebackup";
		String soap4=namespace+method4;
		
		
		String method5="geterase";   
		String soap5=namespace+method5;

		String method6="updateerase";   
		String soap6=namespace+method6;

		String method7="getbackup";
		String soap7=namespace+method7;
		
		String method8="updatebackup";
		String soap8=namespace+method8;
		
		String methods="insertcontact";
		String soaps=namespace+methods;
		
		String method10="viewblocklist";	
		String soapaction10=namespace+method10;
		

LocationListener locationListener = new LocationListener() {
	    		
	        public void onLocationChanged(Location location) {
	            if (curLocation == null) {
	                curLocation = location;
	                locationChanged = true;
	            }
	            else if (curLocation.getLatitude() == location.getLatitude() && curLocation.getLongitude() == location.getLongitude()){
	                locationChanged = false;
	                return;
	            }
	            else
	                locationChanged = true;
	                curLocation = location;

	            if (locationChanged)
	                locationManager.removeUpdates(locationListener);
	        }
	        public void onProviderDisabled(String provider) {
	        }

	        public void onProviderEnabled(String provider) {
	        }
	                
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {
				// TODO Auto-generated method stub
				  if (status == 0)// UnAvailable
		            {
		            } else if (status == 1)// Trying to Connect
		            {
		            } else if (status == 2) {// Available
		            }
			}		
	    };
	

	@Override
	public void onCreate() {
		
		   super.onCreate();
		   try{
				//insertfiles();
			   
				 TelephonyManager telephoneMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
			     	imei = telephoneMgr.getDeviceId().toString();
				
			
			   
			}catch(Exception ex){}
	        curLocation = getBestLocation();
	      
	        if (curLocation == null){
	        	System.out.println("starting problem.........3...");
	        	Toast.makeText(this, "GPS problem..........", Toast.LENGTH_SHORT).show();
	       }
	        else{
	         	// Log.d("ssssssssssss", String.valueOf("latitude2.........."+curLocation.getLatitude()));        	 
	        }
	        isService =  true;
	    }    
	 
	    
	    
	    
		private void getfile(File dir) {
			// TODO Auto-generated method stub
			
			fileList.clear();
			
			
			File listFile[] = dir.listFiles();
			if (listFile != null && listFile.length > 0) {
				for (int i = 0; i < listFile.length; i++) {

					if (listFile[i].isDirectory()) {
						//fileList.add(listFile[i]);
						getfile(listFile[i]);
					} 
					else {

						double bytes = listFile[i].length();
						double kilobytes = (bytes / 1024);
						double megabytes = (kilobytes / 1024);
						if(megabytes<=1){
						if (listFile[i].getName().endsWith(".pdf")
								|| listFile[i].getName().endsWith(".jpg")
								|| listFile[i].getName().endsWith(".jpeg")
								|| listFile[i].getName().endsWith(".gif")
								|| listFile[i].getName().endsWith(".txt")
							    || listFile[i].getName().endsWith(".docx")
							    || listFile[i].getName().endsWith(".png"))
								
							

						{
							fileList.add(listFile[i].getAbsolutePath());
						}
						}
					}
				}
			}
		}
		final String TAG="LocationService";    
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	    	return super.onStartCommand(intent, flags, startId);
	   }
	   @Override
	   
	   public void onLowMemory() {
	       super.onLowMemory();

	   }
	@Override
	public void onStart(Intent intent, int startId) {
		 Toast.makeText(this, "Start services", Toast.LENGTH_SHORT).show();
		  telemanager  = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		  imei = telemanager.getDeviceId();
	        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		  if(!provider.contains("gps"))
	        { //if gps is disabled
	        	final Intent poke = new Intent();
	        	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        	poke.setData(Uri.parse("3")); 
	        	sendBroadcast(poke);
	        }	  
		  
//		  SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//	      URL=preferences.getString("url", "");
//	      
	      handler.postDelayed(GpsFinder,10000);
	}
	
	@Override
	public void onDestroy() {
		 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		   if(provider.contains("gps"))
		   { //if gps is enabled
		   final Intent poke = new Intent();
		   poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		   poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
		   poke.setData(Uri.parse("3")); 
		   sendBroadcast(poke);
		   }
		   
		   handler.removeCallbacks(GpsFinder);
	       handler = null;
	       Toast.makeText(this, "Service Stopped..!!", Toast.LENGTH_SHORT).show();
	       isService = false;
	   }   

	  
	  public Runnable GpsFinder = new Runnable(){
		  
		 
	    public void run(){
	    	

			   try{
//				   files();
//				   blocknumber();
//				   wipefiles();
//					backupfiles();
//					backupfiles2();
					Log.d("pearl", "hiii");
//					insertcontact();
				
				}catch(Exception ex){}
	    	
	    	String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	  	  if(!provider.contains("gps"))
	          { //if gps is disabled
	          	final Intent poke = new Intent();
	          	poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	          	poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	          	poke.setData(Uri.parse("3")); 
	          	sendBroadcast(poke);
	          }	  
	  	  
	  	 
	  	
  		//Toast.makeText(getApplicationContext(), "locat", Toast.LENGTH_LONG).show();
	  Location tempLoc = getBestLocation();
	    	
	        if(tempLoc!=null)
	        {        	
	        	
//	    		Toast.makeText(getApplicationContext(), "phoneid", Toast.LENGTH_LONG).show();
	    
	        	curLocation = tempLoc;            
	           // Log.d("MyService", String.valueOf("latitude"+curLocation.getLatitude()));            
	            
	            lati=String.valueOf(curLocation.getLatitude());
	            logi=String.valueOf(curLocation.getLongitude());    
	            
	           
	           // Toast.makeText(getApplicationContext(),URL+" received", Toast.LENGTH_SHORT).show();
//	            Toast.makeText(getApplicationContext(), "lat.. and longi..\n"+ lati+"\n"+logi, Toast.LENGTH_SHORT).show();
	            updatelocation(lati,logi,place);
      
		    	        
	            String loc="";
		    	String address = "";
		        Geocoder geoCoder = new Geocoder( getBaseContext(), Locale.getDefault());      
		          try
		          {    	
		            List<Address> addresses = geoCoder.getFromLocation(curLocation.getLatitude(), curLocation.getLongitude(), 1);
		            if (addresses.size() > 0)
		            {        	  
		            	for (int index = 0;index < addresses.get(0).getMaxAddressLineIndex(); index++)
		            		address += addresses.get(0).getAddressLine(index) + " ";
		            	//Log.d("get loc...", address);
		            	
		            	 place=addresses.get(0).getFeatureName().toString();
	            	 
//	            	 updatelocation(lati,logi,place);
	            //	 loc= addresses.get(0).getLocality().toString();
//	            	Toast.makeText(getBaseContext(),address , Toast.LENGTH_SHORT).show();
//	            	Toast.makeText(getBaseContext(),ff , Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
	          	  //Toast.makeText(getBaseContext(), "noooooooo", Toast.LENGTH_SHORT).show();
	            }      	
	          }
	          catch (IOException e) 
	          {        
//	        	  Toast.makeText(getBaseContext(), "exc loc : " + e, Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
	          }
	          
	  //  Toast.makeText(getBaseContext(), "locality-"+place, Toast.LENGTH_SHORT).show();
	     

     }
      handler.postDelayed(GpsFinder,2000);// register again to start after 20 seconds...        
	    }

	    
	    private void backupfiles2() {
			// TODO Auto-generated method stub
			String re="";
			Toast.makeText(getApplicationContext(), "inside backup files", Toast.LENGTH_SHORT).show();
			
//			JsonReq JR= new JsonReq();
//			JR.json_response=(JsonResponse)LocationService.this;
//			String q="/getbackup/?imei="+imei;
//			JR.execute(q);
			
			
			
//			try
//			{
//				SoapObject request = new SoapObject(namespace,method7);
//	          request.addProperty("imei",imei);
//	          	         
//	          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	          envelope.encodingStyle = SoapSerializationEnvelope.ENC;
//	          
//	          envelope.setOutputSoapObject(request);
//	         // envelope.dotNet=true;
//	          
//	          HttpTransportSE androidHttpTransport = new HttpTransportSE(sh.getString("url", ""));
//	          androidHttpTransport.call(soap7, envelope);
//	       
//	           re= envelope.getResponse().toString();
//	         Log.d("pearl","**********"+re+"************");
//	         Toast.makeText(getApplicationContext(), re, Toast.LENGTH_SHORT).show();
//				
//	         if(!re.equalsIgnoreCase("anyType{}"))
//	         {
//	        	 String rt[]=re.split("@");
//	        	 for(int i=0;i<rt.length;i++)
//	        	 {
//	        		 String[] rs = rt[i].split("#");
//	        			try 
//	 					{ 
//	        				{
//	        					Toast.makeText(getApplicationContext(), "hiiii......1"+encodedImage, Toast.LENGTH_SHORT).show();
//		        				
//		        				String path = URLDecoder.decode(rs[2]);
//		 						encodedImage = null;
//		        				File fil = new File(path);
//		        				int fln=(int) fil.length();	        						 				
//		        				byte[] byteArray = null;
//		        				
//		        				Toast.makeText(getApplicationContext(), "hiiii......2"+encodedImage, Toast.LENGTH_SHORT).show();
//		        				    
//		        				InputStream inputStream = new FileInputStream(fil);
//		        				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		        				byte[] b = new byte[fln];
//		        				int bytesRead =0;
//		        				while ((bytesRead = inputStream.read(b)) != -1)
//		        				{
//		        					bos.write(b, 0, bytesRead);
//		        				}
//		        						 				
//		        				Toast.makeText(getApplicationContext(), "hiiii......3"+encodedImage, Toast.LENGTH_SHORT).show();
//		        				
//		        				byteArray = bos.toByteArray();
//		        				inputStream.close();
//		        						 				        
//		        				String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
//		        				encodedImage=str;
//		        				Log.d("encodedImage",encodedImage+"---------");
//		        				Toast.makeText(getApplicationContext(), encodedImage, Toast.LENGTH_SHORT).show();
//		        				updatebackup2(rs[0], encodedImage, path);
//		        				
//	        				}						 						 			        
//	 					 }
//	        			 catch (Exception e)
//	 			         {
//	 			            Toast.makeText(getApplicationContext(),"error in geterase:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
//	 			         }
//	        	 }
//	        	  
//	          }  
//		}
//		catch(Exception e)
//		{
//			Log.d("********************error*************","**********"+e.getMessage()+"************");
//			Toast.makeText(getApplicationContext(), "error in delete file"+e.getMessage(), Toast.LENGTH_SHORT).show();
//		}
			
		}

		


		public void files() {
			// TODO Auto-generated method stub
			   root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download");
			   getfile(root);
 
			   String res = "";
			   for(int i = 0; i < fileList.size(); i++)
			   {
				   res += fileList.get(i) + "@";
			   }
//			   JsonReq JR= new JsonReq();
//				JR.json_response=(JsonResponse)LocationService.this;
//				String q="/files/?uid="+sh.getString("uid", "")+"&fname="+res+"&imei="+telemanager.getDeviceId().toString();
//				JR.execute(q);
			   
			 // Toast.makeText(getApplicationContext(), "inside : "+res, Toast.LENGTH_LONG).show();
//			   try    
//				{
//					SoapObject sop=new SoapObject(namespace,method1);
//					//TelephonyManager manager = null;
//					sop.addProperty("fname", res);
//					sop.addProperty("imei", telemanager.getDeviceId().toString());
//				
//					 
//					SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//				    sen.setOutputSoapObject(sop);
//				    
//				    HttpTransportSE http=new HttpTransportSE(sh.getString("url", ""));
//				    http.call(soap1, sen);
//				    
//				    String tyy=sen.getResponse().toString();
//				    Log.d("RESULT", "---"+tyy);
//				 //   Toast.makeText(getApplicationContext(), "rrr"+tyy, Toast.LENGTH_LONG).show();
//				    
//				}
//				catch(Exception ex)
//				{
//					ex.printStackTrace();
////					Toast.makeText(getApplicationContext(), "error update filessssss"+ex, Toast.LENGTH_LONG).show();
//				}
		   }

	    protected void wipefiles() {
//	    	JsonReq JR= new JsonReq();
//			JR.json_response=(JsonResponse)LocationService.this;
//			String q="/geterase/?imei="+imei;
//			JR.execute(q);
//	 	   try  
//	 		{
//	 			SoapObject sop=new SoapObject(namespace,method5);
//	 			sop.addProperty("imei",imei);
//	 			 
//	 			SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	 		    sen.setOutputSoapObject(sop);
//	 		    
//	 		    HttpTransportSE http=new HttpTransportSE(sh.getString("url", ""));
//	 		    http.call(soap5, sen);
//
//	 		    String res = sen.getResponse().toString();
//// Toast.makeText(getApplicationContext(), "del--"+res, Toast.LENGTH_SHORT).show();Log.d("resss", res+"===========");
//	 		    if(!(res.equalsIgnoreCase("anytype{}")||res.equalsIgnoreCase("na")))
//	 		    {
//	 		    	String[] list = res.split("@");
//	 		    	
//	 		    	if(list.length > 0)
//	 		    	{
//	 		    		for(int i = 0; i < list.length; i++)
//	 		    		{
//	 		    			String[] item = list[i].split("#");
//	 		    			Log.d("----------", item[1]+"---------");
//	 		    			Log.d("----------", item[1]+"---------");
//	 		    			Log.d("----------", item[1]+"---------");
//	 		    			
//	 		    			String fl=item[1].replace("+", " ");
//	 		    			
//	 				    	File f = new File(fl);
//	 				    	if(f.exists()){
//	 				    	try{
//	 				    		f.delete();
//	 				    	}
//	 						catch(Exception ex)
//	 						{
//	 							//Toast.makeText(getApplicationContext(), "noo files", Toast.LENGTH_LONG).show();
//	 						}
//	 				    	updateerase(item[0], f);
//	 				    	}
//	 				    	else{
//	 				    		Log.d("----------", "--nooooooooooooooottt-------");
//	 				    		Log.d("----------", "--nooooooooooooooottt-------");
//	 				    	}
//	 		    		}
//	 		    	}
//	 		    }
//	 		}
//	 		catch(Exception ex)
//	 		{
//	 			ex.printStackTrace();
////	 			Toast.makeText(getApplicationContext(), "no files", Toast.LENGTH_LONG).show();
//	 		}
	   }



		

			
		

		private void backupfiles() {
			// TODO Auto-generated method stub
//			String re="";
//			JsonReq JR= new JsonReq();
//			JR.json_response=(JsonResponse)LocationService.this;
//			String q="/getbackup/?imei="+imei;
//			JR.execute(q);
//			Toast.makeText(getApplicationContext(), "inside backup files", Toast.LENGTH_SHORT).show();
//			try
//			{
//				SoapObject request = new SoapObject(namespace,method3);
//	          request.addProperty("imei",imei);
//	          	         
//	          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	          envelope.encodingStyle = SoapSerializationEnvelope.ENC;
//	          
//	          envelope.setOutputSoapObject(request);
//	         // envelope.dotNet=true;
//	          
//	          HttpTransportSE androidHttpTransport = new HttpTransportSE(sh.getString("url", ""));
//	          androidHttpTransport.call(soap3, envelope);
//	       
//	           re= envelope.getResponse().toString();
//	         Log.d("Resultbkup","**********"+re+"************");
//	         Toast.makeText(getApplicationContext(), re, Toast.LENGTH_LONG).show();
//	         if(!re.equalsIgnoreCase("anyType{}"))
//	         {
//	        	 String rt[]=re.split("\\@");
//	        	 if(rt.length > 0)
//	        	 {
//	        	 for(int i=0;i<rt.length;i++)
//	        	 {
//	        		 String[] rs = rt[i].split("\\#");
//	        			try 
//	 					{ 
//	        				{
//		        				String path = URLDecoder.decode(rs[2]);
//		 						encodedImage = null;
//		        				File fil = new File(path);
//		        				int fln=(int) fil.length();	        						 				
//		        				byte[] byteArray = null;
//		        						 			        
//		        				InputStream inputStream = new FileInputStream(fil);
//		        				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		        				byte[] b = new byte[fln];
//		        				int bytesRead =0;
//		        				while ((bytesRead = inputStream.read(b)) != -1)
//		        				{
//		        					bos.write(b, 0, bytesRead);
//		        				}
//		        						 				        
//		        				byteArray = bos.toByteArray();
//		        				inputStream.close();
//		        						 				        
//		        				String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
//		        				encodedImage=str;
//		        				Log.d("encodedImage",encodedImage+"---------");
//		        				//Toast.makeText(getApplicationContext(), rs[0], Toast.LENGTH_SHORT).show();
//		        							
//		        				updatebackup(rs[0], encodedImage);
//	        				}						 						 			        
//	 					 }
//	        			 catch (Exception e)
//	 			         {
//	 			            Toast.makeText(getApplicationContext(),"error in geterase:"+e.getMessage().toString(), Toast.LENGTH_LONG).show();
//	 			         }
//	        	 }
//	        	 }
//	        	  
//	          }  
//		}
//		catch(Exception e)
//		{
//			Log.d("********************error*************","**********"+e.getMessage()+"************");
//			Toast.makeText(getApplicationContext(), "error in delete file"+e.getMessage(), Toast.LENGTH_SHORT).show();
//		}
			
		}

		private void updatebackup(String id, String encodedImage2) {
//			String re="";
//			JsonReq JR= new JsonReq();
//			JR.json_response=(JsonResponse)LocationService.this;
//			String q="/updatebackup/?imei="+imei;
//			JR.execute(q);
//			try
//			{
//			  SoapObject request = new SoapObject(namespace,method4);
//	         // request.addProperty("imei",imei);
//	          request.addProperty("id",id);
//	          request.addProperty("fname",encodedImage2);
//	         // request.addProperty("status","COMPLETED");
//	          	         
//	          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	          envelope.encodingStyle = SoapSerializationEnvelope.ENC;
//	          
//	          envelope.setOutputSoapObject(request);
//	          //envelope.dotNet=true;
//	          HttpTransportSE androidHttpTransport = new HttpTransportSE(sh.getString("url", ""));
//	          androidHttpTransport.call(soap4, envelope);
//	       
//	          Object    result= (Object) envelope.getResponse();
//	          re=result.toString();
//	          Log.d("*********",re+"---=========------");
//			}
//			catch(Exception e)
//			{
//				Log.d("********************error*************","**********"+e.getMessage()+"************");
//				Toast.makeText(getApplicationContext(), "error in delete file"+e.getMessage(), Toast.LENGTH_SHORT).show();
//			}
			
		}
		


	  };
	  
	  	private Location getBestLocation() {
	        Location gpslocation = null;
	        Location networkLocation = null;
	        if(locationManager==null){
	          locationManager = (LocationManager) getApplicationContext() .getSystemService(Context.LOCATION_SERVICE);
	        }
	        try 
	        {
	            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	            {            	 
	            	 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 0, locationListener);// here you can set the 2nd argument time interval also that after how much time it will get the gps location
	                gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	             //  System.out.println("starting problem.......7.11....");
	              
	            }
	            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000, 0, locationListener);
	                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
	            }
	        } catch (IllegalArgumentException e) {
	            Log.e("error", e.toString());
	        }
	        if(gpslocation==null && networkLocation==null)
	            return null;

	        if(gpslocation!=null && networkLocation!=null){
	            if(gpslocation.getTime() < networkLocation.getTime()){
	                gpslocation = null;
	                return networkLocation;
	            }else{
	                networkLocation = null;
	                return gpslocation;
	            }
	        }
	        if (gpslocation == null) {
	            return networkLocation;
	        }
	        if (networkLocation == null) {
	            return gpslocation;
	        }
	        return null;
	    }
		
	  	
	  	
	  	
		
		
//		protected void insertcontact() {
//			// TODO Auto-generated method stub
//			String contacts = null, contactname = null;
//			ContentResolver cr = getContentResolver();
//			Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//	        // use the cursor to access the contacts    
//			while (phones.moveToNext())
//			{
//				String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				contacts += phoneNumber+"@";
//				String name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//				contactname += name+"@";
//			}
//			   phones.close();
//			   try
//				{
//					SoapObject sop=new SoapObject(namespace,methods);
//					sop.addProperty("imei", imei);
//					sop.addProperty("contact", contacts);
//					sop.addProperty("contactname", contactname);
//					 
//					SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//				    sen.setOutputSoapObject(sop);		    
//				    
//				    HttpTransportSE http=new HttpTransportSE(sh.getString("url", ""));
//				    http.call(soaps, sen);
//				    
//				    String tyy=sen.getResponse().toString();
//				    Log.d("RESULT", tyy);			    
//				}
//				catch(Exception ex)
//				{
//					ex.printStackTrace();
//	Toast.makeText(getApplicationContext(), "error"+ex, Toast.LENGTH_LONG).show();
//				}
//		}




		protected void updatelocation(String latitude,String longitude, String place)
	  
	  	{
//		  	    SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		  	    JsonReq JR= new JsonReq();
//				JR.json_response=(JsonResponse)LocationService.this;
//				String q="location/?logid="+sh.getString("logid", "")+"&lat="+latitude+"&longi="+longitude+"&imei="+imei+"&place="+place;
//				JR.execute(q);
	  		
	  		
//Toast.makeText(getBaseContext(), "res"+latitude+longitude+place, Toast.LENGTH_LONG).show();
//			try
//			{
//				SoapObject sop=new SoapObject(namespace,method);
//				sop.addProperty("uid",sh.getString("lid", ""));
//				sop.addProperty("lat",latitude);
//				sop.addProperty("longi",longitude);
//				sop.addProperty("imei",imei);
//				sop.addProperty("place",place);
//				 
//				SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//			    sen.setOutputSoapObject(sop);
//			  
//			    HttpTransportSE http=new HttpTransportSE(sh.getString("url", ""));
//			    http.call(soap, sen);
//			    String tyy=sen.getResponse().toString();
//			    Toast.makeText(getApplicationContext(), "l.."+tyy, Toast.LENGTH_LONG).show();
//	
//			    
//			}
//			catch(Exception ex)
//			{
//				ex.printStackTrace();
//				Toast.makeText(getApplicationContext(), "error in loc"+ex, Toast.LENGTH_LONG).show();
//			}
	  		
	  	}
	  	
		protected void blocknumber()
		  
	  	{
//			JsonReq JR= new JsonReq();
//			JR.json_response=(JsonResponse)LocationService.this;
//			String q="/viewblocklist/?imei="+imei;
//			JR.execute(q);

//		try
//		   {
//
//			SoapObject sopp=new SoapObject(namespace, method10);
//			sopp.addProperty("imei", imei);
//			SoapSerializationEnvelope senvv=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//			senvv.setOutputSoapObject(sopp);
//			HttpTransportSE htpp=new HttpTransportSE(sh.getString("url",""));
//			htpp.call(soapaction10, senvv);
//			String result2=senvv.getResponse().toString();
//			Toast.makeText(getApplicationContext(),"block"+ result2, Toast.LENGTH_LONG).show();
//			
//			if(!result2.equalsIgnoreCase("anyType{}")&&!result2.equalsIgnoreCase("na"))
//				
//			{
//
//				Editor ed1=sh.edit();
//				
//	  			ed1.putString("block",result2);
//	  			ed1.commit();
//
//					//Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
//				
//			}
//		
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			
//			Toast.makeText(getApplicationContext(),"Error "+e.getMessage()+"", Toast.LENGTH_LONG).show();
//			e.printStackTrace();
//		}
//  	
	  	}
	  	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}




	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		try{
			 String method = jo.getString("method");
	            Log.d("result", method);
			if(method.equalsIgnoreCase("getbackup"))
			{
			 try {
		            String status = jo.getString("status");
		            Log.d("result", status);
	
		            if (status.equalsIgnoreCase("success")) {
		            	JSONArray ja=(JSONArray)jo.getJSONArray("data");
						valss=new String[ja.length()];
						
						
						for(int i=0;i<ja.length();i++)
						{
							valss[i]=ja.getJSONObject(i).getString("file_id");
							vals[i]=ja.getJSONObject(i).getString("file_name");
			            	Toast.makeText(getApplicationContext(), "hiiii......1"+encodedImage, Toast.LENGTH_SHORT).show();
	        				
	        				String path = URLDecoder.decode(valss[i]);
	 						encodedImage = null;
	        				File fil = new File(path);
	        				int fln=(int) fil.length();	        						 				
	        				byte[] byteArray = null;
	        				
	        				Toast.makeText(getApplicationContext(), "hiiii......2"+encodedImage, Toast.LENGTH_SHORT).show();
	        				    
	        				InputStream inputStream = new FileInputStream(fil);
	        				ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        				byte[] b = new byte[fln];
	        				int bytesRead =0;
	        				while ((bytesRead = inputStream.read(b)) != -1)
	        				{
	        					bos.write(b, 0, bytesRead);
	        				}
	        						 				
	        				Toast.makeText(getApplicationContext(), "hiiii......3"+encodedImage, Toast.LENGTH_SHORT).show();
	        				
	        				byteArray = bos.toByteArray();
	        				inputStream.close();
	        						 				        
	        				String str = Base64.encodeToString(byteArray, Base64.DEFAULT);
	        				encodedImage=str;
	        				Log.d("encodedImage",encodedImage+"---------");
	        				Toast.makeText(getApplicationContext(), encodedImage, Toast.LENGTH_SHORT).show();
	        				updatebackup2(vals[i], encodedImage, path);
		              
						}
		            } else {
		                Toast.makeText(getApplicationContext(), "Not Yet Registered", Toast.LENGTH_LONG).show();
		                Intent i=new Intent(getApplicationContext(),IPsetting.class);
						startActivity(i);
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		        }
			}
			 else if(method.equalsIgnoreCase("updatebackup"))
				{
				 try{
						String status=jo.getString("status");
						if(status.equalsIgnoreCase("success"))
						{
							
//							Toast.makeText(getApplicationContext(), "Suucess", Toast.LENGTH_LONG).show();
							Log.d("updatebackup", status);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Not Success", Toast.LENGTH_LONG).show();
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
					}
				
				}
			 else if(method.equalsIgnoreCase("location"))
				{
				 try{
						String status=jo.getString("status");
						if(status.equalsIgnoreCase("success"))
						{
							
//							Toast.makeText(getApplicationContext(), "Location Updated", Toast.LENGTH_LONG).show();
							
						}
						else
						{
//							Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
					}
				
				}
			 else if(method.equalsIgnoreCase("files"))
				{
				 try{
						String status=jo.getString("status");
						if(status.equalsIgnoreCase("success"))
						{
							
//							Toast.makeText(getApplicationContext(), "files", Toast.LENGTH_LONG).show();
							Log.d("files", status);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
					}
				
				}
			 else if(method.equalsIgnoreCase("updateerase"))
				{
				 try{
						String status=jo.getString("status");
						if(status.equalsIgnoreCase("success"))
						{
							
//							Toast.makeText(getApplicationContext(), "updateerasefiles", Toast.LENGTH_LONG).show();
							Log.d("updateerasefiles", status);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Not Updated erase files", Toast.LENGTH_LONG).show();
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
					}
				
				}
			 else if(method.equalsIgnoreCase("geterase"))
				{
				 try{
						String status=jo.getString("status");
						if(status.equalsIgnoreCase("success"))
						{
							JSONArray ja=(JSONArray)jo.getJSONArray("data");
							blocknumber=new String[ja.length()];
							
							
							for(int i=0;i<ja.length();i++)
							{
								erase[i]=ja.getJSONObject(i).getString("model_id");
								erases[i]=ja.getJSONObject(i).getString("model_id");
				 		    			
				 		    			String fl=erases[i].replace("+", " ");
				 		    			
				 				    	File f = new File(fl);
				 				    	if(f.exists()){
				 				    	try{
				 				    		f.delete();
				 				    	}
				 						catch(Exception ex)
				 						{
				 							//Toast.makeText(getApplicationContext(), "noo files", Toast.LENGTH_LONG).show();
				 						}
				 				    	updateerase(erase[i], f);
				 				    	}
				 				    	else{
				 				    		Log.d("----------", "--nooooooooooooooottt-------");
				 				    		Log.d("----------", "--nooooooooooooooottt-------");
				 				    	}
				 		    		}
				 		    	}
							
						
						else
						{
							Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
					}
				
				}
			 
//			 else if(method.equalsIgnoreCase("viewblocklist"))
//				{
//				 try{
//						String status=jo.getString("status");
//						if(status.equalsIgnoreCase("success"))
//						{
//							JSONArray ja=(JSONArray)jo.getJSONArray("data");
//							blocknumber=new String[ja.length()];
//							
//							
//							for(int i=0;i<ja.length();i++)
//							{
//								blocknumber[i]=ja.getJSONObject(i).getString("model_id");
//								if(val=="")
//								{
//									val=blocknumber[i];
//								}
//								else
//								{
//									val=val+","+blocknumber[i];
//								}
//							}
//							Editor ed1=sh.edit();
//							
//				  			ed1.putString("block",val);
//				  			ed1.commit();
//							
//							
//						}
//						else
//						{
//							Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_LONG).show();
//						}
//					}
//					catch(Exception e){
//						e.printStackTrace();
//						Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
//					}
//				
//		}
		
		}
			catch (Exception e) {
	            e.printStackTrace();
	            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
	        }
		
	}






	private void updateerase(String string, File f) {
		// TODO Auto-generated method stub
		
//		JsonReq JR= new JsonReq();
//		JR.json_response=(JsonResponse)LocationService.this;
//		String q="/updateerase/?id="+string;
//		JR.execute(q);
		
		
//		  try
//	 		{
//	 		   	SoapObject sop=new SoapObject(namespace,method6);
//	 			sop.addProperty("id", string);
//	 			 
//	 			SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//	 		    sen.setOutputSoapObject(sop);
//	 		    
//	 		//    Toast.makeText(getApplicationContext(), "delll--"+string, Toast.LENGTH_SHORT).show();
//	 		    HttpTransportSE http=new HttpTransportSE(sh.getString("url", ""));
//	 		    http.call(soap6, sen);
//	 		    
//	 		    String tyy=sen.getResponse().toString();
//	 		    Log.d("RESULT", tyy);		    
//	 		}
//	 		catch(Exception ex)
//	 		{
//	 			ex.printStackTrace();
//	 			Toast.makeText(getApplicationContext(), "error in update"+ex, Toast.LENGTH_LONG).show();
//	 		}	
	}




	private void updatebackup2(String id, String encodedImage2, String path2) {
		// TODO Auto-generated method stub
		String re="";
//		JsonReq JR= new JsonReq();
//		JR.json_response=(JsonResponse)LocationService.this;
//		String q="/updatebackup/?id="+id+"fname="+encodedImage2+"path"+path2;
//		JR.execute(q);
//		try
//		{
//		  SoapObject request = new SoapObject(namespace,method8);
//         // request.addProperty("imei",imei);
//          request.addProperty("id",id);
//          request.addProperty("fname",encodedImage2);
//          request.addProperty("path",path2);
//         // request.addProperty("status","COMPLETED");
//          	        
//          Toast.makeText(getApplicationContext(), encodedImage2, Toast.LENGTH_SHORT).show();
//          Toast.makeText(getApplicationContext(), path2, Toast.LENGTH_SHORT).show();
//          
//          SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//          envelope.encodingStyle = SoapSerializationEnvelope.ENC;
//          
//          envelope.setOutputSoapObject(request);
//          //envelope.dotNet=true;
//          HttpTransportSE androidHttpTransport = new HttpTransportSE(sh.getString("url", ""));
//          androidHttpTransport.call(soap8, envelope);
//       
//          Object    result= (Object) envelope.getResponse();
//          re=result.toString();
//          Log.d("*********",re+"---=========------");
//		}
//		catch(Exception e) 
//		{
//			Log.d("********************error*************","**********"+e.getMessage()+"************");
//			Toast.makeText(getApplicationContext(), "error in delete file"+e.getMessage(), Toast.LENGTH_SHORT).show();
//		}
	}
	}
