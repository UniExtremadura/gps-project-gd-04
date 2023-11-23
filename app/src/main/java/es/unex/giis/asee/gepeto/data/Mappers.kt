package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.api.Equipments
import es.unex.giis.asee.gepeto.data.api.Ingredient
import es.unex.giis.asee.gepeto.data.api.RecipesItem
import es.unex.giis.asee.gepeto.data.api.StepsItem
import es.unex.giis.asee.gepeto.model.Equipamiento
import es.unex.giis.asee.gepeto.model.Ingrediente
import es.unex.giis.asee.gepeto.model.Pasos
import es.unex.giis.asee.gepeto.model.Receta

fun RecipesItem.toRecipe() = Receta(
    recetaId = id,
    nombre = title ,
    descripcion = "",
    favorita = false,
    ingredientes = (usedIngredients.map { it.name } + missedIngredients.map { it.name }).joinToString (separator = ";"),
    equipamientos = "",
    imagen = R.drawable.ejemplo_plato,
    imagenPath = image
)


fun StepsItem.toRecipe() = Pasos (
    descripcion = steps.map { it.step },
)

fun Equipments.toEquipamiento() = Equipamiento (
    descripcion = equipment.map { it.name },
)