package si.fri.rso.uniborrow.reviews.models.models.converters;

import si.fri.rso.uniborrow.reviews.lib.ItemReview;
import si.fri.rso.uniborrow.reviews.models.models.entities.ItemReviewEntity;

public class ItemReviewConverter {

    public static ItemReview toDto(ItemReviewEntity entity) {
        var dto = new ItemReview();
        dto.setItemReviewId(entity.getId());
        dto.setItemId(entity.getItemId());
        dto.setMessage(entity.getMessage());
        dto.setStars(entity.getStars());
        dto.setUserReviewerId(entity.getUserReviewerId());

        return dto;
    }

    public static ItemReviewEntity toEntity(ItemReview dto) {
        var entity = new ItemReviewEntity();
        entity.setItemId(dto.getItemId());
        entity.setMessage(dto.getMessage());
        entity.setStars(dto.getStars());
        entity.setUserReviewerId(dto.getUserReviewerId());

        return entity;
    }
}
