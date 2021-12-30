package si.fri.rso.uniborrow.reviews.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("admin-properties")
@ApplicationScoped
public class AdminProperties {

    @ConfigValue(watch = true)
    private Boolean disableItems;

    @ConfigValue(watch = true)
    private Boolean disableUsers;

    public Boolean getDisableItems() {
        return disableItems;
    }

    public void setDisableItems(Boolean disableItems) {
        this.disableItems = disableItems;
    }

    public Boolean getDisableUsers() {
        return disableUsers;
    }

    public void setDisableUsers(Boolean disableUsers) {
        this.disableUsers = disableUsers;
    }
}
