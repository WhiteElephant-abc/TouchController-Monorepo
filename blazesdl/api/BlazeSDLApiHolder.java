package top.fifthlight.blazesdl.api;

import org.jspecify.annotations.Nullable;

import java.util.ServiceLoader;

class BlazeSDLApiHolder {
    static final @Nullable BlazeSDLAPI INSTANCE = ServiceLoader.load(BlazeSDLAPI.class).findFirst().orElse(null);
}
