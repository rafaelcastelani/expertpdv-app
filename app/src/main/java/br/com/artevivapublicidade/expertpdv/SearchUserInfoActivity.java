package br.com.artevivapublicidade.expertpdv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user_info);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
           /* case R.id.btnNextSearchUserInfo:
                User user = new User(getApplicationContext());
               //user.findUserByUserAccessCode();*/
        }
    }
}
