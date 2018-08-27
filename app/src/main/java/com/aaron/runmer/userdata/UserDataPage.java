package com.aaron.runmer.userdata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aaron.runmer.R;
import com.aaron.runmer.base.BaseActivity;
import com.aaron.runmer.map.MapPage;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.google.common.base.Preconditions.checkNotNull;

public class UserDataPage extends BaseActivity implements UserDataContract.View, View.OnClickListener {

    private EditText mEditTxtUserName;
    private EditText mEditTxtUserEmail;
    private EditText mEditTxtUserBirth;
    private EditText mEditTxtUserHeight;
    private EditText mEditTxtUserWeight;
    private ImageView mImageUser;
    private ImageButton mImageBtnUserGarllery;
    private RadioButton mRdoBtnMale;
    private RadioButton mRdoBtnFemale;
    private RadioGroup mRadioGroup;
    private String mUserName;
    private String mUserEmail;
    private String mUserBirth;
    private String mUserHeight;
    private String mUserWeight;
    private String mUserPhoto;
    private String mUserGender;
    private Button mBtnOk;
    private FirebaseAuth mAuth;
    private UserDataContract.Presenter mPresenter;
    private UserData mUserData = new UserData();
    private DisplayMetrics mPhoto;

    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        init();
        getUserDataAndIntentToMapPage();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION);
        }
    }

    private void getUserDataAndIntentToMapPage() {
        mBtnOk.setOnClickListener(this);
    }

    private void init() {
        setContentView(R.layout.activity_user_data);
        findView();
        mPresenter = new UserDataPresenter(this);
        mPresenter.start();
        mPresenter.setUserNameAndEmail();
        mPresenter.setUserPhoto();
        mPresenter.setUserBirth();

        rdobtnclick();
        imagebtnclick();
    }

    private void imagebtnclick() {
        mImageBtnUserGarllery.setOnClickListener(this);
    }

    private void rdobtnclick() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkGenderBtn = mRadioGroup.getCheckedRadioButtonId();
                switch (checkGenderBtn) {
                    case R.id.rdobtn_male:
                        mUserGender = getString(R.string.male);
                        Log.d(Constants.TAG, "Gender : " + mUserGender);
                        break;
                    case R.id.rdobtn_female:
                        mUserGender = getString(R.string.female);
                        Log.d(Constants.TAG, "Gender : " + mUserGender);
                        break;
                    default:
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
        mImageUser = findViewById(R.id.imageUser_dataView);
        mImageBtnUserGarllery = findViewById(R.id.imagebtn_user_gallery);
        mRadioGroup = findViewById(R.id.rdogroup);
        mRdoBtnMale = findViewById(R.id.rdobtn_male);
        mRdoBtnFemale = findViewById(R.id.rdobtn_female);
    }

    @Override
    public void onClick(View view) {
        mEditTxtUserName.setText(userName);
        mEditTxtUserEmail.setText(userEmail);
    }

    @Override
    public void showUserBirth() {
            @Override
            public void onClick(View v) {
            case R.id.edittxt_birth:
                final Calendar c = Calendar.getInstance();       // Get Current Date
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                mEditTxtUserBirth.setText(year + "." + (monthOfYear + 1) + "." + dayOfMonth);
                            }
                        }, 1985, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btn_ok:
                mUserName = mEditTxtUserName.getText().toString().trim();
            }
                mUserEmail = mEditTxtUserEmail.getText().toString().trim();
                mUserBirth = mEditTxtUserBirth.getText().toString().trim();
                mUserHeight = mEditTxtUserHeight.getText().toString().trim();
                mUserWeight = mEditTxtUserWeight.getText().toString().trim();
                Log.e(Constants.TAG, "UserPhoto1 : " + mUserPhoto);

                if (!"".equals(mUserName) && !mUserPhoto.equals("") && !mUserEmail.equals("")
                        && !mUserBirth.equals("") && !mUserHeight.equals("") && !mUserWeight.equals("") && !mUserGender.equals("")) {
                    if (!mPresenter.isEmail(mUserEmail)) {
                        Toast.makeText(mContext, "Please type the correct email !", Toast.LENGTH_SHORT).show();
                    } else {
                        mUserData.setUserName(mUserName);
                        mUserData.setUserPhoto(mUserPhoto);
                        mUserData.setUserEmail(mUserEmail);
                        mUserData.setUserBirth(mUserBirth);
                        mUserData.setUserHeight(mUserHeight);
                        mUserData.setUserWeight(mUserWeight);
                        mUserData.setUserStatus(false);

                        getSharedPreferences(Constants.USER_FIREBASE, MODE_PRIVATE).edit()
                                .putString(Constants.USER_FIREBASE_NAME, mUserName)
                                .putString(Constants.USER_FIREBASE_EMAIL, mUserEmail)
                                .putString(Constants.USER_FIREBASE_PHOTO, mUserPhoto)
                                .putString(Constants.USER_FIREBASE_BIRTH, mUserBirth)
                                .putString(Constants.USER_FIREBASE_HEIGHT, mUserHeight)
                                .putString(Constants.USER_FIREBASE_WEIGHT, mUserWeight)
                                .putString(Constants.USER_FIREBASE_GENDER, mUserGender).apply();

                        mPresenter.setUserDataToFirebase(mUserData);

                        intent = new Intent();
                        intent.setClass(UserDataPage.this, MapPage.class);
                        startActivity(intent);
                        UserDataPage.this.finish();
                    }
                } else {
                    Toast.makeText(mContext, "Please do not leave any blank field !", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showUserNameAndEmail(String userName, String userEmail) {
        mEditTxtUserName.setText(userName);
        mEditTxtUserEmail.setText(userEmail);
    }

    @Override
    public void showUserBirth() {
        mEditTxtUserBirth.setInputType(InputType.TYPE_NULL);        //回頭修改時軟鍵盤無法收合(第一次)
        mEditTxtUserBirth.setOnClickListener(this);
    }

    @Override
    public void showUserPhoto(String userimage) {
        Log.d(Constants.TAG, "UserImage :" + userimage);
        Picasso.get().load(userimage).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(mImageUser);
    }

    @Override
    public void setPresenter(UserDataContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}