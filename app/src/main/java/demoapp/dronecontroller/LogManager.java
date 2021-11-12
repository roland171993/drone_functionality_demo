package demoapp.dronecontroller;

import android.app.Activity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dji.sdk.sample.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import demoapp.dronecontroller.utils.Utils;

public class LogManager {
    public static final String TAG = LogManager.class.getSimpleName();
    public static void add(String tag, String funcName, @NonNull String msg) {
        Log.v(tag, " EXCEPTION OR  HAPPEN  funcName "+funcName);
        // Log Error before Add To DB
        if (Utils.isNull(msg)){
            Log.v(tag, " EXCEPTION OR  HAPPEN  msg IS NULL ");
        }else {
            Log.v(tag, " EXCEPTION OR  HAPPEN  msg : "+msg);
        }
    }
    public static void add(String tag, String funcName, @Nullable Exception exception, @Nullable Error error) {
        Log.v(tag, " EXCEPTION OR  HAPPEN  funcName "+funcName);
        // Log Error before Add To DB
        if (!Utils.isNull(exception)){
            exception.printStackTrace();
        }
        if (!Utils.isNull(error)){
            error.printStackTrace();
        }



    }

    public static void caughtExceptionOrError(@NonNull final Context ctx){
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable throwable)
            {
                final Writer result = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(result);
                throwable.printStackTrace(printWriter);
                String stacktrace = result.toString();
                printWriter.close();
                Log.d("stacktrace", stacktrace);
                add(TAG,"uncaughtException", stacktrace);
                saveInDocumentAndExit(stacktrace);
            }

            private void saveInDocumentAndExit(String fileContent){
                try {
                    final int ERROR = 1;

                    if (fileContent != null){

                        // Get Directory
                        File directory  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+"/"+ctx.getString(R.string.app_name)).getAbsoluteFile();
                        if(!directory.exists() && !directory.mkdirs()){
                            //Log.v(Constants.APP_NAME, TAG + "Error creating directory " + directory);
                            return;
                        }
                        String backupName = "CrashLog for "+Utils.getToday();
                        String FILE_EXTENSION_BKP = ".txt";


                        String bkpPath = backupName + FILE_EXTENSION_BKP;
                        File bkp = new File (directory, bkpPath);

//                        if (bkp.exists()){
//                            bkp.delete();
//
//                        }

                        //Add new file
                        bkp = new File (directory, bkpPath);

                        File finalBkp = bkp;
                        if (fileContent.trim().contentEquals(""))
                            return;


                        FileOutputStream fos = new FileOutputStream(finalBkp, true); // save
                        fos.write(fileContent.getBytes());
                        fos.close();

                        // Auto refresh
                        MediaScannerConnection msc =  new MediaScannerConnection(ctx, new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {
                                MediaScannerConnection.scanFile(ctx, new String[]{finalBkp.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                                    public void onScanCompleted(String path, Uri uri) {
                                        //Log.v(Constants.APP_NAME, TAG + " KML BUILD SAVE OK PATH "+ kml.getAbsolutePath() +" IS EXIST "+kml.exists());
                                    }
                                });
                            }

                            @Override
                            public void onScanCompleted(String s, Uri uri) {
                            }
                        });
                        if(!msc.isConnected()){
                            msc.connect();
                        }
                        ((Activity)ctx).finishAffinity();
                        //System.exit(ERROR);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }catch (Error er){
                    er.printStackTrace();
                }

            }
        });
    }

}
