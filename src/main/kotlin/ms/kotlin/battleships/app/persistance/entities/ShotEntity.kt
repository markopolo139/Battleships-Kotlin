package ms.kotlin.battleships.app.persistance.entities

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

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}