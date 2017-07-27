package pucit.edu.pk.fp;


import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import static android.content.ContentValues.TAG;

public class MainActivity extends ActionBarActivity {

	LoginDataBaseAdapter loginDataBaseAdapter;
	Button login;
	Button registerr;
	EditText enterpassword,e;
	TextView forgetpass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		login=(Button)findViewById(R.id.login_btn);
		registerr=(Button)findViewById(R.id.register_btn);
		enterpassword=(EditText)findViewById(R.id.password_edt);
		e =(EditText)findViewById(R.id.email1);
		forgetpass=(TextView)findViewById(R.id.textView2);

		loginDataBaseAdapter = new LoginDataBaseAdapter(getApplicationContext());
		loginDataBaseAdapter.open();

		registerr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,Registration.class);
				startActivity(i);
				finish();
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});


		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String Password=enterpassword.getText().toString();
				String email = e.getText().toString();
				String storedEmail = loginDataBaseAdapter.getSinlgeEntry(email,Password);
				//String pass= loginDataBaseAdapter.getSinlgeEntryPassword(Password);
				if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
					Toast.makeText(MainActivity.this, "Please Validate Your email ", Toast.LENGTH_LONG).show();
				}
				if (Password.isEmpty() || Password.length() < 4 || Password.length() > 10) {
					Toast.makeText(MainActivity.this, "Please Validate Your Password", Toast.LENGTH_LONG).show();
				}

				// TODO Auto-generated method stub
				final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_LightBlue_Dialog
					);
				progressDialog.setIndeterminate(true);
				progressDialog.setMessage("Authenticating...");
				progressDialog.setCanceledOnTouchOutside(true);

				progressDialog.show();
				progressDialog.getWindow().setGravity(Gravity.BOTTOM);
				// TODO: Implement your own authentication logic here.

				new android.os.Handler().postDelayed(
						new Runnable() {
							public void run() {
								// On complete call either onLoginSuccess or onLoginFailed

								onLoginSuccess();

								// onLoginFailed();
								progressDialog.dismiss();
							}
						}, 3000);

			}


		});
		forgetpass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.getWindow();
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  
				dialog.setContentView(R.layout.forget_search);
				dialog.show();

				final  EditText security=(EditText)dialog.findViewById(R.id.securityhint_edt);
				final  TextView getpass=(TextView)dialog.findViewById(R.id.textView3);

				Button ok=(Button)dialog.findViewById(R.id.getpassword_btn);
				Button cancel=(Button)dialog.findViewById(R.id.cancel_btn);

				ok.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						String userName=security.getText().toString();
						if(userName.equals(""))
						{
							Toast.makeText(getApplicationContext(), "Please enter your securityhint", Toast.LENGTH_SHORT).show();
						}
						else
						{
							String storedPassword=loginDataBaseAdapter.getAllTags(userName);
							if(storedPassword==null)
							{
								Toast.makeText(getApplicationContext(), "Please enter correct securityhint", Toast.LENGTH_SHORT).show();
							}else{
								Log.d("GET PASSWORD",storedPassword);
								getpass.setText(storedPassword);
							}
						}
					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}

	private void onLoginFailed() {
	}

	public boolean validate() {
		boolean valid = true;

		String email = e.getText().toString();
		String password=enterpassword.getText().toString();

		if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			Toast.makeText(MainActivity.this, "Validate your email", Toast.LENGTH_LONG).show();
			valid = false;
		}

		return valid;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Close The Database
		loginDataBaseAdapter.close();
	}

	public void onLoginSuccess()
	{
		String Password=enterpassword.getText().toString();
		String email = e.getText().toString();
		String storedEmail = loginDataBaseAdapter.getSinlgeEntry(email,Password);
		//String pass= loginDataBaseAdapter.getSinlgeEntryPassword(Password);
		if(email.equals(storedEmail))
		{
			Toast.makeText(MainActivity.this, "Congrats: Login Successfully", Toast.LENGTH_LONG).show();
			Intent ii=new Intent(MainActivity.this,Home.class);
			startActivity(ii);
		}
		else
		{
			Toast.makeText(MainActivity.this, "User:" + "  " +storedEmail.toString(), Toast.LENGTH_LONG).show();
		}

	}

}
