package com.eden.orchid.presentations

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.presentations.components.PresentationComponent
import com.eden.orchid.utilities.addToSet

class PresentationsModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, PresentationsResourceSource>()
        addToSet<OrchidGenerator, PresentationsGenerator>()
        addToSet<OrchidComponent, PresentationComponent>()
        addToSet<OptionExtractor<*>, PresentationOptionExtractor>()
    }

}
