package com.pichler.gitlabplugin.gui

import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.pichler.gitlabplugin.GitlabConfigurationStateService.GitlabConfigurationState
import java.util.*
import javax.swing.JPanel
import javax.swing.JTextField

class ConfigurationPanel(private var state: GitlabConfigurationState) {
    val configPane: JPanel
    private val gitlabTokeField: JTextField = JTextField()
    private val gitlabURLField: JTextField = JTextField()

    init {
        this.configPane = panel {
            row("Gitlab Token:") {
                gitlabTokeField(CCFlags.growX, CCFlags.pushX)
            }

            row("Gitlab URI:") {
                gitlabURLField(CCFlags.growX, CCFlags.pushX)
            }
        }
    }

    fun isModified(): Boolean = !Objects.equals(gitlabTokeField.text, state.token) ||
            !Objects.equals(gitlabTokeField.text, state.gitlabURL)

    fun toState(): GitlabConfigurationState = GitlabConfigurationState(gitlabURLField.text, gitlabTokeField.text)

    fun reset(state: GitlabConfigurationState) {
        this.state = state
        gitlabTokeField.text = this.state.token
        gitlabURLField.text = this.state.gitlabURL
    }
}