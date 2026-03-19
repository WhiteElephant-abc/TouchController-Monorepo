package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface BlazeSDLAPI {
    @Nullable
    static BlazeSDLAPI getInstance() {
        return BlazeSDLApiHolder.INSTANCE;
    }

    void registerEventHandler(@NonNull BlazeSDLEventHandler handler);
}
