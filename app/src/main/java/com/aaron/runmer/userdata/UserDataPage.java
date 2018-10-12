package com.aaron.runmer.userdata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aaron.runmer.R;
import com.aaron.runmer.base.BaseActivity;
import com.aaron.runmer.map.MapPage;
import com.aaron.runmer.objects.UserData;
import com.aaron.runmer.util.CircleTransform;
import com.aaron.runmer.util.Constants;
import com.aaron.runmer.viewpagermain.ViewPagerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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

    private LinearLayout mCameraLayout;
    private LinearLayout mAlbumLayout;
    private AlertDialog pictureDialog;

    private int mYear;
    private int mMonth;
    private int mDay;

    private File mCameraFile;
    private File mGalleryFile;
    private Uri mCameraUri;
    private Uri mGalleryUri;

    @SuppressLint("SdCardPath")
    File file = new File(
            "/data/data/com.aaron.runmer/shared_prefs/Users.xml");

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
        Intent intent;
        pictureDialog = new AlertDialog.Builder(mContext).create();
        switch (view.getId()) {
            case R.id.imagebtn_user_gallery:
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View dialogGallery = inflater.inflate(R.layout.dialog_gallery, null);
                pictureDialog.setView(dialogGallery);
                mAlbumLayout = dialogGallery.findViewById(R.id.layout_album);
                mCameraLayout = dialogGallery.findViewById(R.id.layout_camera);
                mAlbumLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.CODE_READ_EXTERNAL);
                        }
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("image/*");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            File galleryPhotoFile = null;
                            try {
                                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                galleryPhotoFile = mPresenter.createImageFile(storageDir);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (galleryPhotoFile != null) {
                                mCameraUri = FileProvider.getUriForFile(mContext,
                                        "com.aaron.runmer.fileprovider",
                                        galleryPhotoFile);
                            }
                            Log.e(Constants.TAG, "getPicFromAlbm: " + mCameraUri);
                            startActivityForResult(intent, Constants.CODE_GALLERY_REQUEST);
                        }
                        pictureDialog.dismiss();
                    }
                });
                mCameraLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureDialog.dismiss();
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constants.CODE_CAMERA_REQUEST);
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            mCameraFile = null;
                            try {
                                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                mCameraFile = mPresenter.createImageFile(storageDir);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (mCameraFile != null) {
                                mCameraUri = FileProvider.getUriForFile(mContext,
                                        "com.aaron.runmer.fileprovider",
                                        mCameraFile);
                            }
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
                        intent.putExtra("ImageUri", mCameraUri);
                        startActivityForResult(intent, Constants.CODE_CAMERA_REQUEST);
                        pictureDialog.dismiss();
                    }
                });
                pictureDialog.show();
                break;
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
                if (mCameraUri == null) {
                    mUserPhoto = mAuth.getCurrentUser().getPhotoUrl() + "?type=large";
                } else {
                    mUserPhoto = mPresenter.catchUserPhoto();
                }
                mUserEmail = mEditTxtUserEmail.getText().toString().trim();
                mUserBirth = mEditTxtUserBirth.getText().toString().trim();
                mUserHeight = mEditTxtUserHeight.getText().toString().trim();
                mUserWeight = mEditTxtUserWeight.getText().toString().trim();
                Log.e(Constants.TAG, "UserPhoto1 : " + mUserPhoto);

                if (!isPreference()) {
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
                } else {
                    intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setClass(UserDataPage.this, ViewPagerActivity.class);
                    startActivity(intent);
                    UserDataPage.this.finish();
                }
                break;
            default:
                break;
        }
    }

    private boolean isPreference() {
        if (file.exists()) {
            Log.d(Constants.TAG, "SharedPreferences Name_of_your_preference : exist");
            return true;
        } else {
            Log.d(Constants.TAG, "Setup default preferences");
            return false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case Constants.CODE_CAMERA_REQUEST:   //調用相機後返回
                if (resultCode == RESULT_OK) {
                    //用相機返回的照片去調用剪裁也需要對Uri進行處理
                    Log.d(Constants.TAG, "CAMERA_REQUEST: " + mCameraUri);
                    cropPhoto(mCameraUri, mCameraUri);
                }
                break;
            case Constants.CODE_GALLERY_REQUEST:    //調用相冊後返回
                if (resultCode == RESULT_OK) {
                    mGalleryUri = intent.getData();
                    Log.e(Constants.TAG, "GALLERY_REQUEST: " + mGalleryUri);
                    String path = getRealPathFromUri(mGalleryUri);
                    mGalleryFile = new File(path);
                    Log.e(Constants.TAG, "GALLERY_REQUEST: " + mGalleryFile);
                    cropPhoto(mCameraUri, FileProvider.getUriForFile(mContext, "com.aaron.runmer.fileprovider", mGalleryFile));
                }
                break;
            case Constants.CODE_CROP_REQUEST:     //調用剪裁後返回
                mPresenter.uploadAndReturnUrl(mCameraUri);          //上傳並回傳 url
                Picasso.get().load(mCameraUri).placeholder(R.drawable.user_image).transform(new CircleTransform(mContext)).into(mImageUser);
                break;
            default:
                break;
        }
    }

    private void cropPhoto(Uri uri, Uri cropImageUri) {
        try {
            Intent intent1 = new Intent("com.android.camera.action.CROP");
            intent1.setDataAndType(cropImageUri, "image/*");
            intent1.putExtra("crop", "true");
            intent1.putExtra("aspectX", 1);
            intent1.putExtra("aspectY", 1);
            intent1.putExtra("outputX", 300);
            intent1.putExtra("outputY", 300);
            intent1.putExtra("scale", true);
            intent1.putExtra("return-data", false);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent1, PackageManager.MATCH_DEFAULT_ONLY);
            Log.e(Constants.TAG, "cropPhoto: " + String.valueOf(resInfoList));
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                Log.e(Constants.TAG, "cropPhoto: " + packageName);
                mContext.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //Call the method Context.grantUriPermission(package, Uri, mode_flags) for the content:// Uri, using the desired mode flags.
            }
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent1, Constants.CODE_CROP_REQUEST);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Current Device Has No Crop Support", Toast.LENGTH_LONG).show();
        }
    }

    private String getRealPathFromUri(Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };

        return getDataColumn(mContext, contentUri, selection, selectionArgs);
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}