package com.example.aaron.runmer.UserData;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.aaron.runmer.Base.BaseActivity;
import com.example.aaron.runmer.Map.MapPage;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPage extends BaseActivity implements UserDataContract.View {

    private EditText mEditTxtUserName, mEditTxtUserEmail, mEditTxtUserBirth, mEditTxtUserHeight, mEditTxtUserWeight;
    private ImageView mImageUser;
    private ImageButton mImageBtnUserGarllery;
    private RadioButton mRdoBtnMale, mRdoBtnFemale;
    private RadioGroup mRadioGroup;
    private Button mBtnOk;
    private FirebaseAuth mAuth;
    private UserDataContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

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
        mAuth = FirebaseAuth.getInstance();
        findView();
        mPresenter = new UserDataPresenter(this);
        mPresenter.start();
        mPresenter.setUserNameAndEmail();
        mPresenter.setUserPhoto();
    }

    private void findView() {
        mBtnOk = findViewById(R.id.btn_ok);
        mEditTxtUserName = findViewById(R.id.edittxt_name);
        mEditTxtUserEmail = findViewById(R.id.edittxt_email);
        mEditTxtUserBirth = findViewById(R.id.edittxt_birth);
        mEditTxtUserHeight = findViewById(R.id.edittxt_height);
        mEditTxtUserWeight = findViewById(R.id.edittxt_weight);
        mImageUser = findViewById(R.id.image_user);
        mImageBtnUserGarllery = findViewById(R.id.imagebtn_user_gallery);
        mRadioGroup = findViewById(R.id.rdogroup);
        mRdoBtnMale = findViewById(R.id.rdobtn_male);
        mRdoBtnFemale = findViewById(R.id.rdobtn_female);
    }

    @Override
    public void showUserNameAndEmail() {
        mEditTxtUserName.setText(mAuth.getCurrentUser().getDisplayName());
        mEditTxtUserEmail.setText(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void showUserBirth() {

    }

    @Override
    public void showUserPhoto() {
        String userimage = mAuth.getCurrentUser().getPhotoUrl() + "?type=large";
        Log.d(Constants.TAG,"UserImage :" + String.valueOf(userimage));
        Picasso.get().load(userimage).placeholder(R.drawable.userholderimage).transform(new CircleTransform(mContext)).into(mImageUser);
    }

    @Override
    public void setPresenter(UserDataContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}