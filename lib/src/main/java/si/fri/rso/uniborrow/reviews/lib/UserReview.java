package si.fri.rso.uniborrow.reviews.lib;

public class UserReview {

    private Integer userReviewId;
    private Integer userId;
    private Integer userReviewerId;
    private String message;
    private Integer stars;

    public Integer getUserReviewId() {
        return userReviewId;
    }

    public void setUserReviewId(Integer userReviewId) {
        this.userReviewId = userReviewId;
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
