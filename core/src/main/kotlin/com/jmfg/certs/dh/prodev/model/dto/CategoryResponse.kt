package com.jmfg.certs.dh.prodev.model.dto

data class CategoryResponse(val categories: List<CategoryItem>) {
    data class CategoryItem(
        val name: String,
        val imageUrl: String,
        val numberOfLodgings: Int
    )
}