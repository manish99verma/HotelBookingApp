package com.manish.hotelbookingapp.data.model.hotel_search

data class Field(
    val __typename: String,
    val action: Action,
    val analytics: Analytics,
    val expando: Expando,
    val icon: Icon,
    val id: String,
    val label: Any,
    val multiSelectionOptions: List<MultiSelectionOption>,
    val options: List<Option>,
    val placeholder: String,
    val primary: String,
    val range: Range,
    val secondary: Any,
    val selected: Any,
    val tileMultiSelectionOptions: List<TileMultiSelectionOption>,
    val typeaheadInfo: TypeaheadInfo,
    val dropdownFilterOptions: List<DropdownFilterOption>,
)