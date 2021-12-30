package si.fri.rso.uniborrow.reviews.models.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "item_reviews")
@NamedQueries(value = {
        @NamedQuery(
                name = "ItemReviewEntity.getAll",
                query = "SELECT i FROM ItemReviewEntity i"
        ),
        @NamedQuery(
                name = "ItemReviewEntity.getForItem",
                query = "SELECT i FROM ItemReviewEntity i WHERE i.itemId = :itemId"
        ),
        @NamedQuery(
                name = "ItemReviewEntity.getByUser",
                query = "SELECT i FROM ItemReviewEntity i WHERE i.userReviewerId = :userId"
        )
})
public class ItemReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "user_reviewer_id")
    private Integer userReviewerId;

    @Column(name = "message")
    private String message;

    @Column(name = "stars")
    private Integer stars;


    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserReviewerId() {
        return userReviewerId;
    }

    public void setUserReviewerId(Integer userReviewerId) {
        this.userReviewerId = userReviewerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
