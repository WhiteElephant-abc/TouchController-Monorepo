#pragma once
#include <cstdint>
#include <cstring>
#include <optional>
#include <string>
#include <variant>
#include <vector>

#if defined(_WIN32)
#include <winsock2.h>
#else
#include <arpa/inet.h>
#endif

#if defined(__MINGW32__) || not(defined(_WIN32)) || (defined(WINVER) && WINVER < 0x0602)
static float htonf(float hostfloat) {
    static_assert(sizeof(float) == sizeof(uint32_t));
    uint32_t hostlong;
    std::memcpy(&hostlong, &hostfloat, sizeof(float));
    uint32_t netlong = htonl(hostlong);
    float netfloat;
    std::memcpy(&netfloat, &netlong, sizeof(float));
    return netfloat;
}
static float ntohf(float netfloat) {
    static_assert(sizeof(float) == sizeof(uint32_t));
    uint32_t netlong;
    std::memcpy(&netlong, &netfloat, sizeof(float));
    uint32_t hostlong = ntohl(netlong);
    float hostfloat;
    std::memcpy(&hostfloat, &hostlong, sizeof(float));
    return hostfloat;
}
#endif

namespace touchcontroller {
namespace protocol {

enum VibrateKind {
    UNKNOWN = -1,
    BLOCK_BROKEN = 0,
};

struct AddData {
    uint32_t index;
    float x;
    float y;
};

struct RemoveData {
    uint32_t index;
};

struct ClearData {};

struct VibrateData {
    VibrateKind kind;
};

struct CapabilityData {
    std::string name;
    bool enabled;
};

struct LargeData {
    uint8_t length;
    bool end;
    uint8_t payload[240];
};

struct InputStatusData {
    bool has_status;
    std::string text;
    int composition_start;
    int composition_length;
    int selection_start;
    int selection_length;
    bool selection_left;
};

struct KeyboardShowData {
    bool show;
};

struct InputCursorData {
    bool has_cursor_rect;
    float left;
    float top;
    float width;
    float height;
};

struct InitializeData {};

struct InputAreaData {
    bool has_area_rect;
    float left;
    float top;
    float width;
    float height;
};

struct MoveViewData {
    bool screen_based;
    float delta_pitch;
    float delta_yaw;
};

using ProxyMessage =
    std::variant<AddData, RemoveData, ClearData, VibrateData, CapabilityData, LargeData, InputStatusData,
                 KeyboardShowData, InputCursorData, InitializeData, InputAreaData, MoveViewData>;

enum ProxyMessageType : uint32_t {
    Add = 1,
    Remove = 2,
    Clear = 3,
    Vibrate = 4,
    Capability = 5,
    Large = 6,
    InputStatus = 7,
    KeyboardShow = 8,
    InputCursor = 9,
    Initialize = 10,
    InputArea = 11,
    MoveView = 12,
};

std::vector<uint8_t> serialize_event(const ProxyMessage& message);
std::optional<ProxyMessage> deserialize_event(const std::vector<uint8_t>& data);

}  // namespace protocol
}  // namespace touchcontroller