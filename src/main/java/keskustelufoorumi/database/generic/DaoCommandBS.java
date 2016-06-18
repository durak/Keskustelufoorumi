package keskustelufoorumi.database.generic;

public interface DaoCommandBS<T> {
    
    public T execute(DaoManagerBS manager);
    
}
