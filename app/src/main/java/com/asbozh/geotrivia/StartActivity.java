package com.asbozh.geotrivia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartActivity extends AppCompatActivity {

    private static final String DATABASE_TABLE_GEO = "TABLE_GEO_Questions";
    private static final String DATABASE_TABLE_HIS = "TABLE_HIS_Questions";
    private static final String DATABASE_TABLE_BIO = "TABLE_BIO_Questions";
    private static final String DATABASE_TABLE_PHI = "TABLE_PHI_Questions";

    SQLHandler SQLdb = new SQLHandler(this);
    ProgressBar progressBar;

    // SharedPreferences object and variables
    SharedPreferences mSharedPreferences;
    private boolean isSQLdbCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loading saved shared preferences
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        isSQLdbCreated = mSharedPreferences.getBoolean(getString(R.string.database_created), false);

        if (!isSQLdbCreated) {
            setContentView(R.layout.activity_start);
            progressBar = (ProgressBar) findViewById(R.id.pbCreatingDb);
            progressBar.setMax(100);

            new CreateDatabase().execute("");

        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private class CreateDatabase extends AsyncTask <String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            BufferedReader fileReader = null;

            // Geography questions
            try {
                fileReader = new BufferedReader(new InputStreamReader(getAssets().open("table_geo_questions.txt"), "UTF-8"));
                String mLine;
                SQLdb.open();

                while ((mLine = fileReader.readLine()) != null) {
                    String[] separated = mLine.split("\\;");
                    SQLdb.createEntry(DATABASE_TABLE_GEO, separated[1], separated[2], separated[3], separated[4], separated[5], separated[6]);
                }
                SQLdb.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            } finally {
                Log.d("asbozh", "first file read and added");
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            publishProgress(25);

            // History questions

            try {
                fileReader = new BufferedReader(new InputStreamReader(getAssets().open("table_his_questions.txt"), "UTF-8"));
                String mLine;
                SQLdb.open();

                while ((mLine = fileReader.readLine()) != null) {
                    String[] separated = mLine.split("\\;");
                    SQLdb.createEntry(DATABASE_TABLE_HIS, separated[1], separated[2], separated[3], separated[4], separated[5], separated[6]);
                }
                SQLdb.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            } finally {
                Log.d("asbozh", "second file read and added");
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            publishProgress(50);

            // Biology questions

            try {
                fileReader = new BufferedReader(new InputStreamReader(getAssets().open("table_bio_questions.txt"), "UTF-8"));
                String mLine;
                SQLdb.open();

                while ((mLine = fileReader.readLine()) != null) {
                    String[] separated = mLine.split("\\;");
                    SQLdb.createEntry(DATABASE_TABLE_BIO, separated[1], separated[2], separated[3], separated[4], separated[5], separated[6]);
                }
                SQLdb.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            } finally {
                Log.d("asbozh", "second file read and added");
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            publishProgress(75);

            // Philosophy questions

            try {
                fileReader = new BufferedReader(new InputStreamReader(getAssets().open("table_phi_questions.txt"), "UTF-8"));
                String mLine;
                SQLdb.open();

                while ((mLine = fileReader.readLine()) != null) {
                    String[] separated = mLine.split("\\;");
                    SQLdb.createEntry(DATABASE_TABLE_PHI, separated[1], separated[2], separated[3], separated[4], separated[5], separated[6]);
                }
                SQLdb.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            } finally {
                Log.d("asbozh", "second file read and added");
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            publishProgress(100);

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                isSQLdbCreated = true;
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean(getString(R.string.database_created), isSQLdbCreated);
                editor.apply();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Log.d("asbozh", "result is false");
                Toast.makeText(getApplicationContext(), "Critical error! Please reinstall the application.", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
