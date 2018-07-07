package com.example.acer.currentlocation;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.acer.currentlocation.Profile.addProfile;
import com.example.acer.currentlocation.ServerConnection.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class RecService extends Service {
    private int RECORD_AUDIO_REQUEST_CODE =123 ;
    private MediaRecorder mRecorder;
    private String fileName = null;
    private MediaPlayer mPlayer;
    SessionManager session;
    String uid;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef2;
    FirebaseStorage storage;
    String path,path2;
    Uri duri;
    StorageReference storageReference;
    public RecService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        session = new SessionManager(getApplicationContext());
        uid=session.getValue(RecService.this,"uid");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        startRecording();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        stopRecording();
        super.onDestroy();
    }
    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName =  root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" +
                String.valueOf(System.currentTimeMillis() + ".mp3");
        Log.d("filename",fileName);
       // Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();

        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    private void stopRecording() {

        try{
            mRecorder.stop();
            mRecorder.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mRecorder = null;
        path="Audios/" + UUID.randomUUID().toString();
        Uri uriAudio = Uri.fromFile(new File(fileName).getAbsoluteFile());
        final StorageReference ref = storageReference.child(path);

        ref.putFile(uriAudio)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        duri=taskSnapshot.getDownloadUrl();
                        path2=duri.toString();

                        database = FirebaseDatabase.getInstance();
                        myRef =database.getReference(uid).child("Audio");

                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        RecData rec= new RecData(path2);
                        myRef.child(mydate).setValue(rec);
                        // path =taskSnapshot.getMetadata().getDownloadUrl();

                        //progressDialog.dismiss();
                       // Toast.makeText(RecService.this, "Uploaded"+path2, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // progressDialog.dismiss();
                        // Toast.makeText(ProfileEditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }

                });
        //showing the play button
        // Toast.makeText(this, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
    }
}
