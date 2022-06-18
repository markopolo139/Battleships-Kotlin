package ms.kotlin.battleships.web.models.response

import javax.validation.Valid

class ShipModel(
    val shipElements: List<@Valid ShipElementModel>
)
