package ms.kotlin.battleships.app

import ms.kotlin.battleships.app.entities.AppShipEntity
import ms.kotlin.battleships.app.entities.AppShotEntity
import ms.kotlin.battleships.app.entities.ShipElementEntity
import ms.kotlin.battleships.app.persistence.embedabble.ShipElementEmbeddable
import ms.kotlin.battleships.business.value.Position
import ms.kotlin.battleships.web.models.PositionModel
import ms.kotlin.battleships.web.models.response.ShipElementModel
import ms.kotlin.battleships.web.models.response.ShipModel
import ms.kotlin.battleships.web.models.response.ShotModel

fun PositionModel.toApp() = Position(x, y)
fun Position.toModel() = PositionModel(x, y)

fun ShipElementEntity.toModel() = ShipElementModel(position.toModel())

fun AppShipEntity.toModel() = ShipModel(shipElements.map { (it as ShipElementEntity).toModel() })

fun AppShotEntity.toModel() = ShotModel(position.toModel(), shotType.name)