package com.jmfg.certs.dh.prodev.model.dto

data class CategoryResponse(val categories: List<Category>) {
    data class Category(
        val name: String,
        val imageUrl: String,
        val numberOfLodgings: Int
    )
}