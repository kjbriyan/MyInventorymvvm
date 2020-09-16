package com.conceptdesignarchitect.laporanku.models

data class User(
    val id: Int,
    val nama: String?,
    val password: String?,
    val email: String?,
    val posisi: String?,
    val nip: String?
)
