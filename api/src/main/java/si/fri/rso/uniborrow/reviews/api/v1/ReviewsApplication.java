package si.fri.rso.uniborrow.reviews.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Uniborrow Reviews API",
                version = "v1",
                contact = @Contact(email = "lh5107@student.uni-lj.si"),
                license = @License(name = "dev"),
                description = "API for managing reviews for Uniborrow application."
        ),
        servers = @Server(url = "http://35.223.79.242/uniborrow-reviews/")
)
@ApplicationPath("/v1")
@RegisterService(value = "uniborrow-reviews-service", environment = "dev", version = "1.0.0")
public class ReviewsApplication extends Application {
}
