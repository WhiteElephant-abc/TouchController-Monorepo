package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.NonNull;
import top.fifthlight.blazesdl.api.impl.BlazeSDLAPIImpl;

public interface BlazeSDLAPI {
    static BlazeSDLAPI getInstance() {
        return BlazeSDLAPIImpl.getInstance();
    }

    void registerEventHandler(@NonNull BlazeSDLEventHandler handler);
}
