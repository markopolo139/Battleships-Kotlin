package ms.kotlin.battleships.app.persistance.repositories

import ms.kotlin.battleships.app.persistance.entities.ShotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShotRepository: JpaRepository<ShotEntity, Int>