package groovyfx.music.db.services

interface CrudDAO<T, R> {
    List<T> findAll();
    Optional<T> findOne(R id);
    T findByName(String name);
    T save(T t);
    T update(T t);
    void delete(R id);
}
