package com.pichler.gitlabplugin.gui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.DoubleClickListener
import com.intellij.ui.TableSpeedSearch
import com.intellij.ui.ToggleActionButton
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.layout.CCFlags
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import com.intellij.util.containers.Convertor
import com.pichler.gitlabplugin.api.GitlabAPI
import org.funktionale.option.Option
import org.funktionale.option.optionTry
import org.funktionale.option.toOption
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.ListSelectionModel
import javax.swing.SwingUtilities
import com.pichler.gitlabplugin.model.Project as GitlabProject

class ChooseProjectDialog(
        project: Project,
        private val gitlabAPI: GitlabAPI
) {

    private val EMPTY_MODEL = buildTableModel(emptyList())

    private var projectTableModel: ColumnTableModel<GitlabProject> = EMPTY_MODEL

    private val projectTable: JBTable = buildTable(projectTableModel)

    private val projectChooseDialog: ProjectChooseDialog = buildDialogWrapper(project, projectTable)

    private var filterStaredProjects: Boolean = false

    init {
        refreshProjects()
    }

    private fun buildTableModel(projects: List<GitlabProject>): ColumnTableModel<GitlabProject> {
        return ColumnTableModel(listOf(
                column("Name", GitlabProject::nameWithNamespace)
        ), projects)
    }

    private fun buildTable(projectTableModel: ColumnTableModel<GitlabProject>): JBTable {
        val table = JBTable(projectTableModel)

        table.tableHeader = null

        TableSpeedSearch(table, Convertor {
            if (it is GitlabProject)
                it.nameWithNamespace
            else
                "$it"
        })

        return table
    }

    private fun buildDialogWrapper(project: Project, projectTable: JBTable): ProjectChooseDialog {

        val projectTableDecorated = ToolbarDecorator.createDecorator(projectTable)
                .disableAddAction()
                .disableUpDownActions()
                .disableRemoveAction()
                .addExtraAction(object : ToggleActionButton("Stared", icons.GitlabPluginIcons.Star) {
                    override fun isSelected(e: AnActionEvent?): Boolean = filterStaredProjects

                    override fun setSelected(e: AnActionEvent?, state: Boolean) {
                        filterStaredProjects = state
                        refreshProjects()
                    }
                })
                .initPosition()
                .createPanel()

        val dialog = ProjectChooseDialog(project, projectTableDecorated) {
            this.projectTable.selectedRowCount > 0
        }

        projectTable.selectionModel.addListSelectionListener {
            val listSelectionModel = it.source as ListSelectionModel

            if (!it.valueIsAdjusting)
                dialog.isOKActionEnabled = !listSelectionModel.isSelectionEmpty
        }

        object : DoubleClickListener() {
            override fun onDoubleClick(event: MouseEvent?): Boolean {
                getSelectedProject().forEach { dialog.doOKAction() }

                return true
            }
        }.installOn(projectTable)

        return dialog
    }

    fun refreshProjects() {
        projectChooseDialog.loadingPanel.startLoading()

        gitlabAPI.listProjectsAsync(simple = true, starred = filterStaredProjects)
                .whenCompleteAsync { projects, _ ->
                    SwingUtilities.invokeLater {
                        if (projects != null) {
                            installTableModel(projects)
                        } else {
                            installTableModel(projects)
                        }

                        projectChooseDialog.loadingPanel.stopLoading()
                    }
                }
    }

    private fun installTableModel(projects: List<GitlabProject>) {
        installTableModel(buildTableModel(projects))
    }

    private fun installTableModel(tableModel: ColumnTableModel<GitlabProject>) {
        this.projectTableModel = tableModel
        this.projectTable.model = tableModel
    }

    fun getSelectedProject(): Option<GitlabProject> = optionTry { projectTableModel.rows[projectTable.selectedRow] }

    fun show(): Option<GitlabProject> = projectChooseDialog.showAndGet().toOption()
            .filter { it }
            .flatMap { getSelectedProject() }
}

private class ProjectChooseDialog(
        project: Project,
        private val centerComponent: JComponent,
        private val okActive: () -> Boolean
) : DialogWrapper(project, null, true, IdeModalityType.IDE) {
    val loadingPanel = JBLoadingPanel(BorderLayout(), disposable)

    init {
        title = "Gitlab Projects"
        setResizable(false)

        this.okAction.isEnabled = false

        init()
    }

    override fun createCenterPanel(): JComponent {
        val panel = panel {
            row {
                centerComponent(CCFlags.grow, CCFlags.push)
            }
        }

        panel.apply {
            minimumSize = Dimension(400, 300)
        }

        loadingPanel.add(panel, BorderLayout.CENTER)

        return loadingPanel
    }

    override fun getPreferredFocusedComponent(): JComponent? = centerComponent

    public override fun doOKAction() {
        if (okAction.isEnabled && okActive()) {
            super.doOKAction()
        }
    }
}