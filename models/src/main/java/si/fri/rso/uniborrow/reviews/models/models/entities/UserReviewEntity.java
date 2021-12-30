package si.fri.rso.uniborrow.reviews.models.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_reviews")
@NamedQueries(value = {
        @NamedQuery(
                name = "UserReviewEntity.getAll",
                query = "SELECT u FROM UserReviewEntity u"
        ),
        @NamedQuery(
                name = "UserReviewEntity.getForUser",
                query = "SELECT u FROM UserReviewEntity u WHERE u.userId = :userId"
        ),
        @NamedQuery(
                name = "UserReviewEntity.getByUser",
                query = "SELECT u FROM UserReviewEntity u WHERE u.userReviewerId = :userId"
        )
})
public class UserReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_reviewer_id")
    private Integer userReviewerId;

    @Column(name = "message")
    private String message;

    @Column(name = "stars")
    private Integer stars;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
