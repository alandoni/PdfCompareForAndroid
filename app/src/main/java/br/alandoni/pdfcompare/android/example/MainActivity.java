package br.alandoni.pdfcompare.android.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import br.alandoni.pdfcompare.android.CompareResult;
import br.alandoni.pdfcompare.android.PdfComparator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Task.TaskCompletion completion = file -> {
            PDFView pdfView = (PDFView) findViewById(R.id.pdfview);
            pdfView.fromFile(new File(file))
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        };
        Task task = new Task(this, completion);
        task.execute();
    }

    static class Task extends AsyncTask<String, Void, String> {

        interface TaskCompletion {
            void onCompleted(String file);
        }

        private WeakReference<Context> context;
        private final TaskCompletion taskCompletion;

        public Task(Context context, TaskCompletion taskCompletion) {
            this.context = new WeakReference<>(context);
            this.taskCompletion = taskCompletion;
        }

        @Override
        protected String doInBackground(String... objects) {
            try {
                InputStream actual = context.get().getAssets().open("actual.pdf");
                InputStream expected = context.get().getAssets().open("expected.pdf");

                File newDir = new File(context.get().getExternalCacheDir().getAbsolutePath() + "/diff/");
                if (!newDir.exists()) {
                    newDir.mkdirs();
                }
                File newFile = new File(newDir.getAbsolutePath() + "/fileDiff.pdf");
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                OutputStream outputStream = new FileOutputStream(newFile);

                CompareResult result = new PdfComparator(expected, actual).compare();
                result.writeTo(outputStream);

                return newFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file) {
            super.onPostExecute(file);

            taskCompletion.onCompleted(file);
        }
    }
}