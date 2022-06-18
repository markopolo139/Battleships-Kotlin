package ms.kotlin.battleships.app.utils

import ms.kotlin.battleships.app.persistence.entities.UserEntity
import ms.kotlin.battleships.business.value.Shot
import ms.kotlin.battleships.web.models.request.RegistryModel

fun Set<Shot>.toPositionMap() = associate { it.position to it }.toMutableMap()

fun RegistryModel.toPersistence(): UserEntity = UserEntity(
    0, username, password, email, null, mutableSetOf(), mutableSetOf(), mutableSetOf("USER")
)