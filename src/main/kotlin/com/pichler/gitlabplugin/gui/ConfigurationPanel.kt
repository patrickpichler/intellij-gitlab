package com.pichler.gitlabplugin.gui

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.util.ui.components.BorderLayoutPanel
import com.pichler.gitlabplugin.GitlabConfigurationStateService.GitlabConfigurationState
import com.pichler.gitlabplugin.GitlabConfigurationStateService.ProjectState
import com.pichler.gitlabplugin.api.buildGitlabAPI
import org.funktionale.option.Option
import org.funktionale.option.getOrElse
import org.funktionale.option.toOption
import org.funktionale.tries.Try
import java.awt.BorderLayout
import java.util.*
import javax.swing.*
import com.pichler.gitlabplugin.model.Project as GitlabProject

class ConfigurationPanel(
        private var project: Project,
        private var state: GitlabConfigurationState
) {
    val configPane: JPanel
    private val gitlabTokenField: JTextField = JTextField()
    private val gitlabURLField: JTextField = JTextField()
    private val projectSelection: SelectionComponent<ProjectState> = SelectionComponent(ProjectState::name,
            {
                Try {
                    val api = buildGitlabAPI(gitlabURLField.text, gitlabTokenField.text)

                    object : Dialog<Option<ProjectState>> {
                        override fun show(): Option<ProjectState> =
                                ChooseProjectDialog(project, api).show()
                                        .map { ProjectState(it.id, it.nameWithNamespace) }
                    }
                }

            })

    init {
        this.configPane = panel {
            row("Gitlab Token:") {
                gitlabTokenField(CCFlags.growX, CCFlags.pushX)
            }

            row("Gitlab URI:") {
                gitlabURLField(CCFlags.growX, CCFlags.pushX)
            }

            row("Project:") {
                projectSelection(CCFlags.growX, CCFlags.pushX)
            }
        }
    }

    fun isModified(): Boolean =
            !Objects.equals(gitlabTokenField.text, state.token) ||
                    !Objects.equals(gitlabURLField.text, state.gitlabURL) ||
                    !Objects.equals(projectSelection.selected, Option.Some(state.projectState))

    fun toState(): GitlabConfigurationState {
        when (projectSelection.selected) {
            Option.None -> throw ConfigurationException("No project selected!")
        }

        return GitlabConfigurationState(gitlabURLField.text, gitlabTokenField.text, projectSelection.selected.get())
    }

    fun reset(state: GitlabConfigurationState) {
        this.state = state
        gitlabTokenField.text = this.state.token
        gitlabURLField.text = this.state.gitlabURL

        val projectState = this.state.projectState.toOption()

        when (projectState) {
            is Option.None -> projectSelection.clear()
            is Option.Some -> projectSelection.updateSelected(projectState)
        }
    }
}

interface Dialog<T> {
    fun show(): T
}

class SelectionComponent<T>(
        private val transformer: (T) -> String,
        private val dialogProvider: () -> Try<Dialog<Option<T>>>
) : BorderLayoutPanel(2, 0) {
    private val textField: JTextField = JTextField().apply {
        isEditable = false
    }

    private val button: JButton = JButton("...")

    var selected: Option<T> = Option.None
        private set

    init {
        button.addActionListener {
            dialogProvider()
                    .fold({ dialog -> updateSelected(dialog.show()) },
                            { e ->
                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(button), e.message,
                                        "Error", JOptionPane.ERROR_MESSAGE)
                            })

        }

        add(textField, BorderLayout.CENTER)
        add(button, BorderLayout.EAST)
    }

    fun clear() {
        selected = Option.None
        textField.text = ""
    }

    fun updateSelected(selected: Option<T>) {
        if (selected.isEmpty())
            return

        this.selected = selected

        val text: String = selected
                .map(transformer)
                .getOrElse { "" }

        textField.text = text
    }

}