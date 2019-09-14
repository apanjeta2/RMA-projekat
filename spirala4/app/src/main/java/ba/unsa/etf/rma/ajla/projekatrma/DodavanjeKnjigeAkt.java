package ba.unsa.etf.rma.ajla.projekatrma;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class DodavanjeKnjigeAkt extends AppCompatActivity {

    public static ArrayList<String> NaziviKnjiga;
    ImageView naslovnaStrana;
    public static final int PICK_IMAGE = 1;
    Uri UriSlike;
    public static Bitmap bSlike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);
        Button upisiKnjigu=(Button) findViewById(R.id.dUpisiKnjigu);
        Button ponisti=(Button) findViewById(R.id.dPonisti);
        Button nadjiSliku=(Button) findViewById(R.id.dNadjiSliku);
        final Spinner kategorijeKnjige=(Spinner) findViewById(R.id.sKategorijaKnjige);

        final EditText Autor=(EditText) findViewById(R.id.imeAutora);
        final EditText Naziv=(EditText) findViewById(R.id.nazivKnjige);

        naslovnaStrana=(ImageView) findViewById(R.id.naslovnaStr);

        //String[] novi= KategorijeAkt.nizStringova.toArray(new String[0]);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, KategorijeAkt.listeFragment.Baza.dajSveNaziveKategorija());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategorijeKnjige.setAdapter(adapter);

        NaziviKnjiga=new ArrayList<String>();

        upisiKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kategorijeKnjige;
                Knjiga nova=new Knjiga(Autor.getText().toString(), Naziv.getText().toString(), kategorijeKnjige.getSelectedItem().toString());
                nova.setSlika(bSlike);
                KategorijeAkt.listeFragment.Baza.dodajKnjigu(nova);
                NaziviKnjiga.add(nova.getNazivKnjige());
                Toast.makeText(DodavanjeKnjigeAkt.this,
                        "Dodali ste novu knjigu", Toast.LENGTH_LONG).show();
            }
        });

        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Vracanje=new Intent(DodavanjeKnjigeAkt.this, KategorijeAkt.class);
                startActivity(Vracanje);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode==RESULT_OK) {
            UriSlike=data.getData();
            naslovnaStrana.setImageURI(UriSlike);
            try {
                bSlike = getBitmapFromUri(UriSlike);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void NadjiSlikuOnClick(View view) {
        otvoriGaleriju();
    }
    private void otvoriGaleriju() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /*FileOutputStream outputStream;
    outputStream = openFileOutput(NAZIV_SLIKE, Context.MODE_PRIVATE);
    getBitmapFromUri(UriSlike).compress(Bitmap.CompressFormat.JPEG,90,outputStream);
    outputStream.close();*/
}

