package com.example.consultasql;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ConfigurationServer extends Activity {
	SqlProcess conection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration_server);
		
		
		// Asigno los valores 
				TextView textIP = (TextView)findViewById(R.id.DtIpnom);      
				TextView textPuerto = (TextView)findViewById(R.id.DTPuerto);
				TextView textContrasena = (TextView)findViewById(R.id.DTPass);
				TextView textUsuario = (TextView)findViewById(R.id.DTUser);
				Button buttonProbarConexion = (Button) findViewById(R.id.button1);
				
				
				
				conection= new SqlProcess(textUsuario.getText().toString(), textContrasena.getText().toString(), 
						textIP.getText().toString(), textPuerto.getText().toString(), getApplicationContext());
				
				buttonProbarConexion.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(conection.EstablishConection("") ){
							Intent intent = new Intent(getApplicationContext(), PageConsult.class);
							startActivity(intent);
							
						}	
						
					}
				});
		
	}
}
