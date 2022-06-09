package ms.kotlin.battleships.app.persistance.repositories

import ms.kotlin.battleships.app.persistance.entities.ShipEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShipRepository: JpaRepository<ShipEntity, Int>