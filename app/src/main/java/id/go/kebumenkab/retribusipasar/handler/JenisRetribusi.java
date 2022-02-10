package id.go.kebumenkab.retribusipasar.handler;

public class JenisRetribusi {
    private String id;
    private String nama;

    public JenisRetribusi(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof JenisRetribusi){
            JenisRetribusi c = (JenisRetribusi )obj;
            if(c.getNama().equals(nama) && c.getId()==id ) return true;
        }
        return false;
    }

}
