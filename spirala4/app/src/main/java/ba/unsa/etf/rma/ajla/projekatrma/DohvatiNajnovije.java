package ba.unsa.etf.rma.ajla.projekatrma;

import android.os.AsyncTask;

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
import java.util.Collection;
import java.util.Collections;

public class DohvatiNajnovije extends AsyncTask<String, Integer, Void> {
    public interface IDohvatiNajnovijeDone{
        public void onNajnovijeDone(ArrayList<Knjiga> rez);
    }
    ArrayList<Knjiga> rez=new ArrayList<Knjiga>();
    private DohvatiNajnovije.IDohvatiNajnovijeDone pozivatelj;
    public DohvatiNajnovije (IDohvatiNajnovijeDone p) {pozivatelj = p;};
    //private ArrayList<String> datumi=new ArrayList<String>();
    @Override
    protected Void doInBackground(String... params) {
        String query = null;
        for(int k=0; k<params.length; k++) {
            try {
                query = URLEncoder.encode(params[k], "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //formirajmo ispravan url
            String url1 = "https://www.googleapis.com/books/v1/volumes?q=author:" + query+"&orderBy=newest";
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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < items.length(); i++) {
                String naziv = " ";
                String datum = " ";
                String opis = " ";
                JSONArray autori = new JSONArray();
                String autor = " ";
                ArrayList<Autor> ostaliAutori =new ArrayList<Autor>();
                String id = " ";
                String thumbnail = " ";
                int brStranica = 0;
                try {
                    JSONObject item = items.getJSONObject(i);
                    id = item.getString("id");
                    JSONObject podaci = item.getJSONObject("volumeInfo");
                    naziv = podaci.getString("title");
                    JSONObject linkoviSlike = podaci.getJSONObject("imageLinks");
                    thumbnail = linkoviSlike.getString("thumbnail");
                    autori = podaci.getJSONArray("authors");
                        autor = autori.getString(0);

                    for (int j = 0; j < autori.length(); j++) {
                        Autor pomocni = new Autor(autori.getString(j));
                        ostaliAutori.add(pomocni);
                    }
                    datum = podaci.getString("publishedDate");
                    opis = podaci.getString("description");
                    brStranica = podaci.getInt("pageCount");
                    //datum=datum.substring(0, 4);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Knjiga nova = new Knjiga(autor, naziv, "katrgorija");
                try {
                    URL slika;
                    if(thumbnail.equals(" ")) slika = new URL("https://books.google.com/books/content?id=-Ld-GwAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
                    else slika= new URL (thumbnail);
                    nova.setSlikaURL(slika);
                    //nova.setSlika(BitmapFactory.decodeStream((InputStream) slika.getContent()));

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
            //Collections.sort(rez);
            //rez.subList(5, rez.size()).clear();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onNajnovijeDone(rez);
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



