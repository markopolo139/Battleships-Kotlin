package ms.kotlin.battleships.app.utils

import ms.kotlin.battleships.business.value.Shot

fun Set<Shot>.shotSetToMap() = associate { it.position to it }.toMutableMap()