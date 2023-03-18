package com.dentalhelpinghands;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class MyDHHClass extends Application {
    public static MyDHHClass application;

//    public RequestQueue queue;

    public MyDHHClass() {
        application = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;


        /*Logger.Companion.d("test_KEY_IS_CLEAR_DATA: ",
                Preference.Companion.getBooleanValue(Preference.KEY_IS_CLEAR_DATA));

        if (Preference.Companion.getBooleanValue(Preference.KEY_IS_CLEAR_DATA) == Preference.VALUE_DEFAULT_IS_CLEAR_DATA) {
            clearApplicationData();
            Preference.Companion.setBooleanValue(Preference.KEY_IS_CLEAR_DATA, true);

            //FirebaseApp.initializeApp(getAppContext());
        }*/
    }

    /*public RequestQueue getQueueReference() {
        if (queue == null) {
            queue = Volley.newRequestQueue(application);
        }
        return queue;
    }*/

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static MyDHHClass getAppInstance() {
        if (application == null) {
            application = new MyDHHClass();
        }
        return application;
    }

    public static Context getAppContext() {
        if (application == null) {
            application = new MyDHHClass();
        }
        return application;
    }

//    private void clearApplicationData() {
//        File cacheDirectory = getCacheDir();
//        File applicationDirectory = new File(Objects.requireNonNull(cacheDirectory.getParent()));
//        if (applicationDirectory.exists()) {
//            String[] fileNames = applicationDirectory.list();
//
//            if (fileNames != null && fileNames.length > 0) {
//                for (String fileName : fileNames) {
//                    if (!fileName.equals("lib")) {
//                        deleteFile(new File(applicationDirectory, fileName));
//                    }
//                }
//            }
//        }
//    }

//    private boolean deleteFile(File file) {
//        boolean deletedAll = true;
//        if (file != null) {
//            if (file.isDirectory()) {
//                String[] children = file.list();
//
//                if (children != null && children.length > 0) {
//                    for (String child : children) {
//                        deletedAll = deleteFile(new File(file, child)) && deletedAll;
//                    }
//                }
//            } else {
//                deletedAll = file.delete();
//            }
//        }
//
//        return deletedAll;
//    }
}
