package ms.kotlin.battleships.app.persistence.entities

import javax.persistence.*

@Entity
@Table(name = "players")
class UserEntity(
    @Id
    @GeneratedValue
    @Column(name = "players_id")
    val id: Int,

    @Column(name = "username")
    val username: String,

    @Column(name = "`password`")
    val password: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "game_token")
    val gameToken: String?,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "players_id", referencedColumnName = "players_id", insertable = true, updatable = true, nullable = false)
    val ships: MutableSet<ShipEntity>,

    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "players_id", referencedColumnName = "players_id", insertable = true, updatable = true, nullable = false)
    val shots: MutableSet<ShotEntity>,

    @ElementCollection
    @CollectionTable(name = "player_roles", joinColumns = [JoinColumn(name = "players_id")])
    @Column(name = "`role`")
    val roles: MutableSet<String>

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}