package com.manish.hotelbookingapp.data.model.hotel_search

data class UniversalSortAndFilter(
    val __typename: String,
    val applyAction: ApplyAction,
    val filterSections: List<FilterSection>,
    val revealAction: RevealAction,
    val sortSections: List<SortSection>,
    val toolbar: Toolbar
)