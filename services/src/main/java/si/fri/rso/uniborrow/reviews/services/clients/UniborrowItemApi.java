package si.fri.rso.uniborrow.reviews.services.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.fri.rso.uniborrow.reviews.services.dtos.UniborrowItemRequest;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(configKey = "uniborrow-items-api")
@Path("/v1/items")
@Dependent
public interface UniborrowItemApi {

    @GET
    @Path("/{id}")
    UniborrowItemRequest getById(@PathParam("id") Integer id);
}
