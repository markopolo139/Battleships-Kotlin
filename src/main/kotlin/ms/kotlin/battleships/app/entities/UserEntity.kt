package ms.kotlin.battleships.app.entities

data class UserEntity(
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val gameToken: String,
    val shipBoard: Set<ShipEntity>,
    val shotBoard: Set<ShotEntity>
)