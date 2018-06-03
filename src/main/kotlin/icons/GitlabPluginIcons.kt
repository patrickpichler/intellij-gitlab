package icons

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object GitlabPluginIcons {
    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, GitlabPluginIcons::class.java)
    }

    val Star = load("/icons/star.png")
}