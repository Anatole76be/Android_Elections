package com.example.a_elections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	Button bouton;
	TextView texte;
	//TextView reponse;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bouton = (Button)findViewById(R.id.bouton);
		texte = (TextView) findViewById(R.id.texte);
		//reponse = (TextView) findViewById(R.id.reponse);

		
		OnClickListener clickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				texte.setText("Coucou");
				new MyAsyncTask().execute();
				
				
			}
		};
		bouton.setOnClickListener(clickListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		 
 
	//Async TASK
	private class MyAsyncTask extends AsyncTask<String, Integer, String>{
		
		TextView reponse = (TextView)findViewById(R.id.reponse);
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//postData(params[0]);
			String data =  getData();
			return data;
		}
 
		
		public String getData() {
	        String result ="";
	        InputStream isr = null;
	        try {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost post = new HttpPost("http://109.88.27.206/ANDROID/getListe.php");
	            HttpResponse repons = httpclient.execute(post);
	            HttpEntity enty = repons.getEntity();
	            isr=enty.getContent();

	            Log.d("http", "ok");
	        } catch (Exception e) {
	            Log.d("Error", "http error");
	            
	        }

	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	                while ((line = reader.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                isr.close();
	                result=sb.toString();
	                Log.d("result", result);
	        } catch (Exception e) {
	            Log.d("Error", "converting Error");
	        }


	        try {
	            JSONArray jArray =  new JSONArray(result);  
	            String s="";
	            for (int i = 0; i < jArray.length(); i++) {
	                JSONObject json = jArray.getJSONObject(i);  
	                s =s+"ID :"+json.getInt("ID")+"\n";
	            }     

	        } catch (Exception e) {
	            Log.d("Error", "Errof"+e.toString());
	        }
			return result;
	    }
 
		public void postData(String valueIWantToSend) {
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://109.88.27.206/ANDROID/vote.php");
 
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("NOM", valueIWantToSend));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);				
				
 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
 
	}
		
		
		 @Override
		    protected void onPostExecute(String  result) {  // result is data returned by doInBackground
		        super.onPostExecute(result);
		        reponse.setText(result.toString());
		        
		    }

	}
	
}
