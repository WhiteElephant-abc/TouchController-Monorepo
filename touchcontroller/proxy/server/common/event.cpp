#include "event.hpp"

#include <deque>
#include <mutex>

#include "touchcontroller/proxy/server/common/protocol.hpp"

static std::mutex g_event_queue_mutex;
static std::deque<std::vector<uint8_t>> g_event_queue;

namespace touchcontroller {
namespace event {

void push_event(const protocol::ProxyMessage& message) {
    std::lock_guard<std::mutex> lock(g_event_queue_mutex);

    std::vector<uint8_t> msg_buffer = protocol::serialize_event(message);
    g_event_queue.push_back(msg_buffer);
}

std::optional<std::vector<uint8_t>> poll_event() {
    std::lock_guard<std::mutex> lock(g_event_queue_mutex);

    if (g_event_queue.empty()) {
        return std::nullopt;
    }

    std::vector<uint8_t> msg_buffer = g_event_queue.front();
    g_event_queue.pop_front();
    return msg_buffer;
}

}  // namespace event
}  // namespace touchcontroller