package ms.kotlin.battleships.app.persistence.entities

import ms.kotlin.battleships.business.value.ShotType
import javax.persistence.*

@Entity
@Table(name = "shots")
class ShotEntity(
    @Id
    @GeneratedValue
    @Column(name = "shots_id")
    val id: Int,

    @Column(name = "pos_x")
    val x: Int,

    @Column(name = "pos_y")
    val y: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "`type`")
    val shotType: ShotType

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShotEntity

        if (id != other.id) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (shotType != other.shotType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + shotType.hashCode()
        return result
    }
}