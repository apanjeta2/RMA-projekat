package ba.unsa.etf.rma.ajla.projekatrma;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 25.3.2018.
 */
/*Da bi proslijedili listu knjiga u fragment trebamo modifikovati klasu Knjiga tako da
implementira Parcelabele interfejs. Ovaj interfejs omogućava da se objekti mogu prosljeđivati
između različitih android komponenti, pa i aplikacija. On serializuje objekte neke klase u oblik
pogodan za prosljeđivanje (ovakvu implementaciju možemo iskoristiti i za prosljeđivanje cijelih
objekata kroz intent).
*/
public class Knjiga implements Parcelable, Comparable {
    private String id;
    private ArrayList<Autor> autori=new ArrayList<Autor>();
    private String nazivKnjige;
    private String opis;
    private String datumObjavljivanja;
    private URL slikaURL;
    private int brojStranica;

    private String imeAutora;
    private String kategorija;
    private Bitmap slika;
    private boolean Odabrana; // da provjeravamo je li kliknuta knjiga, kako bi mogla ostati obojena i nakon vracanja na pocetnu aktivnost

    public Knjiga() {}
    public Knjiga(String imeAutora, String nazivKnjige, String kategorija) {
        this.nazivKnjige = nazivKnjige;
        this.imeAutora = imeAutora;
        this.kategorija = kategorija;
        this.Odabrana=false;
    }

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slikaURL, int brojStranica) {
        this.id=id;
        this.nazivKnjige=naziv;
        this.autori=autori;
        this.opis=opis;
        this.datumObjavljivanja=datumObjavljivanja;
        this.slikaURL=slikaURL;
        this.brojStranica=brojStranica;
    }

    protected Knjiga(Parcel in) {  //klasa koja implementira parcelable mora sadržavati konstruktor koji prima Parcel-u njemu se pretvara objekat iz Parcel u objekat klase koju implementiramo
        imeAutora=in.readString();
        nazivKnjige=in.readString();
        kategorija=in.readString();
        id=in.readString();
        opis=in.readString();
        datumObjavljivanja=in.readString();
        brojStranica=in.readInt();
        //Odabrana=in.readInt();
    }
    public static final Creator<Knjiga> CREATOR=new Creator<Knjiga>() { //Implementaciju interfejsa Parcelable.Creator<Knjiga>
        @Override
        public Knjiga createFromParcel(Parcel parcel) {
            return new Knjiga(parcel);
        }

        @Override
        public Knjiga[] newArray(int i) {
            return new Knjiga[i];
        }
    };
    public void setOdabrana() {this.Odabrana=true;}
    public boolean getOdabrana() {return this.Odabrana;}

    public Bitmap getSlika() {
        return slika;
    }

    public void setSlika(Bitmap slika) {
        this.slika = slika;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeAutora);
        dest.writeString(nazivKnjige);
        dest.writeString(kategorija);
        dest.writeString(datumObjavljivanja);
        dest.writeString(opis);
        dest.writeInt(brojStranica);
        dest.writeString(id);
    }
    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlikaURL() {
        return slikaURL;
    }

    public void setSlikaURL(URL slikaURL) {
        this.slikaURL = slikaURL;
    }

    public void setBrojStranica(int brojStranica)  {
        this.brojStranica=brojStranica;
    }

    public int getBrojStranica() {return brojStranica; }

    public ArrayList<Autor> getAutori() {return autori;}
    public void setAutori(ArrayList<Autor> autori) {
        this.autori=autori;
    }


    @Override
    public int compareTo(@NonNull Object c) {
        String datum1=((Knjiga)c).getDatumObjavljivanja();
        String datum2=this.getDatumObjavljivanja();
        return datum1.compareTo(datum2);
    }
}
