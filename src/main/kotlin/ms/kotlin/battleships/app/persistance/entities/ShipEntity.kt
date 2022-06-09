package ms.kotlin.battleships.app.persistance.entities

import ms.kotlin.battleships.app.persistance.embedabble.ShipElementEmbeddable
import org.aspectj.weaver.IntMap
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "ships")
class ShipEntity(
    @Id
    @GeneratedValue
    @Column(name = "ships_id")
    val id: Int,

    @ElementCollection
    @CollectionTable(name = "ship_elements", joinColumns = [JoinColumn(name = "ships_id")])
    val shipElements: Set<ShipElementEmbeddable>
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShipEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}