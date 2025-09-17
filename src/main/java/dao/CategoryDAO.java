package dao;


import java.util.List;

import entity.Category;
import entity.User;

public class CategoryDAO extends AbstractDAO<Category> {

    public CategoryDAO() {
        super(Category.class);
    }

    // Các hàm đặc thù
    public List<Category> findByUser(int userId) {
        return em.createQuery("SELECT c FROM Category c WHERE c.user.user_id = :uid", Category.class)
                 .setParameter("uid", userId)
                 .getResultList();
    }

    public List<Category> findByUser(User user) {
        return em.createQuery("SELECT c FROM Category c WHERE c.user = :u", Category.class)
                 .setParameter("u", user)
                 .getResultList();
    }
}