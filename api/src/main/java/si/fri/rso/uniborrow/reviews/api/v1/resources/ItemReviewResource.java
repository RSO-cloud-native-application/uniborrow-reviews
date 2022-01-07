package si.fri.rso.uniborrow.reviews.api.v1.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
    @Operation(
            description = "Get item reviews based on query parameters. We can get all item reviews, reviews for a specific item or reviews by a specific user.",
            summary = "Get item reviews"
    )
    @APIResponses(
            @APIResponse(
                    responseCode = "200",
                    description = "Array of DTO representing item reviews",
                    content = @Content(schema = @Schema(implementation = ItemReview.class, type = SchemaType.ARRAY))
            )
    )
    public Response getItemReviews(
            @Parameter(
                    description = "User ID",
                    in = ParameterIn.QUERY
            ) @QueryParam("userId") Integer userId,
            @Parameter(
                    description = "Item ID",
                    in = ParameterIn.QUERY
            )
            @QueryParam("itemId") Integer itemId) {
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
    @Operation(description = "Get an item review by its ID", summary = "Get an item review")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "DTO for item review",
                    content = @Content(schema = @Schema(implementation = ItemReview.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Item review not found"
            )
    })
    public Response getItemReview(
            @Parameter(
                    description = "Item Review ID",
                    required = true
            ) @PathParam("itemReviewId") Integer itemReviewId) {
        ItemReview itemReview = itemReviewBean.getItemReview(itemReviewId);
        if (itemReview == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(itemReview).build();
    }

    @POST
    @Counted(name = "num_created_item_reviews")
    @Operation(description = "Create an item review", summary = "Create an item review")
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Successfully added an item review",
                    content = @Content(schema = @Schema(implementation = ItemReview.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Bad request"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Item or user not found"
            )
    })
    public Response createItemReview(
            @RequestBody(
                    description = "DTO for item review",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ItemReview.class))
            ) ItemReview itemReview) {
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
    @Operation(description = "Delete an item review.", summary = "Delete an item review")
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "Successfully deleted an item review"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Item review not found"
            )
    })
    public Response deleteItemReview(
            @Parameter(
                    description = "Item review ID",
                    required = true
            ) @PathParam("itemReviewId") Integer itemReviewId) {
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
