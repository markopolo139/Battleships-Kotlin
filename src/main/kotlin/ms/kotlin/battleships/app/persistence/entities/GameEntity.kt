package ms.kotlin.battleships.app.persistence.entities

import ms.kotlin.battleships.business.value.GameType
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
@Table(name = "games")
class GameEntity(
    @Id
    @GeneratedValue
    @Column(name = "games_id")
    val id: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type")
    val gameType: GameType,

    @OneToOne(cascade = [CascadeType.MERGE])
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    val playerA: UserEntity,

    @OneToOne(cascade = [CascadeType.MERGE])
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    val playerB: UserEntity,

    @OneToOne(cascade = [CascadeType.MERGE])
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    val currentTurnPlayer: UserEntity,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}