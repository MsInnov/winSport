package com.mscode.data.remoteconfig.datasource

import com.mscode.data.remoteconfig.model.Path
import com.mscode.data.remoteconfig.model.Url

class LocalConfigDataSource {

    private val _urls = emptyList<Url>().toMutableList()
    val urls: List<Url> = _urls

    private val _paths = emptyList<Path>().toMutableList()
    val paths: List<Path> = _paths

    fun saveUrl(url: Url) {
        _urls.add(url)
    }

    fun savePath(path: Path) {
        _paths.add(path)
    }

}