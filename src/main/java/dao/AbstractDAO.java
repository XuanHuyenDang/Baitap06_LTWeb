package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public abstract class AbstractDAO<T> {
    protected static EntityManagerFactory emf;
    protected EntityManager em;
    private Class<T> clazz;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("myPU");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AbstractDAO(Class<T> clazz) {
        this.clazz = clazz;
        em = emf.createEntityManager();
    }

    // --- CRUD cơ bản ---
    public T find(Object id) {
        return em.find(clazz, id);
    }

    public void save(T entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }

    public void update(T entity) {
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    public void delete(T entity) {
        em.getTransaction().begin();
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        em.getTransaction().commit();
    }

    // --- Bổ sung ---
    // Lấy tất cả
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz)
                 .getResultList();
    }

    // Tìm theo field bất kỳ (dùng LIKE)
    public List<T> search(String field, String keyword) {
        String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e WHERE e." + field + " LIKE :kw";
        return em.createQuery(jpql, clazz)
                 .setParameter("kw", "%" + keyword + "%")
                 .getResultList();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
