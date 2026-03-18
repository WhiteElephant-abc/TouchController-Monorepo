# TouchController 的静默转头机制

## 起源

<!-- ANCHOR: p1 -->
这个机制起源于 [#148](https://github.com/TouchController/TouchController/discussions/148) 和 [#153](https://github.com/TouchController/TouchController/discussions/153)，其主要内容是 TouchController 无法在正确的位置进行某些交互，其典型例子便是使用水桶放水。

众所周知，只要不开启分离控制，那么您点击屏幕的位置便是实际交互的位置。但是在手持水桶点击屏幕时，水却直接放置在了准星所对准的位置，就像基岩版中的末影珍珠一样。这是因为 Minecraft 的服务端（单人游戏会使用内置服务端）没有使用 TouchController 提供的目标位置。

为什么不使用呢？

我们先讲一下 Minecraft 寻找目标的机制，Minecraft 通常是在客户端处理这件事情的，所以 TouchController 可以直接通过修改客户端来给服务端提供正确的目标位置。但是这个默认机制会忽略掉流体，因此这种机制下流体永远不能成为目标。那我们如何装水、如何放船呢？

为此，Minecraft 为某些物品启用了一个特殊的机制，这个机制与默认机制不同，能正确地判定目标。但是这个机制会同时在客户端和服务端运行，此时服务端不再使用来自客户端的目标，而是根据玩家的实际朝向来确认目标。

为了让水桶等物品的交互方向正确，TouchController 加入了静默转头的机制（具体机制见下一节），并且可以在“[需要修正使用方向的物品](../gui/config-screen/tab/item.md)”中配置。通过适当配置，您甚至可以完成一些基岩版中做不到的操作。在基岩版中，使用末影珍珠会直接向准星方向扔出，即使未启用分离控制。但如果您在 TouchController 中将末影珍珠加入静默转头物品表，您就可以实现“指哪打哪”的投掷效果。
<!-- ANCHOR_END: p1 -->

## 机制

<!-- ANCHOR: p2 -->
未开启分离控制时，手持“[需要修正使用方向的物品](../gui/config-screen/tab/item.md)”中的物品，点击屏幕进行交互后 TouchController 会进行转头，使准星对准点击的方向 -> 交互 -> 然后再恢复原位。这一切都在一帧之内完成，这样您的客户端或服务器内其他玩家的客户端都不会渲染您的转头动作，故称为“静默转头”。
<!-- ANCHOR_END: p2 -->
