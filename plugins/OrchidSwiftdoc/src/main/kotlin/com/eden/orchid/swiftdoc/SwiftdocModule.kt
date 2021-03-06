package com.eden.orchid.swiftdoc

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.swiftdoc.menu.SwiftdocMenuItem
import com.eden.orchid.utilities.addToSet

class SwiftdocModule : OrchidModule() {

    override fun configure() {
        addToSet<PluginResourceSource, SwiftdocResourceSource>()
        addToSet<OrchidGenerator, SwiftdocGenerator>()
        addToSet<OrchidMenuItem, SwiftdocMenuItem>()
    }

}