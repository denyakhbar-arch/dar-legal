package com.example.data.model

data class ProductCommodity(
    val id: String,
    val companyId: String,
    val name: String,
    val category: String,
    val pricePerKg: Long,
    val minOrderKg: Int,
    val availableStockKg: Long,
    val specifications: String,
    val moistureContent: String,
    val gradeQuality: String,
    val packagingType: String,
    val harvestOrigin: String
)

data class CompanyPostAd(
    val id: String,
    val companyId: String,
    val title: String,
    val content: String,
    val tag: String, // Produktivitas, Ekspor, Sertifikasi, Iklan
    val publishedDate: String,
    val author: String,
    val likesCount: Int = 42,
    val viewsCount: Int = 1250,
    val isPromoted: Boolean = true
)
