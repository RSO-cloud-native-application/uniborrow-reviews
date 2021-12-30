package si.fri.rso.uniborrow.reviews.models.models.converters;

import si.fri.rso.uniborrow.reviews.lib.UserReview;
import si.fri.rso.uniborrow.reviews.models.models.entities.UserReviewEntity;

public class UserReviewConverter {

    public static UserReview toDto(UserReviewEntity entity) {
        var dto = new UserReview();
        dto.setUserId(entity.getUserId());
        dto.setMessage(entity.getMessage());
        dto.setUserReviewId(entity.getId());
        dto.setUserReviewerId(entity.getUserReviewerId());
        dto.setStars(entity.getStars());

        return dto;
    }

    public static UserReviewEntity toEntity(UserReview dto) {
        var entity = new UserReviewEntity();
        entity.setUserReviewerId(dto.getUserReviewerId());
        entity.setMessage(dto.getMessage());
        entity.setStars(dto.getStars());
        entity.setUserId(dto.getUserId());

        return entity;
    }
}
