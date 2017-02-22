package realizer.com.mysurvey;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.Locale;

import realizer.com.mysurvey.utils.Config;
import realizer.com.mysurvey.utils.FontManager;
import realizer.com.mysurvey.utils.GetImages;
import realizer.com.mysurvey.utils.ImageStorage;
import realizer.com.mysurvey.utils.OnTaskCompleted;
import realizer.com.mysurvey.utils.Utility;
import realizer.com.mysurvey.views.FullImageViewActivity;
import realizer.com.mysurvey.views.ProgressWheel;

/**
 * Created by Win on 23/12/2016.
 */
public class UserProfileActivity extends AppCompatActivity implements OnTaskCompleted
{
    ProgressWheel loading;

    TextView email,dob,phone,userid,txtEmail,txtDob,txtPhone,txtUserid,userfullname,NewProfilePic,userInitial;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView userimg;
    Bitmap bitmap;
    String thumbnailUrl;
    String UserID;
    String localPath="";
    String image64bit="";
    private Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_activity);
        email= (TextView) findViewById(R.id.user_prof_email_ico);
        dob= (TextView) findViewById(R.id.user_prof_dob_ico);
        phone= (TextView) findViewById(R.id.user_prof_phone_ico);
        userid= (TextView) findViewById(R.id.user_prof_userid_ico);
        userfullname= (TextView) findViewById(R.id.setting_user_fullname);
        userimg= (ImageView) findViewById(R.id.setting_user_icon);
        txtEmail= (TextView) findViewById(R.id.setting_user_emailId);
        txtDob= (TextView) findViewById(R.id.setting_user_dob);
        txtPhone= (TextView) findViewById(R.id.setting_user_phoneno);
        txtUserid= (TextView) findViewById(R.id.setting_user_userid);
        loading = (ProgressWheel) findViewById(R.id.loading);
        NewProfilePic= (TextView) findViewById(R.id.setting_new_profile);
        userInitial= (TextView) findViewById(R.id.setting_user_initial);

        NewProfilePic.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));

        Typeface textface= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/BRLNSR.TTF");
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        email.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));
        dob.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));
        phone.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));
        userid.setTypeface(FontManager.getTypeface(this,FontManager.FONTAWESOME));
        userInitial.setText(sharedpreferences.getString("userInitial", "").toUpperCase());

        txtEmail.setTypeface(textface);
        txtDob.setTypeface(textface);
        txtUserid.setTypeface(textface);
        txtPhone.setTypeface(textface);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnailUrl=sharedpreferences.getString("userThumbnailURL","");
        SetThumbnail(thumbnailUrl);
        UserID=sharedpreferences.getString("userId", "");
        userfullname.setText(sharedpreferences.getString("userFullName", ""));
        txtEmail.setText(sharedpreferences.getString("userEmail",""));
        txtDob.setText(DateFormat(sharedpreferences.getString("userDOB", "")));
        txtUserid.setText(sharedpreferences.getString("userLoginId", ""));
        txtPhone.setText(sharedpreferences.getString("userContact",""));

        NewProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOption();
            }
        });
        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b1=new Bundle();
                b1.putString("ImgURLUSER",thumbnailUrl);
                Intent i=new Intent(UserProfileActivity.this, FullImageViewActivity.class);
                i.putExtras(b1);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_info:
                Intent i=new Intent(UserProfileActivity.this,AboutMySurveyActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public String DateFormat(String dateinput)
    {
        String[] setnttime=dateinput.split("T");
        String[] date=setnttime[0].split("-");
        String month= Config.getMonth(Integer.valueOf(date[1]));
        String newdate=date[2]+"/"+ month+"/"+date[0];
        return newdate;
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
    private static File getOutputMediaFile(int type) {

        //External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            File sdcard = Environment.getExternalStorageDirectory() ;

            File folder = new File(sdcard.getAbsoluteFile(), Config.IMAGE_DIRECTORY_NAME);//the dot makes this directory hidden to the user
            folder.mkdir();

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
                    image64bit=path;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ProfilePicPath", path);
                    editor.commit();
                    launchUploadActivity(data);
                } else
                {launchUploadActivity(data);}

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

    //Encode image to Base64 to send to server
    private void setPhoto(Bitmap bitmapm) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                /*Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");*/

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
                /*localPath = ImageStorage.saveEventToSdCard(bitmap, "userImages", UserProfileActivity.this);*/
                userimg.setImageBitmap(bitmap);
                String path = encodephoto(bitmap);
                image64bit=path;


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bitmap = (Bitmap) data.getExtras().get("data");
            localPath = ImageStorage.saveEventToSdCard(bitmap, "userImages", UserProfileActivity.this);
            userimg.setImageBitmap(bitmap);
            String path = encodephoto(bitmap);
            image64bit=path;
        }
        UploadThumbnail();
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

    public void UploadThumbnail()
    {
        if (Config.isConnectingToInternet(UserProfileActivity.this)) {
            loading.setVisibility(View.VISIBLE);
            userimg.setEnabled(false);
            NewProfilePic.setEnabled(false);
            UserProfileAsyncTaskPost thumbnailPut = new UserProfileAsyncTaskPost(UserID, image64bit, UserProfileActivity.this, UserProfileActivity.this);
            thumbnailPut.execute();
        }
        else {
            Config.alertDialog(UserProfileActivity.this,"Network Error","No Internet Connection");
        }
    }

    @Override
    public void onTaskCompleted(String s)
    {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loading.setVisibility(View.GONE);
        userimg.setEnabled(true);
        NewProfilePic.setEnabled(true);
        String[] onTask=s.split("@@@");
        if (onTask[1].equals("ProfilePic"))
        {
            JSONObject rootObj = null;
            Log.d("String", onTask[0]);
            try {

                rootObj = new JSONObject(onTask[0]);
                String success=rootObj.getString("Success");
                String Url = rootObj.getString("thumbnilUrl");
                if (success.equals("true"))
                {
                    SharedPreferences.Editor edit = sharedpreferences.edit();
                    edit.putString("userThumbnailURL", Url);
                    edit.commit();
                    thumbnailUrl=Url;
                    Config.alertDialog(UserProfileActivity.this,"Success","Profile pic successfully updated.");
                }

            } catch (JSONException e) {
                e.printStackTrace();

                Log.e("JSON", e.toString());
                Log.e("Login.JLocalizedMessage", e.getLocalizedMessage());
                Log.e("Login(JStackTrace)", e.getStackTrace().toString());
                Log.e("Login(JCause)", e.getCause().toString());
                Log.wtf("Login(JMsg)", e.getMessage());
            }
            SetThumbnail(thumbnailUrl);
        }
    }
    public void SetThumbnail(String ThumbnailUrl)
    {
        if (ThumbnailUrl.equals("")||ThumbnailUrl.equals("null")||ThumbnailUrl.equals(null))
        {
            userInitial.setVisibility(View.VISIBLE);
            userimg.setVisibility(View.GONE);
        }
        else{
            userInitial.setVisibility(View.GONE);
            userimg.setVisibility(View.VISIBLE);

            String newURL= Utility.getURLImage(ThumbnailUrl);
            if(!ImageStorage.checkifImageExists(newURL.split("/")[newURL.split("/").length - 1]))
                new GetImages(newURL,userimg,newURL.split("/")[newURL.split("/").length-1]).execute(newURL);
            else
            {
                File image = ImageStorage.getImage(newURL.split("/")[newURL.split("/").length-1]);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                //  bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                userimg.setImageBitmap(bitmap);
            }
        }
    }
}
