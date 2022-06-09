package ms.kotlin.battleships.app.persistence.repositories

import ms.kotlin.battleships.app.persistence.entities.ShotEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShotRepository: JpaRepository<ShotEntity, Int>