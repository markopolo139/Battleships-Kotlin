package ms.kotlin.battleships.business.value

open class Shot(
    val position: Position,
    val shotType: ShotType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shot

        if (position != other.position) return false
        if (shotType != other.shotType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + shotType.hashCode()
        return result
    }
}