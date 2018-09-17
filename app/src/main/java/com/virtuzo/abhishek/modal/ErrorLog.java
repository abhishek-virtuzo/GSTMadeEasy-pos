package com.virtuzo.abhishek.modal;

import android.content.SharedPreferences;

import com.virtuzo.abhishek.DashBoardScreen;

/**
 * Created by Abhishek Aggarwal on 2/3/2018.
 */

public class ErrorLog {

    public static final String ERRORTYPE_APICALL = "Api Call Error";
    public static final String ERRORTYPE_DATABASE = "Database Error";
    public static final String ERRORTYPE_UNHANDLED = "Unhandled Error";
    public static final String ERRORTYPE_SAVING_DB_BACKUP = "Saving DB Backup Error";
    public static final String ERRORTYPE_VOLLEY = "Volley Error";

    public static final int ERRORCODE_APICALL = 0;
    public static final int ERRORCODE_DATABASE = 1;
    public static final int ERRORCODE_UNHANDLED = 2;
    public static final int ERRORCODE_SAVING_DB_BACKUP = 3;
    public static final int ERRORCODE_VOLLEY = 4;

    private long ID;
    private int LogDate;
    private String LogTime;
    private String TabID; // ClientCode - OutletID - TabID - UserID
    private String Origin;
    private String TypeOfError;
    private String LogMessage;
    private int Synced = 0;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getLogDate() {
        return LogDate;
    }

    public void setLogDate(int logDate) {
        LogDate = logDate;
    }

    public String getLogTime() {
        return LogTime;
    }

    public void setLogTime(String logTime) {
        LogTime = logTime;
    }

    public String getTabID() {
        return TabID;
    }

    public void setTabID(String tabID) {
        TabID = tabID;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getTypeOfError() {
        return TypeOfError;
    }

    public void setTypeOfError(String typeOfError) {
        TypeOfError = typeOfError;
    }

    public String getLogMessage() {
        return LogMessage;
    }

    public void setLogMessage(String logMessage) {
        LogMessage = logMessage;
    }

    public int getSynced() {
        return Synced;
    }

    public void setSynced(int synced) {
        Synced = synced;
    }

    public static ErrorLog createErrorLog(int errorCode, String origin, String LogMessage) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setLogDate(MyFunctions.getCurrentDateNumeric());
        errorLog.setLogTime(MyFunctions.getCurrentTime());
        SharedPreferences sharedPreferences = MyFunctions.getContext().getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);

        // ClientCode - OutletID - TabID - UserID
        String ClientCode = sharedPreferences.getString("ClientCode", "Unknown");
        String OutletID = sharedPreferences.getString("OutletID", "Unknown");
        String TabCode = sharedPreferences.getString("TabCode", "Unknown");
        String UserID = sharedPreferences.getString("UserID", "Unknown");
        errorLog.setTabID(ClientCode + " - " + OutletID + " - " + TabCode + " - " + UserID + " (App Version:" + MyFunctions.getAppVersion() + ")");
        switch (errorCode) {
            case ERRORCODE_APICALL:
                errorLog.setTypeOfError(ERRORTYPE_APICALL);
                break;

            case ERRORCODE_DATABASE:
                errorLog.setTypeOfError(ERRORTYPE_DATABASE);
                break;

            case ERRORCODE_UNHANDLED:
                errorLog.setTypeOfError(ERRORTYPE_UNHANDLED);
                break;

            case ERRORCODE_SAVING_DB_BACKUP:
                errorLog.setTypeOfError(ERRORTYPE_SAVING_DB_BACKUP);
                break;

            case ERRORCODE_VOLLEY:
                errorLog.setTypeOfError(ERRORTYPE_VOLLEY);
                break;
        }
        errorLog.setOrigin(origin);
        errorLog.setLogMessage(LogMessage);
        return errorLog;
    }
}
