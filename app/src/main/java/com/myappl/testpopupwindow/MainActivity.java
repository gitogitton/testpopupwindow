package com.myappl.testpopupwindow;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.WindowCallbackWrapper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = getClass().getSimpleName();

    private PopupWindow mPopupWindow = null;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d( LOG_TAG, "onCreate()" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d( LOG_TAG, "onWindowFocusChanged()" );
        super.onWindowFocusChanged(hasFocus);

        //
        //ハードウェアそのものの display サイズ
        //
        WindowManager windowManager = (WindowManager)getSystemService( WINDOW_SERVICE );
        Display disp = windowManager.getDefaultDisplay();
        Point realSize = new Point();
        disp.getRealSize( realSize );
        int realScreenWidth = realSize.x;
        int realScreenHeight = realSize.y;
        Log.d( LOG_TAG, "real display area (width/height))->"+realScreenWidth+"/"+realScreenHeight );

        //
        //status bar, title bar の高さ
        //
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame( rectangle );
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById( Window.ID_ANDROID_CONTENT ).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        Log.d( LOG_TAG, "statusBar/titleBar height->"+statusBarHeight+"/"+titleBarHeight );

        //
        //navigation bar の高さ
        //
        //これが realScreenHeight より小さい時はナビゲーションバーがソアプリで実装されてる？？
        //実機では realScreenHeight=960 で、rectangle.bottom=960 だからナビゲーションバーのアプリでの実装はないと判定出来る？
        //（機種：京セラLuce）
        //
        Log.d( LOG_TAG, "rectangle.bottom="+rectangle.bottom );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main, menu );
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d( LOG_TAG, "onPrepareOptionsMenu()" );
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.menu_item_1 :
                displayPopupMenu();
                return true;
            case R.id.menu_item_2 :
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        if ( mPopupWindow.isShowing() ) {
            Log.d( LOG_TAG, "dismiss() onDestroy()" );
            mPopupWindow.dismiss();
        }
        super.onDestroy();
    }

    //
    //private methods
    //
    private void displayPopupMenu() {

        LayoutInflater layoutInflater = getLayoutInflater();
        View popupMenuView = layoutInflater.inflate( R.layout.layout_popup_menu, null );

        TextView textView = popupMenuView.findViewById( R.id.text_menu_1 );
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d( LOG_TAG, "onClick() "+mPopupWindow.isShowing() );
                if ( mPopupWindow.isShowing() ) {
                    mPopupWindow.dismiss();
                }
                Log.d( LOG_TAG, "onClick() mPopupMenu-> "+mPopupWindow );
            }
        });

        if ( mPopupWindow != null ) { Log.d( LOG_TAG, "before new mPopupMenu-> "+mPopupWindow ); }

        if ( mPopupWindow == null ) {
            mPopupWindow = new PopupWindow( this );
        }

        mPopupWindow.setContentView( popupMenuView );

        mPopupWindow.setWidth( 500 );
        mPopupWindow.setHeight( 300 );
        mPopupWindow.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT ) ); //枠を消している
        mPopupWindow.setFocusable( true ); //フォーカスを取得する。（これをしないとオプションメニューが押下出来てしまう。）

        mPopupWindow.showAtLocation( findViewById( R.id.textView ), Gravity.CENTER, 0, 0 );

        //mPopupWindow.isShowing();
        //mPopupWindow.dismiss();
    }
}
