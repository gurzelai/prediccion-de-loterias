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
import java.util.Locale;
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
        builder.setTitle(R.string.aviso_inicial);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                empezarPreguntas();
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
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
        if (Locale.getDefault().getLanguage().equals("ar")){
            resultado = traducirResultadoAArabe(resultado);
        }
        tvNumero.setText(resultado);
    }

    private String traducirResultadoAArabe(String resultado) {
        char[] aCaracteres = resultado.toCharArray();
        resultado= "";
        for(int i = 0; i<aCaracteres.length; i++){
            resultado = resultado + cambiarNumAr(aCaracteres[i]);
        }
        return resultado;
    }

    private String cambiarNumAr(char num) {
        switch (num){
            case 1:getResources().getString(R.string.uno);break;
            case 2:getResources().getString(R.string.dos);break;
            case 3:getResources().getString(R.string.tres);break;
            case 4:getResources().getString(R.string.cuatro);break;
            case 5:getResources().getString(R.string.cinco);break;
            case 6:getResources().getString(R.string.seis);break;
            case 7:getResources().getString(R.string.siete);break;
            case 8:getResources().getString(R.string.ocho);break;
            case 9:getResources().getString(R.string.nueve);break;
            case 0: getResources().getString(R.string.cero);break;
        }
        return null;
    }

    private void dialogoResultado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.resolviendo);
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
        pregunta(getString(R.string.nombre), InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 1);
    }

    public void pregunta(String titulo, int inputType, boolean obligatoria, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(((obligatoria) ? titulo + " *" : titulo));
        final EditText input = new EditText(this);
        input.setInputType(inputType);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (obligatoria && input.getText().length() == 0) {
                    pregunta(titulo, inputType, obligatoria, id);
                } else {
                    if (titulo.equals(getResources().getString(R.string.cantidad_de_numeros))) {
                        cantNumeros = Integer.valueOf(input.getText().toString());
                    }
                    siguientePregunta(id);
                }
            }
        });
        if (!obligatoria) {
            builder.setNegativeButton(R.string.pasar, new DialogInterface.OnClickListener() {
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
                pregunta(getString(R.string.apellido), InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 2);
                break;
            case 2:
                pregunta(getString(R.string.año_nacimiento), InputType.TYPE_CLASS_NUMBER, false, 3);
                break;
            case 3:
                pregunta(getString(R.string.mes_nacimiento), InputType.TYPE_CLASS_NUMBER, false, 4);
                break;
            case 4:
                pregunta(getString(R.string.dia_nacimiento), InputType.TYPE_CLASS_NUMBER, false, 5);
                break;
            case 5:
                pregunta(getString(R.string.numero_de_hijos), InputType.TYPE_CLASS_NUMBER, false, 6);
                break;
            case 6:
                pregunta(getString(R.string.color_favorito), InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 7);
                break;
            case 7:
                pregunta(getString(R.string.pais_de_residencia), InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 8);
                break;
            case 8:
                pregunta(getString(R.string.pais_loteria), InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, false, 9);
                break;
            case 9:
                pregunta(getString(R.string.cantidad_de_numeros), InputType.TYPE_CLASS_NUMBER, true, 0);
                break;
            case 0:
                resultado();
                break;
        }
    }
}