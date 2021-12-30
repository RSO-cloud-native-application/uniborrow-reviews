package si.fri.rso.uniborrow.reviews.services.beans;

import si.fri.rso.uniborrow.reviews.lib.HelperFunctions;
import si.fri.rso.uniborrow.reviews.lib.ItemReview;
import si.fri.rso.uniborrow.reviews.models.models.converters.ItemReviewConverter;
import si.fri.rso.uniborrow.reviews.models.models.entities.ItemReviewEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class ItemReviewBean {

    private final Logger log = Logger.getLogger(ItemReviewBean.class.getSimpleName());

    @Inject
    private EntityManager em;

    public List<ItemReview> getAllItemReviews() {
        TypedQuery<ItemReviewEntity> query = em.createNamedQuery("ItemReviewEntity.getAll", ItemReviewEntity.class);
        List<ItemReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(ItemReviewConverter::toDto).collect(Collectors.toList());
    }

    public List<ItemReview> getItemReviewsForItem(Integer itemId) {
        TypedQuery<ItemReviewEntity> query = em.createNamedQuery("ItemReviewEntity.getForItem", ItemReviewEntity.class)
                .setParameter("itemId", itemId);
        List<ItemReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(ItemReviewConverter::toDto).collect(Collectors.toList());
    }

    public List<ItemReview> getItemReviewsByUser(Integer userId) {
        TypedQuery<ItemReviewEntity> query = em.createNamedQuery("ItemReviewEntity.getByUser", ItemReviewEntity.class)
                .setParameter("userId", userId);
        List<ItemReviewEntity> resultList = query.getResultList();
        return resultList.stream().map(ItemReviewConverter::toDto).collect(Collectors.toList());
    }

    public ItemReview getItemReview(Integer id) {
        ItemReviewEntity entity = em.find(ItemReviewEntity.class, id);
        if (entity == null) {
            throw new NotFoundException();
        }
        return ItemReviewConverter.toDto(entity);
    }

    public ItemReview createItemReview(ItemReview itemReview) throws Exception {
        if (!HelperFunctions.checkStars(itemReview.getStars())) {
            throw new Exception("Stars not correct!");
        }
        itemReview.setMessage(HelperFunctions.sanitizeString(itemReview.getMessage()));
        ItemReviewEntity entity = ItemReviewConverter.toEntity(itemReview);
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
        return ItemReviewConverter.toDto(entity);
    }

    public boolean deleteItemReview(Integer id) {
        ItemReviewEntity entity = em.find(ItemReviewEntity.class, id);
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
