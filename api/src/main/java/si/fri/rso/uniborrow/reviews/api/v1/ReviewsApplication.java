package si.fri.rso.uniborrow.reviews.api.v1;

import com.kumuluz.ee.discovery.annotations.RegisterService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@RegisterService(value = "uniborrow-reviews-service", environment = "dev", version = "1.0.0")
public class ReviewsApplication extends Application {
}
