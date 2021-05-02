package kz.fis.jsoup;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textView; //для данных котировок

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        JSOUPShowsTask task = new JSOUPShowsTask(); // Создание потока определения котировок
        task.execute(); // Активация потока
    }

    public void onClick(View view) {
        new JSOUPShowsTask().execute(); // Создание и активация потока определения котировок
    }

    // Класс отдельного асинхронного потока
    @SuppressLint("StaticFieldLeak")
    private class JSOUPShowsTask extends AsyncTask<String, Void, String> {

        // Тут реализуем фоновую асинхронную загрузку данных, требующих много времени
        @Override
        protected String doInBackground(String... params) {
            return ShowsReader.getShowsData();
        }

        // Тут реализуем что нужно сделать после окончания загрузки данных
        @Override
        protected void onPostExecute(final String shows) {
            super.onPostExecute(shows);

            // Выдаем данные о котировках в компонент
            textView.post(new Runnable() { //  с использованием синхронизации UI
                @Override
                public void run() {
                    if (shows != null) {
                        textView.setText(shows);
                    } else {
                        textView.setText("");
                        textView.append("Нет данных!" + "\nПроверьте доступность Интернета");
                    }
                }
            });
        }
    }
}
