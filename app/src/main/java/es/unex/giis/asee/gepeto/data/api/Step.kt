package es.unex.giis.asee.gepeto.data.api

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<IngredientX>,
    val length: Length,
    val number: Int,
    val step: String
)