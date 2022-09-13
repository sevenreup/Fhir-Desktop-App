package codeviewer.ui.filetree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeviewer.platform.VerticalScrollbar
import codeviewer.platform.pointerMoveFilter
import codeviewer.util.withoutWidthConstraints

@Composable
fun FileTreeViewTabView() = Surface {
    Row(
        Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Files",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun FileTreeView(model: FileTree, open: (codeviewer.platform.File) -> Unit) = Surface(
    modifier = Modifier.fillMaxSize()
) {
    with(LocalDensity.current) {
        Box {
            val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier.fillMaxSize().withoutWidthConstraints(),
                state = scrollState
            ) {
                items(model.items.size) {
                    FileTreeItemView(14.sp, 14.sp.toDp() * 1.5f, model.items[it], open)
                }
            }

            VerticalScrollbar(
                Modifier.align(Alignment.CenterEnd),
                scrollState
            )
        }
    }
}

@Composable
private fun FileTreeItemView(
    fontSize: TextUnit,
    height: Dp,
    model: FileTree.Item,
    open: (codeviewer.platform.File) -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
//                when (model.type) {
//                    is FileTree.ItemType.Folder -> model.toggleExpanded()
//                }
            }
            .padding(start = 24.dp * model.level)
            .height(height)
            .fillMaxWidth()
    ) {
        val active = remember { mutableStateOf(false) }

        FileItemIcon(Modifier.align(Alignment.CenterVertically), model)
        Text(
            text = model.name,
            color = if (active.value) LocalContentColor.current.copy(alpha = 0.60f) else LocalContentColor.current,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clipToBounds()
                .pointerMoveFilter(
                    onEnter = {
                        active.value = true
                        true
                    },
                    onExit = {
                        active.value = false
                        true
                    },
                    onMove = {
                        true
                    }
                ),
            softWrap = true,
            fontSize = fontSize,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
private fun FileItemIcon(modifier: Modifier, model: FileTree.Item) = Box(modifier.size(24.dp).padding(4.dp)) {
    when (val type = model.type) {
        is FileTree.ItemType.Folder -> when {
            !type.canExpand -> Unit
            type.isExpanded -> Icon(
                Icons.Default.KeyboardArrowDown, contentDescription = null, tint = LocalContentColor.current
            )

            else -> Icon(
                Icons.Default.KeyboardArrowRight, contentDescription = null, tint = LocalContentColor.current
            )
        }

        is FileTree.ItemType.File -> when (type.ext) {
            "kt" -> Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF3E86A0))
            "xml" -> Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFFC19C5F))
            "txt" -> Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "md" -> Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF87939A))
            "gitignore" -> Icon(Icons.Default.BrokenImage, contentDescription = null, tint = Color(0xFF87939A))
            "gradle" -> Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF87939A))
            "kts" -> Icon(Icons.Default.Build, contentDescription = null, tint = Color(0xFF3E86A0))
            "properties" -> Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF62B543))
            "bat" -> Icon(Icons.Default.Launch, contentDescription = null, tint = Color(0xFF87939A))
            else -> Icon(Icons.Default.TextSnippet, contentDescription = null, tint = Color(0xFF87939A))
        }
    }
}