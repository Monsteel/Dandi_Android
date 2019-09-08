package org.techtown.schooler.network.Api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkFileUpload extends AsyncTask<Call, String, String> {

    private final String uploadName;
    private OnTaskFinishListener onTaskFinishListener;

    public NetworkFileUpload(String uploadName){
        this.uploadName = uploadName;
    }

    @Override
    protected String doInBackground(Call... calls) {
        try {
            Call<retrofit2.Response> call = calls[0];
            Response<Response> response= call.execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return uploadName;
            }
        } catch (IOException e) {
            Log.e("error", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (onTaskFinishListener != null) {
            onTaskFinishListener.onTaskFinish(s);
        }
    }

    public NetworkFileUpload setOnTaskFinishListener(OnTaskFinishListener onTaskFinishListener) {
        this.onTaskFinishListener = onTaskFinishListener;
        return this;
    }

    public interface OnTaskFinishListener {
        void onTaskFinish(String uploadName);
    }
}
