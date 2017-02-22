package realizer.com.mysurvey.registration;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import realizer.com.mysurvey.LoginActivity;
import realizer.com.mysurvey.R;
import realizer.com.mysurvey.registration.asynctask.RegistrationAsyncTaskPost;
import realizer.com.mysurvey.registration.model.RegistrationModel;
import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.ImageStorage;
import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.views.ProgressWheel;

/**
 * Created by Win on 30/11/2016.
 */
public class RegistrationActivity extends AppCompatActivity implements OnTaskCompleted
{
    ImageView userimg;
    Calendar myCalendar;
    String imagebase64="";
    Button btnregistration;

    RadioButton rdomale,rdofemale;
    ProgressWheel loading;
    DatePickerDialog.OnDateSetListener date;
    EditText fName,lName,eMailid,phoneNo,userId,pass,confirmPass,magicWord;
    TextView txtRegDob;
    private Uri fileUri;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    String localPath="",dob="";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    Bitmap bitmap;
    static String conpass,passstr,studFname,studLname,emailid,phoneno,userIdstr,magicWordstr,gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Registration");
        getSupportActionBar().setLogo(R.drawable.chat_icon_new);

        myCalendar = Calendar.getInstance();
        InitializeVariable();
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOption();
            }
        });
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                upDateLable();
            }

        };

    }
    public void InitializeVariable()
    {
        userimg= (ImageView) findViewById(R.id.reg_user_image);
        fName= (EditText) findViewById(R.id.edt_reg_stdfname);
        lName= (EditText) findViewById(R.id.edt_reg_stdlname);
        eMailid= (EditText) findViewById(R.id.edt_reg_emailid);
        phoneNo= (EditText) findViewById(R.id.edt_reg_phoneno);
        userId= (EditText) findViewById(R.id.edt_reg_userid);
        pass= (EditText) findViewById(R.id.edt_reg_pass);
        confirmPass= (EditText) findViewById(R.id.edt_reg_confirmpass);
        magicWord= (EditText) findViewById(R.id.edt_reg_magicword);
        txtRegDob= (TextView) findViewById(R.id.txt_reg_dob);
        rdomale= (RadioButton) findViewById(R.id.rdoMale);
        rdofemale= (RadioButton) findViewById(R.id.rdoFemale);
        loading = (ProgressWheel) findViewById(R.id.loading);
        btnregistration= (Button) findViewById(R.id.btnRegister);
    }
    public void regdobclick(View view)
    {
        new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public void RegisterClick(View v)
    {
        GetAllData();
        if (studFname.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this, "Alert", "Please Enter First Name");
        }
        else if (studLname.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter Last Name");
            // Toast.makeText(Registration2Activity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
        }
        else if (emailid.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter Email ID");
            //Toast.makeText(Registration2Activity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
        }
        else if (!emailid.matches(emailPattern))
        {
            Config.alertDialog(RegistrationActivity.this, "Error", "Email ID is not Valid");
            //Toast.makeText(Registration2Activity.this, "Enter Valid Emailid", Toast.LENGTH_SHORT).show();
        }
        else if (dob.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Select Date of Birth");
            //Toast.makeText(Registration2Activity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (gender.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Select Gender");
            //Toast.makeText(Registration2Activity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }

        else if (phoneno.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter Contact Number");
            //Toast.makeText(Registration2Activity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if(userIdstr.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter User Id");
            // Toast.makeText(Registration2Activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(userIdstr.contains(" "))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","User Id doesn't have blank spaces");
            // Toast.makeText(Registration2Activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(userIdstr.trim().length()>10 || userIdstr.trim().length()<6)
        {
            Config.alertDialog(RegistrationActivity.this,"Error","User Id must be 6 characters");
        }
        else if(passstr.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter Password");
            // Toast.makeText(Registration2Activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if(passstr.trim().length()>10||passstr.trim().length()<6)
        {
            Config.alertDialog(RegistrationActivity.this, "Error", "Password Must Be 6 Characters or Digits");
        }
        else if (conpass.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this, "Alert", "Please Enter Confirm Password");
            //Toast.makeText(Registration2Activity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
        }
        else if (magicWordstr.equals(""))
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Please Enter Magic Word");
            //Toast.makeText(Registration2Activity.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
            magicWord.requestFocus();
        }

        else if (passstr.equals(conpass))
        {
            //Toast.makeText(RegistrationActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
            //Config.alertDialog(RegistrationActivity.this,"",""+studFname +studLname+dob+emailid+gender+phoneno+userIdstr+passstr+magicWordstr);
            RegistrationModel rgm=new RegistrationModel();
            rgm.setDob(dob);
            rgm.setFname(studFname);
            rgm.setLname(studLname);
            rgm.setEmailid(emailid);
            rgm.setPassword(passstr);
            rgm.setUserId(userIdstr);
            rgm.setContactNo(phoneno);
            rgm.setGender(gender);
            rgm.setMagicWord(magicWordstr);
            rgm.setThumbnailUrl(imagebase64);
            //   Boolean connect=;
            if (Config.isConnectingToInternet(RegistrationActivity.this)) {
                loading.setVisibility(View.VISIBLE);
                btnregistration.setEnabled(false);
                RegistrationAsyncTaskPost asyncTaskPost = new RegistrationAsyncTaskPost(rgm, RegistrationActivity.this, RegistrationActivity.this);
                asyncTaskPost.execute();
            }
            else
            {
                Config.alertDialog(RegistrationActivity.this,"Network Error","Please check your internet connection");
                // Toast.makeText(Registration2Activity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
            /*Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(i);*/
        }
        else
        {
            Config.alertDialog(RegistrationActivity.this,"Alert","Password mismatch");
            //Toast.makeText(Registration2Activity.this, "Password not match..!", Toast.LENGTH_SHORT).show();
        }


    }
    public void getOption() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.putExtra("crop", "true");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Choose Action");

        Intent[] intentArray = {cameraIntent};

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooser, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    public Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        //External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            File sdcard = Environment.getExternalStorageDirectory() ;

            File folder = new File(sdcard.getAbsoluteFile(), Config.IMAGE_DIRECTORY_NAME);//the dot makes this directory hidden to the user
            folder.mkdir();
            /*if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create " + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }*/
        }

        // Create search_layout media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (data == null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    // down sizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
                    Log.d("PATH", fileUri.getPath());
                    setPhoto(bitmap);
                    userimg.setImageBitmap(bitmap);
                    String path = encodephoto(bitmap);
                    imagebase64=path;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ProfilePicPath", path);
                    editor.commit();
                    launchUploadActivity(data);
                } else
                    launchUploadActivity(data);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
    private void launchUploadActivity(Intent data) {

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userid = sharedpreferences.getString("UidName", "");
        if (data.getData() != null) {
            try {
                if (bitmap != null) {
                    //bitmap.recycle();
                }

                InputStream stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                localPath = ImageStorage.saveEventToSdCard(bitmap, "P2PDP", RegistrationActivity.this);
                userimg.setImageBitmap(bitmap);
                String path = encodephoto(bitmap);
                imagebase64=path;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
                /*DALMyPupilInfo dp=new DALMyPupilInfo(RegistrationActivity.this);
                String[] student=dp.GetAllTableData(preferences.getString("StudentUserID",""));*/
                long n=2;
               /* n=dp.updateStudentInfo(student[15], student[16], student[3], student[4], student[17], student[5], student[0], student[1], student[2], student[6], student[8], student[9], student[18], student[10],
                        student[19], student[11], student[7], student[20], student[21], student[22],student[23],student[13],student[12],localPath,student[24]);*/
                if (n>0)
                {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                    //DatabaseQueries qr =new DatabaseQueries(RegistrationActivity.this);
                    /*n = qr.insertQueue(1010, "ProfilePic", "1", df1.format(calendar.getTime()));
                    if (n>0)
                    {
                        SharedPreferences.Editor edit = sharedpreferences.edit();
                        edit.putString("NewThumbnailID", localPath);
                        edit.putString("ProfilePicPath", path);
                        edit.commit();
                    }*/
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = (Bitmap) data.getExtras().get("data");
            localPath = ImageStorage.saveEventToSdCard(bitmap, "P2PDP",RegistrationActivity.this);
            userimg.setImageBitmap(bitmap);
            String path = encodephoto(bitmap);
            imagebase64=path;
        }
    }

    //Encode image to Base64 to send to server
    private void setPhoto(Bitmap bitmapm) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");

            }
        }
        else {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            //4
            File file = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpeg");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                //5
                fo.write(bytes.toByteArray());
                fo.close();
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file)));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }
    //Encode image to Base64 to send to server
    private String encodephoto(Bitmap bitmapm) {
        String imagebase64string = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] byteArrayImage = baos.toByteArray();
            imagebase64string = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagebase64string;
    }
    public void GetAllData()
    {
        passstr=pass.getText().toString();
        conpass=confirmPass.getText().toString();
        studFname=fName.getText().toString();
        studLname=lName.getText().toString();
        emailid=eMailid.getText().toString();
        phoneno=phoneNo.getText().toString();
        userIdstr=userId.getText().toString();
        magicWordstr=magicWord.getText().toString();
        if (rdomale.isChecked()==true)
        {
            gender=rdomale.getText().toString();
        }
        else if (rdofemale.isChecked()==true)
        {
            gender=rdofemale.getText().toString();
        }
        else {
            gender = "";
        }

    }
    public void upDateLable()
    {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtRegDob.setText(sdf.format(myCalendar.getTime()));
        dob=txtRegDob.getText().toString();
    }
    public void setProfilePicDialog()
    {
        final Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/font.ttf");
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.set_registration_profilepic_, null);
        Button set = (Button)dialoglayout.findViewById(R.id.btn_set);
        Button skip = (Button)dialoglayout.findViewById(R.id.btn_skip);
        EditText edtUserid= (EditText) dialoglayout.findViewById(R.id.edt_profpic_userid);
        edtUserid.setText(userIdstr.toString());
        edtUserid.setEnabled(false);
        userimg= (ImageView) dialoglayout.findViewById(R.id.reg_user_image);
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOption();
            }
        });
        set.setTypeface(face);
        skip.setTypeface(face);
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setView(dialoglayout);

        final AlertDialog alertDialog = builder.create();
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(RegistrationActivity.this, "Profile pic Set Successfully", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(i);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        alertDialog.show();
    }

    @Override
    public void onTaskCompleted(String s) {
        loading.setVisibility(View.GONE);
        /*if (s.equals("true"))
        {
            finish();
            Config.alertDialog(this,"Success","Successfully Registered");
            Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(i);
        }
        else if (s.equals("400"))
        {
            Config.alertDialog(this,"Error","User Id Already Exist");
        }
        else
        {
            Config.alertDialog(this,"Error","Registration Failed");
        }*/
        String[] onTask=s.split("@@@");
        if (onTask[1].equals("Register"))
        {
            if (onTask[0].equals("true"))
            {
               /* Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);*/
                finish();
               /* Intent i=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(i);*/
            }
            else
            {
                Config.alertDialog(this,"Error","Registration Failed");
            }
        }
        else if (onTask[1].equals("SameUserRegister"))
        {
            JSONObject obj = null;
            try {
                obj = new JSONObject(onTask[0]);
                String message=obj.getString("Message");
                Config.alertDialog(RegistrationActivity.this, "Error", message);
                btnregistration.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", e.toString());
                Log.e("Login.JLocalizedMessage", e.getLocalizedMessage());
                Log.e("Login(JStackTrace)", e.getStackTrace().toString());
                Log.e("Login(JCause)", e.getCause().toString());
                Log.wtf("Login(JMsg)", e.getMessage());
            }
        }
        else
        {
            //Config.alertDialog(this,"Error","Registration Failed");
        }

    }
}

