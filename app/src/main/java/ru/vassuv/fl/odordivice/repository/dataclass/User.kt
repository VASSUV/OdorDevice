package ru.vassuv.fl.odordivice.repository.dataclass

data class User (var id: Long,
            var name: String,
            var fullName: String,
            var photo: String,
            var bio: String,
            var webSite: String,
            var media: Int,
            var follows: Int,
            var followedBy: Int )