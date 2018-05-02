package com.example.aaron.runmer.UserData;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.aaron.runmer.Base.BaseActivity;
import com.example.aaron.runmer.Map.MapContract;
import com.example.aaron.runmer.Map.MapPage;
import com.example.aaron.runmer.R;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPage extends BaseActivity implements UserDataContract.View {

    private EditText mEditTxtUserName, mEditTxtUserEmail, mEditTxtUserBirth, mEditTxtUserHeight, mEditTxtUserWeight;
    private RadioButton mRdoBtnMale, mRdoBtnFemale;
    private RadioGroup mRadioGroup;
    private Button mBtnOk;
    private FirebaseAuth mAuth;
    private UserDataContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        mAuth = FirebaseAuth.getInstance();
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserDataPage.this, MapPage.class);
                startActivity(intent);
                UserDataPage.this.finish();
            }
        });
    }

    private void init() {
        setContentView(R.layout.activity_user_data);
        findView();
        mPresenter = new UserDataPresenter(this);
        mPresenter.start();
        mPresenter.setUserNameAndEmail();
    }

    private void findView() {
        mBtnOk = findViewById(R.id.btn_ok);
        mEditTxtUserName = findViewById(R.id.edittxt_name);
        mEditTxtUserEmail = findViewById(R.id.edittxt_email);
        mEditTxtUserBirth = findViewById(R.id.edittxt_birth);
        mEditTxtUserHeight = findViewById(R.id.edittxt_height);
        mEditTxtUserWeight = findViewById(R.id.edittxt_weight);
        mRadioGroup = findViewById(R.id.rdogroup);
        mRdoBtnMale = findViewById(R.id.rdobtn_male);
        mRdoBtnFemale = findViewById(R.id.rdobtn_female);
    }

    @Override
    public void showUserNameAndEmail() {
        mAuth = FirebaseAuth.getInstance();
        mEditTxtUserName.setText(mAuth.getCurrentUser().getDisplayName());
        mEditTxtUserEmail.setText(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void showUserBirth() {

    }

    @Override
    public void showUserPhoto() {

    }

    @Override
    public void setPresenter(UserDataContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}