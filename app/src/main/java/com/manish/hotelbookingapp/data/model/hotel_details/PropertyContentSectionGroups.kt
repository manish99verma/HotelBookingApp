package com.manish.hotelbookingapp.data.model.hotel_details

data class PropertyContentSectionGroups(
    val __typename: String,
    val aboutThisProperty: PropertyContentSubSectionGroups,
    val cleanliness: Any?,
    val importantInfo: Any?,
    val policies: Policies
)

data class PropertyContentSubSectionGroups(
    val sections: List<PropertyContentSection>
)

data class PropertyContentSection(
    val bodySubSections: List<PropertyContentSubSection>
)

data class PropertyContentSubSection(
    val elements: List<PropertyContent>
)

data class PropertyContent(
    val items: List<PropertyContentItemMarkup>
)

data class PropertyContentItemMarkup(
    val content: MarkupText?
)

data class MarkupText(
    val text: String?,
    val markupType: String?
)