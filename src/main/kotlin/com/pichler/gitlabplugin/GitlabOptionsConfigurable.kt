package com.pichler.gitlabplugin

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.util.Disposer
import com.pichler.gitlabplugin.gui.ConfigurationPanel
import javax.swing.JComponent

class GitlabOptionsConfigurable : SearchableConfigurable, Disposable {
    private var configurationPanel: ConfigurationPanel? = null

    override fun isModified(): Boolean = configurationPanel!!.isModified()

    override fun getId(): String = "gitlab"

    override fun getDisplayName(): String = "Gitlab"

    override fun apply() {
        val state = configurationPanel!!.toState()

        GitlabConfigurationStateService.instance.loadState(state)
    }

    override fun createComponent(): JComponent? {
        configurationPanel = ConfigurationPanel(GitlabConfigurationStateService.instance.state)
        return configurationPanel!!.configPane
    }

    override fun dispose() {
        configurationPanel = null
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    override fun reset() = configurationPanel!!.reset(GitlabConfigurationStateService.instance.state)
}