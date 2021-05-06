package com.gurzelai.predicciondeloteria;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvNumero;
    int cantNumeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cargarAnuncio();
        Button nuevo = findViewById(R.id.btnNuevo);
        nuevo.setOnClickListener(v -> nuevo());
        tvNumero = findViewById(R.id.tvNumero);
        pantallaCompleta();
    }

    private void cargarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


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
            case '1': return getResources().getString(R.string.uno);
            case '2':return getResources().getString(R.string.dos);
            case '3':return getResources().getString(R.string.tres);
            case '4':return getResources().getString(R.string.cuatro);
            case '5':return getResources().getString(R.string.cinco);
            case '6':return getResources().getString(R.string.seis);
            case '7':return getResources().getString(R.string.siete);
            case '8':return getResources().getString(R.string.ocho);
            case '9':return getResources().getString(R.string.nueve);
            case '0': return getResources().getString(R.string.cero);
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