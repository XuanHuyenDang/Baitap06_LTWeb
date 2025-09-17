package dao;

import entity.User;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    // Hàm đặc thù cho User
    public User login(String username, String password) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :uname AND u.password = :pwd",
                    User.class)
                    .setParameter("uname", username)
                    .setParameter("pwd", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public User findByEmail(String email) {
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email",
                    User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null; 
        }
    }
}