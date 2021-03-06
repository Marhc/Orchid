package com.eden.orchid.forms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.utilities.addToSet

class SearchModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, SearchResourceSource>()
        addToSet<OrchidGenerator, SearchIndexGenerator>()
    }

}
