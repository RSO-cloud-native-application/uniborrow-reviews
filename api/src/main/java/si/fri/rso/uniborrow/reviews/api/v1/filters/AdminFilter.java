package si.fri.rso.uniborrow.reviews.api.v1.filters;

import si.fri.rso.uniborrow.reviews.services.config.AdminProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
@ApplicationScoped
public class AdminFilter implements ContainerRequestFilter {

    private final static Logger log = Logger.getLogger(AdminFilter.class.getSimpleName());

    @Inject
    private AdminProperties adminProperties;

    @Override
    public void filter(ContainerRequestContext ctx) {
        if (ctx.getUriInfo().getPath().equals("users") && adminProperties.getDisableUsers()) {
            log.info("Users are currently disabled!");
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{ \"message\": \"User reviews are currently disabled\" }")
                    .build());
        } else if (ctx.getUriInfo().getPath().equals("items") && adminProperties.getDisableItems()) {
            log.info("Items are currently disabled!");
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity("{ \"message\": \"Item reviews are currently disabled\" }")
                    .build());
        }
    }
}
