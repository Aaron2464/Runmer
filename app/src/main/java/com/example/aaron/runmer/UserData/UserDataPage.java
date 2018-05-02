package com.example.aaron.runmer.UserData;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

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

    private int mYear,mMonth,mDay;

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
        mPresenter.setUserBirth();
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
    public void showUserNameAndEmail() {
        mEditTxtUserName.setText(mAuth.getCurrentUser().getDisplayName());
        mEditTxtUserEmail.setText(mAuth.getCurrentUser().getEmail());
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
                                mEditTxtUserBirth.setText( year+ "." + dayOfMonth + "." + (monthOfYear + 1));
                            }
                        }, 1985, mMonth, mDay);
                datePickerDialog.show();
            }
        });
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