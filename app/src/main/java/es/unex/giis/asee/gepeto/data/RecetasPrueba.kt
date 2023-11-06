package es.unex.giis.asee.gepeto.data

import es.unex.giis.asee.gepeto.model.Receta
import es.unex.giis.asee.gepeto.R

val recetasPrueba: List<Receta> = emptyList<Receta>()

/*listOf(
    Receta(
        idReceta = "1",
        nombre = "Tacos al Pastor",
        descripcion = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus at suscipit nisi. Curabitur ut aliquam libero. Phasellus imperdiet pulvinar velit. Sed a turpis sit amet massa aliquet placerat eu sit amet dui. Duis ac risus pretium, tincidunt velit venenatis, scelerisque sapien. Suspendisse potenti. Nulla facilisi.\n" +
                "\n" +
                "Quisque tellus odio, feugiat rhoncus orci in, tempus consequat leo. Suspendisse varius elit et ullamcorper interdum. Aenean aliquet risus id nulla accumsan pulvinar. Praesent in purus aliquam, malesuada enim ac, hendrerit dolor. Nulla fringilla ante mauris, a varius mauris pellentesque ac. Sed pellentesque, sapien in ullamcorper convallis, enim erat rhoncus arcu, quis tincidunt orci arcu ut augue. Duis cursus neque eu leo imperdiet bibendum. Sed cursus tincidunt ex, imperdiet eleifend ligula ultrices vitae. Fusce nibh mauris, congue in viverra lobortis, dictum ac purus.\n" +
                "\n" +
                "Nam cursus justo nec ante bibendum, eu ornare magna sollicitudin. Sed quis augue eu lorem porta dignissim ultrices ut dui. Morbi facilisis nibh a magna molestie, in ullamcorper metus vehicula. Maecenas eget nisi in est maximus lobortis ut sed elit. Nunc blandit vel ante non interdum. Suspendisse pretium pulvinar tortor, a tristique tortor consectetur sit amet. Ut faucibus felis eu orci varius congue sit amet non dolor. Quisque id sollicitudin nunc. Maecenas metus massa, accumsan in pharetra in, convallis in quam. Proin dictum accumsan risus in tincidunt. In lectus risus, luctus eget cursus in, sagittis eget massa. Curabitur nec euismod turpis.\n" +
                "\n" +
                "Praesent rhoncus massa at dictum pharetra. Donec sodales dolor ut lacus dapibus, eu dignissim purus placerat. Vestibulum pharetra tortor ornare tortor tincidunt, id imperdiet purus lacinia. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum sollicitudin lectus ut dolor tincidunt efficitur. Aenean semper elementum gravida. Cras tempor, justo vitae scelerisque aliquam, tellus lacus feugiat libero, ac euismod tortor lorem convallis ligula. Aenean viverra feugiat velit ullamcorper ullamcorper. Maecenas lobortis tristique libero, pulvinar porta nunc elementum at.\n" +
                "\n" +
                "Quisque id tristique nibh. Etiam at placerat orci. Quisque posuere elit nisi, at aliquet erat volutpat sed. Sed vitae mauris erat. Proin porttitor, nulla malesuada ultricies imperdiet, tellus sapien suscipit nisl, vel pulvinar augue metus luctus quam. Aliquam ultrices felis tristique porta semper. Nam eleifend nunc elit, id cursus nunc faucibus sit amet. Mauris ac vulputate nisl, non tincidunt odio. Morbi imperdiet placerat lacus sit amet rhoncus. Sed posuere ornare ex, vel fringilla elit dignissim vel. Cras iaculis facilisis nibh, sed mollis metus. Cras condimentum varius aliquet. Praesent augue arcu, molestie eu tristique non, gravida non magna. In nulla libero, molestie at tincidunt at, fermentum a augue. Suspendisse id augue sed sapien condimentum maximus. Vivamus et aliquet magna.",
        favorita = true,
        ingredientes = listOf(
            "Tortillas de maíz",
            "Carne de cerdo",
            "Achiote",
            "Piña",
            "Cebolla",
            "Cilantro"
        ),
        equipamientos = listOf(
            "Trompo para tacos",
            "Parrilla"
        ),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "2",
        nombre = "Ensalada César",
        descripcion = "Ensalada fresca con lechuga romana, crutones, parmesano y aderezo César.",
        favorita = false,
        ingredientes = listOf("Lechuga romana", "Crutones", "Queso parmesano", "Pechuga de pollo", "Aderezo César"),
        equipamientos = listOf("Tazón grande", "Sartén"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "3",
        nombre = "Spaghetti Carbonara",
        descripcion = "Pasta italiana cocida al dente con una salsa cremosa de huevo, panceta, queso parmesano y pimienta negra.",
        favorita = true,
        ingredientes = listOf("Spaghetti", "Panceta", "Yemas de huevo", "Queso parmesano", "Pimienta negra"),
        equipamientos = listOf("Olla para cocinar pasta", "Sartén"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "4",
        nombre = "Sushi de Salmón",
        descripcion = "Rollitos de arroz, alga nori, salmón fresco, aguacate y pepino, servidos con salsa de soja y wasabi.",
        favorita = true,
        ingredientes = listOf("Arroz para sushi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi"),
        equipamientos = listOf("Estera de bambú para hacer sushi", "Cuchillo afilado"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "5",
        nombre = "Brownies de Chocolate",
        descripcion = "Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.",
        favorita = true,
        ingredientes = listOf("Chocolate negro", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces"),
        equipamientos = listOf("Molde para brownies", "Bol para mezclar"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),
    Receta(
        idReceta = "6",
        nombre = "Tacos al Pastor",
        descripcion = "Tacos de cerdo marinado con especias, cocidos en un trompo y servidos con cebolla, cilantro y piña.",
        favorita = true,
        ingredientes = listOf("Tortillas de maíz", "Carne de cerdo", "Achiote", "Piña", "Cebolla", "Cilantro"),
        equipamientos = listOf("Trompo para tacos", "Parrilla"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "7",
        nombre = "Ensalada César",
        descripcion = "Ensalada fresca con lechuga romana, crutones, parmesano y aderezo César.",
        favorita = false,
        ingredientes = listOf("Lechuga romana", "Crutones", "Queso parmesano", "Pechuga de pollo", "Aderezo César"),
        equipamientos = listOf("Tazón grande", "Sartén"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "8",
        nombre = "Spaghetti Carbonara",
        descripcion = "Pasta italiana cocida al dente con una salsa cremosa de huevo, panceta, queso parmesano y pimienta negra.",
        favorita = false,
        ingredientes = listOf("Spaghetti", "Panceta", "Yemas de huevo", "Queso parmesano", "Pimienta negra"),
        equipamientos = listOf("Olla para cocinar pasta", "Sartén"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "9",
        nombre = "Sushi de Salmón",
        descripcion = "Rollitos de arroz, alga nori, salmón fresco, aguacate y pepino, servidos con salsa de soja y wasabi.",
        favorita = false,
        ingredientes = listOf("Arroz para sushi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi", "Alga nori", "Salmón fresco", "Aguacate", "Pepino", "Salsa de soja", "Wasabi"),
        equipamientos = listOf("Estera de bambú para hacer sushi", "Cuchillo afilado"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    ),

    Receta(
        idReceta = "10",
        nombre = "Brownies de Chocolate",
        descripcion = "Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.Deliciosos brownies de chocolate con trozos de nueces, suaves por dentro y ligeramente crujientes por fuera.",
        favorita = true,
        ingredientes = listOf("Chocolate negro", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces", "Mantequilla", "Azúcar", "Huevos", "Harina", "Nueces"),
        equipamientos = listOf("Molde para brownies bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar", "Bol para mezclar"),
        imagen = R.drawable.ejemplo_plato,
        imagenPath = ""
    )
)

 */