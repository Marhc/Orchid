package com.eden.orchid.wiki

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.wiki.menu.WikiPagesMenuItemType
import com.eden.orchid.wiki.menu.WikiSectionsMenuItemType

class WikiModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidGenerator, WikiGenerator>()
        addToSet<PluginResourceSource, WikiResourceSource>()
        addToSet<OrchidMenuItem>(
                WikiPagesMenuItemType::class,
                WikiSectionsMenuItemType::class)
    }
}

