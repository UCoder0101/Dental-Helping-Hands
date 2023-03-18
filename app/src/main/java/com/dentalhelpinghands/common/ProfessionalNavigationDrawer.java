package com.dentalhelpinghands.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.IntDef;
import androidx.cardview.widget.CardView;

import com.dentalhelpinghands.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ProfessionalNavigationDrawer extends RelativeLayout {
    //Indicates that any drawer is open. No animation is in progress.
    public static final int STATE_OPEN = 0;
    //Indicates that any drawer is closed. No animation is in progress.
    public static final int STATE_CLOSED = 1;
    //Indicates that a drawer is in the process of opening.
    public static final int STATE_OPENING = 2;
    //Indicates that a drawer is in the process of closing.
    public static final int STATE_CLOSING = 3;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    protected RelativeLayout rootLayout, appbarRL;
    protected CardView containerCV;
    protected LinearLayout containerLL;
    private boolean navOpen = false;
    //Listeners
    private ProfessionalNavigationDrawer.OnHamMenuClickListener onHamMenuClickListener;
    private ProfessionalNavigationDrawer.OnMenuItemClickListener onMenuItemClickListener;
    private ProfessionalNavigationDrawer.DrawerListener drawerListener;

    public ProfessionalNavigationDrawer(Context context) {
        super(context);
    }


    public ProfessionalNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                com.dentalhelpinghands.R.styleable.CustomNavigationDrawer,
                0, 0);
        setAttributes();
        a.recycle();

    }

    //Adding the child views inside CardView LinearLayout
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (containerLL == null) {
            super.addView(child, index, params);
        } else {
            //Forward these calls to the content view
            containerLL.addView(child, index, params);
        }
    }

    public void init(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        View rootView = mLayoutInflater.inflate(R.layout.professional_navigation_drawer, this, true);
        rootLayout = rootView.findViewById(R.id.rootLayout);
        appbarRL = rootView.findViewById(R.id.appBarRL);
        containerCV = rootView.findViewById(R.id.containerCV);
        containerLL = rootView.findViewById(R.id.containerLL);

        rootView.findViewById(R.id.tvMenuPreviousWork).setOnClickListener(v -> {
            menuItemClicked(0);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuNotification).setOnClickListener(v -> {
            menuItemClicked(1);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuMyProfile).setOnClickListener(v -> {
            menuItemClicked(2);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuPrivacy).setOnClickListener(v -> {
            menuItemClicked(3);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuTC).setOnClickListener(v -> {
            menuItemClicked(4);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuAboutUs).setOnClickListener(v -> {
            menuItemClicked(5);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuContactUs).setOnClickListener(v -> {
            menuItemClicked(6);
            closeDrawer();
        });

        rootView.findViewById(R.id.tvMenuLogout).setOnClickListener(v -> {
            menuItemClicked(7);
            closeDrawer();
        });
    }

    public ProfessionalNavigationDrawer.OnHamMenuClickListener getOnHamMenuClickListener() {
        return onHamMenuClickListener;
    }

    public void setOnHamMenuClickListener(ProfessionalNavigationDrawer.OnHamMenuClickListener onHamMenuClickListener) {
        this.onHamMenuClickListener = onHamMenuClickListener;
    }

    public ProfessionalNavigationDrawer.OnMenuItemClickListener getOnMenuItemClickListener() {
        return onMenuItemClickListener;
    }

    public void setOnMenuItemClickListener(ProfessionalNavigationDrawer.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public ProfessionalNavigationDrawer.DrawerListener getDrawerListener() {
        return drawerListener;
    }

    public void setDrawerListener(ProfessionalNavigationDrawer.DrawerListener drawerListener) {
        this.drawerListener = drawerListener;
    }

    protected void hamMenuClicked() {
        if (onHamMenuClickListener != null) {
            onHamMenuClickListener.onHamMenuClicked();
        }
    }

    protected void menuItemClicked(int position) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClicked(position);

        }
    }

    protected void drawerOpened() {
        if (drawerListener != null) {
            drawerListener.onDrawerOpened();
            drawerListener.onDrawerStateChanged(STATE_OPEN);
        }
    }

    protected void drawerClosed() {
        System.out.println("Drawer Closing");
        if (drawerListener != null) {
            drawerListener.onDrawerClosed();
            drawerListener.onDrawerStateChanged(STATE_CLOSED);
        }
    }

    protected void drawerOpening() {
        if (drawerListener != null) {
            drawerListener.onDrawerOpening();
            drawerListener.onDrawerStateChanged(STATE_OPENING);
        }
    }

    protected void drawerClosing() {
        if (drawerListener != null) {
            drawerListener.onDrawerClosing();
            drawerListener.onDrawerStateChanged(STATE_CLOSING);
        }
    }

    //Closes drawer
    public void closeDrawer() {
        drawerClosing();
        navOpen = false;
        containerCV.animate().translationX(rootLayout.getX()).translationY(rootLayout.getY()).setDuration(500).start();

        containerCV.setRotation(0);


        final Handler handler = new Handler();
        handler.postDelayed(() -> {

            drawerClosed();
            containerCV.setCardElevation((float) 0);
            containerCV.setRadius((float) 0);

        }, 500);
    }

    //Opens Drawer
    public void openDrawer() {

        drawerOpening();
        navOpen = true;
        containerCV.setCardElevation((float) 100.0);
        containerCV.setRadius((float) 60.0);
//        containerCV.animate().translationX(rootLayout.getX() + (rootLayout.getWidth() / 8) + (rootLayout.getWidth() / 2)).translationY(250).setDuration(500).start();

        containerCV.animate().translationX(rootLayout.getX() + (rootLayout.getWidth() / 8) + (rootLayout.getWidth() / 2)).translationY(150).setDuration(500).start();

        containerCV.setRotation(-10);

        final Handler handler = new Handler();
        handler.postDelayed(this::drawerOpened, 250);
    }

    //set Attributes from xml
    protected void setAttributes() {
    }

    //To check if drawer is open or not
    public boolean isDrawerOpen() {
        return navOpen;
    }

    @IntDef({STATE_OPEN, STATE_CLOSED, STATE_OPENING, STATE_CLOSING})
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }

    //Hamburger button Click Listener
    public interface OnHamMenuClickListener {

        public void onHamMenuClicked();

    }

    //Listener for menu item click
    public interface OnMenuItemClickListener {

        public void onMenuItemClicked(int position);

    }

    //Listener for monitoring events about drawer.
    public interface DrawerListener {

        //Called when a drawer is opening.
        void onDrawerOpening();

        //Called when a drawer is closing.
        void onDrawerClosing();

        //Called when a drawer has settled in a completely open state.
        void onDrawerOpened();

        //Called when a drawer has settled in a completely closed state.
        void onDrawerClosed();

        //Called when the drawer motion state changes. The new state will
        void onDrawerStateChanged(@ProfessionalNavigationDrawer.State int newState);

    }

}


