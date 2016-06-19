package keskustelufoorumi.database.genericEI_KAYTOSSA;

public interface DaoCommandBS<T> {
    
    public T execute(DaoManagerBS manager);
    
}
