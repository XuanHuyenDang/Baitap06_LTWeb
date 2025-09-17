package dao;

import java.util.List;

import entity.Video;

public class VideoDAO extends AbstractDAO<Video> {

    public VideoDAO() {
        super(Video.class);
    }

    // Tìm kiếm theo tiêu đề
    public List<Video> searchByTitle(String keyword) {
        return em.createQuery(
                "SELECT v FROM Video v WHERE v.title LIKE :kw", Video.class)
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    // Lấy danh sách video đang active
    public List<Video> findActive() {
        return em.createQuery("SELECT v FROM Video v WHERE v.active = true", Video.class)
                 .getResultList();
    }

    // Lấy top N video theo views
    public List<Video> findTopViews(int limit) {
        return em.createQuery("SELECT v FROM Video v ORDER BY v.views DESC", Video.class)
                 .setMaxResults(limit)
                 .getResultList();
    }
}