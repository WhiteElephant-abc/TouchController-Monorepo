package top.fifthlight.blazesdl.api;

import org.lwjgl.sdl.SDL_Event;

public interface BlazeSDLEventHandler {
    int getPriority();

    boolean handleEvent(SDL_Event event);
}
