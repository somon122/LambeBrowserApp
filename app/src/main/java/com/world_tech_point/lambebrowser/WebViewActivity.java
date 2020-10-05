package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.lambebrowser.Database.DB_Manager;
import com.world_tech_point.lambebrowser.Database.LinkClass;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;

import static android.webkit.WebView.HitTestResult.IMAGE_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_ANCHOR_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;


public class WebViewActivity extends AppCompatActivity {


    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_FINE_LOCATION = 4;
    private ValueCallback<Uri[]> mUploadMessage;
    private String mCameraPhotoPath = null;
    private long size = 0;


    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String filePath;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mUploadMessage == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        try {
            String file_path = mCameraPhotoPath.replace("file:", "");
            File file = new File(file_path);
            size = file.length();

        } catch (Exception e) {
            Log.e("Error!", "Error while opening image file" + e.getLocalizedMessage());
        }

        if (data != null || mCameraPhotoPath != null) {
            Integer count = 0; //fix fby https://github.com/nnian
            ClipData images = null;
            try {
                images = data.getClipData();
            } catch (Exception e) {
                Log.e("Error!", e.getLocalizedMessage());
            }

            if (images == null && data != null && data.getDataString() != null) {
                count = data.getDataString().length();
            } else if (images != null) {
                count = images.getItemCount();
            }
            Uri[] results = new Uri[count];
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (size != 0) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else if (data.getClipData() == null) {
                    results = new Uri[]{Uri.parse(data.getDataString())};
                } else {

                    for (int i = 0; i < images.getItemCount(); i++) {
                        results[i] = images.getItemAt(i).getUri();
                    }
                }
            }

            mUploadMessage.onReceiveValue(results);
            mUploadMessage = null;
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED || cameraPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                boolean allow = false;
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed this permission
                    allow = true;

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("url", target_url);
                    startActivity(intent);

                }
                if (mGeolocationCallback != null) {
                    // call back to web chrome client
                    mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
                }
                break;
        }
    }

    String mGeolocationOrigin;
    GeolocationPermissions.Callback mGeolocationCallback;

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    String appname;

    LinearLayout backButton, forwardButton, homeButton, copyButton, shareButton, webBottomNavigationView, wPopUpMenu;

    LinearLayout history, exits, wallet, file, videoList, mp3List;
    ImageView web_cancelPopup;
    TextView webToolBar;
    BottomSheetDialog bottomSheetDialog;

    EditText webViewUrlEditText;
    LinearLayout webViewSearchUrl;
    FloatingActionButton favDownload;

    ProgressDialog pDialog;
    ImageView imageIcon;
    String mURl;
    String mID;
    String target_url;
    String youTubeLink;
    int bCount;

    String shareUrl;
    String shareTitle;

    DB_Manager db_manager;
    LinkClass linkClass;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        backButton = findViewById(R.id.wBackButton_id);
        forwardButton = findViewById(R.id.wForwardButton_id);
        homeButton = findViewById(R.id.wHomeButton_id);
        copyButton = findViewById(R.id.wCopyUrl_id);
        shareButton = findViewById(R.id.wShareUrlButton_id);
        wPopUpMenu = findViewById(R.id.wPopUpMenu_id);
        webBottomNavigationView = findViewById(R.id.webBottomNavigationView);

        progressBar = findViewById(R.id.progressBar_id);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        webView = findViewById(R.id.webView_id);
        appname = (String) getTitle();
        webViewSearchUrl = findViewById(R.id.webViewSearchUrl_id);
        webViewUrlEditText = findViewById(R.id.webViewUrlEditText_id);
        imageIcon = findViewById(R.id.imageIcon);
        favDownload = findViewById(R.id.favDownload_id);
        favDownload.setVisibility(View.GONE);
        db_manager = new DB_Manager(WebViewActivity.this);
        linkClass = new LinkClass();
        pDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            target_url = bundle.getString("url");
        }

        favDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                youtubeDownloadAlert(youTubeLink);

            }
        });


        verifyStoragePermissions(this);
        webViewSearchUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = webViewUrlEditText.getText().toString().trim();
                if (url.isEmpty()) {
                    webViewUrlEditText.setError("Enter valid address");
                } else {
                    String lastUrl = url;
                    Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", lastUrl);
                    startActivity(intent);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(this, "FBDownloader");
        webView.setClickable(true);


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                final WebView webview = (WebView) view;
                final WebView.HitTestResult result = webview.getHitTestResult();

                if (result.getType() == SRC_ANCHOR_TYPE) {
                    Toast.makeText(WebViewActivity.this, "SRC_ANCHOR_TYPE  "+result.getExtra(), Toast.LENGTH_SHORT).show();

                    return true;
                }

                if (result.getType() == SRC_IMAGE_ANCHOR_TYPE) {

                    Toast.makeText(WebViewActivity.this, "SRC_IMAGE_ANCHOR_TYPE "+result.getExtra(), Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (result.getType() == IMAGE_TYPE) {

                    Toast.makeText(WebViewActivity.this, "IMAGE_TYPE "+result.getExtra(), Toast.LENGTH_SHORT).show();
                    return true;
                }


                return false;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                String cookie = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookie);
                request.addRequestHeader("user_agent", userAgent);
                request.setDescription("Download loading....");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(WebViewActivity.this, "Download File", Toast.LENGTH_SHORT).show();


            }
        });

        wPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(WebViewActivity.this,
                        R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.web_menu_popup,
                        (LinearLayout) findViewById(R.id.categoryPopUp_id));


                history = bottomSheetView.findViewById(R.id.web_History_id);
                exits = bottomSheetView.findViewById(R.id.web_Exits_id);
                wallet = bottomSheetView.findViewById(R.id.web_Wallet_id);
                file = bottomSheetView.findViewById(R.id.web_download_id);
                videoList = bottomSheetView.findViewById(R.id.web_Video_id);
                mp3List = bottomSheetView.findViewById(R.id.web_Mp3Song_id);
                web_cancelPopup = bottomSheetView.findViewById(R.id.web_cancelPopup);
                webToolBar = bottomSheetView.findViewById(R.id.webToolBar_id);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    webToolBar.setText(user.getDisplayName());
                }
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    if (bCount > 0) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        bCount = bCount + 1;
                    }

                }

            }
        });
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (webView.canGoForward()) {
                    webView.goForward();
                }


            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shareUrl != null) {

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Link", shareUrl);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(WebViewActivity.this, "Copied!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(WebViewActivity.this, "Text Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Site link: " + shareUrl;
                String shareSub = "Important Link";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, shareTitle));

            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                progressBar.setVisibility(View.GONE);
                webViewUrlEditText.setText(url);
                Glide.with(WebViewActivity.this).asBitmap().load(favicon).placeholder(R.drawable.world).into(imageIcon);
                shareUrl = url;
                shareTitle = view.getTitle();

                if (haveNetwork()) {
                    String logo = String.valueOf(favicon);
                    linkClass = new LinkClass(view.getTitle(), url, logo);
                    boolean insert = db_manager.Save_All_Data(linkClass);
                    if (insert) {
                        String check = db_manager.getData(url);
                        if (check == null) {

                            //Toast.makeText(WebViewActivity.this, "" + check, Toast.LENGTH_SHORT).show();
                            SuggestionClass suggestionClass = new SuggestionClass(WebViewActivity.this);
                            suggestionClass.insertSuggestion(url);

                        }
                    }
                    if (url.contains("youtube")) {

                        webBottomNavigationView.setVisibility(View.GONE);

                        try {
                            YouTubeUriExtractor uriExtractor = new YouTubeUriExtractor(WebViewActivity.this) {
                                @Override
                                public void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles) {

                                    try {

                                        if (ytFiles != null) {
                                            int tag = 22;
                                            youTubeLink = ytFiles.get(tag).getUrl();
                                            if (youTubeLink != null) {
                                                favDownload.setVisibility(View.VISIBLE);
                                            }

                                        } else {

                                        }

                                    } catch (Exception e) {

                                    }
                                }
                            };

                            uriExtractor.execute(url);

                        } catch (Exception e) {

                        }
                    }

                } else {

                    Toast.makeText(WebViewActivity.this, "Please connect your net", Toast.LENGTH_SHORT).show();
                }


                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                WebViewActivity.this.webView.loadUrl("javascript:(function() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\");');}}})()");
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                WebViewActivity.this.webView.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;console.log(i);var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
                WebViewActivity.this.webView.loadUrl("javascript:( window.onload=prepareVideo;)()");
            }
        });
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();

        webView.loadUrl(target_url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);

                String perm = Manifest.permission.ACCESS_FINE_LOCATION;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                        ContextCompat.checkSelfPermission(WebViewActivity.this, perm) == PackageManager.PERMISSION_GRANTED) {
                    // we're on SDK < 23 OR user has already granted permission
                    callback.invoke(origin, true, false);
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(WebViewActivity.this, perm)) {
                        // ask the user for permission
                        ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{perm}, REQUEST_FINE_LOCATION);

                        // we will use these when user responds
                        mGeolocationOrigin = origin;
                        mGeolocationCallback = callback;
                    }
                }
            }

            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                // Double check that we don't have any existing callbacks
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                }
                mUploadMessage = filePath;
                Log.e("FileCooserParams => ", filePath.toString());

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[2];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(Intent.createChooser(chooserIntent, "Select images"), 1);
                return true;
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, android.os.Message resultMsg) {
                WebView.HitTestResult result = view.getHitTestResult();
                String data = result.getExtra();
                Context context = view.getContext();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                context.startActivity(browserIntent);
                return false;
            }

        });

       // registerForContextMenu(webView);

    }

    ////   onCreate    end  =============


  /*  @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Get the web view hit test result
        final WebView.HitTestResult result = webView.getHitTestResult();

        // If user long press on url
        if (result.getType() == WebView.HitTestResult.ANCHOR_TYPE ||
                result.getType() == SRC_ANCHOR_TYPE) {

            // Set the title for context menu
            menu.setHeaderTitle("Hello");
            Toast.makeText(this, ""+result.getExtra()+"This "+result.getType(), Toast.LENGTH_SHORT).show();
           *//* // Add an item to the menu
            menu.add(0, 1, 0, "Show URL");
            menu.add(0, 1, 0, "Show URL")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String Pressed_url = result.getExtra();
                            Toast.makeText(WebViewActivity.this, "URL is:-" + Pressed_url,
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });*//*
        }
    }
*/

    public void webPopUp(View view) {

        int id = view.getId();

        if (id == R.id.web_cancelPopup) {
            bottomSheetDialog.dismiss();
        }else if (id == R.id.web_Exits_id) {
            finishAffinity();

        } else if (id == R.id.web_Video_id) {

            startActivity(new Intent(getApplicationContext(), VideoShowActivity.class));
            finish();

        } else if (id == R.id.web_Mp3Song_id) {

            startActivity(new Intent(getApplicationContext(), MP3_PlayActivity.class));
            finish();

        } else if (id == R.id.web_History_id) {
            Toast.makeText(this, "web_History_id", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.web_Wallet_id) {
            startActivity(new Intent(getApplicationContext(), WalletActivity.class));
            finish();

        } else if (id == R.id.web_download_id) {
            Toast.makeText(this, "web_download_id", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            if (bCount > 0) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            } else {
                bCount = bCount + 1;
            }

        }

    }


    private boolean haveNetwork() {
        boolean have_WiFi = false;
        boolean have_Mobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfo) {

            if (info.getTypeName().equalsIgnoreCase("WIFI")) {
                if (info.isConnected()) {
                    have_WiFi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (info.isConnected()) {
                    have_Mobile = true;
                }
            }

        }
        return have_WiFi || have_Mobile;

    }


    public String BitMapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private void alertUser(final String url, final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);

        builder.setTitle("Alert");
        builder.setMessage("Choose any one");
        builder.setPositiveButton("Watch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                videoPlay(url);

            }
        }).setNeutralButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                downloadVideo(url, id);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void videoPlay(String url) {

        Intent intent = new Intent(WebViewActivity.this, VideoPlayActivity.class);
        intent.putExtra("video_url", url);
        startActivity(intent);
    }


    private void youtubeDownloadAlert(final String url) {

        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);

        builder.setTitle("Alert");
        builder.setMessage("Are you sure Download?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                youtubeDownload(url);


            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                favDownload.setVisibility(View.GONE);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void downloadVideo(String url, String id) {


        try {
            String mBaseFolderPath = android.os.Environment
                    .getExternalStorageDirectory()
                    + File.separator
                    + "FacebookVideos" + File.separator;
            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }
            String mFilePath = "file://" + mBaseFolderPath + "/" + id + ".mp4";

            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request req = new DownloadManager.Request(downloadUri);
            req.setDestinationUri(Uri.parse(mFilePath));
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            DownloadManager dm = (DownloadManager) getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
            dm.enqueue(req);
            Toast.makeText(WebViewActivity.this, "Download Started", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(WebViewActivity.this, "Download Failed: " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    @JavascriptInterface
    public void processVideo(final String vidData, final String vidID) {

        mURl = vidData;
        mID = vidID;

        alertUser(mURl, mID);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }


    private void youtubeDownload(String url) {

        favDownload.setVisibility(View.GONE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        Toast.makeText(WebViewActivity.this, "" + url, Toast.LENGTH_SHORT).show();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "hello" + ".mp4");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);


    }


}

