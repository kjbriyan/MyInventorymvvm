package com.example.myinventory.model

data class ResponseLogin(
	val data: List<DataItem?>? = null,
	val messages: String? = null,
	val error: Any? = null,
	val status: Int? = null
)

data class DataItem(
	val password: String? = null,
	val name: String? = null,
	val idUser: String? = null,
	val username: String? = null
)

