package keskustelufoorumi.database.generic;

public interface DaoCommand<T> {
    
    public T execute(DaoManagerBS manager);
    
}
