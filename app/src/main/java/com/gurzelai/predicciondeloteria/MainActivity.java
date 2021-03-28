package com.gurzelai.predicciondeloteria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;

public class MainActivity extends AppCompatActivity {

    TextView tvNumero;
    int cantNumeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nuevo = findViewById(R.id.btnNuevo);
        nuevo.setOnClickListener(v -> nuevo());
        tvNumero = findViewById(R.id.tvNumero);
        pantallaCompleta();
    }

    public void pantallaCompleta() {
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void nuevo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Para un mejor resultado responda todas las preguntas");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                empezarPreguntas();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void resultado() {
        dialogoResultado();
    }

    private void mostrarNumero() {
        String resultado = "";
        Random r = new Random();
        if (cantNumeros == 1) {
            resultado = String.valueOf(r.nextInt(9));
        } else {
            for (int i = 0; i < cantNumeros; i++) {
                resultado = resultado + (r.nextInt(9));
            }
        }

        tvNumero.setText(resultado);
    }

    private void dialogoResultado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resolviendo...");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mostrarNumero();
            }
        });
        builder.show();
        builder.setCancelable(false);
    }


    private void empezarPreguntas() {
        pregunta("Nombre", InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 1);
    }

    public void pregunta(String titulo, int inputType, boolean obligatoria, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(((obligatoria) ? titulo + " *" : titulo));
        final EditText input = new EditText(this);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
        input.setInputType(inputType);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (obligatoria && input.getText().length() == 0) {
                    pregunta(titulo, inputType, obligatoria, id);
                } else {
                    if (titulo.equals("Boleto de cuantos numeros")) {
                        cantNumeros = Integer.valueOf(input.getText().toString());
                    }
                    siguientePregunta(id);
                }
            }
        });
        if (!obligatoria) {
            builder.setNegativeButton("PASAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    siguientePregunta(id);
                }
            });
        }
        builder.show();
    }

    private void siguientePregunta(int id) {
        switch (id) {
            case 1:
                pregunta("Apellido", InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 2);
                break;
            case 2:
                pregunta("Año nacimiento", InputType.TYPE_CLASS_NUMBER, false, 3);
                break;
            case 3:
                pregunta("Mes nacimiento", InputType.TYPE_CLASS_NUMBER, false, 4);
                break;
            case 4:
                pregunta("Dia nacimiento", InputType.TYPE_CLASS_NUMBER, false, 5);
                break;
            case 5:
                pregunta("Numero de hijos", InputType.TYPE_CLASS_NUMBER, false, 6);
                break;
            case 6:
                pregunta("Color favorito", InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 7);
                break;
            case 7:
                pregunta("País de residencía", InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 8);
                break;
            case 8:
                pregunta("País en el que se hace la lotería", InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 9);
                break;
            case 9:
                pregunta("Boleto de cuantos numeros", InputType.TYPE_CLASS_NUMBER, true, 0);
                break;
            case 0:
                resultado();
                break;
        }
    }
}