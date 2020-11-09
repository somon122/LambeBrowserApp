package com.world_tech_point.lambebrowser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.world_tech_point.lambebrowser.Database.DB_Manager;
import com.world_tech_point.lambebrowser.Database.LinkClass;
import com.world_tech_point.lambebrowser.mp3Folder.MP3_PlayActivity;
import com.world_tech_point.lambebrowser.serviceFragment.DownloadMsgShow;
import com.world_tech_point.lambebrowser.videoShowFolder.VideoShowActivity;
import com.world_tech_point.lambebrowser.wallet.WalletActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.huber.youtubeExtractor.YouTubeUriExtractor;
import at.huber.youtubeExtractor.YtFile;
import de.mrapp.android.tabswitcher.AbstractState;
import de.mrapp.android.tabswitcher.AddTabButtonListener;
import de.mrapp.android.tabswitcher.Animation;
import de.mrapp.android.tabswitcher.Layout;
import de.mrapp.android.tabswitcher.PeekAnimation;
import de.mrapp.android.tabswitcher.PullDownGesture;
import de.mrapp.android.tabswitcher.RevealAnimation;
import de.mrapp.android.tabswitcher.StatefulTabSwitcherDecorator;
import de.mrapp.android.tabswitcher.SwipeGesture;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.TabPreviewListener;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.TabSwitcherListener;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.util.multithreading.AbstractDataBinder;
import de.mrapp.util.Condition;

import static android.webkit.WebView.HitTestResult.IMAGE_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_ANCHOR_TYPE;
import static android.webkit.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE;
import static de.mrapp.android.util.DisplayUtil.getDisplayWidth;


public class WebViewActivity extends AppCompatActivity implements TabSwitcherListener {

    /**
     * The state of tabs, which display list items in a list view.
     */
    private class State extends AbstractState
            implements AbstractDataBinder.Listener<ArrayAdapter<String>, Tab, ListView, Void>,
            TabPreviewListener {

        /**
         * The adapter, which contains the list items of the tab.
         */
        private ArrayAdapter<String> adapter;

        /**
         * Creates a new state of a tab, which displays list items in a list view.
         *
         * @param tab
         *         The tab, the state should correspond to, as an instance of the class {@link Tab}.
         *         The tab may not be null
         */
        State(@NonNull final Tab tab) {
            super(tab);
        }

        /**
         * Loads the list items of the tab.
         *
         * @param listView
         *         The list view, which should be used to display the list items, as an instance of
         *         the class {@link ListView}. The list view may not be null
         */
        public void loadItems(@NonNull final ListView listView) {
            Condition.INSTANCE.ensureNotNull(listView, "The list view may not be null");

            if (adapter == null) {
                dataBinder.addListener(this);
                dataBinder.load(getTab(), listView);
            } else if (listView.getAdapter() != adapter) {
                listView.setAdapter(adapter);
            }
        }

        @Override
        public boolean onLoadData(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
                @NonNull final Tab key, @NonNull final Void... params) {
            return true;
        }

        @Override
        public void onCanceled(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder) {

        }

        @Override
        public void onFinished(
                @NonNull final AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> dataBinder,
                @NonNull final Tab key, @Nullable final ArrayAdapter<String> data,
                @NonNull final ListView view, @NonNull final Void... params) {
            if (getTab().equals(key)) {
                view.setAdapter(data);
                adapter = data;
                dataBinder.removeListener(this);
            }
        }

        @Override
        public final void saveInstanceState(@NonNull final Bundle outState) {
            if (adapter != null && !adapter.isEmpty()) {
                String[] array = new String[adapter.getCount()];

                for (int i = 0; i < array.length; i++) {
                    array[i] = adapter.getItem(i);
                }

                outState.putStringArray(String.format(ADAPTER_STATE_EXTRA, getTab().getTitle()),
                        array);
            }
        }

        @Override
        public void restoreInstanceState(@Nullable final Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                String key = String.format(ADAPTER_STATE_EXTRA, getTab().getTitle());
                String[] items = savedInstanceState.getStringArray(key);

                if (items != null && items.length > 0) {
                    adapter = new ArrayAdapter<>(WebViewActivity.this,
                            android.R.layout.simple_list_item_1, items);
                }
            }
        }

        @Override
        public boolean onLoadTabPreview(@NonNull final TabSwitcher tabSwitcher,
                                        @NonNull final Tab tab) {
            return !getTab().equals(tab) || adapter != null;
        }

    }

    /**
     * The decorator, which is used to inflate and visualize the tabs of the activity's tab
     * switcher.
     */
    private class Decorator extends StatefulTabSwitcherDecorator<State> {


        @Nullable
        @Override
        protected State onCreateState(@NonNull final Context context,
                                      @NonNull final TabSwitcher tabSwitcher,
                                      @NonNull final View view, @NonNull final Tab tab,
                                      final int index, final int viewType,
                                      @Nullable final Bundle savedInstanceState) {
           /* if (viewType == 2) {
                State state = new State(tab);
                tabSwitcher.addTabPreviewListener(state);

                if (savedInstanceState != null) {
                    state.restoreInstanceState(savedInstanceState);
                }

                return state;
            }*/

            return null;
        }

        @Override
        protected void onClearState(@NonNull final State state) {
            tabSwitcher.removeTabPreviewListener(state);
        }

        @Override
        protected void onSaveInstanceState(@NonNull final View view, @NonNull final Tab tab,
                                           final int index, final int viewType,
                                           @Nullable final State state,
                                           @NonNull final Bundle outState) {
            if (state != null) {
                state.saveInstanceState(outState);
            }
        }

        @NonNull
        @Override
        public View onInflateView(@NonNull final LayoutInflater inflater,
                                  @Nullable final ViewGroup parent, final int viewType) {
            View view;

            view = inflater.inflate(R.layout.tab_text_view, parent, false);


          /*  if (viewType == 0) {
                view = inflater.inflate(R.layout.tab_text_view, parent, false);
            } else if (viewType == 1) {
                view = inflater.inflate(R.layout.tab_edit_text, parent, false);
            } else {
                view = inflater.inflate(R.layout.tab_list_view, parent, false);
            }*/

            Toolbar toolbar = view.findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.tab);
            toolbar.setOnMenuItemClickListener(createToolbarMenuListener());
            Menu menu = toolbar.getMenu();
            TabSwitcher.setupWithMenu(tabSwitcher, menu, createTabSwitcherButtonListener());
            return view;
        }


        @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
        @Override
        public void onShowTab(@NonNull final Context context,
                              @NonNull final TabSwitcher tabSwitcher, @NonNull final View view,
                              @NonNull final Tab tab, final int index, final int viewType,
                              @Nullable final State state,
                              @Nullable final Bundle savedInstanceState) {
            final TextView title = findViewById(android.R.id.title);
            final Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setVisibility(tabSwitcher.isSwitcherShown() ? View.GONE : View.VISIBLE);

            final CardView cardView;
            final LinearLayout defaultFrame;
            final List<String> sList;

            cardView = findViewById(R.id.searchViewItem);
            defaultFrame = findViewById(R.id.defaultFrame);

            progressBar = findViewById(R.id.progressBar_id);
            webView = findViewById(R.id.webView_id);
            webViewSearchUrl = findViewById(R.id.webViewSearchUrl_id);
            webViewUrlEditText = findViewById(R.id.webViewUrlEditText_id);
            imageIcon = findViewById(R.id.imageIcon);
            imageIcon2 = findViewById(R.id.imageIcon2);

            SuggestionClass suggestionClass = new SuggestionClass(context);
            sList = suggestionClass.suggestionList();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,sList);
            webViewUrlEditText.setAdapter(arrayAdapter);

                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cardView.setVisibility(View.VISIBLE);
                        defaultFrame.setVisibility(View.GONE);
                    }
                });

                webViewUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {

                        cardView.setVisibility(View.VISIBLE);
                        defaultFrame.setVisibility(View.GONE);

                    }
                });
            webViewSearchUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = webViewUrlEditText.getText().toString().trim();
                    if (url.isEmpty()) {
                        webViewUrlEditText.setError("Enter valid address");
                    } else {
                        if (url.contains("https://www.")){

                            webView.loadUrl(url);

                        }else if (url.contains("www")){

                            String lastUrl = "https://"+url;
                            webView.loadUrl(lastUrl);


                        }else if (url.contains("https")){

                            webView.loadUrl(url);

                        }else {
                            String lastUrl = "https://www.google.com/search?q="+url;
                            webView.loadUrl(lastUrl);
                        }
                    }
                }
            });


            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {


                    final WebView webview = (WebView) view;
                    final WebView.HitTestResult result = webview.getHitTestResult();

                    if (result.getType() == SRC_ANCHOR_TYPE) {

                        if (result.getExtra().contains(".mp4")) {

                            showOptionMethod(result.getExtra(), "mp4");
                        } else if (result.getExtra().contains(".3gp")){

                            showOptionMethod(result.getExtra(), "3gp");

                        }else if (result.getExtra().contains(".WMV")){
                            showOptionMethod(result.getExtra(), "WMV");

                        }else if (result.getExtra().contains(".AVI")){
                            showOptionMethod(result.getExtra(), "AVI");

                        }else {
                            showOptionMethod(result.getExtra(),"mp4");
                        }
                        return true;
                    }

                    if (result.getType() == SRC_IMAGE_ANCHOR_TYPE) {

                        if (result.getExtra().contains(".png")) {

                            showOptionMethod(result.getExtra(), "png");
                        } else if (result.getExtra().contains(".JPG")){

                            showOptionMethod(result.getExtra(), "jpg");
                        }else if (result.getExtra().contains(".jpeg")){
                            showOptionMethod(result.getExtra(), "jpeg");

                        }else if (result.getExtra().contains(".webp")){
                            showOptionMethod(result.getExtra(), "webp");

                        }else {
                            showOptionMethod(result.getExtra(),"jpg");
                        }
                        return true;
                    }

                    if (result.getType() == IMAGE_TYPE) {
                        showOptionMethod(result.getExtra(),"jpg");
                        return true;
                    }
                    return false;
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

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    progressBar.setVisibility(View.GONE);
                    cardView.setVisibility(View.GONE);
                    defaultFrame.setVisibility(View.VISIBLE);
                    webViewUrlEditText.setText(url);
                    Glide.with(WebViewActivity.this).asBitmap().load(favicon).placeholder(R.drawable.world).into(imageIcon);
                    Glide.with(WebViewActivity.this).asBitmap().load(favicon).placeholder(R.drawable.world).into(imageIcon2);
                    shareUrl = url;
                    shareTitle = view.getTitle();
                    title.setText(shareTitle);

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
                        if (url.contains("YouTube")) {

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
                                                }else {
                                                    Toast.makeText(context, "No use", Toast.LENGTH_SHORT).show();
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



            CookieSyncManager.createInstance(WebViewActivity.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            CookieSyncManager.getInstance().startSync();

            if (loadURL == 1){
                webView.loadUrl(target_url);
                loadURL=0;
            }else {
                progressBar.setVisibility(View.GONE);
            }

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




           /* if (viewType == 1) {
                EditText editText = findViewById(android.R.id.edit);
                Button button = findViewById(R.id.bbb);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Hello user", Toast.LENGTH_SHORT).show();
                    }
                });

                if (savedInstanceState == null) {
                    editText.setText(null);
                }

                editText.requestFocus();
            } else if (viewType == 2 && state != null) {
                ListView listView = findViewById(android.R.id.list);
                state.loadItems(listView);
            }*/
        }

        @JavascriptInterface
        public void processVideo(final String vidData, final String vidID) {

            mURl = vidData;
            mID = vidID;

            alertUser(mURl, mID);

        }

        private void showOptionMethod(String url, String ext) {

            builder = new AlertDialog.Builder(WebViewActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.option_model, null);
            builder.setView(view1);

            newTabURL = url;
            extension = ext;

            newTabModel = view1.findViewById(R.id.newTab_Model);
            copyLinkModel = view1.findViewById(R.id.copyLink_model);
            downloadModel = view1.findViewById(R.id.download_model);
            shareModel = view1.findViewById(R.id.share_model);

            dialog = builder.create();
            dialog.show();


        }




        /*  @Override
          public int getViewTypeCount() {
              return 3;
          }
  */
        @Override
        public int getViewType(@NonNull final Tab tab, final int index) {
            Bundle parameters = tab.getParameters();
            return parameters != null ? parameters.getInt(VIEW_TYPE_EXTRA) : 0;
        }

    }

    /**
     * A data binder, which is used to asynchronously load the list items, which are displayed by a
     * tab.
     */
    private static class DataBinder
            extends AbstractDataBinder<ArrayAdapter<String>, Tab, ListView, Void> {

        /**
         * Creates a new data binder, which is used to asynchronously load the list items, which are
         * displayed by a tab.
         *
         * @param context
         *         The context, which should be used by the data binder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public DataBinder(@NonNull final Context context) {
            super(context.getApplicationContext());
        }

        @Nullable
        @Override
        protected ArrayAdapter<String> doInBackground(@NonNull final Tab key,
                                                      @NonNull final Void... params) {
            String[] array = new String[10];

            for (int i = 0; i < array.length; i++) {
                array[i] = String.format(Locale.getDefault(), "%s, item %d", key.getTitle(), i + 1);
            }

            try {
                // Simulate a long loading time...
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // There's nothing we can do...
            }

            return new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array);
        }

        @Override
        protected void onPostExecute(@NonNull final ListView view,
                                     @Nullable final ArrayAdapter<String> data, final long duration,
                                     @NonNull final Void... params) {
            if (data != null) {
                view.setAdapter(data);
            }
        }

    }

    /**
     * The name of the extra, which is used to store the view type of a tab within a bundle.
     */
    private static final String VIEW_TYPE_EXTRA = WebViewActivity.class.getName() + "::ViewType";

    /**
     * The name of the extra, which is used to store the state of a list adapter within a bundle.
     */
    private static final String ADAPTER_STATE_EXTRA = State.class.getName() + "::%s::AdapterState";

    /**
     * The number of tabs, which are contained by the example app's tab switcher.
     */
    private static final int TAB_COUNT = 1;

    /**
     * The activity's tab switcher.
     */
    private TabSwitcher tabSwitcher;

    /**
     * The decorator of the activity's tab switcher.
     */
    private Decorator decorator;

    /**
     * The activity's snackbar.
     */
    private Snackbar snackbar;

    /**
     * The data binder, which is used to load the list items of tabs.
     */
    private DataBinder dataBinder;

    /**
     * Creates a listener, which allows to apply the window insets to the tab switcher's padding.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnApplyWindowInsetsListener}. The listener may not be nullFG
     */
    @NonNull
    private OnApplyWindowInsetsListener createWindowInsetsListener() {
        return new OnApplyWindowInsetsListener() {

            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View v,
                                                          final WindowInsetsCompat insets) {
                int left = insets.getSystemWindowInsetLeft();
                int top = insets.getSystemWindowInsetTop();
                int right = insets.getSystemWindowInsetRight();
                int bottom = insets.getSystemWindowInsetBottom();
                tabSwitcher.setPadding(left, top, right, bottom);
                float touchableAreaTop = top;

                if (tabSwitcher.getLayout() == Layout.TABLET) {
                    touchableAreaTop += getResources()
                            .getDimensionPixelSize(R.dimen.tablet_tab_container_height);
                }

                RectF touchableArea = new RectF(left, touchableAreaTop,
                        getDisplayWidth(WebViewActivity.this) - right, touchableAreaTop +
                        ThemeUtil.getDimensionPixelSize(WebViewActivity.this, R.attr.actionBarSize));
                tabSwitcher.addDragGesture(
                        new SwipeGesture.Builder().setTouchableArea(touchableArea).create());
                tabSwitcher.addDragGesture(
                        new PullDownGesture.Builder().setTouchableArea(touchableArea).create());
                return insets;
            }

        };
    }


    @NonNull
    private View.OnClickListener createAddTabListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                int index = tabSwitcher.getCount();
                Animation animation = createRevealAnimation();
                tabSwitcher.addTab(createTab(index), 0, animation);
                Toast.makeText(WebViewActivity.this, "New", Toast.LENGTH_SHORT).show();

            }

        };
    }


    @NonNull
    private Toolbar.OnMenuItemClickListener createToolbarMenuListener() {
        return new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.remove_tab_menu_item:
                        Tab selectedTab = tabSwitcher.getSelectedTab();
                        if (selectedTab != null) {
                            tabSwitcher.removeTab(selectedTab);
                        }

                        return true;
                    case R.id.add_tab_menu_item:

                        int index = tabSwitcher.getCount();
                        Animation animation = createRevealAnimation();
                        tabSwitcher.addTab(createTab(index), 0, animation);

                        return true;
                    case R.id.clear_tabs_menu_item:
                        tabSwitcher.clear();
                        Intent intent2 = new Intent(WebViewActivity.this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                        return true;
                    case R.id.settings_menu_item:
                        Intent intent = new Intent(WebViewActivity.this, SettingActivity.class);
                        startActivity(intent);
                    default:
                        return false;
                }
            }

        };
    }

    /**
     * Inflates the tab switcher's menu, depending on whether it is empty, or not.
     */
    private void inflateMenu() {
        tabSwitcher
                .inflateToolbarMenu(tabSwitcher.getCount() > 0 ? R.menu.tab_switcher : R.menu.tab,
                        createToolbarMenuListener());
    }


    @NonNull
    private View.OnClickListener createTabSwitcherButtonListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                tabSwitcher.toggleSwitcherVisibility();
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to add a new tab to the tab switcher, when the
     * corresponding button is clicked.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * AddTabButtonListener}. The listener may not be null
     */
    @NonNull
    private AddTabButtonListener createAddTabButtonListener() {
        return new AddTabButtonListener() {

            @Override
            public void onAddTab(@NonNull final TabSwitcher tabSwitcher) {
                int index = tabSwitcher.getCount();
                Tab tab = createTab(index);
                tabSwitcher.addTab(tab, 0);

            }

        };
    }


    @NonNull
    private View.OnClickListener createUndoSnackbarListener(@NonNull final Snackbar snackbar,
                                                            final int index,
                                                            @NonNull final Tab... tabs) {
        return new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                snackbar.setAction(null, null);

                if (tabSwitcher.isSwitcherShown()) {
                    tabSwitcher.addAllTabs(tabs, index);
                } else if (tabs.length == 1) {
                    tabSwitcher.addTab(tabs[0], 0, createPeekAnimation());
                }

            }

        };
    }

    /**
     * Creates and returns a callback, which allows to observe, when a snackbar, which allows to
     * undo the removal of tabs, has been dismissed.
     *
     * @param tabs
     *         An array, which contains the tabs, which have been removed, as an array of the type
     *         {@link Tab}. The tab may not be null
     * @return The callback, which has been created, as an instance of the type class {@link
     * BaseTransientBottomBar.BaseCallback}. The callback may not be null
     */
    @NonNull
    private BaseTransientBottomBar.BaseCallback<Snackbar> createUndoSnackbarCallback(
            final Tab... tabs) {
        return new BaseTransientBottomBar.BaseCallback<Snackbar>() {

            @Override
            public void onDismissed(final Snackbar snackbar, final int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    for (Tab tab : tabs) {
                        tabSwitcher.clearSavedState(tab);
                        decorator.clearState(tab);
                        if (tabSwitcher.getCount()== 0){
                            startActivity(new Intent(WebViewActivity.this,MainActivity.class));
                            finish();
                        }
                    }
                }
            }
        };
    }

    /**
     * Shows a snackbar, which allows to undo the removal of tabs from the activity's tab switcher.
     *
     * @param text
     *         The text of the snackbar as an instance of the type {@link CharSequence}. The text
     *         may not be null
     * @param index
     *         The index of the first tab, which has been removed, as an {@link Integer} value
     * @param tabs
     *         An array, which contains the tabs, which have been removed, as an array of the type
     *         {@link Tab}. The array may not be null
     */
    private void showUndoSnackbar(@NonNull final CharSequence text, final int index,
                                  @NonNull final Tab... tabs) {
        snackbar = Snackbar.make(tabSwitcher, text, Snackbar.LENGTH_LONG).setActionTextColor(
                ContextCompat.getColor(this, R.color.snackbar_action_text_color));
        snackbar.setAction(R.string.undo, createUndoSnackbarListener(snackbar, index, tabs));
        snackbar.addCallback(createUndoSnackbarCallback(tabs));
        snackbar.show();
    }

    /**
     * Creates a reveal animation, which can be used to add a tab to the activity's tab switcher.
     *
     * @return The reveal animation, which has been created, as an instance of the class {@link
     * Animation}. The animation may not be null
     */
    @NonNull
    private Animation createRevealAnimation() {
        float x = 0;
        float y = 0;
        View view = getNavigationMenuItem();

        if (view != null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            x = location[0] + (view.getWidth() / 2f);
            y = location[1] + (view.getHeight() / 2f);
        }

        return new RevealAnimation.Builder().setX(x).setY(y).create();
    }

    /**
     * Creates a peek animation, which can be used to add a tab to the activity's tab switcher.
     *
     * @return The peek animation, which has been created, as an instance of the class {@link
     * Animation}. The animation may not be null
     */
    @NonNull
    private Animation createPeekAnimation() {
        return new PeekAnimation.Builder().setX(tabSwitcher.getWidth() / 2f).create();
    }

    /**
     * Returns the menu item, which shows the navigation icon of the tab switcher's toolbar.
     *
     * @return The menu item, which shows the navigation icon of the tab switcher's toolbar, as an
     * instance of the class {@link View} or null, if no navigation icon is shown
     */
    @Nullable
    private View getNavigationMenuItem() {
        Toolbar[] toolbars = tabSwitcher.getToolbars();

        if (toolbars != null) {
            Toolbar toolbar = toolbars.length > 1 ? toolbars[1] : toolbars[0];
            int size = toolbar.getChildCount();

            for (int i = 0; i < size; i++) {
                View child = toolbar.getChildAt(i);

                if (child instanceof ImageButton) {
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * Creates and returns a tab.
     *
     * @param index
     *         The index, the tab should be added at, as an {@link Integer} value
     * @return The tab, which has been created, as an instance of the class {@link Tab}. The tab may
     * not be null
     */
    @NonNull
    private Tab createTab(final int index) {
        CharSequence title = getString(R.string.tab_title, index + 1);
        Tab tab = new Tab(title);
        Bundle parameters = new Bundle();
        parameters.putInt(VIEW_TYPE_EXTRA, index);
        tab.setParameters(parameters);
        return tab;
    }

    @Override
    public final void onSwitcherShown(@NonNull final TabSwitcher tabSwitcher) {

    }

    @Override
    public final void onSwitcherHidden(@NonNull final TabSwitcher tabSwitcher) {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public final void onSelectionChanged(@NonNull final TabSwitcher tabSwitcher,
                                         final int selectedTabIndex,
                                         @Nullable final Tab selectedTab) {

    }

    @Override
    public final void onTabAdded(@NonNull final TabSwitcher tabSwitcher, final int index,
                                 @NonNull final Tab tab, @NonNull final Animation animation) {
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void onTabRemoved(@NonNull final TabSwitcher tabSwitcher, final int index,
                                   @NonNull final Tab tab, @NonNull final Animation animation) {
        CharSequence text = getString(R.string.removed_tab_snackbar, tab.getTitle());
        showUndoSnackbar(text, index, tab);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void onAllTabsRemoved(@NonNull final TabSwitcher tabSwitcher,
                                       @NonNull final Tab[] tabs,
                                       @NonNull final Animation animation) {
        CharSequence text = getString(R.string.cleared_tabs_snackbar);
        showUndoSnackbar(text, 0, tabs);
        inflateMenu();
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
    }

    @Override
    public final void setTheme(final int resid) {
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String themeKey = getString(R.string.theme_preference_key);
        String themeDefaultValue = getString(R.string.theme_preference_default_value);
        int theme = Integer.valueOf(sharedPreferences.getString(themeKey, themeDefaultValue));

        if (theme != 0) {
            super.setTheme(R.style.AppTheme_Translucent_Dark);
        } else {
            super.setTheme(R.style.AppTheme_Translucent_Light);
        }
    }




    //End Tab System


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



    String mGeolocationOrigin;
    GeolocationPermissions.Callback mGeolocationCallback;


    String appname;
    WebView webView;
    ProgressBar progressBar;

    LinearLayout backButton, forwardButton, homeButton, copyButton, shareButton, webBottomNavigationView, wPopUpMenu;

    LinearLayout history, exits, wallet, file, videoList, mp3List;
    ImageView web_cancelPopup;
    TextView webToolBar;
    BottomSheetDialog bottomSheetDialog;
    AutoCompleteTextView webViewUrlEditText;
    LinearLayout webViewSearchUrl;
    FloatingActionButton favDownload;
    ProgressDialog pDialog;
    ImageView imageIcon;
    ImageView imageIcon2;
    String mURl;
    String mID;

    String target_url;
    String newTabURL;
    String extension;
    int loadURL;

    Button newTabModel, copyLinkModel, downloadModel, shareModel;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    String youTubeLink;
    int bCount;
    String shareUrl;
    String shareTitle;
    DB_Manager db_manager;
    LinkClass linkClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        dataBinder = new DataBinder(this);
        decorator = new Decorator();
        tabSwitcher = findViewById(R.id.tab_switcher);
        tabSwitcher.clearSavedStatesWhenRemovingTabs(false);
        ViewCompat.setOnApplyWindowInsetsListener(tabSwitcher, createWindowInsetsListener());
        tabSwitcher.setDecorator(decorator);
        tabSwitcher.addListener(this);
        tabSwitcher.showToolbars(true);

        for (int i = 0; i < TAB_COUNT; i++) {
            tabSwitcher.addTab(createTab(i));
        }

        tabSwitcher.showAddTabButton(createAddTabButtonListener());
        tabSwitcher.setToolbarNavigationIcon(R.drawable.ic_plus_24dp, createAddTabListener());
        TabSwitcher.setupWithMenu(tabSwitcher, createTabSwitcherButtonListener());
        inflateMenu();


        backButton = findViewById(R.id.wBackButton_id);
        forwardButton = findViewById(R.id.wForwardButton_id);
        homeButton = findViewById(R.id.wHomeButton_id);
        copyButton = findViewById(R.id.wCopyUrl_id);
        shareButton = findViewById(R.id.wShareUrlButton_id);
        wPopUpMenu = findViewById(R.id.wPopUpMenu_id);
        webBottomNavigationView = findViewById(R.id.webBottomNavigationView);

        appname = (String) getTitle();
        favDownload = findViewById(R.id.favDownload_id);
        favDownload.setVisibility(View.GONE);
        db_manager = new DB_Manager(WebViewActivity.this);
        linkClass = new LinkClass();
        pDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            target_url = bundle.getString("url");
            loadURL= 1;
        }

        favDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youtubeDownloadAlert(youTubeLink);
            }
        });


        verifyStoragePermissions(this);

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
                       finish();
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



    }

    ////   onCreate    end  =============


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
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));

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

    public void showOption(View view) {

        int id = view.getId();

        if (id == R.id.newTab_Model) {

            target_url = newTabURL;
            loadURL=1;
            int index = tabSwitcher.getCount();
            Animation animation = createRevealAnimation();
            tabSwitcher.addTab(createTab(index), 0, animation);
            dialog.dismiss();

        } else if (id == R.id.copyLink_model) {

            if (newTabURL != null) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Link", newTabURL);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(WebViewActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            } else {

                Toast.makeText(WebViewActivity.this, "Text Empty", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        } else if (id == R.id.download_model) {

          anyFileDownload(newTabURL,extension);
            dialog.dismiss();

        } else if (id == R.id.share_model) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody = "Site link: " + newTabURL;
            String shareSub = "Important Link";
            intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Link Address"));
            dialog.dismiss();

        }
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

        @SuppressLint
        ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String dateTime = sdf.format(new Date());

        favDownload.setVisibility(View.GONE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        Toast.makeText(WebViewActivity.this, "" + url, Toast.LENGTH_SHORT).show();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dateTime + ".mp4");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);


    }


    private void anyFileDownload(String url, String ext) {

      /*  DownloadFileFromURL downloadMsgShow = new DownloadFileFromURL();
        downloadMsgShow.execute(url,ext);*/

    @SuppressLint
        ("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String dateTime = sdf.format(new Date());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, dateTime + "."+ext);
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(WebViewActivity.this);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            String currentDateAndTime = sdf.format(new Date());

            try {
                pathFolder = Environment.getExternalStorageDirectory() + "/download_file";
                pathFile = pathFolder + "/"+currentDateAndTime+f_url[1];
                File futureStudioIconFile = new File(pathFolder);
                if(!futureStudioIconFile.exists()){
                    futureStudioIconFile.mkdirs();
                }

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lengthOfFile = connection.getContentLength();
                String lengthOfFile2 = connection.getContentType();

                Toast.makeText(WebViewActivity.this, ""+lengthOfFile2, Toast.LENGTH_SHORT).show();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(pathFile);

                byte data[] = new byte[1024]; //anybody know what 1024 means ?
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return pathFile;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pd!=null) {
                pd.dismiss();
            }
        }
    }
}

