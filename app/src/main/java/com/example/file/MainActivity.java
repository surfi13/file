package com.example.file;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String FILE_NAME = "document.txt";
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);

        Button btnBold = findViewById(R.id.btnBold);
        Button btnItalic = findViewById(R.id.btnItalic);

        btnBold.setOnClickListener(v -> applyStyle(Typeface.BOLD));
        btnItalic.setOnClickListener(v -> applyStyle(Typeface.ITALIC));
        openText(null);
    }

    private File getExtermalPath(){
        return new File(getExternalFilesDir(null), FILE_NAME);
    }

    public void saveText(View view){
        try(FileOutputStream fos = new FileOutputStream(getExtermalPath())) {
            EditText texBox = findViewById((R.id.editor));
            String text = texBox.getText().toString();
            fos.write(text.getBytes());
            Toast.makeText(this, "Файл сохранен", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ex){Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();}
    }

    public void openText(View view){
        File file = getExtermalPath();
        if(!file.exists()) return;
        try(FileInputStream fin = new FileInputStream(file) ){
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            editor.setText(text);
            TextView textView = findViewById(R.id.editor);
            textView.setText(text);
        }
        catch (IOException ex){Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();}
    }

    private void applyStyle(int style) {
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        if (start == end) {
            Toast.makeText(this, "Выделите текст", Toast.LENGTH_SHORT).show();
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(editor.getText());
        StyleSpan[] spans = builder.getSpans(start, end, StyleSpan.class);
        boolean alreadyApplied = false;
        for (StyleSpan span : spans) {
            if (span.getStyle() == style) {
                builder.removeSpan(span);
                alreadyApplied = true;
            }
        }
        if (!alreadyApplied) {
            builder.setSpan(new StyleSpan(style), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        editor.setText(builder);
        editor.setSelection(end);
    }
}