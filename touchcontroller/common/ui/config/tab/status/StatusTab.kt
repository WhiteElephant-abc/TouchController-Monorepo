package top.fifthlight.touchcontroller.common.ui.config.tab.status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import top.fifthlight.combine.data.Text
import top.fifthlight.combine.data.TextColor
import top.fifthlight.combine.layout.Arrangement
import top.fifthlight.combine.modifier.Modifier
import top.fifthlight.combine.modifier.drawing.border
import top.fifthlight.combine.modifier.placement.fillMaxSize
import top.fifthlight.combine.modifier.placement.fillMaxWidth
import top.fifthlight.combine.modifier.placement.padding
import top.fifthlight.combine.modifier.scroll.verticalScroll
import top.fifthlight.combine.widget.layout.Column
import top.fifthlight.combine.widget.ui.Text
import top.fifthlight.touchcontroller.assets.Texts
import top.fifthlight.touchcontroller.common.ui.config.tab.Tab
import top.fifthlight.touchcontroller.common.ui.config.tab.TabOptions
import top.fifthlight.touchcontroller.common.ui.config.tab.status.model.StatusTabModel
import top.fifthlight.touchcontroller.common.ui.theme.LocalTouchControllerTheme

object StatusTab : Tab() {
    override val options = TabOptions(
        titleId = Texts.SCREEN_CONFIG_STATUS_TITLE,
        group = null,
        index = 1,
    )

    @Composable
    override fun Content() {
        val tabModel = remember { StatusTabModel() }
        val uiState by tabModel.uiState.collectAsState()
        Column(
            modifier = Modifier
                .padding(8)
                .verticalScroll(background = LocalTouchControllerTheme.current.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8),
        ) {
            uiState.currentPlatform?.let {
                Text(Text.format(Texts.SCREEN_CONFIG_STATUS_PLATFORM, it))
            } ?: run {
                Text(
                    Text.format(
                        Texts.SCREEN_CONFIG_STATUS_PLATFORM,
                        Text.translatable(Texts.SCREEN_CONFIG_STATUS_PLATFORM_UNAVAILABLE)
                    ).color(TextColor.RED)
                )
            }

            uiState.warningMessage?.let { systemInfo ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(LocalTouchControllerTheme.current.listButtonDrawablesUnchecked.normal),
                    verticalArrangement = Arrangement.spacedBy(8),
                ) {
                    uiState.warningMessage?.let { Text(it) }
                }
            }

            uiState.systemInfo?.let { systemInfo ->
                Text(Text.translatable(Texts.SCREEN_CONFIG_STATUS_DEBUG_INFO_TITLE))

                Text(Text.format(Texts.SCREEN_CONFIG_STATUS_DEBUG_INFO_SYSTEM, systemInfo.system))
                Text(Text.format(Texts.SCREEN_CONFIG_STATUS_DEBUG_INFO_ARCHITECTURE, systemInfo.arch))
            }
        }
    }
}
