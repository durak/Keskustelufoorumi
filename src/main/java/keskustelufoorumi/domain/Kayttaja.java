package keskustelufoorumi.domain;

public class Kayttaja {
    
    private String id;
    
    public Kayttaja(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return this.id;
    }
    
    
}
