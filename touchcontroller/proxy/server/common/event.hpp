#pragma once
#include <optional>

#include "protocol.hpp"

namespace touchcontroller {
namespace event {

void push_event(const protocol::ProxyMessage& message);
std::optional<std::vector<uint8_t>> poll_event();

}  // namespace event
}  // namespace touchcontroller
