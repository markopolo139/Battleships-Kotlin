package ms.kotlin.battleships.app.entities

import ms.kotlin.battleships.business.entity.Ship

class AppShipEntity(
    val id: Int,
    shipElements: Set<ShipElementEntity>
): Ship(shipElements)