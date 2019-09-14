package ba.unsa.etf.rma.ajla.projekatrma;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService {
    public static final int STATUS_START=0, STATUS_FINISH=1, STATUS_ERROR=2;
    public KnjigePoznanika() {
        super(null);
    }
    Bundle b=new Bundle();
    public KnjigePoznanika(String name) {
        super(name);
        //ovdje ubacimo sta treba da radi konstruktor
    }
   @Override
   public void onCreate() {
        super.onCreate();
        //akcije koje se trebaju obaviti pri kreiranju servisa
   }
   @SuppressLint("RestrictedApi")
   @Override
    protected void onHandleIntent(Intent intent) {
        String idKorisnika = intent.getStringExtra("idKorisnika");
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        receiver.send(STATUS_START, Bundle.EMPTY);

       ArrayList<String> IDBookshelf=new ArrayList<String>();
       String IdNovi = "";
       ArrayList<Knjiga> rez=new ArrayList<Knjiga>();

           String query = null;
               try {
                   query = URLEncoder.encode(idKorisnika, "utf-8");
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }
               //formirajmo ispravan url
               String url1 = "https://www.googleapis.com/books/v1/users/"+query+"/bookshelves";//"https://www.googleapis.com/books/v1/users/116497631937586219953/bookshelves";
               //String url1 = "https://www.googleapis.com/books/v1/users/"+idKorisnika+"/bookshelves";
               JSONArray items = new JSONArray();
               try {
                   URL url = new URL(url1);
                   //pozivanje web servisa
                   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                   InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                   String rezultat = convertStreamToString(in);
                   JSONObject jo = new JSONObject(rezultat);
                   items = jo.getJSONArray("items");
               } catch (MalformedURLException e) {
                   b.putString(Intent.EXTRA_TEXT, e.toString());
                   receiver.send(2, b);
                   e.printStackTrace();
               } catch (IOException e) {
                   b.putString(Intent.EXTRA_TEXT, e.toString());
                   receiver.send(2, b);
                   e.printStackTrace();
               } catch (JSONException e) {
                   b.putString(Intent.EXTRA_TEXT, e.toString());
                   receiver.send(2, b);
                   e.printStackTrace();
               }

               for (int i = 0; i < items.length(); i++) {
                   try {
                       JSONObject item = items.getJSONObject(i);
                       IdNovi = item.getString("id");
                       String access = item.getString("access");
                       if (access.equalsIgnoreCase("PUBLIC")) {
                           IDBookshelf.add(IdNovi);
                       }


                   } catch (JSONException e) {
                       b.putString(Intent.EXTRA_TEXT, e.toString());
                       receiver.send(2, b);
                       e.printStackTrace();
                   }

               }

               ArrayList<String> naziviKnjiga= new ArrayList<String>();
       for (int z = 0; z < IDBookshelf.size(); z++) {
           String url2 = "https://www.googleapis.com/books/v1/users/"+query+"/bookshelves/"+IDBookshelf.get(z).toString()+"/volumes";
                   //"https://www.googleapis.com/books/v1/users/116497631937586219953/bookshelves/1001/volumes";
           JSONArray items2 = new JSONArray();
           try {
               URL url = new URL(url2);
               //pozivanje web servisa
               HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               InputStream in = new BufferedInputStream(urlConnection.getInputStream());
               String rezultat = convertStreamToString(in);
               JSONObject jo = new JSONObject(rezultat);
               items2 = jo.getJSONArray("items");
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           } catch (JSONException e) {
               e.printStackTrace();
           }
           for (int i = 0; i < items2.length(); i++) {
               if (i == 5) break;
               String naziv = " ";
               String datum = " ";
               String opis = " ";
               ArrayList<Autor> ostaliAutori=new ArrayList<Autor>();
               JSONArray autori = new JSONArray();
               String autor = " ";
               String id = " ";
               String thumbnail = " ";
               int brStranica = 0;
               try {
                   JSONObject item = items2.getJSONObject(i);
                   id = item.getString("id");
                   JSONObject podaci = item.getJSONObject("volumeInfo");
                   naziv = podaci.getString("title");
                   naziviKnjiga.add(naziv);
                   autori = podaci.getJSONArray("authors");
                   JSONObject linkoviSlike = podaci.getJSONObject("imageLinks");
                   thumbnail = linkoviSlike.getString("thumbnail");
                   autor=autori.getString(0);
                   for (int j = 0; j < autori.length(); j++) {
                       Autor pomocni = new Autor(autori.getString(j));
                       ostaliAutori.add(pomocni);
                   }
                   datum = podaci.getString("publishedDate");
                   opis = podaci.getString("description");
                   brStranica = podaci.getInt("pageCount");
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               Knjiga nova = new Knjiga(autor, naziv, "katrgorija");
               try {
                   URL slika= new URL (thumbnail);
                   nova.setSlikaURL(slika);
                   nova.setSlika(BitmapFactory.decodeStream((InputStream) slika.getContent()));
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               nova.setId(id);
               nova.setOpis(opis);
               nova.setDatumObjavljivanja(datum);
               nova.setBrojStranica(brStranica);
               nova.setAutori(ostaliAutori);
               rez.add(nova);

           }
           FragmentOnline.listaKnjiga=rez;
}


       String[] rezultati = new String[naziviKnjiga.size()];
       rezultati = naziviKnjiga.toArray(rezultati);
       b.putStringArray("resultData", rezultati);
       receiver.send(1, b);

   }




    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

}
