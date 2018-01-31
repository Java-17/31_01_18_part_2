package com.sheygam.java_17_31_01_18_part2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MY_TAG";
    private TextView resultTxt;
    private MyTask myTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTxt = findViewById(R.id.resultTxt);
        findViewById(R.id.startBtn)
                .setOnClickListener(this);
        if(savedInstanceState == null) {
            myTask = new MyTask();
            myTask.bind(this);
            myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else{
            myTask = (MyTask) getLastCustomNonConfigurationInstance();
            if(myTask!=null){
                myTask.bind(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.startBtn){
            resultTxt.setText("Result");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putString("DATA",resultTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: ");
        resultTxt.setText(savedInstanceState.getString("DATA"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return myTask;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        myTask.unbind();
        myTask = null;
        super.onDestroy();
    }

    class MyTask extends AsyncTask<Void,String,Void>{
        private int id;
        private MainActivity activity;

        public MyTask() {
            Random rnd = new Random();
            id = rnd.nextInt(1000);
        }

        public void bind(MainActivity activity){
            this.activity = activity;
        }

        public void unbind(){
            this.activity = null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(activity != null) {
                activity.resultTxt.setText(values[0]);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 10; i++) {
                Log.d(TAG, "doInBackground "+ id + " : " + i);
                publishProgress(String.valueOf(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(activity != null) {
                activity.resultTxt.setText("DONE");
            }
        }
    }
}
