package keskustelufoorumi.domain;

public class Kayttaja {
    
    private String kayttajatunnus;
    
    public Kayttaja(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
    }
    
    public String getTunnus() {
        return kayttajatunnus;
    }
    
    @Override
    public String toString() {
        return this.kayttajatunnus;
    }
    
    
}
