package com.israel.livraisonexpresspos.ui.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.israel.livraisonexpresspos.MainActivity;
import com.israel.livraisonexpresspos.R;
import com.israel.livraisonexpresspos.app.App;
import com.israel.livraisonexpresspos.databinding.ActivityLoginBinding;
import com.israel.livraisonexpresspos.models.User;
import com.israel.livraisonexpresspos.utils.PreferenceUtils;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity{
    private ActivityLoginBinding mBinding;
    private LoginViewModel mViewModel;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.currentActivity = this;
    }

    private void initUI() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.create();
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        stream();

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verify()){
                    login();
                }
            }
        });
    }

    private void stream() {
        mViewModel.getLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean == null)return;
                if (aBoolean){
                    mDialog.show();
                }else {
                    mDialog.dismiss();
                }
            }
        });

        mViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == null)return;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Oops!")
                        .setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_round_warning_24))
                        .setMessage(s)
                        .setPositiveButton("Ok", null);
                if(!isFinishing())builder.create().show();
            }
        });

        mViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null)return;
                User.setCurrentUser(user);
                PreferenceUtils.setString(getApplicationContext(), PreferenceUtils.CURRENT_USER, new Gson().toJson(user));
                int roleCode = User.getRoleCode();
                if (roleCode == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                            .setIcon(R.drawable.ic_round_warning_24)
                            .setTitle("Non authorisé")
                            .setMessage("Vous n'ètes pas authorisé à utiliser cette application veuillez vous rapprocher du service technique pour plus de détails")
                            .setPositiveButton("Ok", null);
                    builder.create().show();
                    return;
                }

                PreferenceUtils.setString(LoginActivity.this, PreferenceUtils.ACCESS_TOKEN, user.getToken());
                PreferenceUtils.setLong(LoginActivity.this, PreferenceUtils.LAST_CONNECTION, Calendar.getInstance().getTimeInMillis());
                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void login(){
        mViewModel.login(this, mBinding.tietLogin.getText().toString(),
                mBinding.tietPassword.getText().toString());
    }

    private boolean verify(){
        if (mBinding.tietLogin.getText().toString().isEmpty()){
            mBinding.tietLogin.setError(getString(R.string.enter_address));
        }else if(!Patterns.EMAIL_ADDRESS.matcher(mBinding.tietLogin.getText()).matches()){
            mBinding.tietLogin.setError(getString(R.string.enter_valid_email));
        }else {
            mBinding.tietLogin.setError(null);
        }
        if (mBinding.tietPassword.getText().toString().isEmpty()){
            mBinding.tietPassword.setError(getString(R.string.enter_password));
        }else{
            mBinding.tietLogin.setError(null);
        }
        return mBinding.tietLogin.getError() == null &&
                mBinding.tietPassword.getError() == null;
    }
}
