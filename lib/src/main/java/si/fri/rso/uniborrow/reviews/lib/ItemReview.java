package si.fri.rso.uniborrow.reviews.lib;

public class ItemReview {

    private Integer itemReviewId;
    private Integer itemId;
    private Integer userReviewerId;
    private String message;
    private Integer stars;

    public Integer getItemReviewId() {
        return itemReviewId;
    }

    public void setItemReviewId(Integer itemReviewId) {
        this.itemReviewId = itemReviewId;
    }

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
}
