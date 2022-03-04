package com.my.smartgroceries.account;

import android.content.Context;
import android.widget.Toast;

import com.my.smartgroceries.models.UserData;

public class UserManager {
    private static String UID;
    private static UserData userData;
    //private static UserLoad userLoad;
    public static Boolean isUserLoaded=false;
    public static Boolean User_Active = false;

    public static Boolean checkSession(Context context)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        String UID = db.getUser();
        if(UID.equals(DatabaseHelper.DB_NULL))
            return false;
        else
        {
            UserManager.UID=UID;
            return true;
        }
    }
    //public static void setUserLoad(UserLoad userLoad)
    //{
    //    UserManager.userLoad=userLoad;
    //    isUserLoaded=true;
    //}
    public static void setUserData(UserData userData)
    {
        UserManager.userData=userData;
        //userLoad.check(userData);
    }

    public static void Login(final String UID,final UserData userData,final Context context)
    {
        UserManager.UID = UID;
        UserManager.userData=userData;
        User_Active = true;
        loginSession(context);
    }
    private static void loginSession(final Context context)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        if(!db.setUser(UID))
        {
            Toast.makeText(context, "Failed to make Session",Toast.LENGTH_LONG).show();
        }
        db.close();
    }
    public static void Logout(final Context context)
    {
        User_Active = false;
        UID = null;
        userData = null;
        isUserLoaded=false;
        logoutSession(context);
    }
    private static void logoutSession(final Context context)
    {
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteUser();
        db.close();
    }


    public static String getUID() {
        return UID;
    }
    public static UserData getUserData() {
        return userData;
    }
}
