package com.pruebas.patoibanez.myapplication;

        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.TextView;
        import android.os.AsyncTask;

        import java.util.Timer;
        import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    WebView webview;
    ConnectivityManager conexion;
    TextView errorconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorconexion = (TextView) findViewById(R.id.error);
        webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.loadUrl("http://ejlaa.com/iesa/ps4_pro/");
        webview.setWebViewClient(new WebViewClient()
                                 {public boolean shouldOverrideUrlLoading(WebView view, String url){
                                     return false;
                                 }
                                 }
        );

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            check c = new check();
                            c.execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 3000);  //ejecutar en intervalo de 3 segundos.




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK)&& webview.canGoBack()){
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class check extends AsyncTask <Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            conexion = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo infowifi = conexion.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo infodatos = conexion.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (String.valueOf(infowifi.getState()).equals("CONNECTED")){
                errorconexion.setVisibility(errorconexion.GONE);
            }
            else {
                if (String.valueOf(infodatos.getState()).equals("CONNECTED")){
                    errorconexion.setVisibility(errorconexion.GONE);
                }
                else {
                    webview.setVisibility(View.GONE);
                    errorconexion.setVisibility(View.VISIBLE);
                }
            }
            return null;
        }
    }

}
