package br.com.artevivapublicidade.expertpdv.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.artevivapublicidade.expertpdv.R;
import br.com.artevivapublicidade.expertpdv.model.User;
import br.com.artevivapublicidade.expertpdv.service.UserLogin;

public class LoginActivity extends AppCompatActivity {
    public EditText id;
    public EditText password;
    public Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = (EditText) findViewById(R.id.idw);
        id.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                id.setFocusable(true);
                id.setFocusableInTouchMode(true);
                return false;
            }
        });

        password = (EditText) findViewById(R.id.pwdw);
        password.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                password.setFocusable(true);
                password.setFocusableInTouchMode(true);
                return false;
            }
        });
        signin = (Button) findViewById(R.id.signinw);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login()  {
        final String id1 = id.getText().toString().trim();
        final String pwd1 = password.getText().toString().trim();
        if (TextUtils.isEmpty(id1)) {
            Toast.makeText(this, "Entre com cpf/cnpj", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd1)) {
            Toast.makeText(this, "Entre com a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        UserLogin userLogin= new UserLogin(this);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        try {
            progressDialog.setMessage("Entrando...");
            progressDialog.show();
            Boolean usercode = userLogin.doLogin(id1,pwd1);
            if (usercode== true){
                progressDialog.dismiss();
                Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            User user = new User(getApplicationContext());
        }catch (Exception ex){
            progressDialog.dismiss();
            Toast.makeText(this, "Dados incorretos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
