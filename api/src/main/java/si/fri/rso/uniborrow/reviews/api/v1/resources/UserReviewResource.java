package si.fri.rso.uniborrow.reviews.api.v1.resources;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import si.fri.rso.uniborrow.reviews.lib.UserReview;
import si.fri.rso.uniborrow.reviews.services.beans.UserReviewBean;
import si.fri.rso.uniborrow.reviews.services.clients.UniborrowItemApi;
import si.fri.rso.uniborrow.reviews.services.clients.UniborrowUserApi;
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
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserReviewResource {

    private final Logger log = Logger.getLogger(UserReviewResource.class.getSimpleName());

    @Inject
    private UserReviewBean userReviewBean;

    @Inject
    @DiscoverService(value = "uniborrow-users-service", version = "1.0.0", environment = "dev")
    private Optional<URL> usersServiceUrl;

    private UniborrowUserApi uniborrowUserApi;

    @PostConstruct
    private void init() {
        if (usersServiceUrl != null && usersServiceUrl.isPresent()) {
            uniborrowUserApi = RestClientBuilder
                    .newBuilder()
                    .baseUrl(usersServiceUrl.get())
                    .build(UniborrowUserApi.class);
        }
    }

    @GET
    public Response getUserReviews(
            @QueryParam("forUserId") Integer forUserId,
            @QueryParam("byUserId") Integer byUserId
    ) {
        List<UserReview> results;
        if (forUserId != null) {
            results = userReviewBean.getUserReviewsForUser(forUserId);
        } else if (byUserId != null) {
            results = userReviewBean.getUserReviewsByUser(byUserId);
        } else {
            results = userReviewBean.getAllUserReviews();
        }

        return Response.status(Response.Status.OK).entity(results).build();
    }

    @GET
    @Path("/{userReviewId}")
    public Response getItemReview(@PathParam("userReviewId") Integer userReviewId) {
        UserReview userReview = userReviewBean.getUserReview(userReviewId);
        if (userReview == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(userReview).build();
    }

    @POST
    public Response createItemReview(UserReview userReview) {
        if (userReview == null || userReview.getUserReviewId() == null || userReview.getUserId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        UniborrowUserRequest user = uniborrowUserApi.getById(userReview.getUserId());
        UniborrowUserRequest userReviewer = uniborrowUserApi.getById(userReview.getUserReviewerId());
        if (user == null || userReviewer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        try {
            UserReview created = userReviewBean.createUserReview(userReview);
            return (created != null)
                    ? Response.status(Response.Status.CREATED).entity(created).build()
                    : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{userReviewId}")
    public Response deleteItemReview(@PathParam("userReviewId") Integer userReviewId) {
        try {
            boolean success = userReviewBean.deleteUserReview(userReviewId);
            return success
                    ? Response.status(Response.Status.NO_CONTENT).build()
                    : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
