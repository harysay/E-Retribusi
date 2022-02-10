package id.go.kebumenkab.retribusipasar.handler;

import androidx.annotation.NonNull;

public class JumlahBerdasarJenis {
    private String jumlahname;
    private String jumlahid;

    public JumlahBerdasarJenis(String jumlahname, String jumlahid) {
        this.jumlahname = jumlahname;
        this.jumlahid = jumlahid;
    }

    public String getJumlahname() {
        return jumlahname;
    }

    public void setJumlahname(String jumlahname) {
        this.jumlahname = jumlahname;
    }

    public String getJumlahid() {
        return jumlahid;
    }

    public void setJumlahid(String jumlahid) {
        this.jumlahid = jumlahid;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return jumlahname;
    }

    //    public JumlahBerdasarJenis() {
//    }
//
//    public JumlahBerdasarJenis(String jumjenisname, String jumjenisid) {
//        this.jumlahname = jumjenisname;
//        this.jumlahid = jumjenisid;
//    }
//
//    public String getContact_name() {
//        return jumlahname;
//    }
//
//    public void setContact_name(String jumjenisname) {
//        this.jumlahname = jumjenisname;
//    }
//
//    public String getContact_id() {
//        return jumlahid;
//    }
//
//    public void setContact_id(String jumjenisid) {
//        this.jumlahid = jumjenisid;
//    }
//
//    /**
//     * Pay attention here, you have to override the toString method as the
//     * ArrayAdapter will reads the toString of the given object for the name
//     *
//     * @return contact_name
//     */
//    @Override
//    public String toString() {
//        return jumlah_name;
//    }
}
