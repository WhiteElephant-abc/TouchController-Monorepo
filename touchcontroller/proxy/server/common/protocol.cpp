#include "protocol.hpp"

#include <algorithm>
#include <iostream>

namespace touchcontroller {
namespace protocol {

class ByteReader {
    const uint8_t* ptr;
    const uint8_t* end;

   public:
    ByteReader(const std::vector<uint8_t>& data) : ptr(data.data()), end(data.data() + data.size()) {}

    template <typename T>
    T read() {
        if (ptr + sizeof(T) > end) {
            throw std::runtime_error("Not enough data");
        }

        T val;
        std::memcpy(&val, ptr, sizeof(T));
        ptr += sizeof(T);

        return decode(val);
    }

    std::string read_string(uint32_t len) {
        if (ptr + len > end) throw std::runtime_error("String out of bounds");
        std::string result(ptr, ptr + len);
        ptr += len;
        return result;
    }

    void read_bytes(uint8_t* buffer, uint32_t len) {
        if (ptr + len > end) throw std::runtime_error("Bytes out of bounds");
        std::memcpy(buffer, ptr, len);
        ptr += len;
    }

    bool has_data() const { return ptr < end; }

   private:
    static uint32_t decode(uint32_t v) { return ntohl(v); }
    static int32_t decode(int32_t v) { return (int32_t)ntohl((uint32_t)v); }
    static uint8_t decode(uint8_t v) { return v; }
    static float decode(float v) { return ntohf(v); }
};

class ByteWriter {
    std::vector<uint8_t>& buffer;

   public:
    ByteWriter(std::vector<uint8_t>& buffer) : buffer(buffer) {}

    template <typename T>
    void write(const T& value) {
        T encoded = encode(value);
        const uint8_t* p = reinterpret_cast<const uint8_t*>(&encoded);
        buffer.insert(buffer.end(), p, p + sizeof(T));
    }

    void write_string(const std::string& str) { buffer.insert(buffer.end(), str.begin(), str.end()); }

    void write_bytes(const uint8_t* data, uint32_t len) { buffer.insert(buffer.end(), data, data + len); }

    void write_bytes(const uint8_t* data, uint8_t len) { buffer.insert(buffer.end(), data, data + len); }

   private:
    static uint32_t encode(uint32_t v) { return htonl(v); }
    static int32_t encode(int32_t v) { return static_cast<int32_t>(htonl(static_cast<uint32_t>(v))); }
    static uint8_t encode(uint8_t v) { return v; }
    static uint8_t encode(bool v) { return v ? 1 : 0; }
    static float encode(float v) { return htonf(v); }
};

std::vector<uint8_t> serialize_event(const ProxyMessage& message) {
    std::vector<uint8_t> result;
    ByteWriter writer(result);

    std::visit(
        [&writer](auto&& data) {
            using T = std::decay_t<decltype(data)>;

            if constexpr (std::is_same_v<T, AddData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Add));
                writer.write(data.index);
                writer.write(data.x);
                writer.write(data.y);
            } else if constexpr (std::is_same_v<T, RemoveData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Remove));
                writer.write(data.index);
            } else if constexpr (std::is_same_v<T, ClearData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Clear));
            } else if constexpr (std::is_same_v<T, VibrateData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Vibrate));
                writer.write(static_cast<int32_t>(data.kind));
            } else if constexpr (std::is_same_v<T, CapabilityData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Capability));
                uint8_t str_length = static_cast<uint8_t>(std::min(data.name.length(), (size_t)UINT8_MAX));
                writer.write(str_length);
                writer.write_bytes(reinterpret_cast<const uint8_t*>(data.name.data()), str_length);
                writer.write(data.enabled);
            } else if constexpr (std::is_same_v<T, LargeData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Large));
                writer.write(data.length);
                writer.write(data.end);
                writer.write_bytes(data.payload, data.length);
            } else if constexpr (std::is_same_v<T, InputStatusData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::InputStatus));
                writer.write(data.has_status);
                if (data.has_status) {
                    writer.write(static_cast<uint32_t>(data.text.size()));
                    writer.write_string(data.text);
                    writer.write(static_cast<int32_t>(data.composition_start));
                    writer.write(static_cast<int32_t>(data.composition_length));
                    writer.write(static_cast<int32_t>(data.selection_start));
                    writer.write(static_cast<int32_t>(data.selection_length));
                    writer.write(data.selection_left);
                }
            } else if constexpr (std::is_same_v<T, KeyboardShowData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::KeyboardShow));
                writer.write(data.show);
            } else if constexpr (std::is_same_v<T, InputCursorData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::InputCursor));
                writer.write(data.has_cursor_rect);
                if (data.has_cursor_rect) {
                    writer.write(data.left);
                    writer.write(data.top);
                    writer.write(data.width);
                    writer.write(data.height);
                }
            } else if constexpr (std::is_same_v<T, InitializeData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::Initialize));
            } else if constexpr (std::is_same_v<T, InputAreaData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::InputArea));
                writer.write(data.has_area_rect);
                if (data.has_area_rect) {
                    writer.write(data.left);
                    writer.write(data.top);
                    writer.write(data.width);
                    writer.write(data.height);
                }
            } else if constexpr (std::is_same_v<T, MoveViewData>) {
                writer.write(static_cast<uint32_t>(ProxyMessageType::MoveView));
                writer.write(data.screen_based);
                writer.write(data.delta_pitch);
                writer.write(data.delta_yaw);
            }
        },
        message);
    return result;
}

std::optional<ProxyMessage> deserialize_event(const std::vector<uint8_t>& data) {
    ByteReader reader(data);
    try {
        ProxyMessageType type = static_cast<ProxyMessageType>(reader.read<uint32_t>());
        switch (type) {
            case Add: {
                AddData data;
                data.index = reader.read<uint32_t>();
                data.x = reader.read<float>();
                data.y = reader.read<float>();
                return std::optional(data);
            }
            case Remove: {
                RemoveData data;
                data.index = reader.read<uint32_t>();
                return std::optional(data);
            }
            case Clear: {
                ClearData data;
                return std::optional(data);
            }
            case Vibrate: {
                VibrateData data;
                data.kind = static_cast<VibrateKind>(reader.read<int32_t>());
                return std::optional(data);
            }
            case Capability: {
                CapabilityData data;
                uint8_t name_length = reader.read<uint8_t>();
                data.name = reader.read_string(name_length);
                data.enabled = reader.read<uint8_t>() != 0;
                return std::optional(data);
            }
            case Large: {
                LargeData data;
                data.length = reader.read<uint8_t>();
                data.end = reader.read<uint8_t>() != 0;
                reader.read_bytes(data.payload, data.length);
                return std::optional(data);
            }
            case InputStatus: {
                InputStatusData data;
                data.has_status = reader.read<uint8_t>() != 0;
                if (!data.has_status) {
                    return std::optional(data);
                }
                uint32_t text_length = reader.read<uint32_t>();
                data.text = reader.read_string(text_length);
                data.composition_start = reader.read<int32_t>();
                data.composition_length = reader.read<int32_t>();
                data.selection_start = reader.read<int32_t>();
                data.selection_length = reader.read<int32_t>();
                data.selection_left = reader.read<uint8_t>() != 0;
                return std::optional(data);
            }
            case KeyboardShow: {
                KeyboardShowData data;
                data.show = reader.read<uint8_t>() != 0;
                return std::optional(data);
            }
            case InputCursor: {
                InputCursorData data;
                data.has_cursor_rect = reader.read<uint8_t>() != 0;
                if (data.has_cursor_rect) {
                    data.left = reader.read<float>();
                    data.top = reader.read<float>();
                    data.width = reader.read<float>();
                    data.height = reader.read<float>();
                }
                return std::optional(data);
            }
            case Initialize: {
                InitializeData data;
                return std::optional(data);
            }
            case InputArea: {
                InputAreaData data;
                data.has_area_rect = reader.read<uint8_t>() != 0;
                if (data.has_area_rect) {
                    data.left = reader.read<float>();
                    data.top = reader.read<float>();
                    data.width = reader.read<float>();
                    data.height = reader.read<float>();
                }
                return std::optional(data);
            }
            case MoveView: {
                MoveViewData data;
                data.screen_based = reader.read<uint8_t>() != 0;
                data.delta_pitch = reader.read<float>();
                data.delta_yaw = reader.read<float>();
                return std::optional(data);
            }
            default:
                return std::nullopt;
        }
    } catch (const std::runtime_error& err) {
        std::cerr << err.what() << std::endl;
        return std::optional<ProxyMessage>();
    }
}

}  // namespace protocol
}  // namespace touchcontroller