package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.model.Equipamiento
import es.unex.giis.asee.gepeto.model.Ingrediente

fun stringsToIngredientes(strings: List<String>): List<Ingrediente> {
    val ingredientes: MutableList<Ingrediente> = mutableListOf()
    for (i in strings.indices) {
        ingredientes.add(Ingrediente(i, strings[i]))
    }
    return ingredientes
}

fun stringsToEquipamientos(strings: List<String>): List<Equipamiento> {
    val equipamientos: MutableList<Equipamiento> = mutableListOf()
    for (i in strings.indices) {
        equipamientos.add(Equipamiento(i, strings[i]))
    }
    return equipamientos
}

val recetasPrueba: List<Receta> = listOf(
    Receta(
        nombre = "Tacos al Pastor",
        descripcion = "Tacos de cerdo marinado con especias, cocidos en un trompo y servidos con cebolla, cilantro y piña.",
        favorita = true,
        ingredientes = listOf(
            Ingrediente(1, "Tortillas de maíz"),
            Ingrediente(2,"Carne de cerdo"),
            Ingrediente(3,"Achiote"),
            Ingrediente(4,"Piña"),
            Ingrediente(5,"Cebolla"),
            Ingrediente(6,"Cilantro")
        ),
        equipamientos = listOf(
            Equipamiento(0, "Trompo para tacos"),
            Equipamiento(1, "Parrilla")
        ),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Ensalada César",
        descripcion = "Ensalada fresca con lechuga romana, crutones, parmesano y aderezo César.",
        favorita = false,
        ingredientes = stringsToIngredientes(listOf("Lechuga romana", "Crutones", "Queso parmesano", "Pechuga de pollo", "Aderezo César")),
        equipamientos = stringsToEquipamientos(listOf("Tazón grande", "Sartén")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Spaghetti Carbonara",
        descripcion = "Pasta italiana cocida al dente con una salsa cremosa de huevo, panceta, queso parmesano y pimienta negra.",
        favorita = true,
        ingredientes = stringsToIngredientes(listOf("Spaghetti", "Panceta", "Yemas de huevo", "Queso parmesano", "Pimienta negra")),
        equipamientos = stringsToEquipamientos(listOf("Olla para cocinar pasta", "Sartén")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Sushi de Salmón",
        descripcion = "Rollitos de arroz, alga nori, salmón fresco, aguacate y pepino, servidos con salsa de soja y wasabi.",
        favorita = true,
        ingredientes = stringsToIngredientes(listOf("Arroz para sushi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi")),
        equipamientos = stringsToEquipamientos(listOf("Estera de bambú para hacer sushi", "Cuchillo afilado")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Brownies de Chocolate",
        descripcion = "Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.",
        favorita = true,
        ingredientes = stringsToIngredientes(listOf("Chocolate negro", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces")),
        equipamientos = stringsToEquipamientos( listOf("Molde para brownies", "Bol para mezclar")),
        imagen = R.drawable.ejemplo_plato,
    ),
    Receta(
        nombre = "Tacos al Pastor",
        descripcion = "Tacos de cerdo marinado con especias, cocidos en un trompo y servidos con cebolla, cilantro y piña.",
        favorita = true,
        ingredientes = stringsToIngredientes(listOf("Tortillas de maíz", "Carne de cerdo", "Achiote", "Piña", "Cebolla", "Cilantro")),
        equipamientos = stringsToEquipamientos(listOf("Trompo para tacos", "Parrilla")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Ensalada César",
        descripcion = "Ensalada fresca con lechuga romana, crutones, parmesano y aderezo César.",
        favorita = false,
        ingredientes = stringsToIngredientes(listOf("Lechuga romana", "Crutones", "Queso parmesano", "Pechuga de pollo", "Aderezo César")),
        equipamientos = stringsToEquipamientos(listOf("Tazón grande", "Sartén")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Spaghetti Carbonara",
        descripcion = "Pasta italiana cocida al dente con una salsa cremosa de huevo, panceta, queso parmesano y pimienta negra.",
        favorita = false,
        ingredientes = stringsToIngredientes(listOf("Spaghetti", "Panceta", "Yemas de huevo", "Queso parmesano", "Pimienta negra")),
        equipamientos = stringsToEquipamientos(listOf("Olla para cocinar pasta", "Sartén")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Sushi de Salmón",
        descripcion = "Rollitos de arroz, alga nori, salmón fresco, aguacate y pepino, servidos con salsa de soja y wasabi.",
        favorita = false,
        ingredientes = stringsToIngredientes(listOf("Arroz para sushi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi")),
        equipamientos = stringsToEquipamientos(listOf("Estera de bambú para hacer sushi", "Cuchillo afilado")),
        imagen = R.drawable.ejemplo_plato,
    ),

    Receta(
        nombre = "Brownies de Chocolate",
        descripcion = "Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.",
        favorita = true,
        ingredientes = stringsToIngredientes(listOf("Chocolate negro", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces")),
        equipamientos = stringsToEquipamientos(listOf("Molde para brownies bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar")),
        imagen = R.drawable.ejemplo_plato,
    )
)