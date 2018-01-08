package com.eden.orchid.pages.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.snakeCase
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words

@Archetype(value = ConfigArchetype::class, key = "staticPages")
class StaticPage(resource: OrchidResource)
    : OrchidPage(resource, "page", resource.reference.title.from { snakeCase { capitalize() } }.to { words() }) {

    @Option @BooleanDefault(true)
    var usePrettyUrl: Boolean = true

    @Option @StringDefault("template")
    lateinit var renderMode: String

    override fun onPostExtraction() {
        reference.isUsePrettyUrl = usePrettyUrl
    }
}

