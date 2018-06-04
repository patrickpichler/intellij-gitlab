package com.pichler.gitlabplugin

import org.funktionale.option.Option

fun <T> Collection<T>.head(): Option<T> =
        if (isEmpty())
            Option.None
        else
            Option.Some(first())