package si.fri.rso.uniborrow.reviews.services.beans;

import si.fri.rso.uniborrow.reviews.lib.HelperFunctions;
import si.fri.rso.uniborrow.reviews.lib.UserReview;
import si.fri.rso.uniborrow.reviews.models.models.converters.UserReviewConverter;
import si.fri.rso.uniborrow.reviews.models.models.entities.UserReviewEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class UserReviewBean {

    private final Logger log = Logger.getLogger(UserReviewBean.class.getSimpleName());

    @Inject
    private EntityManager em;

    public List<UserReview> getAllUserReviews() {
        TypedQuery<UserReviewEntity> query = em.createNamedQuery("UserReviewEntity.getAll", UserReviewEntity.class);
        List<UserReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(UserReviewConverter::toDto).collect(Collectors.toList());
    }

    public List<UserReview> getUserReviewsForUser(Integer userId) {
        TypedQuery<UserReviewEntity> query = em.createNamedQuery("UserReviewEntity.getForUser", UserReviewEntity.class)
                .setParameter("userId", userId);
        List<UserReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(UserReviewConverter::toDto).collect(Collectors.toList());
    }

    public List<UserReview> getUserReviewsByUser(Integer userId) {
        TypedQuery<UserReviewEntity> query = em.createNamedQuery("UserReviewEntity.getByUser", UserReviewEntity.class)
                .setParameter("userId", userId);
        List<UserReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(UserReviewConverter::toDto).collect(Collectors.toList());
    }

    public UserReview getUserReview(Integer id) {
        UserReviewEntity entity = em.find(UserReviewEntity.class, id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return UserReviewConverter.toDto(entity);
    }

    public UserReview createUserReview(UserReview userReview) throws Exception {
        if (!HelperFunctions.checkStars(userReview.getStars())) {
            throw new Exception("Stars not correct!");
        }
        userReview.setMessage(HelperFunctions.sanitizeString(userReview.getMessage()));
        UserReviewEntity entity = UserReviewConverter.toEntity(userReview);
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
        } catch (Exception e) {
            log.warning(e.getMessage());
            rollbackTransaction();
        }

        if (entity.getId() == null) {
            log.warning("Failed to create a user!");
            return null;
        }
        return UserReviewConverter.toDto(entity);
    }

    public boolean deleteUserReview(Integer id) {
        UserReviewEntity entity = em.find(UserReviewEntity.class, id);
        if (entity == null) {
            throw new NotFoundException();
        }
        try {
            beginTransaction();
            em.remove(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            log.warning("Deleting chat failed! Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private  void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
