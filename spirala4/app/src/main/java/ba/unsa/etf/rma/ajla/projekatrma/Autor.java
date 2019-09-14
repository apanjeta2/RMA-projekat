package ba.unsa.etf.rma.ajla.projekatrma;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by User on 6.4.2018.
 */

public class Autor implements Parcelable {

    private String imeIPrezime;
    private int brKnjiga;
    private ArrayList<String> knjige=new ArrayList<String>();

    protected Autor(Parcel in) {
        imeIPrezime=in.readString();
        brKnjiga=in.readInt();
        //knjige=in.readArrayList(knjige);
        //Odabrana=in.readInt();
    }
    public Autor(String imeIPrezime, int brKnjiga) {
        this.brKnjiga = brKnjiga;
        this.imeIPrezime = imeIPrezime;
    }
    public Autor(String imeIPrezime, String idKnjige) {
        this.imeIPrezime=imeIPrezime;
        knjige.add(idKnjige);
    }
    public Autor (String imeIPrezime) {
        this.imeIPrezime=imeIPrezime;
    }
    public static final Creator<Autor> CREATOR=new Creator<Autor>() { //Implementaciju interfejsa Parcelable.Creator<Knjiga>
        @Override
        public Autor createFromParcel(Parcel parcel) {
            return new Autor(parcel);
        }

        @Override
        public Autor[] newArray(int i) {
            return new Autor[i];
        }
    };

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public String getImeIPrezime() {
        return imeIPrezime;
    }

    public void setImeIPrezime(String imeIPrezime) {
        this.imeIPrezime = imeIPrezime;
    }

    public int getBrKnjiga() {
        return brKnjiga;
    }

    public void setBrKnjiga(int brKnjiga) {
        this.brKnjiga = brKnjiga;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imeIPrezime);
        dest.writeInt(brKnjiga);
        dest.writeStringList(knjige);
    }

    public void dodajKnjigu(String id) {
        knjige.add(id);
    }
}
