package si.fri.rso.uniborrow.reviews.api.v1.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import si.fri.rso.uniborrow.reviews.lib.ItemReview;
import si.fri.rso.uniborrow.reviews.services.beans.ItemReviewBean;
import si.fri.rso.uniborrow.reviews.services.clients.UniborrowItemApi;
import si.fri.rso.uniborrow.reviews.services.clients.UniborrowUserApi;
import si.fri.rso.uniborrow.reviews.services.dtos.UniborrowItemRequest;
import si.fri.rso.uniborrow.reviews.services.dtos.UniborrowUserRequest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemReviewResource {

    private final Logger log = Logger.getLogger(ItemReviewResource.class.getSimpleName());

    @Inject
    private ItemReviewBean itemReviewBean;

    @Inject
    @DiscoverService(value = "uniborrow-users-service", version = "1.0.0", environment = "dev")
    private Optional<URL> usersServiceUrl;

    private UniborrowUserApi uniborrowUserApi;

    @Inject
    @DiscoverService(value = "items-service", version = "1.0.0", environment = "dev")
    private Optional<URL> itemsServiceUrl;

    private UniborrowItemApi uniborrowItemApi;

    @PostConstruct
    private void init() {
        if (usersServiceUrl != null && usersServiceUrl.isPresent()) {
            uniborrowUserApi = RestClientBuilder
                    .newBuilder()
                    .baseUrl(usersServiceUrl.get())
                    .build(UniborrowUserApi.class);
        }
        if (itemsServiceUrl != null && itemsServiceUrl.isPresent()) {
            uniborrowItemApi = RestClientBuilder
                    .newBuilder()
                    .baseUrl(itemsServiceUrl.get())
                    .build(UniborrowItemApi.class);
        }
    }

    @GET
    @Timed(name = "get_item_reviews_time")
    public Response getItemReviews(
            @QueryParam("userId") Integer userId,
            @QueryParam("itemId") Integer itemId
    ) {
        List<ItemReview> results;
        if (userId != null) {
            results = itemReviewBean.getItemReviewsByUser(userId);
        } else if (itemId != null) {
            results = itemReviewBean.getItemReviewsForItem(itemId);
        } else {
            results = itemReviewBean.getAllItemReviews();
        }

        return Response.status(Response.Status.OK).entity(results).build();
    }

    @GET
    @Path("/{itemReviewId}")
    public Response getItemReview(@PathParam("itemReviewId") Integer itemReviewId) {
        ItemReview itemReview = itemReviewBean.getItemReview(itemReviewId);
        if (itemReview == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(itemReview).build();
    }

    @POST
    @Counted(name = "num_created_item_reviews")
    public Response createItemReview(ItemReview itemReview) {
        if (itemReview.getItemId() == null || itemReview.getUserReviewerId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UniborrowUserRequest user = uniborrowUserApi.getById(itemReview.getUserReviewerId());
        UniborrowItemRequest item = uniborrowItemApi.getById(itemReview.getItemId());
        if (user == null || item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            ItemReview created = itemReviewBean.createItemReview(itemReview);
            return (created != null)
                    ? Response.status(Response.Status.CREATED).entity(created).build()
                    : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{itemReviewId}")
    @Counted(name = "num_deleted_item_reviews")
    public Response deleteItemReview(@PathParam("itemReviewId") Integer itemReviewId) {
        try {
            boolean success = itemReviewBean.deleteItemReview(itemReviewId);
            return success
                    ? Response.status(Response.Status.NO_CONTENT).build()
                    : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
