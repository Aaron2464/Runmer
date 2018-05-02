package com.example.aaron.runmer.UserData;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aaron.runmer.Base.BaseActivity;
import com.example.aaron.runmer.Map.MapPage;
import com.example.aaron.runmer.Objects.UserData;
import com.example.aaron.runmer.R;
import com.example.aaron.runmer.util.CircleTransform;
import com.example.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPage extends BaseActivity implements UserDataContract.View {

    private EditText mEditTxtUserName, mEditTxtUserEmail, mEditTxtUserBirth, mEditTxtUserHeight, mEditTxtUserWeight;
    private ImageView mImageUser;
    private ImageButton mImageBtnUserGarllery;
    private RadioButton mRdoBtnMale, mRdoBtnFemale;
    private RadioGroup mRadioGroup;
    private String mUserName, mUserEmail, mUserBirth, mUserHeight, mUserWeight, mUserPhoto, mUserGender;
    private Button mBtnOk;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private UserDataContract.Presenter mPresenter;
    private Map<String, String> UserDataMap = new HashMap<String, String>();

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        init();
        getUserDataAndIntentToMapPage();
    }

    private void getUserDataAndIntentToMapPage() {
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mEditTxtUserName.getText().toString().trim();
                mUserPhoto = mAuth.getCurrentUser().getPhotoUrl() + "?type=large";
                mUserEmail = mEditTxtUserEmail.getText().toString().trim();
                mUserBirth = mEditTxtUserBirth.getText().toString().trim();
                mUserHeight = mEditTxtUserHeight.getText().toString().trim();
                mUserWeight = mEditTxtUserWeight.getText().toString().trim();

                if (!mUserName.equals("")
                        && !mUserPhoto.equals("") && !mUserEmail.equals("")
                        && !mUserBirth.equals("") && !mUserHeight.equals("")
                        && !mUserWeight.equals("") && !mUserGender.equals("")) {
                    UserDataMap.put("UserName",mUserName);
                    UserDataMap.put("UserEmail",mUserEmail);
                    UserDataMap.put("UserPhoto",mUserPhoto);
                    UserDataMap.put("UserBirth",mUserBirth);
                    UserDataMap.put("UserHeight",mUserHeight);
                    UserDataMap.put("UserWeight",mUserWeight);
                    UserDataMap.put("UserGender",mUserGender);
                    mPresenter.setUserDataToFirebase(UserDataMap);

                    Intent intent = new Intent();
                    intent.setClass(UserDataPage.this, MapPage.class);
                    startActivity(intent);
                    UserDataPage.this.finish();
                } else {
                    Toast.makeText(mContext, "Please do not leave any blank field !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        setContentView(R.layout.activity_user_data);
        findView();
        mPresenter = new UserDataPresenter(this);
        mPresenter.start();
        mPresenter.setUserNameAndEmail();
        mPresenter.setUserPhoto();
        mPresenter.setUserBirth();

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkGenderBtn = mRadioGroup.getCheckedRadioButtonId();
                switch (checkGenderBtn) {
                    case R.id.rdobtn_male:
                        mUserGender = getString(R.string.male);
                        Log.d(Constants.TAG,"Gender : " + mUserGender);
                        break;
                    case R.id.rdobtn_female:
                        mUserGender = getString(R.string.female);
                        Log.d(Constants.TAG,"Gender : " + mUserGender);
                        break;
                }
            }
        });
    }

    private void findView() {
        mBtnOk = findViewById(R.id.btn_ok);
        mEditTxtUserName = findViewById(R.id.edittxt_name);
        mEditTxtUserEmail = findViewById(R.id.edittxt_email);
        mEditTxtUserBirth = findViewById(R.id.edittxt_birth);
        mEditTxtUserBirth.setInputType(InputType.TYPE_NULL);
        mEditTxtUserHeight = findViewById(R.id.edittxt_height);
        mEditTxtUserWeight = findViewById(R.id.edittxt_weight);
        mImageUser = findViewById(R.id.image_user);
        mImageBtnUserGarllery = findViewById(R.id.imagebtn_user_gallery);
        mRadioGroup = findViewById(R.id.rdogroup);
        mRdoBtnMale = findViewById(R.id.rdobtn_male);
        mRdoBtnFemale = findViewById(R.id.rdobtn_female);
    }

    @Override
    public void showUserNameAndEmail(String userName, String userEmail) {
        mEditTxtUserName.setText(userName);
        mEditTxtUserEmail.setText(userEmail);
    }

    @Override
    public void showUserBirth() {
        mEditTxtUserBirth.setInputType(InputType.TYPE_NULL);        //回頭修改時軟鍵盤無法收合(第一次)
        mEditTxtUserBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();       // Get Current Date
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext
                        , AlertDialog.THEME_HOLO_LIGHT
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mEditTxtUserBirth.setText(year + "." + dayOfMonth + "." + (monthOfYear + 1));
                    }
                }, 1985, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void showUserPhoto(String userimage) {
        Log.d(Constants.TAG, "UserImage :" + userimage);
        Picasso.get().load(userimage).placeholder(R.drawable.userholderimage).transform(new CircleTransform(mContext)).into(mImageUser);
    }

    @Override
    public void setPresenter(UserDataContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}