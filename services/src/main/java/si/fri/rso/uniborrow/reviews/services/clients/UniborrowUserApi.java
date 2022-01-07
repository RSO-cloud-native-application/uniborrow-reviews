package si.fri.rso.uniborrow.reviews.services.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.fri.rso.uniborrow.reviews.services.dtos.UniborrowUserRequest;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient(configKey = "uniborrow-users-api")
@Path("/v1/users")
@Dependent
public interface UniborrowUserApi {

    @GET
    @Path("/{id}")
    UniborrowUserRequest getById(@PathParam("id") Integer id);
}
